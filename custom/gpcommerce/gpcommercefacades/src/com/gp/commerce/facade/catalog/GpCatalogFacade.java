/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.catalog;

import java.util.Set;

import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;

/**
 * The Interface GpCatalogFacade.
 */
public interface GpCatalogFacade extends CatalogFacade{

	/**
	 * Gets the category by id.
	 *
	 * @param catalogId the catalog id
	 * @param catalogVersionId the catalog version id
	 * @param categoryId the category id
	 * @param page the page
	 * @param opts the opts
	 * @return the category by id
	 */
	CategoryHierarchyData getCategoryById(String catalogId, String catalogVersionId, String categoryId, PageOption page,
			Set<CatalogOption> opts);
	
	void sortCategoriesByOrder(CategoryHierarchyData categoryHierarchyData);
}
