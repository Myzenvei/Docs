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
package com.gp.commerce.subscription.service;

import java.util.List;

import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.core.model.GPSubscriptionFrequencyModel;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.user.UserModel;

/**
 * The Interface GPSubscriptionService.
 */
public interface GPSubscriptionService {

	/**
	 * Gets the subsfrequencyfor product.
	 *
	 * @param productcode the productcode
	 * @return the subsfrequencyfor product
	 */
	List<GPSubscriptionFrequencyModel> getSubsfrequencyforProduct(String productcode);

	/**
	 * Adds the to cart.
	 *
	 * @param parameter the parameter
	 * @return the commerce cart modification
	 * @throws CommerceCartModificationException the commerce cart modification exception
	 */
	CommerceCartModification addToCart(CommerceCartParameter parameter) throws CommerceCartModificationException;
	
	/**
	 * Cancel subscription.
	 *
	 * @param code the code
	 * @return the list
	 */
	List<GPSubscriptionCartModel> cancelSubscription(String code);

	/**
	 * Gets the active subscriptions.
	 *
	 * @param currentUser the current user
	 * @return the active subscriptions
	 */
	List<GPSubscriptionCartModel> getActiveSubscriptions(UserModel currentUser) ;
	
	/**
	 * Gets the subscription cart models.
	 *
	 * @param susbscriptionCartStatus the susbscription cart status
	 * @param isActive the is active
	 * @return the subscription cart models
	 */
	public List<GPSubscriptionCartModel>  getSubscriptionCartModels(String susbscriptionCartStatus, boolean isActive);
	
	/**
	 * Update subscription.
	 *
	 * @param addData the add data
	 * @param payInfoData the pay info data
	 * @param code the code
	 */
	void updateSubscription(AddressData addData, CCPaymentInfoData payInfoData, String code);

	/**
	 * Gets the in active subscriptions.
	 *
	 * @param currentUser the current user
	 * @return the in active subscriptions
	 */
	List<GPSubscriptionCartModel> getInActiveSubscriptions(UserModel currentUser);
	
}
