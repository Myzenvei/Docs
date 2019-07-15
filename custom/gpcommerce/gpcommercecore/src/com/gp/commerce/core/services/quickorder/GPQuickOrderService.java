/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.quickorder;

import java.util.List;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;

/**
 * Interface for Quick Order Service
 */
public interface GPQuickOrderService {

	/**
	 * Gets material info for b2b unit
	 * 
	 * @param b2bUnit
	 * 			the b2b unit
	 * @param productCodes
	 * 			the product codes
	 * @return list of material info model for b2b unit
	 */
	List<GPCustomerMaterialInfoModel> getMaterialInfoForB2BUnit(String b2bUnit, String productCodes);
}
