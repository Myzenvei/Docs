/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

public class GPCategoryPathValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {
	private CategorySource categorySource;
	private FieldNameProvider fieldNameProvider;
	private CategoryService categoryService;

	protected CategorySource getCategorySource()
	{
		return categorySource;
	}

	@Required
	public void setCategorySource(final CategorySource categorySource)
	{
		this.categorySource = categorySource;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		ProductModel product = ((PriceRowModel)model).getProduct();
		final Collection<CategoryModel> categories = getCategorySource().getCategoriesForConfigAndProperty(indexConfig,
				indexedProperty, product);
		if (categories != null && !categories.isEmpty())
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			final Set<String> categoryPaths = getCategoryPaths(categories);
			fieldValues.addAll(createFieldValue(categoryPaths, indexedProperty));

			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(final Collection<String> categoryPaths, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			for (final String path : categoryPaths)
			{
				fieldValues.add(new FieldValue(fieldName, path));
			}
		}

		return fieldValues;
	}

	protected Set<String> getCategoryPaths(final Collection<CategoryModel> categories)
	{
		final Set<String> allPaths = new HashSet<String>();

		for (final CategoryModel category : categories)
		{
			if (!(category instanceof ClassificationClassModel) && !category.getCategorizationCategory())
			{
				final Collection<List<CategoryModel>> pathsForCategory = getCategoryService().getPathsForCategory(category);
				if (pathsForCategory != null)
				{
					for (final List<CategoryModel> categoryPath : pathsForCategory)
					{
						accumulateCategoryPaths(categoryPath, allPaths);
					}
				}
			}
		}

		return allPaths;
	}

	protected void accumulateCategoryPaths(final List<CategoryModel> categoryPath, final Set<String> output)
	{
		final StringBuilder accumulator = new StringBuilder();
		for (final CategoryModel category : categoryPath)
		{
			if (category instanceof ClassificationClassModel && !category.getCategorizationCategory())
			{
				break;
			}
			accumulator.append('/').append(category.getCode());
			output.add(accumulator.toString());
		}
	}
}
