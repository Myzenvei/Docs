/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.quickorder;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.facades.data.GPQuickOrderData;

/**
 * Interface for quick order facade.
 */
public interface GPQuickOrderFacade {

	/**
	 * Gets Product list from CSV.
	 *
	 * @param file the file
	 * @return product list from CSV
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	List<ProductData> getProductListFromCSV(MultipartFile file) throws IOException;

	/**
	 * Gets products for quick order.
	 *
	 * @param b2bUnit the b2b unit
	 * @param productList the product list
	 * @return products for quick order
	 */
	GPQuickOrderData getProductsForQuickOrder(String b2bUnit, List<ProductData> productList);

	/**
	 * shares quick order data.
	 *
	 * @param gpQuickOrderData the gp quick order data
	 */
	void shareQuickOrderData(GPQuickOrderData gpQuickOrderData);

	/**
	 * gets suggestions for products in quick order.
	 *
	 * @param component the component
	 * @param gpQuickOrderData the gp quick order data
	 * @return list of suggestions for products in quick order
	 */
	List<ProductData> getSuggestionsForProductsInQuickOrder(CartSuggestionComponentModel component,
			GPQuickOrderData gpQuickOrderData);

}
