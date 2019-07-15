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
package com.gp.commerce.facades.subscription.impl;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.exceptions.GPInvalidPromotionException;
import com.gp.commerce.facades.subscription.GPSubscriptionCartFacade;
import com.gp.commerce.facades.subscription.GPSubscriptionFacade;

import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercefacades.voucher.impl.DefaultVoucherFacade;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
 
/**
 * The Class GPSubscriptionDefaultCartFacade.
 */
public class GPSubscriptionDefaultCartFacade extends DefaultVoucherFacade implements GPSubscriptionCartFacade {

	@Resource(name = "subscriptionCartFacadeImpl")
	private GPSubscriptionFacade subscriptionCartFacadeImpl;
	
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;
	
	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Resource(name = "calculationService")
	private CalculationService calculationService;
	
	
	@Resource(name="promotionsService")
	private PromotionsService promotionsService;
	
	@Resource(name="timeService")
	private TimeService timeService;
 

	private static final Logger LOG = Logger.getLogger(GPSubscriptionDefaultCartFacade.class);

	/**
	 * Apply voucher for cart internal.
	 *
	 * @param voucherId the voucher id
	 * @param cartId the cart id
	 * @return the cart model
	 */
	@Override
	public CartModel applyVoucherForCartInternal(String voucherId, String cartId) {

		final CartModel subscriptionCart = subscriptionCartFacadeImpl.getSubsCartModel(cartId);
		final CartModel sessionCartSnapshot = getCartService().getSessionCart();
		subscriptionCart.setIsSubscription(false);
		// Setting the subscription cart in the session as the OOTB coupon gets applied
		// in the session
		getCartService().setSessionCart(subscriptionCart);

		try {
			voucherFacade.applyVoucher(voucherId);
		} catch (VoucherOperationException ex) {
			throw new GPInvalidPromotionException("109", "Promo code entered is invalid or out of date", ex);
		} finally {
			getCartService().setSessionCart(sessionCartSnapshot);     
			subscriptionCart.setIsSubscription(true);
			modelService.save(subscriptionCart);
		
		}
		return subscriptionCart;
	}
	
	/**
	 * Release voucher.
	 *
	 * @param voucherId the voucher id
	 * @param cartId the cart id
	 */
	@Override
	public void releaseVoucher(String voucherId, String cartId) {
		final CartModel subscriptionCart = subscriptionCartFacadeImpl.getSubsCartModel(cartId);
		final CartModel sessionCartSnapshot = getCartService().getSessionCart();
		subscriptionCart.setIsSubscription(false);
		// Setting the subscription cart in the session as the OOTB coupon gets applied
		// in the session
		getCartService().setSessionCart(subscriptionCart);

		try {
			voucherFacade.releaseVoucher(voucherId);
		} catch (VoucherOperationException ex) {
			throw new GPInvalidPromotionException("109", "Promo code entered is invalid or out of date", ex);
		} finally {
			getCartService().setSessionCart(sessionCartSnapshot);     
			subscriptionCart.setIsSubscription(true);
			modelService.save(subscriptionCart);
		
		}
	}
	
	/**
	 * Release coupons.
	 *
	 * @param cartModel the cart model
	 */
	@Override
	public void releaseCoupons(CartModel cartModel) {
		promotionsService.updatePromotions(new ArrayList<PromotionGroupModel>(), cartModel, true, PromotionsManager.AutoApplyMode.APPLY_ALL,
                PromotionsManager.AutoApplyMode.APPLY_ALL, timeService.getCurrentTime());
		final Collection<String> appliedCouponCodes = cartModel.getAppliedCouponCodes();
		if (CollectionUtils.isNotEmpty(appliedCouponCodes)) {
			for (final String appliedCouponCode : appliedCouponCodes) {
				releaseVoucher(appliedCouponCode, cartModel.getCode());
			}
			try {
				calculationService.calculateTotals(cartModel, true);
			} catch (CalculationException e) {
				LOG.error("Error while calculationg total on promotion removal"+e.getMessage(),e);
			}

		}
	}
	

}
