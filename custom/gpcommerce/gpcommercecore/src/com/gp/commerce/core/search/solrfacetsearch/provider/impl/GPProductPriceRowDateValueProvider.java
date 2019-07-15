/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

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


public class GPProductPriceRowDateValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider{

	private static final String PRICE_DATE_FORMAT = "price.date.format";

	private String indexedField;
	private static final String PRICE_START_DATE = "priceStartDate";
	private static final String PRICE_END_DATE = "priceEndDate";
	
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final PriceRowModel priceRowmodel = (PriceRowModel) model;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			fieldValues.addAll(createFieldValue(priceRowmodel, indexedProperty, indexedField));
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get dates of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final PriceRowModel priceRow, final IndexedProperty indexedProperty,
			final String indexedField)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (PRICE_START_DATE.equalsIgnoreCase(indexedField))
		{
				
					final SimpleDateFormat formatter = new SimpleDateFormat(configurationService.getConfiguration().getString(PRICE_DATE_FORMAT));
					int startDate = configurationService.getConfiguration().getInt("default.start.date");
					super.addFieldValues(fieldValues, indexedProperty, null, (null != priceRow.getStartTime() ? formatter.format(priceRow.getStartTime()) : startDate));
		}
				
		else if(PRICE_END_DATE.equalsIgnoreCase(indexedField))
		{
			final SimpleDateFormat formatter = new SimpleDateFormat(configurationService.getConfiguration().getString(PRICE_DATE_FORMAT));
			int endDate = configurationService.getConfiguration().getInt("default.end.date");
			super.addFieldValues(fieldValues, indexedProperty, null, (null != priceRow.getEndTime() ? formatter.format(priceRow.getEndTime()) : endDate));
		}
		else
		{
			super.addFieldValues(fieldValues, indexedProperty, null, null);
		}
		return fieldValues;
	}

	public String getIndexedField() {
		return indexedField;
	}

	public void setIndexedField(String indexedField) {
		this.indexedField = indexedField;
	}


}
