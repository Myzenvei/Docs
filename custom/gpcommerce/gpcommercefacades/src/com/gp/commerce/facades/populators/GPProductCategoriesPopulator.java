/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.GPDefaultVariantCategoryModel;
import com.gp.commerce.core.model.GPDefaultVariantValueCategoryModel;
import com.gp.commerce.core.util.GPCommerceCoreUtils;


/**
 * Populate the product date with the product's categories
 */
public class GPProductCategoriesPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductCategoriesPopulator<SOURCE, TARGET>
{
	private Converter<CategoryModel, CategoryData> categoryConverter;
	private CommerceProductService commerceProductService;

	protected Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}

	@Required
	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}

	protected CommerceProductService getCommerceProductService()
	{
		return commerceProductService;
	}

	@Required
	public void setCommerceProductService(final CommerceProductService commerceProductService)
	{
		this.commerceProductService = commerceProductService;
	}

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final ProductModel baseProductModel = GPCommerceCoreUtils.getBaseProduct(productModel);
		final Collection<CategoryModel> categoryModels = getCommerceProductService()
				.getSuperCategoriesExceptClassificationClassesForProduct(baseProductModel);
		List<CategoryModel> displayCategories = GPCommerceCoreUtils.getDisplayCategories(categoryModels);
		productData.setCategories(getCategoryConverter().convertAll(displayCategories));
	}
	

}
