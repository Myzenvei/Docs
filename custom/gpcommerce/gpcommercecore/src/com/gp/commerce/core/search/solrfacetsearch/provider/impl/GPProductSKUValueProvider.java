/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class GPProductSKUValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider
{
	private String indexType;
	private static final String PRODUCT_UPPERCASE_SKU = "productUppercaseSku";
	private static final String PRODUCT_ORIGINAL_SKU = "originalSku";
	
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final PriceRowModel priceRow = (PriceRowModel) model;
			final ProductModel product = priceRow.getProduct();
			final Collection<FieldValue> fieldValues = new ArrayList<>();
			fieldValues.addAll(createFieldValue(product, null, indexedProperty, indexType));
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @param product
	 * @param language
	 * @param indexedProperty
	 * @param indexType 
	 * @return fieldValues
	 */
	protected List<FieldValue> createFieldValue(final ProductModel product, final LanguageModel language,
			final IndexedProperty indexedProperty, String indexType) {
		final List<FieldValue> fieldValues = new ArrayList<>();
		final String productCode = product.getCode();
		
		if (PRODUCT_UPPERCASE_SKU.equalsIgnoreCase(indexType) && StringUtils.isNotEmpty(productCode))
		{
			super.addFieldValues(fieldValues, indexedProperty, language, productCode.toUpperCase());
		}
		else if(PRODUCT_ORIGINAL_SKU.equalsIgnoreCase(indexType) && StringUtils.isNotEmpty(productCode))
		{
			super.addFieldValues(fieldValues, indexedProperty, language, productCode);
		}
		else
		{
			super.addFieldValues(fieldValues, indexedProperty, language, null);
		}

		return fieldValues;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
}
