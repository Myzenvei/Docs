/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.quickorder.impl;

import java.util.List;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.quickorder.dao.GPQuickOrderDao;
import com.gp.commerce.core.services.quickorder.GPQuickOrderService;

public class GPQuickOrderServiceImpl implements GPQuickOrderService{

	private GPQuickOrderDao gpQuickOrderDao;

	@Override
	public List<GPCustomerMaterialInfoModel> getMaterialInfoForB2BUnit(String b2bUnit, String productCodes) {

		return gpQuickOrderDao.getMaterialInfoForB2BUnit(b2bUnit, productCodes);
	}

	public GPQuickOrderDao getGpQuickOrderDao() {
		return gpQuickOrderDao;
	}
	
	public void setGpQuickOrderDao(GPQuickOrderDao gpQuickOrderDao) {
		this.gpQuickOrderDao = gpQuickOrderDao;
	}
	
	

}
