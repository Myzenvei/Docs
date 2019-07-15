/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.product;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.gp.commerce.product.data.GPProductDataList;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * A product facade to implement functionalities relevant to GP requirements.
 */
public interface GPProductFacade extends ProductFacade
{

	/**
	 * This method populates additional fields in the product data post the populator calls.
	 *
	 * @param productData
	 *           The product data.
	 */
	void populateAdditionalFields(ProductData productData);
	
	/**
	 * Sets the favorite flag for all the products in the list.
	 *
	 * @param products List of products for which the flag is to be set.
	 */
	public void setFavoriteFlagForProducts(List<ProductData> products);
	
	/**
	 * Gets the GP xpress alert product list.
	 *
	 * @param timestamp the timestamp
	 * @param pageNumber the page number
	 * @param resend the resend
	 * @return the GP xpress alert product list
	 */
	public GPProductDataList getGPXpressAlertProductList(String timestamp, int pageNumber, boolean resend);

	/**
	 * Export images.
	 *
	 * @param productData the product data
	 * @param imageFormat the image format
	 * @param resolution the resolution
	 * @param productCode the product code
	 * @param allimages the allimages
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	File exportImages(ProductData productData, String imageFormat, String resolution, String productCode,boolean allimages) throws IOException;
	
	/**
	 * populate knowledge center data to product.
	 *
	 * @param product the product
	 */
	public void populateKnwoledgeCenterData(final ProductData product);

}
