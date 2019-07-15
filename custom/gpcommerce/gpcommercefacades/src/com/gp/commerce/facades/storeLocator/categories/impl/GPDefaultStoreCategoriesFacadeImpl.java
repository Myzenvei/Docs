/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.storeLocator.categories.impl;

import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gp.commerce.core.model.ProductCategoriesModel;
import com.gp.commerce.facades.populators.GPStoreLocatorCategoriesPopulator;
import com.gp.commerce.facades.store.data.GPStoreCategoriesData;
import com.gp.commerce.facades.store.data.GPStoreCategoriesListData;
import com.gp.commerce.facades.storeLocator.categories.GPStoreCategoriesFacade;

public class GPDefaultStoreCategoriesFacadeImpl implements GPStoreCategoriesFacade
{
	private GPStoreLocatorCategoriesPopulator defaultGpStoreLocatorCategoriesPopulator;

	private BaseSiteService baseSiteService;
	


	@Override
	public  GPStoreCategoriesListData getStoreCategories() {
		
		Collection<ProductCategoriesModel> categories = baseSiteService.getCurrentBaseSite().getCategories();
		 
		GPStoreCategoriesListData categoryData = new GPStoreCategoriesListData();
		List<GPStoreCategoriesData> storeDataList = new ArrayList<>();
		
		for(ProductCategoriesModel category :categories)
		{
			GPStoreCategoriesData storeData = new GPStoreCategoriesData();
			getDefaultGpStoreLocatorCategoriesPopulator().populate(category, storeData);
			storeDataList.add(storeData);
		}
		categoryData.setGpCategoriesList(storeDataList);
		return categoryData;
	}

	public GPStoreLocatorCategoriesPopulator getDefaultGpStoreLocatorCategoriesPopulator()
	{
		return defaultGpStoreLocatorCategoriesPopulator;
	}

	public void setDefaultGpStoreLocatorCategoriesPopulator(
			GPStoreLocatorCategoriesPopulator defaultGpStoreLocatorCategoriesPopulator)
	{
		this.defaultGpStoreLocatorCategoriesPopulator = defaultGpStoreLocatorCategoriesPopulator;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}
	
}
