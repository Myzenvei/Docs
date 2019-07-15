/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.ServiceProductTypeEnum;
import com.gp.commerce.core.model.AlternateUOMModel;
import com.gp.commerce.core.model.GPCertificationsModel;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCompetitorProductModel;
import com.gp.commerce.core.model.GPServiceProductModel;
import com.gp.commerce.core.util.GPCommerceCoreUtils;
import com.gp.commerce.core.util.GPProductSpecificationUtils;

public class GPProductFieldsValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider {

	private static final String GP_USERGROUP = "gpusergroup";
	private static final String HAS_VARIANT = "hasVariant";
	private CommonI18NService commonI18NService;
	private String indexedField;
	private static final String PRODUCT_NAME="name";
	private static final String PRODUCT_DESCRIPTION="description";
	private static final String PRODUCT_SUMMARY="summary";
	private static final String PRODUCT_EAN="ean";
	private static final String PRODUCT_CUSTOMIZABLE="customizable";
	private static final String PRODUCT_SEASONAL="seasonal";
	private static final String PRODUCT_BUNDLE="bundleAvailable";
	private static final String PRODUCT_BULKBUY="bulkBuy";
	private static final String PRODUCT_SUBSCIBABLE="subscribable";
	private static final String PRODUCT_ONLINE_ONLY="onlineOnly";
	private static final String PRODUCT_ASSET_CODE="assetCode";
	private static final String PRODUCT_DROP_SHIP="dropShipEligible";
	private static final String PRODUCT_MAX_ORDER_QTY="maxOrderQuantity";
	private static final String AVAILABLE_FOR_LOCATION="availableForLocation";
	private static final String PRODUCT_TYPE = "productType";
	private static final String PRODUCT_BRAND = "brand";
	private static final String PRODUCT_COLOR = "color";
	private static final String PRODUCT_SIZE = "Size LxWxH";
	private static final String PRODUCT_SUB_BRAND = "subbrand";
	private static final String PRODUCT_SKU_NGRAM = "sku_ngram";
	private static final String PRODUCT_SKU_EDGE = "sku_edge";
	private static final String PRODUCT_GTIN = "gtin";
	private static final String PRODUCT_SAP_DESCRIPTION = "sapDescription";
	private static final String PRODUCT_LEASABLE = "leasable";
	private static final String CERTIFICATIONS="certifications";
	private static final String PRODUCT_CERTIFICATIONS="productCertifications";
	private static final String INSTALLATION_PRODUCT = "installationProduct";
	private static final String PRODUCT_SAMPLE="sample";
	private static final String SEARCH_TEXT="search_text";
	private static final String UPC = "upc";
	private static final String PRODUCT_MATERIAL="GENERIC MATERIAL";
	private static final String PRODUCT_PLY="ply";
	private static final String PRODUCT_AUTOSUGGEST_ENABLE="autoSuggestEnable";
	private static final String COMPETITOR_REPLACEMENT_PRODUCT_CODES = "competitorReplacementProducts";
	private static final String PRODUCT_ITEMTYPE = "itemtype";
	private static final String ROOT_CATEGORY = "rootCategory";
	private static final String DEFAULT_GPUG = "DEFAULT_GPUG";
	private static final String OCCASION = "occasion";
	private static final String PRODUCT_WIDTH ="product_width";
	private static final String PRODUCT_HEIGHT = "product_height";
	private static final String PRODUCT_LENGTH ="product_length";
	private static final String PRODUCT_UOM ="product_uom";
	private static final String PRODUCT_CAPACITY ="product_capacity";
	private static final String PRODUCT_ITEMPEREACH ="product_itemspereach";
	private static final String PRODUCT_ITEMPEREACHUOM ="product_itemspereachuom";
	private static final String PRODUCT_EACHPERSHIPUNIT ="product_eachpershipunit";
	private static final String PRODUCT_EACHPERSHIPUNITUOM ="product_eachpershipunituom";
	private static final String CAPACITY = "Capacity";
	private static final String ITEMPEREACH ="Items Per Each";
	private static final String GP_PCE ="PCE";
	private static final String GP_EA = "EA";
	
	private static final String FREESHIPPING = "freeShipping";
	private static final String INSTALLABLE = "installable";
	private static final String PRODUCT_BRAND_LABEL = "brandLabel";
	private static final String SOLDINDIVIDUALLY = "soldIndividually";
	
	private static final String CASE_GTIN = "Case GTIN";
	private static final String BUNDLE_GTIN = "Bundle GTIN";
	
	
	private static final String CASE_UNIT = "CS";
	private static final String BUNDLE_UNIT = "BD";
	
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "classificationService")
	private ClassificationService classificationService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "gpProductSpecificationUtils")
	private GPProductSpecificationUtils gpProductSpecificationUtils;;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if(model instanceof PriceRowModel) {
			final ProductModel product = ((PriceRowModel)model).getProduct();
			final Collection<FieldValue> fieldValues = new ArrayList<>();

			if (indexedProperty.isLocalized()) {
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages) {
					fieldValues.addAll(createFieldValue(product, (PriceRowModel)model, language, indexedProperty, indexedField));
				}
			} else {
				fieldValues.addAll(createFieldValue(product, (PriceRowModel)model, null, indexedProperty, indexedField));
			}

			return fieldValues;
		}else {
			throw new FieldValueProviderException("Cannot evaluate rating of non-priceRow item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final PriceRowModel price, final LanguageModel language,
			final IndexedProperty indexedProperty, final String indexedField) {
		final List<FieldValue> fieldValues = new ArrayList<>();
		Object productFieldValue = null;
		if(PRODUCT_NAME.equalsIgnoreCase(indexedField)) {
			productFieldValue = language==null ?product.getName():product.getName(commonI18NService.getLocaleForLanguage(language));
		}else if(PRODUCT_DESCRIPTION.equalsIgnoreCase(indexedField)) {
			productFieldValue = language==null ?product.getDescription():product.getDescription(commonI18NService.getLocaleForLanguage(language));
		}else if(PRODUCT_SUMMARY.equalsIgnoreCase(indexedField)) {
			productFieldValue = language==null ?product.getSummary():product.getSummary(commonI18NService.getLocaleForLanguage(language));
		}else if(PRODUCT_EAN.equalsIgnoreCase(indexedField)) {
			productFieldValue = product.getEan();
		}else if(PRODUCT_CUSTOMIZABLE.equalsIgnoreCase(indexedField) && null != product.getCustomizable()) {
			productFieldValue = product.getCustomizable();
		}else if(PRODUCT_SEASONAL.equalsIgnoreCase(indexedField) && null != product.getSeasonal()) {
			productFieldValue = product.getSeasonal();
		}else if(PRODUCT_BUNDLE.equalsIgnoreCase(indexedField) && null != product.getBundleAvailable()) {
			productFieldValue = product.getBundleAvailable();
		}else if(PRODUCT_BULKBUY.equalsIgnoreCase(indexedField) && null != product.getBulkBuy()) {
			productFieldValue = product.getBulkBuy();
		}else if(PRODUCT_SUBSCIBABLE.equalsIgnoreCase(indexedField) && null != product.getSubscribable()) {
			productFieldValue = product.getSubscribable();
		}else if(PRODUCT_ONLINE_ONLY.equalsIgnoreCase(indexedField) && null != product.getOnlineOnly()) {
			productFieldValue = product.getOnlineOnly();
		}else if(PRODUCT_ASSET_CODE.equalsIgnoreCase(indexedField) && null != product.getAssetCode()) {
			productFieldValue = product.getAssetCode();
		}else if(PRODUCT_DROP_SHIP.equalsIgnoreCase(indexedField) && null != product.getDropShipEligible()) {
			productFieldValue = product.getDropShipEligible();
		}else if(PRODUCT_MAX_ORDER_QTY.equalsIgnoreCase(indexedField) && null != product.getMaxOrderQuantity()) {
			productFieldValue = product.getMaxOrderQuantity();
		}else if(AVAILABLE_FOR_LOCATION.equalsIgnoreCase(indexedField)){
			final String displayname = configurationService.getConfiguration().getString("gp.available.display.name");
			productFieldValue = displayname;
		}else if (ROOT_CATEGORY.equalsIgnoreCase(indexedField)){
			final Collection<CategoryModel> rootCategories = new ArrayList<>();
			GPCommerceCoreUtils.getProductRootCategories(rootCategories, product.getSupercategories());
			if(!rootCategories.isEmpty())
			{
				for(final CategoryModel category : rootCategories)
				{
					super.addFieldValues(fieldValues, indexedProperty, language, category.getCode());
				}
			}
		}
		else if (PRODUCT_TYPE.equalsIgnoreCase(indexedField) && null != product.getProductType())
		{
			productFieldValue = product.getProductType();

		}
		else if (PRODUCT_BRAND.equalsIgnoreCase(indexedField) && null != product.getBrand())
		{
			productFieldValue = product.getBrand().getCode();

		}
		else if (PRODUCT_COLOR.equalsIgnoreCase(indexedField) && null != checkIfFeaturePresent(product, language, PRODUCT_COLOR))
		{
			productFieldValue = getFeatureValue(product, language, PRODUCT_COLOR);

		}
		else if (PRODUCT_SIZE.equalsIgnoreCase(indexedField) && null != checkIfFeaturePresent(product, language, PRODUCT_SIZE))
		{
			productFieldValue = getFeatureValue(product, language, PRODUCT_SIZE);

		}
		else if (PRODUCT_SUB_BRAND.equalsIgnoreCase(indexedField) && null != product.getSubbrand())
		{
			productFieldValue = product.getSubbrand();

		}
		else if (PRODUCT_SKU_NGRAM.equalsIgnoreCase(indexedField) && null != product.getCode())
		{
			productFieldValue = getSkungram(product.getCode());

		}
		else if (PRODUCT_SKU_EDGE.equalsIgnoreCase(indexedField) && null != product.getCode())
		{
			productFieldValue = getSkuedge(product.getCode());

		}
		else if (PRODUCT_GTIN.equalsIgnoreCase(indexedField) && null != product.getUnit())
		{
			
			if(BUNDLE_UNIT.equalsIgnoreCase(product.getUnit().getCode()))
			{
				productFieldValue = getFeatureValue(product, language, BUNDLE_GTIN);
			}
			else
			{
				productFieldValue = getFeatureValue(product, language, CASE_GTIN);
			}

		}
		else if(PRODUCT_SAP_DESCRIPTION.equalsIgnoreCase(indexedField)) {
			productFieldValue = language==null ?product.getSapLongDescription():product.getSapLongDescription(commonI18NService.getLocaleForLanguage(language));
		}
		else if(PRODUCT_LEASABLE.equalsIgnoreCase(indexedField) && null != product.getLeaseable()) {
			productFieldValue = product.getLeaseable();
		}
		else if(PRODUCT_CERTIFICATIONS.equalsIgnoreCase(indexedField) && CollectionUtils.isNotEmpty(product.getGpcertifications())) {
			for (final GPCertificationsModel gpCertification : product.getGpcertifications())
			{
				super.addFieldValues(fieldValues, indexedProperty, language, gpCertification.getId().getCode());
			}
		}
		else if(CERTIFICATIONS.equalsIgnoreCase(indexedField) && CollectionUtils.isNotEmpty(product.getGpcertifications())) {
			productFieldValue =  true;
		}
		else if(PRODUCT_SAMPLE.equalsIgnoreCase(indexedField) && null != product.getSample()) {
			productFieldValue = product.getSample();
		}
		else if(INSTALLATION_PRODUCT.equalsIgnoreCase(indexedField)&& product instanceof GPCommerceProductModel) {
			final GPCommerceProductModel gpCommerceProduct = 	(GPCommerceProductModel)product;
			if(null != gpCommerceProduct.getInstallationProduct()){
				productFieldValue = gpCommerceProduct.getInstallationProduct().getCode();
			}
		}
		else if(UPC.equalsIgnoreCase(indexedField) && null != product.getUpc()){
			productFieldValue = product.getUpc();
		}
		else if (PRODUCT_MATERIAL.equalsIgnoreCase(indexedField) && null != checkIfFeaturePresent(product, language, PRODUCT_MATERIAL))
		{
			productFieldValue = getFeatureValue(product, language, PRODUCT_MATERIAL);

		}
		else if (PRODUCT_PLY.equalsIgnoreCase(indexedField) && null != checkIfFeaturePresent(product, language, PRODUCT_PLY))
		{
			productFieldValue = getFeatureValue(product, language, PRODUCT_PLY);

		}
		else if(SEARCH_TEXT.equalsIgnoreCase(indexedField)){
			populateSearchText(product, language, indexedProperty, fieldValues);
		}
		else if(PRODUCT_AUTOSUGGEST_ENABLE.equalsIgnoreCase(indexedField))
		{
			productFieldValue = Boolean.TRUE.toString();
		}
		else if(COMPETITOR_REPLACEMENT_PRODUCT_CODES.equalsIgnoreCase(indexedField) && product instanceof GPCompetitorProductModel){
			final GPCompetitorProductModel competitorProduct = (GPCompetitorProductModel)product;
			if(CollectionUtils.isNotEmpty(competitorProduct.getProduct())){
				for (final ProductModel replacementProduct : competitorProduct.getProduct()) {
					super.addFieldValues(fieldValues, indexedProperty, language, replacementProduct.getCode());
				}
			}
		}
		else if(PRODUCT_ITEMTYPE.equalsIgnoreCase(indexedField) && null != product.getItemtype()) {
			productFieldValue = product.getItemtype();
		}
		else if(HAS_VARIANT.equalsIgnoreCase(indexedField)) {
			if(null!=product.getVariantType()) {
				productFieldValue=Boolean.TRUE;
			}else {
				productFieldValue=Boolean.FALSE;
			}
		}
		else if(GP_USERGROUP.equalsIgnoreCase(indexedField))
		{
			if(null != price.getGpUserPriceGroup()) {
				productFieldValue=price.getGpUserPriceGroup().getCode();
			}
			else
			{
				productFieldValue = DEFAULT_GPUG;
			}
		}
		else if(OCCASION.equalsIgnoreCase(indexedField) && null != product.getOccasion())
		{
			productFieldValue = product.getOccasion().getCode();
		}
		else if(PRODUCT_WIDTH.equalsIgnoreCase(indexedField))
		{
			final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(product,
					GP_PCE);
			if(null != gpAlternateUOMForProduct )
			{
				productFieldValue = gpAlternateUOMForProduct.getWidth();
			}
		}
		else if (PRODUCT_LENGTH.equalsIgnoreCase(indexedField))
		{
			final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(product,
					GP_PCE);
			if (null != gpAlternateUOMForProduct)
			{
				productFieldValue = gpAlternateUOMForProduct.getLength();
			}
		}
		else if (PRODUCT_HEIGHT.equalsIgnoreCase(indexedField))
		{
			final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(product,
					GP_PCE);
			if (null != gpAlternateUOMForProduct)
			{
				productFieldValue = gpAlternateUOMForProduct.getHeight();
			}
		}
		else if (PRODUCT_UOM.equalsIgnoreCase(indexedField))
		{
			final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(product,
					GP_PCE);
			if (null != gpAlternateUOMForProduct)
			{
				productFieldValue = gpAlternateUOMForProduct.getDimensionsuom();
			}
		}
		else if (PRODUCT_EACHPERSHIPUNIT.equalsIgnoreCase(indexedField))
		{
			final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(product,
					GP_EA);
			if (null != gpAlternateUOMForProduct)
			{
				productFieldValue = gpAlternateUOMForProduct.getAltX();
			}
		}
		else if (PRODUCT_EACHPERSHIPUNITUOM.equalsIgnoreCase(indexedField))
		{
			final AlternateUOMModel gpAlternateUOMForProduct = gpProductSpecificationUtils.getGPAlternateUOMForProduct(product,
					GP_EA);
			if (null != gpAlternateUOMForProduct)
			{
				productFieldValue = gpAlternateUOMForProduct.getAltXUOM();
			}
		}
		else if (PRODUCT_ITEMPEREACHUOM.equalsIgnoreCase(indexedField) && null != product.getItemsPerEachUOM())
		{
			productFieldValue = product.getItemsPerEachUOM();
		}
		else if (PRODUCT_CAPACITY.equalsIgnoreCase(indexedField) && null != checkIfFeaturePresent(product, language, CAPACITY))
		{
			productFieldValue = getFeatureValue(product, language, CAPACITY);

		}
		else if (PRODUCT_ITEMPEREACH.equalsIgnoreCase(indexedField) && null != checkIfFeaturePresent(product, language, ITEMPEREACH))
		{
			productFieldValue = getFeatureValue(product, language, ITEMPEREACH);

		}
		else if (FREESHIPPING.equalsIgnoreCase(indexedField) && null != product.getIsFreeShippingAllowed())
		{
			productFieldValue = product.getIsFreeShippingAllowed();
		}
		else if (INSTALLABLE.equalsIgnoreCase(indexedField) && product instanceof GPCommerceProductModel)
		{
			final GPCommerceProductModel gpServiceProductModel = (GPCommerceProductModel) product;
			if (gpServiceProductModel.getInstallationProduct() != null)
			{
				productFieldValue = true;
			}
		}
		else if (PRODUCT_BRAND_LABEL.equalsIgnoreCase(indexedField) && null != product.getBrandLabel())
		{
			productFieldValue = product.getBrandLabel().getCode();

		}
		else if(SOLDINDIVIDUALLY.equalsIgnoreCase(indexedField))
		{
			productFieldValue = BooleanUtils.toBoolean(product.getSoldIndividually());
		}
		if (null != productFieldValue){
			super.addFieldValues(fieldValues, indexedProperty, language, productFieldValue);
		}
		return fieldValues;
	}

	private void populateSearchText(final ProductModel product, final LanguageModel language,
			final IndexedProperty indexedProperty, final List<FieldValue> fieldValues) {
		String fieldValue = language==null ?product.getName():product.getName(commonI18NService.getLocaleForLanguage(language));
		super.addFieldValues(fieldValues, indexedProperty, language, fieldValue);
		fieldValue = language==null ?product.getDescription():product.getDescription(commonI18NService.getLocaleForLanguage(language));
		super.addFieldValues(fieldValues, indexedProperty, language, fieldValue);
		final Object value = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCommonI18NService().setCurrentLanguage(language);
				return getPropertyValue(product, indexedProperty);
			}
		});
		super.addFieldValues(fieldValues, indexedProperty, language, value);

		if(null != product.getFeatureList()){
			for (final String feature : product.getFeatureList()) {
				super.addFieldValues(fieldValues, indexedProperty, language, feature);
			}
		}
	}

	private Boolean checkIfFeaturePresent(final ProductModel product, final LanguageModel language, final String featureCode)
	{

		if (product.getFeatures() != null)
		{
			for (final ProductFeatureModel feature : product.getFeatures())
			{
				if (null != feature && null != feature.getClassificationAttributeAssignment() &&
						null != feature.getClassificationAttributeAssignment().getClassificationAttribute() &&
						feature.getClassificationAttributeAssignment().getClassificationAttribute()
						.getName(null != language ? commonI18NService.getLocaleForLanguage(language):null).equalsIgnoreCase(featureCode))
				{
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private String getFeatureValue(final ProductModel product, final LanguageModel language, final String featureCode)
	{
		if (product.getFeatures() != null)
		{
			for (final ProductFeatureModel productFeature : product.getFeatures())
			{
				if (null != productFeature && null != productFeature.getClassificationAttributeAssignment() &&
						null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute() &&
						productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						.getName(null != language ? commonI18NService.getLocaleForLanguage(language):null).equalsIgnoreCase(featureCode))
				{
					if (productFeature.getValue() != null)
					{
						final Object value = productFeature.getValue();
						if(value instanceof ClassificationAttributeValueModel)
                        {
                               return ((ClassificationAttributeValueModel)value).getName();
                        }
						else if (value instanceof HybrisEnumValue)
						{
							return ((HybrisEnumValue) value).getCode();
						}

						return value != null ? value.toString() : null;
					}
				}
			}
		}
		return null;
	}

	private Object getSkungram(final String code) {
		if(code.length() > 3){
			return code.substring(GpcommerceCoreConstants.SKU_NGRAM_START_INDEX, GpcommerceCoreConstants.SKU_EDGE_NO_CHARACTERS);
		}
		return code;
	}

	private Object getSkuedge(final String code) {
		if(code.length() > 3){
			return code.substring(code.length() - GpcommerceCoreConstants.SKU_EDGE_NO_CHARACTERS);
		}
		return code;
	}

	protected Object getPropertyValue(final Object model, final IndexedProperty indexedProperty)
	{
		if (model instanceof PriceRowModel)
		{
			final ProductModel product = ((PriceRowModel)model).getProduct();
			final Set<String> keywords = new HashSet<>();
			collectProductKeywords(keywords, product);

			if (!keywords.isEmpty())
			{
				final StringBuilder buf = new StringBuilder();

				for (final String keyword : keywords)
				{
					buf.append(keyword).append(' ');
				}

				return buf.toString();
			}
		}
		return null;
	}

	protected void collectProductKeywords(final Set<String> words, final ProductModel product)
	{
		final List<KeywordModel> keywords = product.getKeywords();
		if (keywords != null && !keywords.isEmpty())
		{
			for (final KeywordModel keyword : keywords)
			{
				words.add(keyword.getKeyword());
			}
		}

		if (product instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
			if (baseProduct != null)
			{
				collectProductKeywords(words, baseProduct);
			}
		}
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public String getIndexedField() {
		return indexedField;
	}

	public void setIndexedField(final String indexedField) {
		this.indexedField = indexedField;
	}

}
