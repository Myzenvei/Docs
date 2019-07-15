/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;
import de.hybris.platform.commercefacades.user.data.RegisterData;

import java.util.Map;

/**
 * Interface for Commerce social account service.
 */
public interface GPCommerceSocialAccountService {

	/**
	 * Gets user data from facebook.
	 *
	 * @param registerData the register data
	 * @return Register Data
	 */
	RegisterData getUserDataFromFacebook(RegisterData registerData);

	/**
	 * Gets user data from google.
	 *
	 * @param registerData the register data
	 * @return Register Data
	 */
	RegisterData getUserDataFromGoogle(RegisterData registerData);

	/**
	 * Gets social profile details.
	 *
	 * @param userData the user data
	 * @param socialLoginFlag the social login flag
	 * @return social profile details
	 */
	Map<String, String> getSocialProfileDetails(final String userData, String socialLoginFlag);

	/**
	 * Gets Register data.
	 *
	 * @param registerData the register data
	 * @return RegisterData
	 */
	RegisterData getRegisterData(RegisterData registerData);
}
