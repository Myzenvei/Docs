/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation.service;

import com.gp.commerce.core.dto.onesource.TaxCalculationResponse;

import de.hybris.platform.core.model.order.AbstractOrderModel;



/**
 * Interface for One Source Tax Calculation
 */
public interface GPOneSourceTaxCalculationService {

	/**
	 * Calculates tax for the specified cart
	 * 
	 * @param cartModel the cart used
	 * @return {@link TaxCalculationResponse}
	 */
	TaxCalculationResponse calculateTax(AbstractOrderModel cartModel);

}

