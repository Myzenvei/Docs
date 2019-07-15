/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.cronjob;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.enums.GPConsignmentEntryStatus;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.order.impl.GPOmsOrderService;
import com.gp.commerce.core.order.service.GpOrderService;
import com.gp.commerce.core.services.GPDebitCalculationService;
import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.gpintegration.dto.out.paypal.Amount;
import com.gpintegration.dto.paypal.PayPalCaptureRequest;
import com.gpintegration.dto.paypal.PayPalCaptureResponse;
import com.gpintegration.service.GPCybersourceIntegrationService;
import com.gpintegration.service.GPPayPalIntegrationService;
import com.gpintegration.service.impl.GPDefaultCRMShellOrderReplicationService;

import de.hybris.platform.core.enums.OrderStatus;
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
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class GPPaymentCaptureCronJob extends AbstractJobPerformable<CronJobModel> {

	private static final Logger LOG = Logger.getLogger(GPPaymentCaptureCronJob.class);
	private GpOrderService gpOrderService;
	private GPCybersourceIntegrationService gpCyberSource;
	private GPOmsOrderService gpOMSOrderService;
	private GPPayPalIntegrationService gpPayPalService;
	private GPDebitCalculationService gpDebitCalulationService;
	private static final String REJECT = "REJECT";
	private static final String ACCEPT = "ACCEPT";
	private static final String TRANSACTION_STATUS_SUCCESS = "ACCEPTED";
	private static final String TRANSACTION_STATUS_FAILURE = "REJECTED";
	private static final String PENDING = "pending";
	private static final String COMPLETED = "completed";
	private int CAPTURE_SEQUENCE = 1;
	private int CAPTURE_TOTAL_COUNT = 1;
	@Resource
	private GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService;

	

	@Override
	public PerformResult perform(final CronJobModel cronjob) {
		LOG.info("Inside PaymentCaptureJob");
		final PerformResult cronJobResult;
		List<OrderModel> partiallyShippedOrders = new ArrayList<>();
		List<OrderModel> shippedOrders = new ArrayList<>();
		List<OrderModel> tobeCapturedOrders = new ArrayList<>();
		shippedOrders = getGpOrderService().getOrdersByStatus(OrderStatus.SHIPPED);
		partiallyShippedOrders = getGpOrderService().getOrdersByStatus(OrderStatus.PARTIALLY_SHIPPED);
		tobeCapturedOrders.addAll(shippedOrders);
		tobeCapturedOrders.addAll(partiallyShippedOrders);
		if (CollectionUtils.isEmpty(tobeCapturedOrders)) {
			LOG.info("No orders found in SHIPPED or PARTIALLY_SHIPPED status");
		}
		for (OrderModel order : tobeCapturedOrders) {
			if (clearAbortRequestedIfNeeded(cronjob))
			{
				LOG.debug("The job is aborted.");
				return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
			}
			if (isOrderShippedinOneShot(order)) {
				BigDecimal orderTotalPrice = new BigDecimal(String.valueOf(order.getTotalPrice()));
				BigDecimal orderTotalTax = new BigDecimal(String.valueOf(order.getTotalTax()));
				BigDecimal debitAmount = orderTotalPrice.add(orderTotalTax);
				// Invoke Payment Capture for debit
				boolean paymentStatus = capturePayment(debitAmount, order, true);
				if (!paymentStatus) {
					gpOMSOrderService.updateOrderByStatus(order, OrderStatus.PAYMENT_ERROR);
					LOG.error("Debit failed for order: " + order.getCode());
				} else {
					LOG.info("Debit success for order: " + order.getCode());
					updateAllItemsInOrderasCapture(order);
					gpOMSOrderService.updateOrderByStatus(order, OrderStatus.COMPLETED);
				}

			}
			else {
				for(ConsignmentEntryModel consignmentEntry: getShippedConsignmentEntries(order)) {
					BigDecimal debitAmount = gpDebitCalulationService.calculateDebit(consignmentEntry.getShippedQuantity().intValue(), consignmentEntry);
					if(capturePayment(debitAmount, order, false)) {
						consignmentEntry.setIsCaptured(true);
						modelService.save(consignmentEntry);
						if(isAllItemsAreCaptured(order))
							gpOMSOrderService.updateOrderByStatus(order, OrderStatus.COMPLETED);
					}
					else {
						gpOMSOrderService.updateConsignmentEntryStatus(consignmentEntry, GPConsignmentEntryStatus.PAYMENT_ERROR);
						LOG.error("Debit failed for order: " + order.getCode());
					}
					
				}
				
			}
			LOG.info(" CRM Shell order triggering status update on payment");
			gpCRMShellOrderReplicationService.updateCRMShellOrder(order);
		}

		LOG.info("Successfully completed executing GPPaymentCaptureCronJob");
		cronJobResult = new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.FINISHED);

		return cronJobResult;
	}

	private boolean isAllItemsAreCaptured(OrderModel order) {
		int totalItems = 0;
		int capturedCount = 0;
		for(ConsignmentModel consignment : order.getConsignments()) {
			for(ConsignmentEntryModel consignmentEntry: consignment.getConsignmentEntries()) {
				totalItems++;
				if(consignmentEntry.getIsCaptured() && (consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.SHIPPED) ||
						consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.CANCELLED))) {
					capturedCount++;	
				}
			}
		}	
		if(totalItems==capturedCount) {
			return true;
		}
		return false;
	}


	private void updateAllItemsInOrderasCapture(OrderModel order) {
		for(ConsignmentModel consignment : order.getConsignments()) {
			for(ConsignmentEntryModel consignmentEntry: consignment.getConsignmentEntries()) {
				consignmentEntry.setIsCaptured(true);
				modelService.save(consignmentEntry);
			}
			modelService.save(consignment);
		}
		modelService.save(order);
	}
	
	private List<ConsignmentEntryModel> getShippedConsignmentEntries(OrderModel order) {
		List<ConsignmentEntryModel> listOfShippedConsignmentEntries = new ArrayList<>();
		for(ConsignmentModel consignment : order.getConsignments()) {
			for(ConsignmentEntryModel consignmentEntry: consignment.getConsignmentEntries()) {
				if(!consignmentEntry.getIsCaptured() && consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.SHIPPED)) {
					listOfShippedConsignmentEntries.add(consignmentEntry);
				}
			}
		}
		return listOfShippedConsignmentEntries;
	}

	private boolean isOrderShippedinOneShot(OrderModel order) {
		int totalItems = 0;
		int shippedCount = 0;
		for(ConsignmentModel consignment : order.getConsignments()) {
			for(ConsignmentEntryModel consignmentEntry: consignment.getConsignmentEntries()) {
				totalItems++;
				//Just to make sure old orders to have this Captured field.
				if(consignmentEntry.getIsCaptured()==null)
				{
					consignmentEntry.setIsCaptured(false);
					modelService.saveAll(consignmentEntry,consignment,order);
				}
				
				if(!consignmentEntry.getIsCaptured() && consignmentEntry.getConsignmentEntryStatus().equals(GPConsignmentEntryStatus.SHIPPED)) {
					shippedCount++;
				}
			}
		}
		if((shippedCount==totalItems) && (totalItems!=0) && (shippedCount!=0))
			return true;
		return false;
	}

	/**
	 * 
	 * @param debitAmount
	 * @param order
	 */
	protected boolean capturePayment(BigDecimal debitAmount, OrderModel order, boolean isSingleShotCapture) {
		if(!isSingleShotCapture) {
			CAPTURE_SEQUENCE = getCaptureSequence(order);
			CAPTURE_TOTAL_COUNT = getCaptureTotal(order);
		}
		LOG.debug("Initiate Debit Capture for order: " + order.getCode());

		ReplyMessage replyMessage = null;
		String requestID = null;
		boolean isSuccess = false;

		requestID = getRequestIDofAuth(order);
		LOG.debug("Request Id for Debit Capture: " + requestID);

		try {
				PaymentTransactionEntryModel paymentTransactionEntry = null;
				if(order.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
					replyMessage = gpCyberSource.billCapture(order.getCode(), debitAmount,
							Currency.getInstance(order.getCurrency().getIsocode()), requestID, order.getSite().getUid(),CAPTURE_SEQUENCE,CAPTURE_TOTAL_COUNT);
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
				List<PaymentTransactionEntryModel> transactionEntries = new ArrayList<PaymentTransactionEntryModel>(
						paymentTransaction.getEntries());
				transactionEntries.add(paymentTransactionEntry);
				paymentTransaction.setEntries(transactionEntries);
				modelService.save(paymentTransaction);
				if (paymentTransactionEntry.getTransactionStatus().equals(TRANSACTION_STATUS_SUCCESS))
					isSuccess = true;
				else
					isSuccess = false;
				LOG.info("Payment capture success: " + isSuccess);
		} catch (Exception e) {
			isSuccess = false;
			LOG.error("Error in saving the payment Transactions: " + e.getMessage(),e);
		}

		return isSuccess;
	}

	private String getRequestIDofAuth(OrderModel order) {
		String requestID = null;
		if (order.getPaymentInfo() instanceof CreditCardPaymentInfoModel || order.getPaymentInfo() instanceof PaypalPaymentInfoModel) {
			for (final PaymentTransactionModel txn : order.getPaymentTransactions()) {
				for (final PaymentTransactionEntryModel txnEntry : txn.getEntries()) {
					if (txnEntry.getType().equals(PaymentTransactionType.AUTHORIZATION)
							&& TransactionStatus.ACCEPTED.name().equals(txnEntry.getTransactionStatus())) {
						return txnEntry.getRequestId();
					}
				}
			}
		}
		return requestID;
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
			transactionEntry.setCode(response.getId());
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

	/**
	 * 
	 * @param replyMessage
	 * @return
	 */
	protected PaymentTransactionEntryModel createPaymentTransactionEntry(ReplyMessage replyMessage) {
		String captureTransactionDecision = replyMessage.getDecision();
		LOG.debug("Capture transaction decision: " + captureTransactionDecision);

		PaymentTransactionEntryModel transactionEntry = modelService.create(PaymentTransactionEntryModel.class);
		transactionEntry.setRequestId(replyMessage.getRequestID());
		transactionEntry.setRequestToken(replyMessage.getRequestToken());
		transactionEntry.setCode(replyMessage.getRequestID());
		transactionEntry.setType(PaymentTransactionType.CAPTURE);

		if (ACCEPT.equalsIgnoreCase(captureTransactionDecision)) {
			transactionEntry.setAmount(new BigDecimal(replyMessage.getCcCaptureReply().getAmount()));
			transactionEntry.setSubscriptionID(replyMessage.getMerchantReferenceCode());
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_SUCCESS);
		} else if (REJECT.equalsIgnoreCase(captureTransactionDecision)) {
			transactionEntry.setTransactionStatusDetails(replyMessage.getReasonCode().toString());
			transactionEntry.setAmount(new BigDecimal(0));
			transactionEntry.setTransactionStatus(TRANSACTION_STATUS_FAILURE);
			LOG.error("PAYMENT_ERROR | Payment Capture failed with reasonCode: " + replyMessage.getReasonCode());
		}

		modelService.save(transactionEntry);
		return transactionEntry;
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
	public GpOrderService getGpOrderService() {
		return gpOrderService;
	}

	@Required
	public void setGpOrderService(final GpOrderService gpOrderService) {
		this.gpOrderService = gpOrderService;
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
	
	public GPPayPalIntegrationService getGpPayPalService() {
		return gpPayPalService;
	}

	public void setGpPayPalService(GPPayPalIntegrationService gpPayPalService) {
		this.gpPayPalService = gpPayPalService;
	}

	public GPDebitCalculationService getGpDebitCalulationService() {
		return gpDebitCalulationService;
	}

	public void setGpDebitCalulationService(GPDebitCalculationService gpDebitCalulationService) {
		this.gpDebitCalulationService = gpDebitCalulationService;
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
	
	@Override
	public boolean isAbortable() {
		return true;
	} 
	

}
