/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.user.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.Date;
import java.util.List;

import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;

public interface GPUserDao{
	List<AddressModel> getAddressesForB2BUser(List<B2BUnitModel> units,final Boolean fetchActiveAddresses);
	List<B2BCustomerModel> findUsersByCodes(List<String> uids);
	List<MarketingPreferenceModel> getMarketingPreferencesForSite(final CMSSiteModel site);
	List<MarketingPreferenceModel> getMarketingPreferencesForSiteAndType(final CMSSiteModel site,final MarketingPreferenceTypeModel markPrefType );
	List<MarketingPreferenceTypeModel> getDistMarketingPreferences(final CMSSiteModel site);
	/**
	 * get all addresses in rejected status
	 *
	 * @param List<GPApprovalEnum>
	 *          approvalStatuses
	 *   returns
	 *   List<AddressModel>
	 */
	List<AddressModel> getAllAddressesOnStatus(List<GPApprovalEnum> approvalStatuses);

	/**
	 * get all address for non L1 B2B customers after a particular time
	 *
	 * @param lastModifiedTime
	 * @param site
	 * @return List<AddressModel>
	 */
	List<AddressModel> getAllAddressesForB2B(Date lastModifiedTime, final CMSSiteModel site);

	/**
	 * @param lastModifiedTime
	 * @param site
	 * @return List<B2BCustomerModel>
	 */
	List<B2BCustomerModel> getAllCustomersForB2B(Date lastModifiedTime, CMSSiteModel site);


	/**
	 * @param site
	 * @return List<B2BCustomerModel>
	 */
	List<B2BCustomerModel> getAllNewCustomersForB2B(CMSSiteModel site);
	String getUserForFBUniqueId(String fbUniqueId);
}
