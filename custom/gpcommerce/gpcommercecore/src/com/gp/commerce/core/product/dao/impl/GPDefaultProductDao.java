/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.product.dao.impl;


import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.catalog.GPProductAuditAttribute;
import com.gp.commerce.core.catalog.GPProductAuditRecord;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.BrandLabelEnum;
import com.gp.commerce.core.enums.GPXpressProdAttributeType;
import com.gp.commerce.core.enums.MaterialStatusEnum;
import com.gp.commerce.core.exceptions.GPCommerceDataException;
import com.gp.commerce.core.model.GPAttributeConfigModel;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.model.GPProductType2ClassificationAttributeModel;
import com.gp.commerce.core.model.GPVariantColorCodesModel;
import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;
import com.gp.commerce.core.product.dao.GPProductDao;
import com.gp.commerce.core.services.impl.GPPagedFlexibleSearchService;

public class GPDefaultProductDao extends DefaultProductDao implements GPProductDao {

	private static final String ENABLE_AUTOSUGGEST_OBSELETE = "enable.autosuggest.obselete";
	private static final String GP_CATALOG_ONLINE_VERSION = "gp.catalog.online.version";
	private static final String GP_CATALOG_ID = "gp.catalog.id";
	private static final String ATTRIBUTE_SOURCE = "attributeSource";
	private static final String PROCESSED = "processed";
	private static final String SOLD_TO_ID = "soldToId";
	private static final String UID = "uid";
	private static final String ASSET_CODE = "assetCode";
	private static final String PRODUCT = "product";
	private static final String PRODUCT_CODE = "productCode";
	private static final String UPC = "upc";
	private static final String TIMESTAMP = "timestamp";
	private static final String PRODUCTCODE = "prodCode";
	private static final String ATTRIBUTE_NAME="attribute";
	private static final String PRODUCTTIMESTAMP="productTimeStamp";
	private static final String RETRIEVE_PRODUCT_FOR_CODE = "SELECT {pk} from {Product} where {code} in (?code) AND {catalogversion} IN ({{select {activeCatalogVersion} from {Catalog} where {id} in ('gpUSProductCatalog')}}) ";
	private static final String RETRIEVE_PRODUCT_QUERY_BY_MODIFIED_DATE = "SELECT {pk} from {Product} where {modifiedtime} > (?modifiedTime) AND {catalogversion} IN ({{select {CV.PK} from {Catalog as C join CatalogVersion as CV on {C.PK} = {CV.Catalog}} where {C.id} in ('gpUSProductCatalog') and {CV.version} = (?catalogVersion)}}) ";
	private static final String RETRIEVE_PRODUCT_QUERY_BY_PRODUCT_CODE = "SELECT {pk} from {Product} where {code} = (?productCode) AND {catalogversion} IN ({{select {CV.PK} from {Catalog as C join CatalogVersion as CV on {C.PK} = {CV.Catalog}} where {C.id} in ('gpUSProductCatalog') and {CV.version} = (?catalogVersion)}}) ";

	private GPPagedFlexibleSearchService pagedFlexibleSearchService;

	private CatalogVersionService catalogVersionService;

	public GPDefaultProductDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public String getCMIRCodeForProductAndB2BUnit(final String productCode, final String b2bUnitCode) {

		final String queryString = "SELECT {" + GPCustomerMaterialInfoModel.PK + "} FROM {" + GPCustomerMaterialInfoModel._TYPECODE
				+ "} WHERE {" + GPCustomerMaterialInfoModel.PRODUCTCODE + "}=?productCode AND {"
				+ GPCustomerMaterialInfoModel.SOLDTOID + "}=?soldToId";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(PRODUCT_CODE, productCode);
		query.addQueryParameter(SOLD_TO_ID, b2bUnitCode);

		final List<GPCustomerMaterialInfoModel> cmirCodes = getFlexibleSearchService().<GPCustomerMaterialInfoModel> search(query)
				.getResult();
		if (CollectionUtils.isEmpty(cmirCodes))
		{
			return null;
		}
		else if (cmirCodes.size() > 1)
		{
			throw new GPCommerceDataException("code","Multiple CMIR codes found for product:"+productCode+" and b2bUnit:"+b2bUnitCode);
		}

		return cmirCodes.get(0).getCmirCode();
	}

	@Override
	/**
	 * Fetches all the color codes available.
	 *
	 * @return A list of GPVariantColorCodesModel
	 */
	public List<GPVariantColorCodesModel> getAllColorCodes()
	{
		final String queryString = "SELECT {" + GPVariantColorCodesModel.PK + "} FROM {" + GPVariantColorCodesModel._TYPECODE + "}";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		final SearchResult<GPVariantColorCodesModel> result = getFlexibleSearchService().search(query);

		if (result.getCount() == 0)
		{
			return Collections.emptyList();
		}

		return result.getResult();
	}

	@Override
	public List<GPAttributeConfigModel> getAttributeList(final String uid, final String assetCode)
	{

		final String queryString = "SELECT {a:pk} as PK FROM {GPAttributeConfig AS a JOIN CMSSITE AS c on {a:cmsSite} = {c:PK} } WHERE {a.assetCode}= ?assetCode AND {c:uid} = ?uid ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(UID, uid);
		query.addQueryParameter(ASSET_CODE, assetCode);

		final SearchResult<GPAttributeConfigModel> searchResult = getFlexibleSearchService().search(query);
		final List<GPAttributeConfigModel> results = searchResult.getResult();
		if (CollectionUtils.isNotEmpty(results))
		{
			return results;
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isProductAssociatedWithSite(final ProductModel product, final CMSSiteModel site)
	{

		final String queryString = "SELECT {p:pk} FROM {Site2Product as rel join Product as p on {p:pk}={rel:target} join CMSSite as site on {site:pk} = {rel:source}} WHERE {rel:target}=?product AND {rel:source}=?site";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(PRODUCT, product);
		query.addQueryParameter(GpcommerceCoreConstants.SITE, site);

		final List siteList = getFlexibleSearchService().search(query).getResult();
		if (CollectionUtils.isEmpty(siteList))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public List<ProductModel> getProductForCodeOrUpc(final String code) {
		final String queryString = "SELECT {" + ProductModel.PK + "} FROM {" + ProductModel._TYPECODE
				+ "} WHERE {" + ProductModel.CODE + "} = ?productCode OR {"+ ProductModel.EAN + "} = ?upc";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(PRODUCT_CODE, code);
		query.addQueryParameter(UPC, code);

		final SearchResult<ProductModel> result = getFlexibleSearchService().search(query);

		if (result.getCount() == 0)
		{
			return Collections.emptyList();
		}

		return result.getResult();
	}

	@Override
	public List<GPCustomerMaterialInfoModel> getCMIRCodeForProduct(final String productCode) {
		final String queryString = "SELECT {p:pk} FROM {GPCustomerMaterialInfo AS p} WHERE {p:productCode}=?productCode";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(PRODUCT_CODE, productCode);

		final SearchResult<GPCustomerMaterialInfoModel> results = getFlexibleSearchService().search(query);
		if (results.getCount() == 0)
		{
			return Collections.emptyList();
		}

		return results.getResult();
	}
	@Override
	public SearchPageData<GPXpressAlertProdHistoryModel> getGPXpressProductList(final Date timestamp, final int pageNumber, final int pageSize, final boolean processed)
	{
		final String newQueryString = "SELECT {pk} FROM {GPXpressAlertProdHistory}"
				+ " WHERE {attributeModifiedTime} >=?timestamp AND {processed}=?processed AND {productAttributeSource}=?attributeSource";

		final Map queryParams = new HashMap();

		final FlexibleSearchQuery query = new FlexibleSearchQuery(newQueryString);
		queryParams.put(TIMESTAMP, timestamp);
		queryParams.put(PROCESSED, processed);
		queryParams.put(ATTRIBUTE_SOURCE, GPXpressProdAttributeType.PRODUCT);
		final PageableData pageData = new PageableData();
		pageData.setCurrentPage(pageNumber);
		pageData.setPageSize(pageSize);

		final SearchPageData<GPXpressAlertProdHistoryModel> gpXpressProductList = pagedFlexibleSearchService
				.<GPXpressAlertProdHistoryModel> search(query.getQuery(), queryParams, pageData);

		return gpXpressProductList;
	}

	@Override
	public List<GPXpressAlertProdHistoryModel> getExistingAlertHistoryRecords(final ProductModel gpProd,
			final String attName) {
		final String queryString = "SELECT {" + GPXpressAlertProdHistoryModel.PK + "} FROM {"
				+ GPXpressAlertProdHistoryModel._TYPECODE + "} WHERE {" + GPXpressAlertProdHistoryModel.ATTRIBUTENAME
				+ "}=?attribute AND {" + GPXpressAlertProdHistoryModel.PRODUCTCODE + "}=?prodCode";
		;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(ATTRIBUTE_NAME, attName);
		query.addQueryParameter(PRODUCTCODE, gpProd.getCode());

		return getFlexibleSearchService().<GPXpressAlertProdHistoryModel> search(query).getResult();
	}

	@Override
	public List<GPXpressAlertProdHistoryModel> getExistingAlertHistoryRecords(final String gpProd,
			final Set<String> attNames, final GPXpressProdAttributeType attributeSource)
	{
		final String newQueryString = "SELECT {pk} FROM {GPXpressAlertProdHistory}"
				+ " WHERE {attributeName} IN (?attribute) AND {productCode}=?prodCode AND {productAttributeSource}=?attributeSource";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(newQueryString);
		query.addQueryParameter(ATTRIBUTE_NAME, attNames);
		query.addQueryParameter(PRODUCTCODE, gpProd);
		query.addQueryParameter(ATTRIBUTE_SOURCE, attributeSource);
		return getFlexibleSearchService().<GPXpressAlertProdHistoryModel> search(query).getResult();
	}

	@Override
	public List<GPXpressAlertProdHistoryModel> getProductAttributesToSfdc(final List<String> attributeList,
			final Date timestamp, final Date productTimeStamp, final String productCode) {

		final StringBuilder queryString = new StringBuilder();

		queryString.append(
				"SELECT {" + GPXpressAlertProdHistoryModel.PK + "} FROM {" + GPXpressAlertProdHistoryModel._TYPECODE
						+ "} WHERE {" + GPXpressAlertProdHistoryModel.ATTRIBUTENAME + "} IN (?attribute)");
		if (productTimeStamp == null && productCode == null) {
			queryString.append(" AND {" + GPXpressAlertProdHistoryModel.ATTRIBUTEMODIFIEDTIME + "} >= ?timestamp");
		}
		if (productCode != null) {
			queryString.append(" AND {" + GPXpressAlertProdHistoryModel.PRODUCTCODE + "}= ?productCode");
		}
		if (productTimeStamp != null) {
			queryString.append(" AND {" + GPXpressAlertProdHistoryModel.ATTRIBUTEMODIFIEDTIME + "} >= ?productTimeStamp");
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(ATTRIBUTE_NAME, attributeList);
		if (productTimeStamp == null && productCode == null) {
			query.addQueryParameter(TIMESTAMP, timestamp);
		}
		if (productCode != null) {
			query.addQueryParameter(PRODUCT_CODE, productCode);
		}
		if (productTimeStamp != null) {
			query.addQueryParameter(PRODUCTTIMESTAMP, productTimeStamp);
		}

		return getFlexibleSearchService().<GPXpressAlertProdHistoryModel>search(query).getResult();
	}

	public List<GPXpressAlertProdHistoryModel> getExistingAlertHistoryRecordsForCompetitorProduct(final String gpProd, final Set<String> attNames) {
		final String newQueryString = "SELECT {pk} FROM {GPXpressAlertProdHistory}"
				+ " WHERE {attributeName} IN (?attribute) AND {productCode}=?prodCode AND {productAttributeSource}=?attributeSource";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(newQueryString);
		query.addQueryParameter(ATTRIBUTE_NAME, attNames);
		query.addQueryParameter(PRODUCTCODE, gpProd);
		query.addQueryParameter(ATTRIBUTE_SOURCE, GPXpressProdAttributeType.COMPETITOR);
		return getFlexibleSearchService().<GPXpressAlertProdHistoryModel> search(query).getResult();
	}

	@Override
	public void addXpressAlertRecords(final Map<String, GPProductAuditRecord> gpProductAuditRecordMap) {
		final Set<String> keys = gpProductAuditRecordMap.keySet();
		final List<GPXpressAlertProdHistoryModel> alertHistomerModels = new ArrayList<>();
		GPProductAuditRecord gpAuditRecord;
		for(final String productCode : keys) {

			gpAuditRecord = gpProductAuditRecordMap.get(productCode);

			final Set<String> attributeNames = gpAuditRecord.getProductAttributeMap().keySet();
			for(final String attributeName : attributeNames) {
				final GPXpressAlertProdHistoryModel historyModel = new GPXpressAlertProdHistoryModel();
				final GPProductAuditAttribute attributeDetails = gpAuditRecord.getProductAttributeMap().get(attributeName);

				historyModel.setProductCode(productCode);
				historyModel.setAttributeName(attributeName);
				historyModel.setOldVlaue(attributeDetails.getOldAttributeValue().toString());
				historyModel.setNewValue(attributeDetails.getNewAttributeValue().toString());
				historyModel.setAttributeModifiedTime(attributeDetails.getDateOfChange());
				//TODO: check how the soldTos will be mapped

				alertHistomerModels.add(historyModel);
			}



		}


	}

	/**
	 * Fetched the non-obsolete online products related to the site.
	 * @param site The site for which products are to be fetched
	 * @return List of products for the site.
	 */
	@Override
	public List<ProductModel> getNonObsoleteProductsForSite(final CMSSiteModel site){
		String query = "SELECT {p.pk} FROM {Product as p JOIN ProductAvailabilityAssignment as paa ON {paa.product}={p.pk} JOIN ProductAvailabilityGroup as pag ON {pag.pk}={paa.availabilitygroup} JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag2bs.source}={pag.pk}}\r\n" +
				"WHERE {p.catalogversion}=?catalogVersion AND {pag2bs.target} IN (?stores)";
		final boolean showObsoleteProducts = Boolean.valueOf(site.getSiteConfig().get(ENABLE_AUTOSUGGEST_OBSELETE));
		if(!showObsoleteProducts) {
			query =query + " AND {p.materialStatus}!=?status";
		}
		final CatalogVersionModel catalogVersionModel = getCatalogVersionService().getCatalogVersion(Config.getParameter(GP_CATALOG_ID), Config.getParameter(GP_CATALOG_ONLINE_VERSION));
		final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
		flexQuery.addQueryParameter("stores", site.getStores());
		if(!showObsoleteProducts) {
			flexQuery.addQueryParameter("status", MaterialStatusEnum.OBSOLETE);
		}
		flexQuery.addQueryParameter("catalogVersion", catalogVersionModel);

		return getFlexibleSearchService().<ProductModel> search(flexQuery).getResult();
	}

	@Override
	public List<ProductModel> getProductsForCodes(final List<String> productCodeList) {

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_FOR_CODE);

		final Map<String, Object> params = new HashMap<>();
		params.put("code", productCodeList);
		searchQuery.addQueryParameters(params);
		return getFlexibleSearchService().<ProductModel>search(searchQuery).getResult();
	}

	@Override
	public GPProductType2ClassificationAttributeModel getProductType2ClassAttributes(final String productType) {
		final String query="SELECT {pk} FROM {GPProductType2ClassificationAttribute} WHERE UPPER({productType})=?productType";
		final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
		flexQuery.addQueryParameter("productType", productType.toUpperCase());
		final SearchResult<GPProductType2ClassificationAttributeModel> searchResult = getFlexibleSearchService().search(flexQuery);
		final List<GPProductType2ClassificationAttributeModel> results = searchResult.getResult();
		if(!results.isEmpty())
		{
			return results.get(0);
		}
		return null;
	}

	public GPPagedFlexibleSearchService getPagedFlexibleSearchService() {
		return pagedFlexibleSearchService;
	}

	public void setPagedFlexibleSearchService(final GPPagedFlexibleSearchService pagedFlexibleSearchService) {
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}

	@Override
	public List<GPCommerceProductModel> getGPCommerceProductsForCodes(final List<String> productCodes,final B2BUnitModel soldToUnit,final BrandLabelEnum brandLabel,final CategoryModel category,final int key) {

		FlexibleSearchQuery searchQuery=null;



		final Map<String, Object> params = new HashMap<>();
		final List<CategoryModel> categories=new ArrayList<>();
		final StringBuilder Querybuilder = new StringBuilder();
		Querybuilder.append("SELECT {p:pk} FROM {GPCommerceProduct AS p}");
		Querybuilder.append(" WHERE {p.catalogversion} IN ({{ SELECT {activeCatalogVersion} FROM {Catalog} WHERE {id} IN ('gpUSProductCatalog') }})");

		final StringBuilder CATEGORY_SUB_QUERY = new StringBuilder();
		CATEGORY_SUB_QUERY.append("SELECT {p:pk} FROM {Product AS p JOIN CategoryProductRelation AS l ON {l:target}={p:pk}}");
		CATEGORY_SUB_QUERY.append(" WHERE {l:source} IN (?categories)");
		CATEGORY_SUB_QUERY.append(" AND {p.catalogversion} IN ({{ SELECT {activeCatalogVersion} FROM {Catalog} WHERE {id} IN ('gpUSProductCatalog') }})");


		switch (key) {
		case 1:

			Querybuilder.append(" AND {code} IN (?code)");
			params.put("code", productCodes);
			break;
		case 2:
			if(CollectionUtils.isNotEmpty(soldToUnit.getBrands())) {
			Querybuilder.append(" AND {p.brandLabel} IN ({{ SELECT {l:target} FROM {B2BUnit AS unit JOIN B2B2BrandLabelRel AS l ON {unit:pk}={l:source}} WHERE {pk}=?unit }})");
			params.put("unit", soldToUnit);
			}
			final CategoryModel rootCategory=soldToUnit.getRootCategoryReference();
			if(null !=rootCategory) {
				Querybuilder.append(" AND {p.pk} IN ({{ "+CATEGORY_SUB_QUERY.toString()+" }})");
				categories.add(rootCategory);
				categories.addAll(rootCategory.getAllSubcategories());
				params.put("categories",categories);
			}

			break;

		case 3:
			Querybuilder.append(" AND {p.brandLabel} IN ({{ SELECT {pk} FROM {BrandLabelEnum} WHERE UPPER({code}) in (?code) }})");
			params.put("code",Collections.singletonList(brandLabel.getCode().toUpperCase()));
			if(null !=category) {
				Querybuilder.append(" AND {p.pk} IN ({{ "+CATEGORY_SUB_QUERY.toString()+" }})");
				categories.add(category);
				categories.addAll(category.getAllSubcategories());
				params.put("categories",categories);
			}
			break;

		case 4:
			Querybuilder.append(" AND {p.brandLabel} IN ({{ SELECT {pk} FROM {BrandLabelEnum} WHERE UPPER({code}) in (?code) }})");
			params.put("code",Collections.singletonList(brandLabel.getCode().toUpperCase()));
			break;
		case 5:

			if(null !=category) {
				Querybuilder.append(" AND {p.pk} IN ({{ "+CATEGORY_SUB_QUERY.toString()+" }})");
				categories.add(category);
				categories.addAll(category.getAllSubcategories());
				params.put("categories",categories);
			}
			break;

		default:
			break;
		}

		searchQuery=new FlexibleSearchQuery(Querybuilder.toString());
		searchQuery.addQueryParameters(params);
		 return getFlexibleSearchService().<GPCommerceProductModel>search(searchQuery).getResult();

	}



	@Override
	public List<ProductModel> getAllProductsForSpecsPopulatorJob(final Date endTime, final Date featureUpdateDate, final String productCode, final String catalogVersion) {

		FlexibleSearchQuery searchQuery = null;
		final Map<String, Object> params = new HashMap<>();

		if(featureUpdateDate != null) {
			searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_QUERY_BY_MODIFIED_DATE);
			params.put("modifiedTime", featureUpdateDate);
			params.put("catalogVersion", catalogVersion);
		}
		else if(productCode != null) {
			searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_QUERY_BY_PRODUCT_CODE);
			params.put("productCode", productCode);
			params.put("catalogVersion", catalogVersion);
		}
		else {
			searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_QUERY_BY_MODIFIED_DATE);
			params.put("modifiedTime", endTime);
			params.put("catalogVersion", catalogVersion);
		}
		searchQuery.addQueryParameters(params);
		return getFlexibleSearchService().<ProductModel>search(searchQuery).getResult();
	}
	
	
	public List<ProductModel> getAllProductsForGpBrandLabelPopulatorJob(final Date endTime, final Date featureUpdateDate, final String productCode, final String catalogVersion) {

		FlexibleSearchQuery searchQuery = null;
		final Map<String, Object> params = new HashMap<>();

		if(productCode != null) {			
			searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_QUERY_BY_PRODUCT_CODE);
			params.put("productCode", productCode);
			params.put("catalogVersion", catalogVersion);
		}
		else if(featureUpdateDate != null) {
			searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_QUERY_BY_MODIFIED_DATE);
			params.put("modifiedTime", featureUpdateDate);
			params.put("catalogVersion", catalogVersion);
		}
		else {
			searchQuery = new FlexibleSearchQuery(RETRIEVE_PRODUCT_QUERY_BY_MODIFIED_DATE);
			params.put("modifiedTime", endTime);
			params.put("catalogVersion", catalogVersion);
		}
		searchQuery.addQueryParameters(params);
		return getFlexibleSearchService().<ProductModel>search(searchQuery).getResult();
	}
	

	@Override
	public List<ProductModel> getProductsForSiteAndCatalogVersion(final CMSSiteModel site,
			final CatalogVersionModel catalogVersionModel)
	{
		final String query = "SELECT {p.pk} FROM {Product as p JOIN ProductAvailabilityAssignment as paa ON {paa.product}={p.pk} JOIN ProductAvailabilityGroup as pag ON {pag.pk}={paa.availabilitygroup} JOIN ProductAvailabilityGroup2BaseStoreRel as pag2bs ON {pag2bs.source}={pag.pk}}\r\n"
				+ "WHERE {p.catalogversion}=?catalogVersion AND {pag2bs.target} IN (?stores)";

		final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
		flexQuery.addQueryParameter("stores", site.getStores());
		flexQuery.addQueryParameter("catalogVersion", catalogVersionModel);

		return getFlexibleSearchService().<ProductModel> search(flexQuery).getResult();
	}

}
