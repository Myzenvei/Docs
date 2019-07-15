/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.stock.services;

import com.gp.commerce.core.exceptions.GPInsufficientStockLevelException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.stock.StockService;

/**
 * This interface is used for processing GP stock service
 */
public interface GPStockService extends StockService {
	
	/**
	 * Method to release stock for order 
	 * 
	 * @param order the order
	 * 
	 */
	void releaseStockForOrder(OrderModel order) ;
	
	/**
	 * Method to reserve stock for order 
	 * 
	 * @param order the order
	 * @throws GPInsufficientStockLevelException on error
	 */
	void reserveStockForOrder(OrderModel order) throws GPInsufficientStockLevelException;
}