/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import java.util.List;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;

public interface GPCartDao {

	List<ShippingRestrictionModel> fetchShippingRestrictions(String productCode, String country, String state);

	List<GPEndUserLegalTermsModel> getLeaseAgreementForCountry(String country);

	CartModel getCartForCodeAndSite(String guid, BaseSiteModel currentBaseSite);

}
