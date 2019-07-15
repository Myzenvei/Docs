/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
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
import org.springframework.beans.factory.annotation.Required;

public class GPSolrCategoryCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {
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

			while (var7.hasNext()) {
				CategoryModel category = (CategoryModel) var7.next();
				fieldValues.addAll(this.createFieldValue(category, indexedProperty));
				Iterator var9 = category.getAllSupercategories().iterator();

				while (var9.hasNext()) {
					CategoryModel superCategory = (CategoryModel) var9.next();
					fieldValues.addAll(this.createFieldValue(superCategory, indexedProperty));
				}
			}

			return fieldValues;
		} else {
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(CategoryModel category, IndexedProperty indexedProperty) {
		List<FieldValue> fieldValues = new ArrayList();
		Object value = this.getPropertyValue(category, "code");
		Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, (String) null);
		Iterator var7 = fieldNames.iterator();

		while (var7.hasNext()) {
			String fieldName = (String) var7.next();
			fieldValues.add(new FieldValue(fieldName, value));
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