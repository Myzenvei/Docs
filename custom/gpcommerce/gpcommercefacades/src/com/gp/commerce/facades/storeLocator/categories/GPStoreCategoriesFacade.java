/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.storeLocator.categories;

import com.gp.commerce.facades.store.data.GPStoreCategoriesListData;

/**
 * Interface for Store Categories
 */
public interface GPStoreCategoriesFacade
{
	/**
	 * This method gets store categories
	 * @return store category list data
	 */
	GPStoreCategoriesListData getStoreCategories();
}
