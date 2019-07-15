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
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.gp.commerce.core.enums.CertificationsEnum;

public class GPProductSalePriceValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider{

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final PriceRowModel priceRow = (PriceRowModel) model;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			if (indexedProperty.isLocalized())
			{
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages) {
					fieldValues.addAll(createFieldValue(priceRow, language, indexedProperty));
				}
			}
			else
			{
				fieldValues.addAll(createFieldValue(priceRow, null, indexedProperty));
			}
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
	 * @return fieldValues
	 */
	protected List<FieldValue> createFieldValue(final PriceRowModel priceRow, final LanguageModel language,
			final IndexedProperty indexedProperty) {
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
			super.addFieldValues(fieldValues, indexedProperty, language, null != priceRow.getWeblistPrice() ? true:false);

		return fieldValues;
	}

}
