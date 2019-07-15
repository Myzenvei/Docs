/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;

import java.util.List;


public interface GPEurope1PriceFactory
{
	/**
	 * Fecthes price rows of a product and filters them to get a single price row.
	 *
	 * @param model
	 *           The product model
	 * @return List of Price Rows
	 */
	List<PriceRow> getPriceInformationsForProduct(ProductModel model);

}
