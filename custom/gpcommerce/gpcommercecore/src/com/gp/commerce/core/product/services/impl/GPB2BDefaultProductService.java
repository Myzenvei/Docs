/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.services.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.product.dao.impl.GPDefaultProductDao;

/**
 * The Class GPB2BDefaultProductService.
 */
public class GPB2BDefaultProductService extends GPDefaultProductService {
	
	@Resource(name="gpProductDao")
	private transient GPDefaultProductDao gpProductDao;
	
	/**
	 * This method returns the CMIR code for a product code and B2B unit.
	 * @param productCode The product code for which CMIR code is to be found.
	 * @param b2bUnitCode The B2B unit for which the CMIR code is to be found.
	 * @return CMIR code if available, null otherwise.
	 */
	public String getCMIRCodeForProductAndB2BUnit(String productCode, String b2bUnitCode) {
		return gpProductDao.getCMIRCodeForProductAndB2BUnit(productCode, b2bUnitCode);
	}
	
	

	@Override
	public GPDefaultProductDao getGpProductDao() {
		return gpProductDao;
	}

	public void setGpProductDao(GPDefaultProductDao gpProductDao) {
		this.gpProductDao = gpProductDao;
	}

}
