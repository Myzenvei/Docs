/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.enums.GPConsignmentEntryStatus;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.order.impl.GPOmsOrderService;
import com.gpintegration.dto.out.paypal.Amount;
import com.gpintegration.dto.paypal.PayPalRefundRequest;
import com.gpintegration.dto.paypal.PayPalRefundResponse;
import com.gpintegration.service.GPCybersourceIntegrationService;
import com.gpintegration.service.GPPayPalIntegrationService;
import com.gpsapreturnorder.model.GPRefundEntryModel;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.OrderRefundEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import com.gpreturnorder.hybris.inbound.events.CreditMemoEvent;

public class GPOrderRefundEventListener extends AbstractAcceleratorSiteEventListener<CreditMemoEvent> {

	private static final String REJECT = "REJECT";
	private static final String ACCEPT = "ACCEPT";
	private static final String PENDING = "pending";
	private static final String COMPLETED = "completed";
	private static final String SEND_ORDER_REFUND_PROCESS = "sendOrderRefundProcess";
	private static final Logger LOG = Logger.getLogger(GPOrderRefundEventListener.class);
	private static final String TRANSACTION_STATUS_SUCCESS ="ACCEPTED";
	private static final String TRANSACTION_STATUS_FAILURE ="REJECTED";
	
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	private GPCybersourceIntegrationService gpCyberSource;
	private ConfigurationService configurationService;
	private GPOmsOrderService gpOMSOrderService;
	private EventService eventService;
	private GPPayPalIntegrationService gpPayPalService;
	
	@Override
	protected void onEvent(final CreditMemoEvent creditMemoEvent)
	{
		LOG.info("**** GPOrderRefundEventListener Starts ****");
		String requestCode = creditMemoEvent.getReturnRequestCode();
		
		ReturnRequestModel returnRequest = new ReturnRequestModel();
		returnRequest.setCode(requestCode);
		returnRequest = (ReturnRequestModel)getFlexibleSearchService().getModelByExample(returnRequest);
		
		if(returnRequest != null && returnRequest.getReturnEntries() != null)
		{
			LOG.debug("Refund initiated for Return Request: " + returnRequest.getCode());
			List<ReturnEntryModel> refundEntries = new ArrayList<ReturnEntryModel>(returnRequest.getReturnEntries());
			OrderModel order = (OrderModel)returnRequest.getConsignment().getOrder();

			BigDecimal totalRefundAmount = calculateRefund(refundEntries);
			returnRequest.setSubtotal(totalRefundAmount);
			LOG.debug("Total Refund Amount calculated is: " + totalRefundAmount);
			
			boolean refundResult = initiateRefund(totalRefundAmount, order);
			LOG.debug("*********refundResult *********" +refundResult);
			
			if(refundResult)
			{
				returnRequest.setStatus(ReturnStatus.PAYMENT_REVERSED);
				for(ReturnEntryModel returnEntry : refundEntries) {
					returnEntry.setStatus(ReturnStatus.PAYMENT_REVERSED);
					getModelService().save(returnEntry);
				}
				returnRequest.setReturnEntries(refundEntries);
				returnRequest.setOrder(order);
				getModelService().save(returnRequest);
				
				LOG.info("*********Triggering Order Refund Email*********");
				
				//Triggering Order Refund Email
				OrderProcessModel orderProcess= getModelService().create(OrderProcessModel.class);
				orderProcess.setCode(SEND_ORDER_REFUND_PROCESS + order.getCode() + System.currentTimeMillis());
				orderProcess.setProcessDefinitionName(SEND_ORDER_REFUND_PROCESS);
				orderProcess.setOrder(order);
				orderProcess.setRefundAmount(totalRefundAmount);
				getModelService().save(orderProcess);
				getEventService().publishEvent(new OrderRefundEvent(orderProcess));
			}
			else{
				returnRequest.setStatus(ReturnStatus.PAYMENT_REVERSAL_FAILED);
				
				for(ReturnEntryModel returnEntry : refundEntries) {
					returnEntry.setStatus(ReturnStatus.PAYMENT_REVERSAL_FAILED);
					getModelService().save(returnEntry);
					
					gpOMSOrderService.updateProcessingStatus(((GPRefundEntryModel)returnEntry).getConsignmentEntry(), GPConsignmentEntryStatus.FULFILLMENT_ERROR);
					LOG.debug("Consignment Entry processing status has been updated to :" + GPConsignmentEntryStatus.FULFILLMENT_ERROR + " as refund failed for consignment entry: " + ((GPRefundEntryModel)returnEntry).getConsignmentEntry().getConsignmentEntryNumber());
				}
				
				returnRequest.setReturnEntries(refundEntries);
				returnRequest.setOrder(order);
				getModelService().save(returnRequest);
			}
		}
	}
	
	@Override
	protected SiteChannel getSiteChannelForEvent(CreditMemoEvent event) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @param refundEntries
	 * 
	 */
	protected BigDecimal calculateRefund(List<ReturnEntryModel> refundEntries) {
		
		double totalRefundAmount = 0.0;
		
		for(ReturnEntryModel refundEntry : refundEntries) {
			if(refundEntry instanceof GPRefundEntryModel) {
				totalRefundAmount = totalRefundAmount + ((GPRefundEntryModel)refundEntry).getAmount().doubleValue();
			}
		}
		return BigDecimal.valueOf(totalRefundAmount);
	}
	
	/**
	 * 
	 * @param totalRefundAmount
	 * @param order
	 */
	protected boolean initiateRefund(BigDecimal totalRefundAmount, OrderModel order) {
		
		LOG.debug("Initiate Refund for order: " + order.getCode());
		
		ReplyMessage replyMessage = null;
		String requestID = null;
		boolean isSuccess = false;
		
		requestID = getRequestIDofAuth(order);
		LOG.debug("Request Id for Credit Refund: " + requestID);
		try {
			PaymentTransactionEntryModel paymentTransactionEntry = null;
			if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
				replyMessage = gpCyberSource.voidCapture(order.getCode(), totalRefundAmount,
						Currency.getInstance(order.getCurrency().getIsocode()), requestID, order.getSite().getUid());
				paymentTransactionEntry = createPaymentTransactionEntry(replyMessage, totalRefundAmount);
			} else {
				PayPalRefundRequest request = new PayPalRefundRequest();
				Amount amount = new Amount();
				amount.setCurrency("USD");
				amount.setTotal(totalRefundAmount.toString());
				request.setAmount(amount);
				PayPalRefundResponse response = gpPayPalService.refund(request, requestID);
				paymentTransactionEntry = createPaymentTransactionEntry(response);
			}

			PaymentTransactionModel paymentTransaction = order.getPaymentTransactions().get(0);
			List<PaymentTransactionEntryModel> transactionEntries = new ArrayList<PaymentTransactionEntryModel>(
					paymentTransaction.getEntries());
			transactionEntries.add(paymentTransactionEntry);
			paymentTransaction.setEntries(transactionEntries);
			getModelService().save(paymentTransaction);
			if (paymentTransactionEntry.getTransactionStatus().equals(TRANSACTION_STATUS_SUCCESS)) {
				isSuccess = true;
			} else {
				isSuccess = false;
			}
			LOG.info("isSuccess value: " + isSuccess);
		} catch (Exception e) {
			if (null != replyMessage) {
				LOG.error("FULFILLMENT_ERROR | Refund failed error code :" + replyMessage.getReasonCode().toString());
			}
			LOG.error("FULFILLMENT_ERROR | Credit Refund failed :", e);
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/*
	 * 
	 * @param replyMessage
	 * @return
	 */
	protected PaymentTransactionEntryModel createPaymentTransactionEntry(PayPalRefundResponse response){
		
		String captureTransactionDecision = response.getState();
		LOG.debug("Refund transaction decision: " + captureTransactionDecision);
		
		PaymentTransactionEntryModel transactionEntry = getModelService().create(PaymentTransactionEntryModel.class);	
		transactionEntry.setRequestId(response.getId());
		transactionEntry.setRequestToken(response.getParent_payment());
		transactionEntry.setType(PaymentTransactionType.REFUND_STANDALONE);
		transactionEntry.setCode(response.getId());
		//Should be changed in production , pending is only for sandbox accounts 
		if(COMPLETED.equalsIgnoreCase(captureTransactionDecision) || PENDING.equalsIgnoreCase(captureTransactionDecision))
		{
			
			transactionEntry.setAmount(new BigDecimal(response.getAmount().getTotal()));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
		}
		else 
		{
			transactionEntry.setAmount(new BigDecimal(0));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_FAILURE);
		}
		
		getModelService().save(transactionEntry);
		return transactionEntry;
	}
	
	private String getRequestIDofAuth(OrderModel order) {
		String requestID = null;
		if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel || order.getPaymentInfo() instanceof PaypalPaymentInfoModel)
		{
			for (final PaymentTransactionModel txn : order.getPaymentTransactions())
			{
				for(final PaymentTransactionEntryModel txnEntry: txn.getEntries())
				{
					if (txnEntry.getType().equals(PaymentTransactionType.CAPTURE)
							&& TransactionStatus.ACCEPTED.name().equals(txnEntry.getTransactionStatus()))
					{
						return txnEntry.getRequestId() ;
					}
				}
			}
		}
		return requestID;
	}
	
	/**
	 * 
	 * @param replyMessage
	 * @return
	 */
	protected PaymentTransactionEntryModel createPaymentTransactionEntry(ReplyMessage replyMessage, BigDecimal totalRefundAmount){
		
		String refundTransactionDecision = replyMessage.getDecision();
		
		LOG.debug("Refund transaction decision: " + refundTransactionDecision);
		
		PaymentTransactionEntryModel transactionEntry = getModelService().create(PaymentTransactionEntryModel.class);	
		transactionEntry.setCode(replyMessage.getRequestID()); 
		transactionEntry.setRequestId(replyMessage.getRequestID());
		transactionEntry.setTransactionStatusDetails(replyMessage.getReasonCode().toString());
		transactionEntry.setRequestToken(replyMessage.getRequestToken());
		transactionEntry.setSubscriptionID(replyMessage.getMerchantReferenceCode());
		transactionEntry.setType(PaymentTransactionType.REFUND_STANDALONE);
		if(ACCEPT.equalsIgnoreCase(refundTransactionDecision))
		{
			transactionEntry.setAmount(totalRefundAmount);
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
		}
		else if(REJECT.equalsIgnoreCase(refundTransactionDecision))
		{
			transactionEntry.setAmount(new BigDecimal(0));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_FAILURE);
		}
		
		getModelService().save(transactionEntry);		
		return transactionEntry;
	}

	@Override
	protected void onSiteEvent(CreditMemoEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @return GPOmsOrderService
	 */
	protected GPOmsOrderService getGpOMSOrderService() {
		return gpOMSOrderService;
	}

	/**
	 * 
	 * @param gpOMSOrderService
	 */
	public void setGpOMSOrderService(GPOmsOrderService gpOMSOrderService) {
		this.gpOMSOrderService = gpOMSOrderService;
	}
	
	/**
	 * 
	 * @return ConfigurationService
	 */
	protected ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * 
	 * @param configurationService
	 */
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}


	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * 
	 * @return GPCybersourceIntegrationService
	 */
	protected GPCybersourceIntegrationService getGpCyberSource() {
		return gpCyberSource;
	}

	/**
	 * 
	 * @param gpCyberSource
	 */
	public void setGpCyberSource(GPCybersourceIntegrationService gpCyberSource) {
		this.gpCyberSource = gpCyberSource;
	}
	
	/**
	 * @return the flexibleSearchService
	 */
	protected FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
	
	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	public EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public GPPayPalIntegrationService getGpPayPalService() {
		return gpPayPalService;
	}

	public void setGpPayPalService(GPPayPalIntegrationService gpPayPalService) {
		this.gpPayPalService = gpPayPalService;
	}
	
}
