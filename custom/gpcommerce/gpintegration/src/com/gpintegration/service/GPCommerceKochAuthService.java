/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import de.hybris.platform.commercefacades.user.data.KochAuthData;
import de.hybris.platform.commercefacades.user.data.RegisterData;

/**
 * The Interface GPCommerceKochAuthService.
 *
 * @author spandiyan
 */
public interface GPCommerceKochAuthService {
	
	/**
	 * Gets koch auth token.
	 *
	 * @param registerdata the registerdata
	 * @return register data
	 */
	RegisterData getKochAuthToken(RegisterData registerdata);

	/**
	 * Gets koch auth data.
	 *
	 * @param token the token
	 * @return KochAuthData
	 */
	KochAuthData getKochAuthData(String token);
}
