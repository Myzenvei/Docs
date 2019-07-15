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
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.gp.commerce.core.enums.BrandsEnum;

public class GPProductBrandValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider{

	
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException 
	{
		if (model instanceof PriceRowModel)
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			final ProductModel product = ((PriceRowModel) model).getProduct();
			
			final BrandsEnum brands = product.getBrand();
			if (brands != null)
			{
					fieldValues.addAll(createFieldValue(brands, indexedProperty));
			}
			return fieldValues;
		}
		else{
			return Collections.emptyList();
		}
	}

	/**
	 * @param product
	 * @param language
	 * @param indexedProperty
	 * @return fieldValues
	 */
	protected List<FieldValue> createFieldValue(final BrandsEnum brand, final IndexedProperty indexedProperty) {
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		super.addFieldValues(fieldValues, indexedProperty, null, brand.getCode());
		return fieldValues;

	}

}
