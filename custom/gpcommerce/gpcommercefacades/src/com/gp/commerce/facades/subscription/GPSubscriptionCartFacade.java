/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.subscription;

import de.hybris.platform.core.model.order.CartModel;

/**
 * The Interface GPSubscriptionCartFacade.
 */
public interface GPSubscriptionCartFacade {

	/**
	 * Apply voucher for cart internal.
	 *
	 * @param voucherId the voucher id
	 * @param cartId the cart id
	 * @return the cart model
	 */
	CartModel applyVoucherForCartInternal(String voucherId, String cartId);

	/**
	 * Release voucher.
	 *
	 * @param voucherId the voucher id
	 * @param cartId the cart id
	 */
	void releaseVoucher(String voucherId, String cartId);

	/**
	 * Release coupons.
	 *
	 * @param cartModel the cart model
	 */
	void releaseCoupons(CartModel cartModel);

}
