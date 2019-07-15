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

package com.gp.commerce.core.xpress.alert.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;
import com.gp.commerce.core.xpress.alert.dao.GPProductAlertDao;
import com.gp.commerce.core.xpress.alert.service.GPProductAlertService;

import de.hybris.platform.servicelayer.config.ConfigurationService;

public class GPProductAlertServiceImpl implements GPProductAlertService {
	
	private static final String ALERT_HISTORY_EXPIRY_DURATION = "product.alerts.cleanup.period";
	@Resource
	GPProductAlertDao gpProductAlertDao;
	@Resource
	ConfigurationService configurationService;

	@Override
	public List<GPXpressAlertProdHistoryModel> getExpiredGPProductALertHistoryModels() {
		
		Long expiryDuration=Long.valueOf(configurationService.getConfiguration().getString(ALERT_HISTORY_EXPIRY_DURATION));
		Date modifiedBefore=new Date(System.currentTimeMillis()-expiryDuration);
		return gpProductAlertDao.getGPProductAlertHistoryForDate(modifiedBefore);
	}

}
