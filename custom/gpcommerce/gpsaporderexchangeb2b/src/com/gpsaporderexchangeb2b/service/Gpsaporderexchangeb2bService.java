/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchangeb2b.service;

/**
 * Interface for sap order exchange b2b service.
 */
public interface Gpsaporderexchangeb2bService
{
	
	/**
	 * Gets LOgo url by passing logo code as parameter.
	 *
	 * @param logoCode the logo code
	 * @return logo url
	 */
	String getHybrisLogoUrl(String logoCode);

	/**
	 * Creates Logo using logo code as parameter.
	 *
	 * @param logoCode the logo code
	 */
	void createLogo(String logoCode);
}
