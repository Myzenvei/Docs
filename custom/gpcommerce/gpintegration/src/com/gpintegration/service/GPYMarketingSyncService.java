/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.service;

import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;

/**
 *  Interface for yMarketing
 */
public interface GPYMarketingSyncService {

	/**
	 * Creates contact.
	 *
	 * @param updatePreferences the update preferences
	 * @param create the create
	 * @return contact
	 */
	String createOrUpdateContact(UpdatePreferenceData updatePreferences, boolean create);

}
