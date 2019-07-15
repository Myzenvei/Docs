/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Currency;

import javax.annotation.Resource;

import com.cybersource.schemas.transaction_data_1.*;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;
import com.gp.commerce.order.data.GooglePayPaymentInfoData;
import javolution.io.Struct;
import org.apache.log4j.Logger;

import com.cybersource.schemas.transaction_data.transactionprocessor.ITransactionProcessor;
import com.cybersource.schemas.transaction_data.transactionprocessor.TransactionProcessor;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPDataException;
import com.gpintegration.service.GPCybersourceIntegrationService;
import com.gpintegration.utils.BuildCybsCallData;
import com.gpintegration.utils.GPHeaderHandlerResolver;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

/**
 * The Class DefaultGPCommerceCybersourceIntegrationService.
 */
public class DefaultGPCommerceCybersourceIntegrationService extends DefaultPaymentServiceImpl
		implements GPCybersourceIntegrationService {

	@Resource
	private SessionService sessionService;

	private static final Logger LOG = Logger.getLogger(DefaultGPCommerceCybersourceIntegrationService.class);
	
	private static final String CYBERSOURCE = "CyberSource";

	
	/**
	 * Authorize using cybersource and build request object based on payment type
	 *
	 * @param authorizeDetails the authorize details
	 * @param cardParameter    the card parameter
	 * @param baseSiteId       the base site id
	 * @param isCreditCard     the is credit card
	 * @param isGooglePay      the is google pay
	 * @param isApplePay       the is apple pay
	 * @return the reply message
	 */
	public ReplyMessage authorizeUsingCybersource(CCPaymentInfoData authorizeDetails,CommerceCheckoutParameter cardParameter, String baseSiteId,boolean isCreditCard,boolean isGooglePay,boolean isApplePay) {

		ReplyMessage authorizeResponse = null;

		RequestMessage authorizeRequest = createAuthorizeRequest(authorizeDetails,cardParameter,baseSiteId,isCreditCard, isGooglePay, isApplePay);

		GPHeaderHandlerResolver gpHeaderHandlerResolver = new GPHeaderHandlerResolver(baseSiteId,CYBERSOURCE);

		TransactionProcessor transactionProcessor;

		try {
			transactionProcessor = new TransactionProcessor(new URL(Config.getParameter(GpintegrationConstants.SERVER_URL)));
			transactionProcessor.setHandlerResolver(gpHeaderHandlerResolver);
			final ITransactionProcessor processor = transactionProcessor.getPortXML();
			authorizeResponse = processor.runTransaction(authorizeRequest);
		} catch (MalformedURLException cyberSourceException) {
			LOG.error("Error on forming cybersource server URL", cyberSourceException);
			throw new GPDataException("Execption occured at forming cybersource server URL");
		} catch (Exception exp) {
			LOG.error("Exception occured at cybersource credit card authorize request", exp);
			throw new GPDataException("Error in cybersource credit card authorize request");
		}

		return authorizeResponse;

	}


	private static void addClientLibraryInfo(final RequestMessage request) {
		String CLIENT_ENV = System.getProperty(GpintegrationConstants.OS_NAME) + "/"
				+ System.getProperty(GpintegrationConstants.OS_VERSION) + "/"
				+ System.getProperty(GpintegrationConstants.JAVA_VENDOR) + "/"
				+ System.getProperty(GpintegrationConstants.JAVA_VERSION);
		request.setClientLibrary(Config.getParameter(GpintegrationConstants.CLIENT_LIBRARY));
		request.setClientLibraryVersion(Config.getParameter(GpintegrationConstants.CLIENT_LIB_VERSION));
		request.setClientEnvironment(CLIENT_ENV);
	}

	/**
	 * Method to build auth request object for cybersource.
	 *
	 * @param authorizeDetails the authorize details
	 * @param cardParameter    the card parameter
	 * @param baseSiteId       the base site id
	 * @param isCreditCard     the is credit card
	 * @param isGooglePay      the is google pay
	 * @param isApplePay       the is apple pay
	 * @return RequestMessage - that needs to be sent to cybersource for auth
	 */
	private RequestMessage createAuthorizeRequest(CCPaymentInfoData authorizeDetails, CommerceCheckoutParameter cardParameter, String baseSiteId,boolean isCreditCard,boolean isGooglePay,boolean isApplePay) {

		RequestMessage authorizeRequest = new RequestMessage();
		addClientLibraryInfo(authorizeRequest);
		authorizeRequest.setMerchantID(Config.getParameter(GpintegrationConstants.MERCHANT_ID+baseSiteId));
		if(null!=authorizeDetails && null!=cardParameter) {
		authorizeRequest.setBillTo(mapBillToAddressRequest(authorizeDetails));

		PurchaseTotals purchaseTotals = new PurchaseTotals();
		if(null!=cardParameter.getCart() && null!=cardParameter.getCart().getCurrency()) {
			purchaseTotals.setCurrency(cardParameter.getCart().getCurrency().getIsocode());
			BigDecimal totalCost = BigDecimal.valueOf(cardParameter.getCart().getTotalPrice()).add(BigDecimal.valueOf(cardParameter.getCart().getTotalTax()));
			purchaseTotals.setGrandTotalAmount(Double.toString(totalCost.doubleValue()));
		}
		authorizeRequest.setPurchaseTotals(purchaseTotals);
		authorizeRequest.setCcAuthService(mapCCAuthServiceRequest());

		if(isCreditCard){

			populateCardAuthRequestParams(authorizeRequest,authorizeDetails,cardParameter);
		}

		if(isGooglePay){

			populateGooglePayAuthRequestParams(authorizeRequest,(GooglePayPaymentInfoData) authorizeDetails,cardParameter);
		}
		
		if(isApplePay){

			populateApplePayAuthRequestParams(authorizeRequest,(GPApplePayPaymentInfoData) authorizeDetails,cardParameter);
		}

		}
		return authorizeRequest;
	}

	/**
	 * Add credicard specific params to cybersource request.
	 *
	 * @param authorizeRequest the authorize request
	 * @param authorizeDetails the authorize details
	 * @param cardParameter    the card parameter
	 */
	private void populateCardAuthRequestParams(RequestMessage authorizeRequest,CCPaymentInfoData authorizeDetails,CommerceCheckoutParameter cardParameter){

		String TRANSACTION_DESCRIPTION = Config.getParameter(GpintegrationConstants.TRANSACTION_DECRIPTION);

		String merchantRefCode = (null!=cardParameter && null!=cardParameter.getCart() && null!=cardParameter.getCart().getCode() )?cardParameter.getCart().getCode():TRANSACTION_DESCRIPTION;

		authorizeRequest.setMerchantReferenceCode(merchantRefCode);
		authorizeRequest.setShipTo(mapShipToAddressRequest(authorizeDetails));
		authorizeRequest.setCard(mapCardRequest(cardParameter));
		RecurringSubscriptionInfo subscription = new RecurringSubscriptionInfo();
		subscription.setSubscriptionID(null!=authorizeDetails?authorizeDetails.getPaymentToken():null);
		authorizeRequest.setRecurringSubscriptionInfo(subscription);
	}


	/**
	 * Add Googlepay specific params to cybersource request.
	 *
	 * @param authorizeRequest the authorize request
	 * @param authorizeDetails the authorize details
	 * @param cardParameter    the card parameter
	 */
	private void populateGooglePayAuthRequestParams(RequestMessage authorizeRequest,GooglePayPaymentInfoData authorizeDetails,CommerceCheckoutParameter cardParameter){

		authorizeRequest.setMerchantReferenceCode(authorizeDetails.getMerchantRefCodeDesc());
		authorizeRequest.setCard(mapGooglePayCardType(authorizeDetails.getCardType()));
		authorizeRequest.setEncryptedPayment(buildEncryptedPayament(authorizeDetails.getPaymentToken()));
		authorizeRequest.setPaymentSolution(GpintegrationConstants.GOOGLE_PAY_PAYMENT_SOLUTION);
	}
	
	private void populateApplePayAuthRequestParams(RequestMessage authorizeRequest,
			GPApplePayPaymentInfoData authorizeDetails, CommerceCheckoutParameter cardParameter) {
		
		authorizeRequest.setMerchantReferenceCode(cardParameter.getCart().getCode());
		authorizeRequest.setEncryptedPayment(buildEncryptedPayment(authorizeDetails.getPaymentData()));	
		authorizeRequest.setCard(mapApplePayCardType(authorizeDetails.getCardType()));
		authorizeRequest.setPaymentSolution(GpintegrationConstants.APPLE_PAY_PAYMENT_SOLUTION);
		
	}

	/**
	 * Method to set encoded token to cybersource request.
	 *
	 * @param token the token
	 * @return EncryptedPayment
	 */
	private EncryptedPayment buildEncryptedPayament(String token){

		EncryptedPayment encryptedPayment=new EncryptedPayment();

		String encoded=Base64.getEncoder().encodeToString(token.getBytes());

		encryptedPayment.setData(encoded);

		return encryptedPayment;
	}
	
	private EncryptedPayment buildEncryptedPayment(String token)
	{
		EncryptedPayment encryptedPayment = new EncryptedPayment();
		encryptedPayment.setDescriptor(GpintegrationConstants.APPLE_PAY_ENCRYPTED_PAYMENT_DESC);
		encryptedPayment.setData(token);
		encryptedPayment.setEncoding(GpintegrationConstants.CYBERSOURCE_ENCODING);
		return encryptedPayment;
	}

	private BillTo mapBillToAddressRequest(CCPaymentInfoData authorizeDetails) {
		BillTo authorizeBillTo = null;
			authorizeBillTo = new BillTo();
			authorizeBillTo.setCity(authorizeDetails.getBillingAddress().getTown());
			authorizeBillTo.setCountry(authorizeDetails.getBillingAddress().getCountry().getName());
			authorizeBillTo.setEmail(authorizeDetails.getBillingAddress().getEmail());
			authorizeBillTo.setFirstName(authorizeDetails.getBillingAddress().getFirstName());
			authorizeBillTo.setLastName(authorizeDetails.getBillingAddress().getLastName());
			authorizeBillTo.setPhoneNumber(authorizeDetails.getBillingAddress().getPhone());
			authorizeBillTo.setPostalCode(authorizeDetails.getBillingAddress().getPostalCode());
			authorizeBillTo.setTitle(authorizeDetails.getBillingAddress().getTitle());
			authorizeBillTo.setState(authorizeDetails.getBillingAddress().getRegion().getName());
			authorizeBillTo.setStreet1(authorizeDetails.getBillingAddress().getLine1());
		return authorizeBillTo;
	}

	private ShipTo mapShipToAddressRequest(CCPaymentInfoData authorizeDetails) {
		ShipTo authorizeShipTo = null;
		if (null != authorizeDetails) {
			authorizeShipTo = new ShipTo();
			authorizeShipTo.setCity(authorizeDetails.getBillingAddress().getRegion().getName());
			authorizeShipTo.setCountry(authorizeDetails.getBillingAddress().getCountry().getName());
			authorizeShipTo.setFirstName(authorizeDetails.getBillingAddress().getFirstName());
			authorizeShipTo.setLastName(authorizeDetails.getBillingAddress().getLastName());
			authorizeShipTo.setPhoneNumber(authorizeDetails.getBillingAddress().getPhone());
			authorizeShipTo.setPostalCode(authorizeDetails.getBillingAddress().getPostalCode());
			authorizeShipTo.setState(authorizeDetails.getBillingAddress().getRegion().getName());
			authorizeShipTo.setStreet1(authorizeDetails.getBillingAddress().getLine1());
		}
		return authorizeShipTo;
	}

	private Card mapCardRequest(CommerceCheckoutParameter cardParameter) {
		Card authorizeCard = null;
		if (null != cardParameter) {
			authorizeCard = new Card();
			authorizeCard.setCvNumber(cardParameter.getSecurityCode());
		}
		return authorizeCard;
	}

	private CCAuthService mapCCAuthServiceRequest() {
		CCAuthService ccAuthService = new CCAuthService();
		ccAuthService.setRun(GpintegrationConstants.SERVICE_RUN_TRUE);
		return ccAuthService;

	}

	/**
	 *  Method to set correct code.
	 *
	 * @param cardType the card type
	 * @return Card
	 */
	private Card mapGooglePayCardType(String cardType) {

		Card authorizeCard = new Card();

		switch(cardType.toLowerCase()){

			case GpintegrationConstants.VISA: authorizeCard.setCardType(GpintegrationConstants.VISA_CARDTYPE);
			     break;
			case GpintegrationConstants.MASTER_CARD: authorizeCard.setCardType(GpintegrationConstants.MASTER_CARD_CARDTYPE);
				 break;
			case GpintegrationConstants.AMEX: authorizeCard.setCardType(GpintegrationConstants.AMEX_CARDTYPE);
				 break;
			case GpintegrationConstants.DISCOVER: authorizeCard.setCardType(GpintegrationConstants.DISCOVER_CARDTYPE);
				 break;

		}

		return authorizeCard;
	}
	
	private Card mapApplePayCardType(String cardType) {
			
			Card authorizeCard = new Card();
			
			switch(cardType.toLowerCase()) {
	
				case GpintegrationConstants.CREDIT_CARD_TYPE_VISA: authorizeCard.setCardType(GpintegrationConstants.CREDIT_CARD_TYPE_VISA_VALUE);
				             break;
				case GpintegrationConstants.CREDIT_CARD_TYPE_MASTERCARD: authorizeCard.setCardType(GpintegrationConstants.CREDIT_CARD_TYPE_MASTERCARD_VALUE);
					break;
				case GpintegrationConstants.CREDIT_CARD_TYPE_AMEX: authorizeCard.setCardType(GpintegrationConstants.CREDIT_CARD_TYPE_AMEX_VALUE);
					break;
				case GpintegrationConstants.CREDIT_CARD_TYPE_DISCOVER: authorizeCard.setCardType(GpintegrationConstants.CREDIT_CARD_TYPE_DISCOVER_VALUE);
					break;
			}
	
			return authorizeCard;
		}

	/**
	 * This method does reverse authorization.
	 *
	 * @param referenceCode the reference code
	 * @param amount        the amount
	 * @param currency      the currency
	 * @param requestId     the request id
	 * @param baseSiteId    the base site id
	 * @return reply message object for reverse auth
	 */
	public ReplyMessage reverseAuthorization(String referenceCode, BigDecimal amount, Currency currency, String requestId, String baseSiteId) {
		final RequestMessage request = new RequestMessage();
		request.setMerchantID(Config.getParameter(GpintegrationConstants.MERCHANT_ID+baseSiteId));
		request.setMerchantReferenceCode(referenceCode);
		request.setCcAuthReversalService(new CCAuthReversalService());
		request.getCcAuthReversalService().setRun(GpintegrationConstants.SERVICE_RUN_TRUE);
		request.getCcAuthReversalService().setAuthRequestID(requestId);
		request.setPurchaseTotals(BuildCybsCallData.buildPurchaseTotals(currency.getCurrencyCode(), amount.toString()));

		try {
			final GPHeaderHandlerResolver gpHeaderHandlerResolver = new GPHeaderHandlerResolver(baseSiteId,CYBERSOURCE);
			final TransactionProcessor transactionProcessor = new TransactionProcessor(new URL(Config.getParameter(GpintegrationConstants.SERVER_URL)));
			transactionProcessor.setHandlerResolver(gpHeaderHandlerResolver);
			final ITransactionProcessor processor = transactionProcessor.getPortXML();
			return processor.runTransaction(request);
		} catch (final Exception e) {
			LOG.error("Error in cybersource void capture for subscription Id :" + requestId ,e);
			throw new GPDataException("Error in cybersource credit card reverse authorize request");
		}

	}


	/**
	 * This method is used to refund and unhold.
	 *
	 * @param referenceCode the reference code
	 * @param amount        the amount
	 * @param currency      the currency
	 * @param requestId     the request id
	 * @param baseSiteId    the base site id
	 * @return ReplyMessage
	 */
	public ReplyMessage voidCapture(String referenceCode, BigDecimal amount, Currency currency, String requestId, String baseSiteId) {
		final RequestMessage request = new RequestMessage();
		request.setMerchantID(Config.getParameter(GpintegrationConstants.MERCHANT_ID+baseSiteId));
		request.setMerchantReferenceCode(referenceCode);
		request.setVoidService(new VoidService());
		request.getVoidService().setRun(GpintegrationConstants.SERVICE_RUN_TRUE);
		request.getVoidService().setVoidRequestID(requestId);
		request.setPurchaseTotals(BuildCybsCallData.buildPurchaseTotals(currency.getCurrencyCode(), amount.toString()));
		try {
			final GPHeaderHandlerResolver gpHeaderHandlerResolver = new GPHeaderHandlerResolver(baseSiteId,CYBERSOURCE);
			final TransactionProcessor transactionProcessor = new TransactionProcessor(new URL(Config.getParameter(GpintegrationConstants.SERVER_URL)));
			transactionProcessor.setHandlerResolver(gpHeaderHandlerResolver);
			final ITransactionProcessor processor = transactionProcessor.getPortXML();
			return processor.runTransaction(request);
		} catch (final Exception e) {
			LOG.error("Error in cybersource void capture for subscription Id :" + requestId ,e);
			throw new GPDataException("Error in cybersource credit card void capture request");
		}
	}

	/**
	 * This method is used to credit or settle.
	 *
	 * @param referenceCode   the reference code
	 * @param amount          the amount
	 * @param currency        the currency
	 * @param requestId       the request id
	 * @param baseSiteId      the base site id
	 * @param captureSequence the capture sequence
	 * @param captureTotal    the capture total
	 * @return ReplyMessage
	 */
	public ReplyMessage billCapture(String referenceCode, BigDecimal amount, Currency currency, String requestId, String baseSiteId, int captureSequence, int captureTotal) {
		final RequestMessage request = new RequestMessage();
		request.setMerchantID(Config.getParameter(GpintegrationConstants.MERCHANT_ID+baseSiteId));
		request.setMerchantReferenceCode(referenceCode);
		request.setCcCaptureService(new CCCaptureService());
		if(Boolean.parseBoolean(Config.getParameter(GpintegrationConstants.CAPTURE_COUNT_REQUIRED))) {
			LOG.info("Additional Parametrs CaptureSequence: "+ captureSequence +",captureTotal: "+captureTotal+"are added");
			request.getCcCaptureService().setTotalCount(String.valueOf(captureTotal));
			request.getCcCaptureService().setSequence(String.valueOf(captureSequence));
		}
		request.getCcCaptureService().setRun(GpintegrationConstants.SERVICE_RUN_TRUE);
		request.getCcCaptureService().setAuthRequestID(requestId);
		request.setPurchaseTotals(BuildCybsCallData.buildPurchaseTotals(currency.getCurrencyCode(), amount.toString()));
		try {
			final GPHeaderHandlerResolver gpHeaderHandlerResolver = new GPHeaderHandlerResolver(baseSiteId,CYBERSOURCE);
			final TransactionProcessor transactionProcessor = new TransactionProcessor(new URL(Config.getParameter(GpintegrationConstants.SERVER_URL)));
			transactionProcessor.setHandlerResolver(gpHeaderHandlerResolver);
			final ITransactionProcessor processor = transactionProcessor.getPortXML();
			return processor.runTransaction(request);
		} catch (final Exception e) {
			LOG.error("Error in cybersource void capture for subscription Id :" + requestId ,e);
			throw new GPDataException("Error in cybersource credit card authorize request");
		}
	}

}
