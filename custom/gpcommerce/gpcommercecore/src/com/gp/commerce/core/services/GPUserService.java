/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;
import java.util.Locale;

import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.model.MarketingPreferenceTypeModel;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceDataList;

/**
 * Extension of {@link UserService} 
 */
public interface GPUserService extends UserService
{
	/**
	 * method to register 
	 */
	void register();

	/**
	 * method to get list of the marketing preferences for given site
	 *
	 * @param site
	 *           The cms site for which the marketing preferences are to be fetched.
	 * @param markPrefType
	 *  		  The MarketingPreferenceType with which the marketingPreference is linked
	 * @return List of marketing preferences configured for the given site.
	 */
	List<MarketingPreferenceModel> getMarketingPreferencesForSiteAndType(final CMSSiteModel site,
			final MarketingPreferenceTypeModel markPrefType);

	/**
	 * method to update the selected marketing preferences to customer.
	 *
	 * @param preferencesDataList
	 *           The list of marketing preferences which user has opted in or out.
	 * @param site
	 *           The cms site for which the marketing preferences are opted.
	 */
	void updateCustomerPreferences(MarketingPreferenceDataList preferencesDataList, CMSSiteModel site);

	/**
	 * This method checks if customer is approved based on "userApprovalStatus" field
	 *
	 * @param b2bCustomer
	 *           the b2b customer model
	 * @return the status
	 */
	Boolean isCustomerApproved(B2BCustomerModel b2bCustomer);

	/**
	 * method to check if GP admin is approved
	 * @param addressModel
	 * 			the address model of the gp admin
	 * @return the status
	 */
	Boolean isGPAdminApproved(final AddressModel addressModel);

	/**
	 * method to check if admin is approved
	 * @param addressModel
	 * 			the address model of the admin
	 * @return the status
	 */
	Boolean isAdminApproved(final AddressModel addressModel);


	/**
	 * method to get the Distinct marketing preferences.
	 *
	 * @param site
	 *           The site for which the marketing preference types are to be fetched.
	 * @return list of distinct marketing preferences
	 */
	List<MarketingPreferenceTypeModel> getDistMarketingPreferences(final CMSSiteModel site);

	/**
	 * method to get list of the marketing preferences for given site
	 *
	 * @param site
	 *           The cms site for which the marketing preferences are to be fetched.
	 * @return List of marketing preferences configured for the given site.
	 */
	List<MarketingPreferenceModel> getMarketingPreferences(final CMSSiteModel site);

	/**
	 * method to revise marketing preference update
	 * @param preferences
	 * 			the marketing preferences
	 * @param site
	 * 			The cms site for which the marketing preferences are to be fetched.
	 * @param customer
	 * 			the customer whose marketing preferences are to be fetched
	 * @param optIn
	 * 			the opt in
	 */
	void reviseMarketingPreferenceUpdate(final List<MarketingPreferenceModel> preferences, final CMSSiteModel site,
			final CustomerModel customer, final boolean optIn);

	Locale getCurrentLocale();
}
