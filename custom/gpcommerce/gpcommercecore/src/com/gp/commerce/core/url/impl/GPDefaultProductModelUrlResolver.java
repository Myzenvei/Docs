/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.url.impl;

import com.gp.commerce.core.model.GPDefaultVariantCategoryModel;
import com.gp.commerce.core.model.GPDefaultVariantValueCategoryModel;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;


/**
 * URL resolver for ProductModel instances. The pattern could be of the form:
 * /{category-path}/{product-name}/p/{product-code}
 */
public class GPDefaultProductModelUrlResolver extends DefaultProductModelUrlResolver
{

	private static final String ENCODED_SLASH = "%2F";
	private static final String PRODUCT_CODE = "{product-code}";
	private static final String PRODUCT_NAME = "{product-name}";
	private static final String CATEGORY_PATH = "{category-path}";
	private static final String BASE_SITE_UID = "{baseSite-uid}";

	@Override
	protected String resolveInternal(final ProductModel source)
	{
		final ProductModel baseProduct = getBaseProduct(source);

		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		String url = getPattern();
		
		if (currentBaseSite != null && url.contains(BASE_SITE_UID))
		{
			url = url.replace(BASE_SITE_UID, urlEncode(currentBaseSite.getUid()));
		}
		if (url.contains(CATEGORY_PATH))
		{
			url = url.replace(CATEGORY_PATH, buildPathString(getCategoryPath(baseProduct)));
		}

		if (url.contains(PRODUCT_NAME))
		{
			url = url.replace(PRODUCT_NAME, urlSafe(baseProduct.getName()));
		}
		if (url.contains(PRODUCT_CODE))
		{
			url = url.replace(PRODUCT_CODE, urlEncode(source.getCode())).replaceAll(ENCODED_SLASH, "/");
		}

		return url;
	}
	
	@Override
	protected CategoryModel getPrimaryCategoryForProduct(final ProductModel product)
	{
		// Get the first super-category from the product that isn't a classification category
		for (final CategoryModel category : product.getSupercategories())
		{
			if (!(category instanceof ClassificationClassModel|| category instanceof VariantCategoryModel 
					|| category instanceof GPDefaultVariantCategoryModel
					|| category instanceof VariantValueCategoryModel
					|| category instanceof GPDefaultVariantValueCategoryModel))
			{
				return category;
			}
		}
		return null;
	}

}
