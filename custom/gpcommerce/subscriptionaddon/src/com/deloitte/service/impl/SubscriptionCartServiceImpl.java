/**
 *
 */
package com.deloitte.service.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;

import javax.annotation.Resource;

import com.deloitte.dao.SubscriptionCartDao;
import com.deloitte.model.SubscriptionCartModel;
import com.deloitte.service.SubscriptionCartService;
import com.deloitte.strategy.SubscriptionCommerceAddToCartStrategy;


/**
 * @author asomjal
 *
 */
public class SubscriptionCartServiceImpl implements SubscriptionCartService
{
	@Resource(name = "subscriptionCommerceAddToCartStrategy")
	private SubscriptionCommerceAddToCartStrategy subscriptionCommerceAddToCartStrategy;

	@Resource(name = "subscriptionCartDaoImpl")
	private SubscriptionCartDao subscriptionCartDaoImpl;

	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		return subscriptionCommerceAddToCartStrategy.addToCart(parameter);
	}

	@Override
	public List<SubscriptionCartModel> getSubscriptionCartModels()
	{
		return subscriptionCartDaoImpl.getSubscriptionCartModels();
	}

	@Override
	public List<SubscriptionCartModel> getSubcriptionCartsForUser(final String userId)
	{
		return subscriptionCartDaoImpl.getSubcriptionCartsForUser(userId);
	}
}
