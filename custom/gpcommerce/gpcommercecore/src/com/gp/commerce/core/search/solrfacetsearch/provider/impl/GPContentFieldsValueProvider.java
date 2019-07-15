/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
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

import org.springframework.beans.factory.annotation.Required;


/**
 * /**
 * Resolve the URL for a content page
 */
public class GPContentFieldsValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private String indexedField;
	private static final String PAGE_URL="pageUrl";
	private static final String PAGE="page";
	private static final String CONTENT_TYPE="contentType";
	private static final String IS_KNOWLEDGE_CENTER_DOCUMENT="isKnowledgeCenterDocument";
	private static final String PAGE_TITLE="pageTitle";
	
	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ContentPageModel)
		{
			final ContentPageModel page = (ContentPageModel) model;

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			if (indexedProperty.isLocalized())
			{
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages)
				{
					fieldValues.addAll(createFieldValue(page, language, indexedProperty, indexedField));
				}
			}
			else
			{
				fieldValues.addAll(createFieldValue(page, null, indexedProperty, indexedField));
			}
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate rating of non-page item");
		}
	}

	protected List<FieldValue> createFieldValue(final ContentPageModel page, final LanguageModel language,
			final IndexedProperty indexedProperty, final String indexedField)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		Object contentFieldValue = null;
		if(PAGE_URL.equalsIgnoreCase(indexedField)) {
			contentFieldValue = page.getLabel();
		}else if(CONTENT_TYPE.equalsIgnoreCase(indexedField)) {
			contentFieldValue = PAGE;
		}else if(IS_KNOWLEDGE_CENTER_DOCUMENT.equalsIgnoreCase(indexedField)) {
			contentFieldValue = false;
		}else if(PAGE_TITLE.equalsIgnoreCase(indexedField)) {
			contentFieldValue = page.getTitle();
		}
		
		if (null != contentFieldValue)
		{
			addFieldValues(fieldValues, indexedProperty, language, contentFieldValue);
		}
		
		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}


	public String getIndexedField() {
		return indexedField;
	}

	public void setIndexedField(String indexedField) {
		this.indexedField = indexedField;
	}
}
