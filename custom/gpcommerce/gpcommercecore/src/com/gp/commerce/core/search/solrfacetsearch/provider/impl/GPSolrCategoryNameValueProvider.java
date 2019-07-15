/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;
import de.hybris.platform.europe1.model.PriceRowModel;

public class GPSolrCategoryNameValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {
	private String categoriesQualifier;
	private FieldNameProvider fieldNameProvider;

	public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model)
			throws FieldValueProviderException {
		Collection<CategoryModel> categories = null;
		ProductModel product = ((PriceRowModel)model).getProduct();
		if (product instanceof VariantProductModel) {
			ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
			categories = (Collection) this.modelService.getAttributeValue(baseProduct, this.categoriesQualifier);
		} else {
			categories = (Collection) this.modelService.getAttributeValue(product, this.categoriesQualifier);
		}

		if (categories != null && !categories.isEmpty()) {
			Collection<FieldValue> fieldValues = new ArrayList();
			Iterator var7 = categories.iterator();

			while (true) {
				while (var7.hasNext()) {
					CategoryModel category = (CategoryModel) var7.next();
					if (indexedProperty.isLocalized()) {
						Collection<LanguageModel> languages = indexConfig.getLanguages();
						Iterator var10 = languages.iterator();

						while (var10.hasNext()) {
							LanguageModel language = (LanguageModel) var10.next();
							fieldValues.addAll(this.createFieldValue(category, language, indexedProperty));
							Iterator var12 = category.getAllSupercategories().iterator();

							while (var12.hasNext()) {
								CategoryModel superCategory = (CategoryModel) var12.next();
								fieldValues.addAll(this.createFieldValue(superCategory, language, indexedProperty));
							}
						}
					} else {
						fieldValues.addAll(this.createFieldValue(category, (LanguageModel) null, indexedProperty));
						Iterator var9 = category.getAllSupercategories().iterator();

						while (var9.hasNext()) {
							CategoryModel superCategory = (CategoryModel) var9.next();
							fieldValues.addAll(
									this.createFieldValue(superCategory, (LanguageModel) null, indexedProperty));
						}
					}
				}

				return fieldValues;
			}
		} else {
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(CategoryModel category, LanguageModel language,
			IndexedProperty indexedProperty) {
		List<FieldValue> fieldValues = new ArrayList();
		Object value = null;
		if (language != null) {
			Locale locale = this.i18nService.getCurrentLocale();

			try {
				this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
				value = this.getPropertyValue(category, "name");
			} finally {
				this.i18nService.setCurrentLocale(locale);
			}

			Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
					language.getIsocode());
			Iterator var9 = fieldNames.iterator();

			while (var9.hasNext()) {
				String fieldName = (String) var9.next();
				fieldValues.add(new FieldValue(fieldName, value));
			}
		} else {
			value = this.getPropertyValue(category, "name");
			Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, (String) null);
			Iterator var14 = fieldNames.iterator();

			while (var14.hasNext()) {
				String fieldName = (String) var14.next();
				fieldValues.add(new FieldValue(fieldName, value));
			}
		}

		return fieldValues;
	}

	protected Object getPropertyValue(Object model, String propertyName) {
		return this.modelService.getAttributeValue(model, propertyName);
	}

	@Required
	public void setCategoriesQualifier(String categoriesQualifier) {
		this.categoriesQualifier = categoriesQualifier;
	}

	@Required
	public void setFieldNameProvider(FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}
}
