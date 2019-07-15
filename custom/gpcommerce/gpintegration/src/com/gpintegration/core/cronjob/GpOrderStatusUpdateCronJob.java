/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.cronjob;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gp.commerce.core.enums.GPConsignmentEntryStatus;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.core.order.impl.GPOmsOrderService;
import com.gp.commerce.core.services.GPConsignmentService;
import com.gp.commerce.core.services.GPDebitCalculationService;
import com.gpintegration.dto.netsuite.GPNetsuiteCCAckDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteResponseDTO;
import com.gpintegration.dto.out.paypal.Amount;
import com.gpintegration.dto.paypal.PayPalCaptureRequest;
import com.gpintegration.dto.paypal.PayPalCaptureResponse;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCybersourceIntegrationService;
import com.gpintegration.service.GPNetsuiteReplicationService;
import com.gpintegration.service.GPPayPalIntegrationService;
import com.gpintegration.service.impl.GPDefaultCRMShellOrderReplicationService;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class GpOrderStatusUpdateCronJob extends AbstractJobPerformable<CronJobModel>{

	private static final String REJECT = "REJECT";
	private static final String ACCEPT = "ACCEPT";
	private static final String PENDING = "pending";
	private static final String COMPLETED = "completed";
	private static final String SUCCESS = "success";
	private static final String ERROR = "error";
	private static final String NS = "NS";
	private static final Logger LOG = Logger.getLogger(GpOrderStatusUpdateCronJob.class);
	private GPDebitCalculationService gpDebitCalulationService;
	private GPConsignmentService gpConsignmentService;
	private GPPayPalIntegrationService gpPayPalService;
	private ConfigurationService configurationService;
	private GPOmsOrderService gpOMSOrderService;
	private EventService eventService;
	private GPNetsuiteReplicationService gpNetsuiteReplicationService;
	private GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService;
	private FlexibleSearchService flexibleSearchService;
	private static final String ORDER_DEBIT_RETRY ="order.debit.retry"; 
	private GPCybersourceIntegrationService gpCyberSource;
	private static final String TRANSACTION_STATUS_SUCCESS ="ACCEPTED";
	private static final String TRANSACTION_STATUS_FAILURE ="REJECTED"; 
	private static final String SHIPPMENT_PROCESS_RETRY ="gp.shipment.process.retry.count";

	@Override
	public PerformResult perform(final CronJobModel cronjob) {
		LOG.info("**** Inside GPOrderStatusUpdateCronJob ****");
		
		int retryCount = configurationService.getConfiguration().getInt(SHIPPMENT_PROCESS_RETRY, 1);
		for (ShippingNotificationModel shippingNotification : gpConsignmentService.getShippingCodesToProcess(retryCount)) {
			ConsignmentEntryModel consignmentEntry = null;
			try {
				if (clearAbortRequestedIfNeeded(cronjob))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
			if (shippingNotification != null && shippingNotification.getConsignmentEntryNumber() != null
					&& shippingNotification.getConsignmentCode() != null) {
				consignmentEntry = gpConsignmentService.getConsignmentEntryByEntryNumber(
						shippingNotification.getConsignmentEntryNumber(), shippingNotification.getConsignmentCode());
				if (consignmentEntry != null) {
					LOG.debug("Consignment: " + consignmentEntry.getConsignment().getCode());
					LOG.debug("Consignment Entry: " + consignmentEntry.getConsignmentEntryNumber());
					LOG.debug("Order code: " + consignmentEntry.getOrderEntry().getOrder().getCode());
					OrderModel order = (OrderModel) consignmentEntry.getOrderEntry().getOrder();
					if (shippingNotification.getQuantityShipped() != null) {
						// Trigger shipping
						updateShipping(consignmentEntry,
								Long.valueOf(shippingNotification.getQuantityShipped().longValue()), order,
								shippingNotification);
					} else if (shippingNotification.getQuantityConfirmed() != null) {
						// Trigger Cancel
						long qtyRejected = consignmentEntry.getQuantity().longValue()
								- shippingNotification.getQuantityConfirmed().intValue();
						if (qtyRejected > 0) {
							gpOMSOrderService.partialCancelConsignment(Long.valueOf(qtyRejected),
									consignmentEntry.getConsignment(), consignmentEntry,
									shippingNotification.getReasonForRejection());
						}
					}
					LOG.info(" CRM Shell Order status update Replication");
					try {
						gpCRMShellOrderReplicationService.updateCRMShellOrder(order);
					} catch (Exception e) {
						LOG.error(" CRM Shell Order status update Replication error :" + e.getMessage(), e);
					}
				}
			}
			if(null !=shippingNotification)
			{
				shippingNotification.setIsProcessed(true);
			}
			modelService.save(shippingNotification);
			}catch(Exception e) {
				if(null !=shippingNotification)
				{
					shippingNotification.setIsProcessed(false);
					shippingNotification.setRetryCount(shippingNotification.getRetryCount() + 1);
					modelService.save(shippingNotification);
					LOG.error(" Shipment with shipping code: " + shippingNotification.getCode() + " failed due to: ", e);
				}
			}
		}
		LOG.info("Sucessfully completed executing GPOrderStatusUpdateCronJob");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	/**
	 * 
	 * @param consignmentEntry
	 * @param shippedQuantity
	 * @param order
	 * @param shippingNotification
	 */
	protected void updateShipping(ConsignmentEntryModel consignmentEntry, Long shippedQuantity, OrderModel order,
			ShippingNotificationModel shippingNotification) {
		if (consignmentEntry.getQuantity() >= shippedQuantity) {
			// Compute Total Shipped Quantity
			if (consignmentEntry.getShippedQuantity() != null && consignmentEntry.getShippedQuantity() > 0) {
				shippedQuantity = shippedQuantity + consignmentEntry.getShippedQuantity();
			}
			consignmentEntry.setShippedQuantity(shippedQuantity);

			// Compute Shipped Status for Consignment Entry
			if (consignmentEntry.getQuantity() > consignmentEntry.getShippedQuantity()) {
				consignmentEntry.setConsignmentEntryStatus(GPConsignmentEntryStatus.PARTIALLY_SHIPPED);
			} else {
				consignmentEntry.setConsignmentEntryStatus(GPConsignmentEntryStatus.SHIPPED);
			}

			// Create Tracking and associate it to Consignment Entry
			generateTracking(consignmentEntry, shippingNotification);
			modelService.save(consignmentEntry);
			
			// Acknowledge NetSuite with Auth Code
			if ((shippingNotification.getSource() != null) && (shippingNotification.getSource().equalsIgnoreCase(NS))) {
					ccAckNetSuite(consignmentEntry.getConsignment().getPaymentId(), shippingNotification, getRequestIDofAuth(order));
					try {
						modelService.save(shippingNotification);
					} catch (Exception e) {
						LOG.error("ShippingNotification table is not updated or saved: " ,e);
				}
			}

			// Compute Order Status on basis of Consignment
			gpOMSOrderService.updateOrderAndConsignmentStatus(order, consignmentEntry.getConsignment());
		}
	}

	private TrackingModel generateTracking(ConsignmentEntryModel consignmentEntry, ShippingNotificationModel shippingNotification) {
		
		//Create Tracking and associate it to Consignment Entry
		TrackingModel tracking = modelService.create(TrackingModel.class);
		if(StringUtils.isNotBlank(shippingNotification.getTrackingNumber())) {
			Long totalQuantityShipped = Long.valueOf(shippingNotification.getQuantityShipped().longValue());
			
			tracking.setTrackingID(shippingNotification.getTrackingNumber());
			tracking.setTrackingURL(shippingNotification.getTrackingURL());
			tracking.setCarrier(shippingNotification.getCarrier());
			tracking.setQuantityShipped(totalQuantityShipped);
			
			List<TrackingModel> trackingList = new ArrayList<TrackingModel>(consignmentEntry.getTrackingList());
			trackingList.add(tracking);
			consignmentEntry.setTrackingList(trackingList);
			
			modelService.saveAll(tracking, consignmentEntry);
			
			LOG.debug("New Tracking created with Id: " + tracking.getTrackingID() + " with Quantity Shipped: " + totalQuantityShipped);
		}
		else {
			LOG.error("Tracking number not found in ShippingNotification: " + shippingNotification.getCode());
		}
		return tracking;
	}
	
	private void ccAckNetSuite(String paymentId, ShippingNotificationModel shippingNotification, String paymentToken) {
		GPNetsuiteCCAckDTO ack = new GPNetsuiteCCAckDTO();
		ack.setNsPaymentId(paymentId);
		ack.setMessage(paymentToken);
		ack.setStatus(SUCCESS);
		try {
			LOG.debug("NS ACK Request    Message:"+ ack.getMessage()+" Status:" + ack.getStatus()+" paymentId:"+ack.getNsPaymentId()+ " ");
			GPNetsuiteResponseDTO netSuiteResponse = gpNetsuiteReplicationService.acknowledgeCCOfPayment(ack);
			if (null != netSuiteResponse) {
				LOG.debug("NS ACK Response: "+ netSuiteResponse.getStatus());
				if (netSuiteResponse.getStatus().equalsIgnoreCase(SUCCESS)) {
					shippingNotification.setIsCCAcknowledged(true);
					LOG.debug("Payment is captured and sent to NS ACK");
				} else if (netSuiteResponse.getStatus().equalsIgnoreCase(ERROR)) {
					shippingNotification.setIsCCAcknowledged(true);
					LOG.debug("Payment capture failed and sent to NS ACK");
				} else
				{
					shippingNotification.setIsCCAcknowledged(false);
				}
			}

		} catch (GPIntegrationException e) {
			LOG.error("Acknowledge to NS is not successful"+e.getMessage(),e);
			shippingNotification.setIsCCAcknowledged(false);
		}
		
	}


	/**
	 * 
	 * @param debitAmount
	 * @param order
	 */
	protected boolean capturePayment(BigDecimal debitAmount, OrderModel order) {
		
		LOG.debug("Initiate Debit Capture for order: " + order.getCode());
		
		ReplyMessage replyMessage = null;
		String requestID = null;
		boolean isSuccess = false;
		
		requestID = getRequestIDofAuth(order);
		LOG.debug("Request Id for Debit Capture: " + requestID);
		
		int retryCount = configurationService.getConfiguration().getInt(ORDER_DEBIT_RETRY, 3);
		for (int limit = 0; limit < retryCount; limit++) 
		{
			PaymentTransactionEntryModel paymentTransactionEntry = null;
			try
			{
				LOG.debug("Order debit retry property is configured to :"+retryCount+" times,trying "+ limit +"th time to debit the payment");
				if(order.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
					replyMessage = gpCyberSource.billCapture(order.getCode(), debitAmount, Currency.getInstance(order.getCurrency().getIsocode()), requestID, order.getSite().getUid(),
							getCaptureSequence(order), getCaptureTotal(order));
					paymentTransactionEntry = createPaymentTransactionEntry(replyMessage);
				}
				else {
					PayPalCaptureRequest request = new PayPalCaptureRequest();
					Amount amount = new Amount();
					amount.setCurrency("USD");
					amount.setTotal(debitAmount.toString());
					request.setAmount(amount);
					request.setIs_final_capture(false);
					PayPalCaptureResponse response = gpPayPalService.capture(request, requestID);
					paymentTransactionEntry = createPaymentTransactionEntry(response);
				}
				
				PaymentTransactionModel paymentTransaction = order.getPaymentTransactions().get(0);					
				List<PaymentTransactionEntryModel> transactionEntries = new ArrayList<PaymentTransactionEntryModel>(paymentTransaction.getEntries());
				transactionEntries.add(paymentTransactionEntry);					
				paymentTransaction.setEntries(transactionEntries);					
				modelService.save(paymentTransaction);
				modelService.refresh(paymentTransaction);
				modelService.refresh(order);
				if(paymentTransactionEntry.getTransactionStatus().equals(TRANSACTION_STATUS_SUCCESS))
					isSuccess = true;
				else
					isSuccess = false;
				LOG.info("isSuccess value: " + isSuccess);
				break;
			}
			catch (Exception e) {
				isSuccess = false;
				if(null != replyMessage) {
					LOG.error("PAYMENT_ERROR | Payment Capture failed error code :)" + replyMessage.getReasonCode().toString() );
				}
				LOG.error("PAYMENT_ERROR | Payment Capture failed ", e);
			}
		}
		return isSuccess;
	}
	
	/**
	 * 
	 * @param replyMessage
	 * @return
	 */
	protected PaymentTransactionEntryModel createPaymentTransactionEntry(ReplyMessage replyMessage){
		
		String captureTransactionDecision = replyMessage.getDecision();
		LOG.debug("Capture transaction decision: " + captureTransactionDecision);
		
		PaymentTransactionEntryModel transactionEntry = modelService.create(PaymentTransactionEntryModel.class);	
		transactionEntry.setRequestId(replyMessage.getRequestID());
		transactionEntry.setRequestToken(replyMessage.getRequestToken());
		transactionEntry.setType(PaymentTransactionType.CAPTURE);
		transactionEntry.setCode(replyMessage.getRequestToken());
		
		if(ACCEPT.equalsIgnoreCase(captureTransactionDecision))
		{
			transactionEntry.setAmount(new BigDecimal(replyMessage.getCcCaptureReply().getAmount()));
			transactionEntry.setSubscriptionID(replyMessage.getMerchantReferenceCode());
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
		}
		else if(REJECT.equalsIgnoreCase(captureTransactionDecision))
		{
			transactionEntry.setAmount(new BigDecimal(0));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_FAILURE);
		}
		
		modelService.save(transactionEntry);
		modelService.refresh(transactionEntry);
		return transactionEntry;
	}
	
	/*
	 * 
	 * @param replyMessage
	 * @return
	 */
	protected PaymentTransactionEntryModel createPaymentTransactionEntry(PayPalCaptureResponse response){
		
		String captureTransactionDecision = response.getState();
		LOG.debug("Capture transaction decision: " + captureTransactionDecision);
		
		PaymentTransactionEntryModel transactionEntry = modelService.create(PaymentTransactionEntryModel.class);	
		transactionEntry.setRequestId(response.getId());
		transactionEntry.setRequestToken(response.getParent_payment());
		transactionEntry.setType(PaymentTransactionType.CAPTURE);
		//Should be changed in production , pending is only for sandbox accounts 
		if(COMPLETED.equalsIgnoreCase(captureTransactionDecision) || PENDING.equalsIgnoreCase(captureTransactionDecision))
		{
			transactionEntry.setCode(response.getParent_payment());
			transactionEntry.setAmount(new BigDecimal(response.getAmount().getTotal()));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
		}
		else 
		{
			transactionEntry.setCode("CAPTUR_FAILED");
			transactionEntry.setAmount(new BigDecimal(0));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_FAILURE);
		}
		
		modelService.save(transactionEntry);
		return transactionEntry;
	}
	
	private String getRequestIDofAuth(OrderModel order) {
		String requestID = null;
		if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel || order.getPaymentInfo() instanceof PaypalPaymentInfoModel )
		{
			for (final PaymentTransactionModel txn : order.getPaymentTransactions())
			{
				for(final PaymentTransactionEntryModel txnEntry: txn.getEntries())
				{
					if (txnEntry.getType().equals(PaymentTransactionType.AUTHORIZATION)
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
	 * @return GPConsignmentService
	 */
	protected GPConsignmentService getGpConsignmentService() {
		return gpConsignmentService;
	}

	/**
	 * 
	 * @param gpConsignmentService
	 */
	public void setGpConsignmentService(GPConsignmentService gpConsignmentService) {
		this.gpConsignmentService = gpConsignmentService;
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
	 * 
	 * @return GPDebitCalculationService
	 */
	protected GPDebitCalculationService getGpDebitCalulationService() {
		return gpDebitCalulationService;
	}

	/**
	 * 
	 * @param gpDebitCalulationService
	 */
	public void setGpDebitCalulationService(GPDebitCalculationService gpDebitCalulationService) {
		this.gpDebitCalulationService = gpDebitCalulationService;
	}


	public EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
	public GPNetsuiteReplicationService getGpNetsuiteReplicationService() {
		return gpNetsuiteReplicationService;
	}

	public void setGpNetsuiteReplicationService(GPNetsuiteReplicationService gpNetsuiteReplicationService) {
		this.gpNetsuiteReplicationService = gpNetsuiteReplicationService;
	}

	/**
	 * @return the gpCRMShellOrderReplicationService
	 */
	public GPDefaultCRMShellOrderReplicationService getGpCRMShellOrderReplicationService() {
		return gpCRMShellOrderReplicationService;
	}

	/**
	 * @param gpCRMShellOrderReplicationService the gpCRMShellOrderReplicationService to set
	 */
	public void setGpCRMShellOrderReplicationService(
			GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService) {
		this.gpCRMShellOrderReplicationService = gpCRMShellOrderReplicationService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
	
	private int getCaptureSequence(OrderModel order) {
		int sequence = 1;
		if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
		{
			for (final PaymentTransactionModel txn : order.getPaymentTransactions())
			{
				for(final PaymentTransactionEntryModel txnEntry: txn.getEntries())
				{
					if (txnEntry.getType().equals(PaymentTransactionType.CAPTURE)
							&& TransactionStatus.ACCEPTED.name().equals(txnEntry.getTransactionStatus()))
					{
						sequence++;
					}
				}
			}
		}
		return sequence;
	}
		
	
		
	private int getCaptureTotal(OrderModel order) {
			int totalConsignmentEntries = 0;
			if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
			{
				for (final ConsignmentModel consignment : order.getConsignments())
				{
					for(final ConsignmentEntryModel consignmentEntry: consignment.getConsignmentEntries())
					{
							totalConsignmentEntries++;
					}
				}
			}
			
		return totalConsignmentEntries;
	}
	

	public GPPayPalIntegrationService getGpPayPalService() {
		return gpPayPalService;
	}

	public void setGpPayPalService(GPPayPalIntegrationService gpPayPalService) {
		this.gpPayPalService = gpPayPalService;
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
	@Override
	public boolean isAbortable() {
		return true;
	} 

}
