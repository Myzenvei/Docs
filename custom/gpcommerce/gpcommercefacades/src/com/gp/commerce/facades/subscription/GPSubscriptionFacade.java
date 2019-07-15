/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.subscription;


import java.util.List;
import java.util.Map;

import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.facades.subscription.data.GPSubscriptionFrequencyListData;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;
 
/**
 * The Interface GPSubscriptionFacade.
 */
public interface GPSubscriptionFacade {

	/**
	 * Gets the subscription frequency.
	 *
	 * @param productCode the product code
	 * @return the subscription frequency
	 */
	GPSubscriptionFrequencyListData getSubscriptionFrequency(String productCode);

	/**
	 * Adds the to cart internal.
	 *
	 * @param code the code
	 * @param quantity the quantity
	 * @param subscriptionFrequecy the subscription frequecy
	 * @param cartId the cart id
	 * @return the cart modification data
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	CartModificationData addToCartInternal(String code, long quantity, Map<String, String> subscriptionFrequecy,
			String cartId) throws CommerceCartModificationException;

	/**
	 * Adds the to cart.
	 *
	 * @param addToCartParams the add to cart params
	 * @return the cart modification data
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	CartModificationData addToCart(AddToCartParams addToCartParams) throws CommerceCartModificationException;

	/**
	 * Gets the supported delivery modes.
	 *
	 * @param cartId the cart id
	 * @return the supported delivery modes
	 * @throws Exception the exception
	 */
	List<? extends DeliveryModeData> getSupportedDeliveryModes(String cartId) throws Exception;


	/**
	 * Sets the payment details.
	 *
	 * @param paymentDetailsId the payment details id
	 * @param cartId the cart id
	 * @return true, if successful
	 */
	boolean setPaymentDetails(String paymentDetailsId, String cartId);

	/**
	 * Authorize payment.
	 *
	 * @param securityCode the security code
	 * @param cartId the cart id
	 * @return true, if successful
	 */
	boolean authorizePayment(String securityCode, String cartId);

	/**
	 * Place order.
	 *
	 * @param cartId the cart id
	 * @return the order data
	 * @throws InvalidCartException the invalid cart exception
	 */
	OrderData placeOrder(String cartId) throws InvalidCartException;

	/**
	 * Cancel subscription.
	 *
	 * @param code the code
	 * @return the GP subscription cart model
	 */
	GPSubscriptionCartModel cancelSubscription(String code);
	
	/**
	 * Gets the active subscriptions.
	 *
	 * @return the active subscriptions
	 */
	List<CartData> getActiveSubscriptions();
	
	/**
	 * Update subscription.
	 *
	 * @param addData the add data
	 * @param payInfoData the pay info data
	 * @param code the code
	 */
	void updateSubscription(AddressData addData, CCPaymentInfoData payInfoData, String code);

	/**
	 * Gets the subs cart model.
	 *
	 * @param cartId the cart id
	 * @return the subs cart model
	 */
	CartModel getSubsCartModel(String cartId);

	/**
	 * Removes the in active subscriptions.
	 */
	void removeInActiveSubscriptions();
}
