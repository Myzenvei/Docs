/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.url.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.commerceservices.url.impl.DefaultCategoryModelUrlResolver;
import de.hybris.platform.site.BaseSiteService;

public class GPDefaultCategoryModelUrlResolver extends AbstractUrlResolver<CategoryModel> {

	private static final String BASE_SITE_UID_PARAMETER = "{baseSiteUid}";

	private static final String CATALOG_VERSION = "{catalogVersion}";

	private static final String CATALOG_ID = "{catalog-id}";

	private static final String CATEGORY_CODE = "{category-code}";

	private static final String CATEGORY_PATH = "{category-path}";

	private static final String BASE_SITE_UID = "{baseSite-uid}";

	private final String CACHE_KEY = DefaultCategoryModelUrlResolver.class.getName();

	private CommerceCategoryService commerceCategoryService;
	private BaseSiteService baseSiteService;
	private String pattern;

	protected CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected String getPattern()
	{
		return pattern;
	}

	@Required
	public void setPattern(final String pattern)
	{
		this.pattern = pattern;
	}

	@Override
	protected String getKey(final CategoryModel source)
	{
		return CACHE_KEY + "." + source.getPk().toString();
	}

	@Override
	protected String resolveInternal(final CategoryModel source)
	{
		// Work out values

		// Replace pattern values
		String url = getPattern();
		if (url.contains(BASE_SITE_UID))
		{
			url = url.replace(BASE_SITE_UID, urlEncode(getBaseSiteUid().toString()));
		}
		if (url.contains(CATEGORY_PATH))
		{
			final String categoryPath = buildPathString(getCategoryPath(source));
			url = url.replace(CATEGORY_PATH, categoryPath);
		}
		if (url.contains(CATEGORY_CODE))
		{
			final String categoryCode = urlEncode(source.getCode()).replaceAll("\\+", "%20");
			url = url.replace(CATEGORY_CODE, categoryCode);
		}
		if (url.contains(CATALOG_ID))
		{
			url = url.replace(CATALOG_ID, urlEncode(source.getCatalogVersion().getCatalog().getId()));
		}
		if (url.contains(CATALOG_VERSION))
		{
			url = url.replace(CATALOG_VERSION, urlEncode(source.getCatalogVersion().getVersion()));
		}

		return url;

	}

	protected CharSequence getBaseSiteUid()
	{
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		if (currentBaseSite == null)
		{
			return BASE_SITE_UID_PARAMETER;
		}
		else
		{
			return currentBaseSite.getUid();
		}
	}

	protected String buildPathString(final List<CategoryModel> path)
	{
		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); i++)
		{
			if (i != 0)
			{
				result.append('/');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> paths = getCommerceCategoryService().getPathsForCategory(category);
		// Return first - there will always be at least 1
		return paths.iterator().next();
	}

}
