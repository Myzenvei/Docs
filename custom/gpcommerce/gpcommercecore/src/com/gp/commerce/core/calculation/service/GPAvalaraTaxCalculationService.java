/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation.service;

import com.gp.commerce.core.calculation.AvalaraTaxResponseDTO;

import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * Interface for Avalara tax calculation.
 *
 */
public interface GPAvalaraTaxCalculationService {

	/**
	 * Calculates tax for the order specified
	 * 
	 * @param abstractOrderModel the order
	 * @param isPlaceOrder       boolean value to refer whether the specified order
	 *                           is placed
	 * @return {@link AvalaraTaxResponseDTO}
	 */
	AvalaraTaxResponseDTO calculateTax(AbstractOrderModel abstractOrderModel, boolean isPlaceOrder);
}
