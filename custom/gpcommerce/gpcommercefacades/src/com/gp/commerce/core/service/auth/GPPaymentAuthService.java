/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.service.auth;

import com.gp.commerce.facade.data.PaypalPayerData;
import com.gp.commerce.order.data.GPApplePayPaymentInfoData;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

/**
 * Interface for payment authentication.
 */
public interface GPPaymentAuthService {

	/**
	 * This method gets credit card payment authentication.
	 *
	 * @param cartParameter 			the cart parameter
	 * @return payment transaction authentication
	 */
	PaymentTransactionEntryModel getCCPaymentAuthentication(CommerceCheckoutParameter cartParameter);
	
	/**
	 * Fetches Paypal token for the given baseSite.
	 *
	 * @param baseSite the base site
	 * @return payal token
	 */
	public String getPaypalToken(BaseSiteModel baseSite);
	
	/**
	 * Returns the payment transaction record for the given cart parameter.
	 *
	 * @param cartParameter the cart parameter
	 * @return {@link PaymentTransactionEntryModel}
	 */
	public PaymentTransactionEntryModel executeAuthorizePayment(CommerceCheckoutParameter cartParameter);
	
	/**
	 * Returns authorized payment transaction for the given cart, url and the paypal type.
	 *
	 * @param cart       the cart
	 * @param paypalType the paypal type
	 * @param url        the url
	 * @return paypal transaction
	 */
	public String authorizePaypalTransaction(CartModel cart, String paypalType, String url);
	
	/**
	 * Returns a boolean value, by checking whether there is a payment transaction
	 * for the given cart parameter.
	 *
	 * @param cartParameter the commerce checkout parameter
	 * @return <b>true</b> if there is a void payment transaction <br>
	 *         <b>false</b> otherwise
	 */
	public boolean voidPaypalTransaction(CommerceCheckoutParameter cartParameter);
	
	/**
	 * Returns a boolean value, by checking whether there is a settle transaction
	 * for the given cart parameter, and order.
	 *
	 * @param cartParameter the commerce cehckout parameter
	 * @param orderModel    the order
	 * @return <b>true</b> if there is a settle payment transaction <br>
	 *         <b>false</b> otherwise
	 */
	public boolean settlePaypalTransaction(CommerceCheckoutParameter cartParameter,OrderModel orderModel);
	
	/**
	 * Returns a boolean value, by checking whether the paypal details are saved for
	 * the given cart parameter, and order.
	 *
	 * @param cart the cart
	 * @param paypalPayerInfo the paypal payer info
	 * @return <b>true</b> if the payment details are saved<br>
	 *         <b>false</b> otherwise
	 */
	public boolean savePayPalDetails(CartModel cart, PaypalPayerData paypalPayerInfo);
	
	/**
	 * Returns Payment transaction record for the given ApplePay Payment info and
	 * commerce checkout parameter.
	 *
	 * @param gpApplePayPaymentInfoData the {@link GPApplePayPaymentInfoData}
	 * @param cartParameter             the {@link CommerceCheckoutParameter}
	 * @return {@link PaymentTransactionEntryModel}
	 */
	public PaymentTransactionEntryModel getApplePayPaymentAuthentication(GPApplePayPaymentInfoData gpApplePayPaymentInfoData, CommerceCheckoutParameter cartParameter);

}
