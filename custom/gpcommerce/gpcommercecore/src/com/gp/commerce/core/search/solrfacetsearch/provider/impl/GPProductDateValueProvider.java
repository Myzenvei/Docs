/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;


public class GPProductDateValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider{

	private static final String DATE_FORMAT = "date.format";
	private String indexType;
	private static final String PRODUCT_ONLINE_DATE = "productOnlineDate";
	private static final String PRODUCT_OFFLINE_DATE = "productOfflineDate";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final ProductModel product = ((PriceRowModel) model).getProduct();
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			fieldValues.addAll(createFieldValue(product, indexedProperty, indexType));
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get dates of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexedProperty indexedProperty,
			final String indexType)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (PRODUCT_ONLINE_DATE.equalsIgnoreCase(indexType) && null != product.getOnlineDate())
		{
				final SimpleDateFormat formatter = new SimpleDateFormat(configurationService.getConfiguration().getString(DATE_FORMAT));
				super.addFieldValues(fieldValues, indexedProperty, null, formatter.format(product.getOnlineDate()));
		}
		else if(PRODUCT_OFFLINE_DATE.equalsIgnoreCase(indexType) && null != product.getOfflineDate())
		{
			final SimpleDateFormat formatter = new SimpleDateFormat(configurationService.getConfiguration().getString(DATE_FORMAT));
			super.addFieldValues(fieldValues, indexedProperty, null, formatter.format(product.getOfflineDate()));
		}
		else
		{
			super.addFieldValues(fieldValues, indexedProperty, null, null);
		}
		return fieldValues;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(final String indexType) {
		this.indexType = indexType;
	}

}
