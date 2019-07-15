/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.product.services.GPProductService;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

public class GPProductCMIRCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider{

	private FieldNameProvider fieldNameProvider;
	@Resource(name = "gpProductService")
	GPProductService gpProductService;
	
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
			return Collections.emptyList();
		}
	}

	/**
	 * @param product
	 * @param language
	 * @param indexedProperty
	 * @return fieldValues
	 */
	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexedProperty indexedProperty) {
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if(null != product){
			List<GPCustomerMaterialInfoModel> result = gpProductService.getCMIRCodeForProduct(product.getCode());
			for (GPCustomerMaterialInfoModel gpCustomerMaterialInfoModel : result) {
				addFieldValues(fieldValues, indexedProperty, gpCustomerMaterialInfoModel.getSoldToId()+":"+gpCustomerMaterialInfoModel.getCmirCode());
			}
			
		}
		
		
			
		

		return fieldValues;
	}

	/**
	 * @param fieldValues
	 * @param indexedProperty
	 * @param value
	 */
	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final Object value) {
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,null);
		for (final String fieldName : fieldNames) {
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	public FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}
}
