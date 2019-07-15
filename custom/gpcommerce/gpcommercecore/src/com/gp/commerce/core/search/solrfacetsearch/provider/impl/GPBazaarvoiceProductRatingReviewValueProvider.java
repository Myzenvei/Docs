/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

 /*[y] hybris Platform

   Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.

   This software is the confidential and proprietary information of SAP ("Confidential Information"). You shall not
   disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement
   you entered into with SAP.
*/
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

   /* This ValueProvider will provide the product average review rating. */
public class GPBazaarvoiceProductRatingReviewValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider {
	private static final String RATING = "USD";
	private FieldNameProvider fieldNameProvider;
	private String indexType;
	private static final String RATING_RANGE = "ratingRange";
	private static final String PRODUCT_RATING = "productRating";
	private static final String PRODUCT_REVIEW = "productReviews";

	protected FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final ProductModel product = ((PriceRowModel) model).getProduct();

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			if (indexedProperty.isLocalized()) {
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages) {
					fieldValues.addAll(createFieldValue(product, language, indexedProperty,indexType));
				}
			} else {
				fieldValues.addAll(createFieldValue(product, null, indexedProperty, indexType));
			}
			return fieldValues;
		} else {
			throw new FieldValueProviderException("Cannot evaluate rating of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final LanguageModel language,
			final IndexedProperty indexedProperty, final String indexType) throws FieldValueProviderException {
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if(PRODUCT_RATING.equalsIgnoreCase(indexType) && null != product.getBvAverageRating()){
				final Double productRating = product.getBvAverageRating();
				final DecimalFormat df = new DecimalFormat("###.#");
				addFieldValues(fieldValues, indexedProperty, language, df.format(productRating));
	
		}
		else if(RATING_RANGE.equalsIgnoreCase(indexType) && null != product.getBvAverageRating()) {
			List<String> rangeNameList;
			final Double productRating = product.getBvAverageRating();
			rangeNameList = getRangeNameList(indexedProperty, productRating,RATING);
				final DecimalFormat df = new DecimalFormat("###.#");
				final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
						RATING.toLowerCase());
				addFieldValues(fieldValues, indexedProperty, fieldNames, rangeNameList, df.format(productRating));
		}
		else if(PRODUCT_REVIEW.equalsIgnoreCase(indexType) && null != product.getBvNumberOfReviews()){
				addFieldValues(fieldValues, indexedProperty, language, product.getBvNumberOfReviews());
			}

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final Collection<String> fieldNames, final List<String> rangeNameList, final Object value) {
	
		for (final String fieldName : fieldNames) {
			if (rangeNameList.isEmpty())
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
			else
			{
				for (final String rangeName : rangeNameList)
				{
					fieldValues.add(new FieldValue(fieldName, rangeName == null ? value : rangeName));
				}
			}
		}
	}
	
	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value) {
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		for (final String fieldName : fieldNames) {
				fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(final String indexType) {
		this.indexType = indexType;
	}

}
