/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;

import java.util.List;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.model.SplitEntryModel;

/**
 * Interface for cart service
 */
public interface GPCartService {

	/**
	 * This method shares cart using the mail id
	 * 
	 * @param toEmail     the receivers mail
	 * @param senderName  the sender name
	 * @param senderEmail the sender email
	 * @param cartModel   the cart model
	 */
	void shareCart(final String toEmail, String senderEmail, String senderName, final CartModel cartModel);

	/**
	 * This method fetches shipping restrictions
	 * @param productCode
	 * 			the product code
	 * @param country
	 * 			the country
	 * @param state
	 * 			the state
	 * @return list of shipping restrictions
	 */
	List<ShippingRestrictionModel> fetchShippingRestrictions(String productCode, String country, String state);

	/**
	 * This method gets lease agreement for country
	 * @param country
	 * 			the country
	 * @return lease agreement data
	 */
	List<GPEndUserLegalTermsModel> getLeaseAgreementForCountry(String country);

	/**
	 * Check if there are incompatible products in cart.
	 *
	 * @param cart
	 *           The cart in session.
	 * @return List of incompatible refill products in cart, empty list otherwise.
	 */
	List<ProductModel> checkIncompatibleProducts(CartModel cart);

	/**
	 * Update split entry model
	 *
	 * @param model the split entry
	 */
	void updateSplitEntry(SplitEntryModel model);

	/**
	 * Update delivery mode
	 *
	 * @param deliverymode the deliverymode
	 */
	void updateDeliveryInstruction(DeliveryModeModel deliverymode);

	/**
	 * Update Lease in Cart
	 *
	 * @param cart the cart
	 */
	void updateLease(CartModel cart);

	/**
	 * Gets the cart for code and site.
	 *
	 * @param guid the guid
	 * @param currentBaseSite the current base site
	 * @return the cart for code and site
	 */
	CartModel getCartForCodeAndSite(String guid, BaseSiteModel currentBaseSite);
	
	/**
	 * getCouponForCode.
	 *
	 * @param code the code
	 * @return the coupon for code
	 */
	AbstractCouponModel getCouponForCode(String code);
}
