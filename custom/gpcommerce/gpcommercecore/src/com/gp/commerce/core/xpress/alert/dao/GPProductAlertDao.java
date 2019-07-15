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

package com.gp.commerce.core.xpress.alert.dao;

import java.util.Date;
import java.util.List;

import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;

public interface GPProductAlertDao {
	
	public List<GPXpressAlertProdHistoryModel> getGPProductAlertHistoryForDate(Date modifiedBefore);

}
