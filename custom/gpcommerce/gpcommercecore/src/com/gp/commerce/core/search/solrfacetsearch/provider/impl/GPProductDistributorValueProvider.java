/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
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


public class GPProductDistributorValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider
{
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PriceRowModel)
		{
			final Collection<FieldValue> fieldValues = new ArrayList<>();
			final ProductModel product = ((PriceRowModel) model).getProduct();

			final Collection<B2BUnitModel> distributors = product.getDistributorIds();
			if (null != distributors && !distributors.isEmpty())
			{
				fieldValues.addAll(createFieldValue(distributors, indexedProperty));
			}
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}


	/**
	 * @param distributors
	 * @param indexedProperty
	 * @return
	 */
	protected List<FieldValue> createFieldValue(final Collection<B2BUnitModel> distributors, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<>();
		for (final B2BUnitModel distributor : distributors)
		{
			super.addFieldValues(fieldValues, indexedProperty, null, distributor.getUid());
		}
		return fieldValues;
	}
}
