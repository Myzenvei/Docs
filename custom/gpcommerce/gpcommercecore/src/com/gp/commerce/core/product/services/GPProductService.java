/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gp.commerce.core.catalog.GPProductAuditRecord;
import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.product.data.GPProductDataList;

/**
 * Extension of {@link ProductService}
 */
public interface GPProductService extends ProductService {

	/**
	 * This method fetches all color codes
	 * @return color code map
	 */
	Map<String, String> fetchAllColorCodes();

	/**
	 * This method gets attributes of product
	 * @param assetCode
	 * 			the asset code
	 * @param pageType
	 * 			the page type
	 * @return attributes of product
	 */
	List<String> getAttributesOfProduct(String assetCode, String pageType);

	/**
	 * Returns the Product with the specified code or UPC.
	 *
	 * @param code
	 *           the code of the Product
	 * @return the Product with the specified code or upc.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Product with the specified code is found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Product with the specified code is found
	 */
	ProductModel getProductForCodeOrUpc(String code);

	/**
	 * Fetches list of customer material information for the given product code
	 * 
	 * @param productCode the product code
	 * @return list of {@link GPCustomerMaterialInfoModel}
	 */
	List<GPCustomerMaterialInfoModel> getCMIRCodeForProduct(String productCode);


	/**
	 * Returns {@link GPProductDataList} for GPXpress alert
	 * 
	 * @param timestamp  the timestamp
	 * @param pageNumber the page number
	 * @param resend     a boolean value, indicating whether to resend
	 * @return {@link GPProductDataList}
	 */
	GPProductDataList getProductListForGPXpressAlert(String timestamp, int pageNumber, boolean resend);

	/**
	 * Updates product alert history for the given audit records
	 * 
	 * @param gpProductAuditRecordMap the {@link GPProductAuditRecord}
	 */
	void updateProductAlertHistory(Map<String, GPProductAuditRecord> gpProductAuditRecordMap);

	/**
	 * Fetches an image file by processing the given product, imageformat,
	 * resolution and source
	 * 
	 * @param productData the product
	 * @param imageFormat the format of the image
	 * @param resolution  the resolution of the image
	 * @param source      the source
	 * @param zipFile     the zipfile generated
	 * @param allimages   boolean value
	 * @return an image file
	 * @throws MalformedURLException on url error
	 * @throws FileNotFoundException if the given file is not found
	 * @throws IOException           on input error
	 */
	File exportImages(ProductData productData, String imageFormat, String resolution, String source, String zipFile,
			boolean allimages) throws MalformedURLException, FileNotFoundException, IOException;


	/**
	 * Fetched the kit component quantity for the given product and its
	 * corresponding stocklevels
	 * 
	 * @param product     the product
	 * @param stockLevels list of stock levels
	 * @return kit component quantity
	 */
	Long getKitComponentQuantity(final ProductModel product, Collection<StockLevelModel> stockLevels);

	/**
	 * FReturns the display attributes for the given product
	 * 
	 * @param product the product
	 * @return display attributes
	 */
	public String getDisplayAttributesForProduct(ProductModel product);

	/**
	 * Fetches list of Products for the given endtime, feature update date, product
	 * code and catalog version
	 * 
	 * @param endTime           the end time
	 * @param featureUpdateDate the update data
	 * @param productCode       the product code
	 * @param catalogVersion    the catalog version
	 * @return list of products
	 */
	List<ProductModel> getAllProductsForSpecsPopulatorJob(Date endTime, Date featureUpdateDate, String productCode, String catalogVersion);

	/**
	 * Fetches list of Products for the given endtime, feature update date, product
	 * code and catalog version
	 * 
	 * @param endTime           the end time
	 * @param featureUpdateDate the update data
	 * @param productCode       the product code
	 * @param catalogVersion    the catalog version
	 * @return list of products
	 */
	List<ProductModel> getAllProductsForGpBrandLabelPopulatorJob(Date endTime, Date featureUpdateDate, String productCode, String catalogVersion);

	/**
	 * Fetches list of Products for the given CMS site and catalog verision
	 * 
	 * @param site                the CMS Site
	 * @param catalogVersionModel the catalog version
	 * @return list of products
	 */
	List<ProductModel> getProductsForSiteAndCatalogVersion(CMSSiteModel site, CatalogVersionModel catalogVersionModel);

	/**
	 * Returns the Feature list for the given product code
	 * 
	 * @param productCode the product code
	 * @return {@link FeatureList}
	 */
	FeatureList getFeaturesForProduct(final String productCode);
}
