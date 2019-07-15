/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.quickorder.dao;

import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;

public interface GPQuickOrderDao extends Dao{

	List<GPCustomerMaterialInfoModel> getMaterialInfoForB2BUnit(String b2bUnit, String productCodes);
}
