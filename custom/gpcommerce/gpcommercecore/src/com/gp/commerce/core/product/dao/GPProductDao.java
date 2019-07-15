/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.dao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gp.commerce.core.catalog.GPProductAuditRecord;
import com.gp.commerce.core.enums.BrandLabelEnum;
import com.gp.commerce.core.enums.BrandsEnum;
import com.gp.commerce.core.enums.GPXpressProdAttributeType;
import com.gp.commerce.core.model.GPAttributeConfigModel;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.model.GPProductType2ClassificationAttributeModel;
import com.gp.commerce.core.model.GPVariantColorCodesModel;
import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;


public interface GPProductDao extends Dao {

	/**
	 * This method returns the CMIR code from the DB for a product code and B2B unit.
	 * @param productCode The product code for which CMIR code is to be found.
	 * @param b2bUnitCode The B2B unit for which the CMIR code is to be found.
	 * @return CMIR code if available, null otherwise.
	 */
	String getCMIRCodeForProductAndB2BUnit(String productCode, String b2bUnitCode);


	List<GPAttributeConfigModel> getAttributeList(String uid, String assetCode);

	/**
	 * Fetches all the color codes available.
	 *
	 * @return A list of GPVariantColorCodesModel
	 */
	List<GPVariantColorCodesModel> getAllColorCodes();

	/**
	 * Check if the product is available for the current site
	 *
	 * @param productPK
	 * @param sitePK
	 *
	 * @return boolean
	 */
	boolean isProductAssociatedWithSite(final ProductModel product, final CMSSiteModel site);

	/**
	 * Returns the Product with the specified code or UPC.
	 *
	 * @param code
	 *           the code of the Product
	 * @return the Product with the specified code or upc.
	 */
	List<ProductModel> getProductForCodeOrUpc(String code);

	List<GPCustomerMaterialInfoModel> getCMIRCodeForProduct(String productCode);

	/**
	 *
	 * @param timeStamp
	 * @param pageNumber
	 * @param pageSize
	 * @param resend
	 * @return
	 */
	SearchPageData<GPXpressAlertProdHistoryModel> getGPXpressProductList(Date timeStamp, int pageNumber, int pageSize, boolean resend);

	/**
	 * Returns alert history records for a given product code and attribute name.
	 * @param gpProd the product code
	 * @param attName the attribute name
	 * @return list of existing records for the given product and attribute.
	 */
	List<GPXpressAlertProdHistoryModel> getExistingAlertHistoryRecords(final ProductModel gpProd, final String attName);

	/**
	 *
	 * @param gpProd
	 * @param attNames
	 * @return
	 */
	List<GPXpressAlertProdHistoryModel> getExistingAlertHistoryRecords(final String gpProd, final Set<String> attNames, final GPXpressProdAttributeType attributeSource);

	List<GPXpressAlertProdHistoryModel> getProductAttributesToSfdc(final List<String> attributeList,final Date timeStamp, Date productTimstamp, String productSKU) ;

	void addXpressAlertRecords(Map<String, GPProductAuditRecord> gpProductAuditRecordMap);

	/**
	 * Fetches no obsolete products for a given site
	 * @param site the CMSSiteModel
	 * @return List of GPCommerceProductModels
	 */
	public List<ProductModel> getNonObsoleteProductsForSite(CMSSiteModel site);

	List<ProductModel> getProductsForCodes(List<String> productCodes);

	List<GPCommerceProductModel> getGPCommerceProductsForCodes(List<String> productCodes,B2BUnitModel soldToUnit,BrandLabelEnum brandLabel,CategoryModel category,int key);

	GPProductType2ClassificationAttributeModel getProductType2ClassAttributes(String productType);

	List<ProductModel> getAllProductsForSpecsPopulatorJob(Date endTime, Date featureUpdateDate, String productCode, String catalogVersion);


	List<ProductModel> getProductsForSiteAndCatalogVersion(CMSSiteModel site, CatalogVersionModel catalogVersionModel);


	List<ProductModel> getAllProductsForGpBrandLabelPopulatorJob(Date endTime, Date featureUpdateDate,
			String productCode, String catalogVersion);
}
