/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
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

import javax.annotation.Resource;

import com.gp.commerce.core.enums.CertificationsEnum;

public class GPProductPriceFieldValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider{

	private String indexedField;
	private static final String PRICE_USER_GROUP = "priceUserGroup";
	private static final String PRICE_GP_USER_GROUP = "priceGpUserGroup";
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final PriceRowModel priceRowModel = (PriceRowModel) model;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			if (indexedProperty.isLocalized())
			{
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages) {
					fieldValues.addAll(createFieldValue(priceRowModel, language, indexedProperty, indexedField));
				}
			}
			else
			{
				fieldValues.addAll(createFieldValue(priceRowModel, null, indexedProperty, indexedField));
			}
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @param priceRowModel
	 * @param language
	 * @param indexedProperty
	 * @return fieldValues
	 */
	protected List<FieldValue> createFieldValue(final PriceRowModel priceRowModel, final LanguageModel language,
			final IndexedProperty indexedProperty, final String indexedField) {
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		
		if (PRICE_USER_GROUP.equalsIgnoreCase(indexedField))
		{
			String DEFAULT_USER_PRICE_GROUP = configurationService.getConfiguration().getString("gp.default.price.group");
			final HybrisEnumValue userGroup = priceRowModel.getUg();
				super.addFieldValues(fieldValues, indexedProperty, language, null != userGroup? userGroup.getCode() : DEFAULT_USER_PRICE_GROUP);
		}
		
		else if(PRICE_GP_USER_GROUP.equalsIgnoreCase(indexedField))
		{
			final HybrisEnumValue gpUserGroup = priceRowModel.getGpUserPriceGroup();
				super.addFieldValues(fieldValues, indexedProperty, language, null != gpUserGroup? gpUserGroup.getCode() : "gpug");
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
