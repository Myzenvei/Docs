/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.services;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

import com.gp.commerce.core.model.StoreProductModel;


/**
 * Store Locator Product Service
 */
public interface GPStoreProductService
{
	/**
	 * Method to get list of Store Products
	 *
	 * @return list of Store Product
	 */
	List<StoreProductModel> getStoreProducts();

	/**
	 * Returns {@link StoreFinderSearchPageData} by searching the location based on
	 * the given base store, location, maxRadius and list of product codes
	 * 
	 * @param currentBaseStore the base store
	 * @param locationText     the location
	 * @param pageableData     the {@link PageableData}
	 * @param maxRadius        the max radius
	 * @param productCodes     list of product codes
	 * @return {@link StoreFinderSearchPageData}
	 */
	StoreFinderSearchPageData<PointOfServiceDistanceData> locationSearch(BaseStoreModel currentBaseStore, String locationText,
			PageableData pageableData, double maxRadius, List<String> productCodes);

	/**
	 * Returns {@link StoreFinderSearchPageData} by searching the position based on
	 * the given base store, geo point, radius to search and list of product codes
	 * 
	 * @param currentBaseStore the base store
	 * @param geoPoint         the geographical point
	 * @param pageableData     the {@link PageableData}
	 * @param radiusToSearch   the radius
	 * @param productCodes     list of product codes
	 * @return {@link StoreFinderSearchPageData}
	 */
	StoreFinderSearchPageData<PointOfServiceDistanceData> positionSearch(BaseStoreModel currentBaseStore, GeoPoint geoPoint,
			PageableData pageableData, double radiusToSearch, List<String> productCodes);
	
	/**
	 * Returns {@link StoreFinderSearchPageData} by creating a search result based
	 * on the given base store, geo point, radius to search and list of product codes
	 * 
	 * @param locationText the location
	 * @param geoPoint     the geographical point
	 * @return {@link StoreFinderSearchPageData}
	 */
	public StoreFinderSearchPageData<PointOfServiceDistanceData> createSearchResult(String locationText,GeoPoint geoPoint);
}
