/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.core.xpress.alert.service;

import java.util.List;

import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;

/**
 * The Interface GPProductAlertService.
 */
public interface GPProductAlertService {
	
	/**
	 * Gets the expired GP product Alert history.
	 *
	 * @return the expired GP product Alert history models.
	 */
	public List<GPXpressAlertProdHistoryModel> getExpiredGPProductALertHistoryModels();

}
