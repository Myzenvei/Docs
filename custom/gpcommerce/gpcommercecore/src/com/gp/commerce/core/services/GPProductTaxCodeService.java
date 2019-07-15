/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

/**
 * Interface for Product tax code service
 */
public interface GPProductTaxCodeService {

	/**
	 * This method fetches tax code taking product code
	 * @param productCode
	 * 			the product code
	 * @param taxArea
	 * 			the tax area
	 * @return the tax code
	 */
	String fetchTaxCode(String productCode, String taxArea);
}
