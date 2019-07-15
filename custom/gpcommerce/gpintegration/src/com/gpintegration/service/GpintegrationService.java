/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpintegration.service;

/**
 * Interface for integration service
 */
public interface GpintegrationService
{
	/**
	 * Gets Hybris logo url using logo code
	 * @param logoCode
	 * 			the logo code
	 * @return the hybris logo url
	 */
	String getHybrisLogoUrl(String logoCode);

	/**
	 * Creates logo using logo code.
	 *
	 * @param logoCode the logo code
	 */
	void createLogo(String logoCode);
}
