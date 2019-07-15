/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.configurablebundle;

import com.gp.commerce.core.exceptions.GPBundleSetupException;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.CartEntryModel;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This interface is used for GP bundle services
 */
public interface GPBundleService {

	/**
	 * Validates bundle setup by returning a boolean based on the given bundle ID
	 * 
	 * @param bundleId the bundle ID
	 * @return <b>true</b> if validated <br>
	 *         <b>false</b> otherwise
	 * @throws GPBundleSetupException on setup error
	 */
    Boolean validateBundleSetup(@Nonnull final String bundleId) throws GPBundleSetupException;

	/**
	 * Returns root bundle template for the given bundleID
	 * 
	 * @param bundleId the bundle ID
	 * @return {@link BundleTemplateModel}
	 */
    BundleTemplateModel getRootBundleTemplate(@Nonnull final String bundleId);

	/**
	 * Returns the list of cart entries by processing bundle number and cart ID
	 * 
	 * @param bundleNo the bundle number
	 * @param cartId   the cart ID
	 * @return list of cart entries
	 */
    List<CartEntryModel> getOrderEntriesByBundleNum(int bundleNo, String cartId);
}
