/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import java.math.BigDecimal;

/**
 * Interface for debit calculation service
 */
public interface GPDebitCalculationService {

	/**
	 * This method calculates debit by taking shipped quantity as parameter
	 * 
	 * @param shippedQuantity  total shipped quantity
	 * @param consignmentEntry the consignment entry
	 * @return total calculated debit
	 */
	BigDecimal calculateDebit(Integer shippedQuantity, ConsignmentEntryModel consignmentEntry);

}
