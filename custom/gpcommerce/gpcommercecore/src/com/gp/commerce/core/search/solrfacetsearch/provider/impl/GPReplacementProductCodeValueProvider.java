/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

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
import java.util.List;


public class GPReplacementProductCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider{

	private FieldNameProvider fieldNameProvider;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final ProductModel product = ((PriceRowModel) model).getProduct();
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			fieldValues.addAll(createFieldValue(product, indexedProperty));
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get dates of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (null != product.getReplacedBy())
		{
			addFieldValues(fieldValues, indexedProperty,product.getReplacedBy().getCode());
		}

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}

}
