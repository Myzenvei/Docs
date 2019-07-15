/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.strategies;

import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.strategies.impl.DefaultCartLoaderStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import com.gp.commerce.core.enums.CartTypeEnum;
import com.gp.commerce.core.model.PaypalPaymentInfoModel;
import com.gp.commerce.core.services.GPCMSSiteService;

public class GPCartLoaderStrategy extends DefaultCartLoaderStrategy{

	private static final Logger LOG = LoggerFactory.getLogger(GPCartLoaderStrategy.class);

	private static final String CURRENT_CART = "current";
	private static final String CART_NOT_FOUND_MESSAGE = "Cart not found.";

	private CartFactory cartFactory;
	private UserService userService;
	private CartService cartService;
	private BaseSiteService baseSiteService;
	private CommerceCartService commerceCartService;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	@Override
	public void loadCart(final String cartId, final boolean refresh)
	{
		if (StringUtils.isBlank(cartId))
		{
			throw new CartException("Invalid cart id", CartException.INVALID);
		}

		final UserModel currentUser = userService.getCurrentUser();

		if (currentUser == null)
		{
			throw new IllegalStateException(
					"Current user could not be retrieved from the request. Check filter order in your configuration.");
		}
		else if (!CustomerModel.class.isAssignableFrom(currentUser.getClass()))
		{
			// only customers can own carts
			throw new AccessDeniedException("Access is denied");
		}

		if (getBaseSiteService().getCurrentBaseSite() == null)
		{
			throw new IllegalStateException(
					"BaseSite could not be retrieved from the request. Check filter order in your configuration.");
		}

		if (!userService.isAnonymousUser(currentUser))
		{
			loadUserCart(cartId, refresh);
		}
		else
		{
			loadAnonymousCart(cartId, refresh);
		}
	}
	/**
	 * Loads customer's cart
	 *
	 * @param cartID
	 */
	@Override
	protected void loadUserCart(final String cartID, final boolean refresh)
	{
		String requestedCartID = cartID;
		boolean isSubscriptionCart=false ;
		if (requestedCartID.equals(CURRENT_CART))
		{
			// current means last modified cart
			CartModel cart = getCommerceCartService().getCartForGuidAndSiteAndUser(null, getBaseSiteService().getCurrentBaseSite(),
					userService.getCurrentUser());  
			if (cart == null)  
			{
				cart = cartFactory.createCart();
				updateCartType(cart) ;
			
				if(null == cart) {
					throw new CartException("No cart created yet.", CartException.NOT_FOUND);
				}
			}
			else if (!isBaseSiteValid(cart))
			{
				throw new CartException(CART_NOT_FOUND_MESSAGE, CartException.NOT_FOUND, requestedCartID);
			}
			requestedCartID = cart.getCode();
			if(cart.getIsSubscription()) {
				isSubscriptionCart=true ;
			}
			try {
				updateCartType(cart) ;
				restoreCart(cart, requestedCartID, refresh);
				
			} catch (final Exception e) {
				LOG.error(e.getMessage() + "  Cart Restoring failed Retrying ::: RETRY -1 :: ", e);
			}
		}
		else
		{
			final CartModel cart = getCommerceCartService().getCartForCodeAndUser(requestedCartID, userService.getCurrentUser());
			if (cart == null || !isBaseSiteValid(cart)) 
			{
				throw new CartException(CART_NOT_FOUND_MESSAGE, CartException.NOT_FOUND, requestedCartID);
			}
			isSubscriptionCart=cart.getIsSubscription();
			restoreCart(cart, requestedCartID, refresh);
			
		}
		// code might be different because of cart expiration
		if(!isSubscriptionCart) {
		checkCartExpiration(requestedCartID, getCartService().getSessionCart().getCode());
		}

	}

	private void updateCartType(CartModel cart) {
		if (cmsSiteService.getSiteConfig("enable.samplecart") != null
				&& Boolean.valueOf(cmsSiteService.getSiteConfig("enable.samplecart"))) {
			cart.setCartType(CartTypeEnum.SAMPLE);
		}
	}

	/**
	 * Loads anonymous or guest cart
	 *
	 * @param cartID
	 */
	@Override
	protected void loadAnonymousCart(final String cartID, final boolean refresh)
	{
		if (cartID.equals(CURRENT_CART))
		{
			throw new AccessDeniedException("Access is denied");
		}

		final CartModel cart = commerceCartService.getCartForGuidAndSite(cartID, baseSiteService.getCurrentBaseSite());
		if (cart != null && CustomerModel.class.isAssignableFrom(cart.getUser().getClass()))
		{
			final CustomerModel cartOwner = (CustomerModel) cart.getUser();
			if (userService.isAnonymousUser(cartOwner) || CustomerType.GUEST.equals(cartOwner.getType()))
			{
				if (!isBaseSiteValid(cart))
				{
					throw new CartException(CART_NOT_FOUND_MESSAGE, CartException.NOT_FOUND, cartID);
				}
				try {
					if(cart.getPaymentInfo() instanceof PaypalPaymentInfoModel || cart.getPaymentInfo() instanceof CreditCardPaymentInfoModel) {
						restoreCart(cart, cartID, false);
					}
					else {
						restoreCart(cart, cartID, refresh);
					}
				} catch (final Exception e) {
					LOG.error(e.getMessage() + "  Cart Restoring failed Retrying ::: RETRY -1 :: ", e);
				}
			}
			else
			{
				// 'access denied' presented as 'not found' for security reasons
				throw new CartException(CART_NOT_FOUND_MESSAGE, CartException.NOT_FOUND, cartID);
			}
		}
		else
		{
			throw new CartException(CART_NOT_FOUND_MESSAGE, CartException.NOT_FOUND, cartID);
		}

		// guid might be different because of cart expiration
		checkCartExpiration(cartID, cartService.getSessionCart().getGuid());
	}
	public CartFactory getCartFactory() {
		return cartFactory;
	}

	public void setCartFactory(final CartFactory cartFactory) {
		this.cartFactory = cartFactory;
	}

	public UserService getUserService() {
		return userService;
	}

	@Override
	public void setUserService(final UserService userService) {
		this.userService = userService;
		super.setUserService(userService);
	}

	@Override
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService=baseSiteService;
		super.setBaseSiteService(baseSiteService);
	}

	@Override
	public void setCommerceCartService(final CommerceCartService commerceCartService) {
		this.commerceCartService=commerceCartService;
		super.setCommerceCartService(commerceCartService);
	}

	@Override
	public void setCartService(final CartService cartService) {
		this.cartService=cartService;
		super.setCartService(cartService);
	}




}
