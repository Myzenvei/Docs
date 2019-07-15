/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;

import java.math.BigDecimal;
import java.util.Currency;

import com.cybersource.schemas.transaction_data_1.ReplyMessage;

/**
 * Interface for Cybersource integration.
 */
public interface GPCybersourceIntegrationService {

	/**
	 * Authorizes Credit card.
	 *
	 * @param authorizeDetails the authorize details
	 * @param cartParameter the cart parameter
	 * @param baseSiteId the base site id
	 * @param isCreditCard the is credit card
	 * @param isGooglePay the is google pay
	 * @param isApplePay the is apple pay
	 * @return ReplyMessage
	 * @throws Exception the exception
	 */
	ReplyMessage authorizeUsingCybersource(CCPaymentInfoData authorizeDetails, CommerceCheckoutParameter cartParameter,
			String baseSiteId,boolean isCreditCard,boolean isGooglePay,boolean isApplePay) throws Exception;

	/**
	 * Reverse Authorizes Credit card.
	 *
	 * @param referenceCode the reference code
	 * @param amount the amount
	 * @param currency the currency
	 * @param requestId the request id
	 * @param baseSiteId the base site id
	 * @return ReplyMessage
	 * @throws Exception the exception
	 */
	ReplyMessage reverseAuthorization(String referenceCode, BigDecimal amount, Currency currency, String requestId,
			String baseSiteId) throws Exception;

	/**
	 * Void captures.
	 *
	 * @param referenceCode the reference code
	 * @param amount the amount
	 * @param currency the currency
	 * @param requestId the request id
	 * @param baseSiteId the base site id
	 * @return ReplyMessage
	 * @throws Exception the exception
	 */
	ReplyMessage voidCapture(String referenceCode, BigDecimal amount, Currency currency, String requestId, String baseSiteId)
			throws Exception;

	/**
	 * Bill captures.
	 *
	 * @param referenceCode the reference code
	 * @param amount the amount
	 * @param currency the currency
	 * @param requestId the request id
	 * @param baseSiteId the base site id
	 * @param captureSequence the capture sequence
	 * @param captureTotal the capture total
	 * @return ReplyMessage
	 * @throws Exception the exception
	 */
	public ReplyMessage billCapture(String referenceCode, BigDecimal amount, Currency currency, String requestId, String baseSiteId, int captureSequence, int captureTotal)
			throws Exception;

}
