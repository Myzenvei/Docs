/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import de.hybris.platform.commercefacades.user.data.QuplesData;

import com.gpintegration.exception.GPIntegrationException;

/**
 * Interface for Quples token.
 */
public interface GPQuplesTokenService {

	/**
	 * Gets quples data.
	 *
	 * @param quplesData the quples data
	 * @return QuplesData
	 * @throws GPIntegrationException the GP integration exception
	 */
	QuplesData getQuplesToken(QuplesData quplesData) throws GPIntegrationException;
}
