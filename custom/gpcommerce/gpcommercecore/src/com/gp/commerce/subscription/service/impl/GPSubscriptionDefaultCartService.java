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
package com.gp.commerce.subscription.service.impl;


import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPSubscriptionCartModel ;


/**
 * The Class GPSubscriptionDefaultCartService.
 * 
 * @author shikhgupta
 */
public class GPSubscriptionDefaultCartService extends DefaultCartService
{
	@Resource(name = "baseSiteService")
	private transient BaseSiteService baseSiteService;

	@Resource(name = "baseStoreService")
	private transient BaseStoreService baseStoreService;

	@Resource(name = "guidKeyGenerator")
	private transient KeyGenerator guidKeyGenerator;

	@Resource(name = "cartFactory")
	private transient CartFactory cartFactory;

	@Resource(name = "userService")
	private transient UserService userService;
	@Resource(name = "cartService")
	private transient CartService cartService;
	

	/**
	 * Removes the session cart.
	 */
	@Override
	public void removeSessionCart()
	{
		if (hasSessionCart())
		{
			final CartModel sessionCart = getSessionCart();
			getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
			if (!(sessionCart instanceof GPSubscriptionCartModel))
			{
				getModelService().remove(sessionCart);
			}
			 
		}
	}
	
	/**
	 * @param cart the cart
	 */
	@Override
	public void setSessionCart(final CartModel cart)
	{
		if (cart == null)
		{
			 removeSessionCart();
		}
		else
		{
			if(cart.getIsSubscription()!=null && cart.getIsSubscription()) {
				List<CartModel> cartList=new ArrayList<>();
				final Collection<CartModel> carts = userService.getCurrentUser().getCarts() ;
				for( CartModel cartmodel : carts ) {
					if(cartmodel instanceof CartModel &&  !(cartmodel instanceof GPSubscriptionCartModel)) {
						cartList.add(cartmodel) ;
					}
				}
					if(CollectionUtils.isNotEmpty(cartList)) {
					getSessionService().setAttribute(SESSION_CART_PARAMETER_NAME, cartList.get(0));
					}
				
			}else {
				getSessionService().setAttribute(SESSION_CART_PARAMETER_NAME, cart);
			}
		}
	}  

}
