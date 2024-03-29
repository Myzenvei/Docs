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
package com.gp.commerce.core.sitemap.generator.impl;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.converters.Converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Site Map Generator for CategoryLanding Type
 */
public class GPCategoryLandingPageSiteMapGenerator extends GPAbstractSiteMapGenerator<CategoryModel>
{
	@Override
	public List<SiteMapUrlData> getSiteMapUrlData(final List<CategoryModel> models)
	{
		return Converters.convertAll(models, getSiteMapUrlDataConverter());
	}

	@Override
	protected List<CategoryModel> getDataInternal(final CMSSiteModel siteModel)
	{
		final String query = "SELECT {c.pk} FROM {Category AS c JOIN CatalogVersion AS cv ON {c.catalogVersion}={cv.pk} "
				+ " JOIN Catalog AS cat ON {cv.pk}={cat.activeCatalogVersion} "
				+ " JOIN CMSSite AS site ON {cat.pk}={site.defaultCatalog}}  WHERE {site.pk} = ?site "
				+ " AND exists ({{select {cr.pk} from {CategoriesForRestriction as cr} where {cr.target} = {c.pk} }})";

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", siteModel);
		return doSearch(query, params, CategoryModel.class);
	}
}
