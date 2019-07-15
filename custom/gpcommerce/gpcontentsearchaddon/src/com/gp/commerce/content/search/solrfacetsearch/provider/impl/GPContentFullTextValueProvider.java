/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.solrfacetsearch.provider.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

public class GPContentFullTextValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private static final Logger LOG = Logger.getLogger(GPContentFullTextValueProvider.class);
	final String MV = "_mv";
	@Resource(name="solrFieldNameProvider")
	private transient FieldNameProvider fieldNameProvider;
	private Map<String, String> componentTypeKeyProviders;

	@Override
	public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model)
			throws FieldValueProviderException {
		final List<FieldValue> fieldValues = new ArrayList<>();

		String fieldName = indexedProperty.getName() + "_" + indexedProperty.getType();
		if (indexedProperty.isMultiValue())
		{
			fieldName = fieldName + MV;
		}
		if (model instanceof ContentPageModel )
		{
			final ContentPageModel page = (ContentPageModel) model;
			doContentSearch(page, fieldValues, fieldName, indexConfig, indexedProperty);
		}
		else
		{
			LOG.info("Model is not instance of ContentPage Model so ignoring while indexing" + "-"
					+ indexedProperty);
		}
		return fieldValues;
	}

		/**
		 * Do content search.
		 *
		 * @param page the page
		 * @param fieldValues the field values
		 * @param fieldName the field name
		 * @param indexConfig the index config
		 * @param indexedProperty the indexed property
		 */
		void doContentSearch(final ContentPageModel page, final List<FieldValue> fieldValues, final String fieldName,
				final IndexConfig indexConfig, final IndexedProperty indexedProperty)
		{
			if (null != page)
			{
				if (CollectionUtils.isNotEmpty(page.getContentSlots()))
				{
				LOG.info("Getting Content from page" + page.getName());	
					getContentFromSlots(page.getContentSlots(), fieldValues, fieldName);
				}
				else
				{
					LOG.info("no Content Slot found for page with pageID: " + page.getUid());
				}
				if(CollectionUtils.isNotEmpty(page.getMasterTemplate().getContentSlots()))
				{
					LOG.info("Getting Content from page template of page" + page.getName() + page.getMasterTemplate().getName());	

					getContentFromTemplateSlots(page.getMasterTemplate().getContentSlots(), fieldValues, fieldName);
				}
				else
				{
					LOG.info("no Content Slot found for page template with pageID: " + page.getUid());
				}
			}
		}

		private void getContentFromTemplateSlots(List<ContentSlotForTemplateModel> contentSlots, List<FieldValue> fieldValues,
				String fieldName) {		
			for (int i = 0; i < contentSlots.size(); i++)
			{
				for (final AbstractCMSComponentModel componentModel : contentSlots.get(i).getContentSlot()
						.getCmsComponents())
				{
					getComponentContent(fieldValues, fieldName, componentModel);
				}
			}
		}

		private void getContentFromSlots(final List<ContentSlotForPageModel> list, final List<FieldValue> fieldValues,
				final String fieldName) {
			for (int i = 0; i < list.size(); i++)
			{
				for (final AbstractCMSComponentModel componentModel : list.get(i).getContentSlot()
						.getCmsComponents())
				{
					getComponentContent(fieldValues, fieldName, componentModel);
				}
			}
		}

		private void getComponentContent(final List<FieldValue> fieldValues, final String fieldName,
				final Object componentModel) {
			StringBuilder fullText = new StringBuilder();
			Set<String> componentKey = getComponentTypeKeyProviders().keySet();
			try {
				if(componentKey.contains(componentModel.getClass().getSimpleName())){
					String values = getComponentTypeKeyProviders().get(componentModel.getClass().getSimpleName());
					final List<String> attributeList = Arrays.asList(values.split(","));
					for (String attributeName : attributeList) {
						PropertyDescriptor pd = new PropertyDescriptor(attributeName, componentModel.getClass());
						Method getter = pd.getReadMethod();
						
						if(getter.getReturnType().getName().contains("Collection")){
							appendCollection(fieldValues,fieldName,fullText, (Collection<Object>) getter.invoke(componentModel));
						}
						else if(getter.getReturnType().getName().contains("List")){
							appendList(fieldValues,fieldName,fullText, (List<Object>) getter.invoke(componentModel));
						}
						else{
							appendContent((String) getter.invoke(componentModel), fullText);
						}
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
				LOG.error("Exception occured in Content Value Provider" + e);
			} 
			if(isNotEmpty(fullText.toString()))
			{
				fieldValues.add(new FieldValue(fieldName, getTextFromHtml(fullText)));
			}
		}

		private void appendCollection(List<FieldValue> fieldValues, String fieldName, StringBuilder fullText, Collection<Object> values) {
			for (Object object : values) {
				getComponentContent(fieldValues, fieldName, object);
			}
			
		}

		private void appendList(List<FieldValue> fieldValues, String fieldName, StringBuilder fullText, List<Object> values) {
			for (Object object : values) {
				getComponentContent(fieldValues, fieldName, object);
			}			
		}

		/**
		 * Gets the text from html.
		 *
		 * @param content the content
		 * @return the text from html
		 */
		private Object getTextFromHtml(final StringBuilder content)
		{
			if (null != content && !content.toString().isEmpty())
			{
				return content;
			}
			return content;

		}
		
		private boolean isNotEmpty(final String text)
		{
			if (null != text && !text.trim().isEmpty())
			{
				return true;
			}
			return false;
		}
		
		private void appendContent(final String content, final StringBuilder fullText)
		{
			if(isNotEmpty(content))
			{
				if(isNotEmpty(fullText.toString()))
				{
					fullText.append(" ");
				}
				fullText.append(content);
			}
		}

		public Map<String, String> getComponentTypeKeyProviders() {
			return componentTypeKeyProviders;
		}

		public void setComponentTypeKeyProviders(Map<String, String> componentTypeKeyProviders) {
			this.componentTypeKeyProviders = componentTypeKeyProviders;
		}
	}
