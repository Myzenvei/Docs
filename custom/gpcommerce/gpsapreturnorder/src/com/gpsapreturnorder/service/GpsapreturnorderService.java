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
package com.gpsapreturnorder.service;

/**
 * Interface for sap return order service.
 */
public interface GpsapreturnorderService
{
	
	/**
	 * Gets hybris logo url by taking logo code as argument.
	 *
	 * @param logoCode the logo code
	 * @return hybris logo url
	 */
	String getHybrisLogoUrl(String logoCode);

	/**
	 * Creates logo by taking argument logo code.
	 *
	 * @param logoCode the logo code
	 */
	void createLogo(String logoCode);
}
