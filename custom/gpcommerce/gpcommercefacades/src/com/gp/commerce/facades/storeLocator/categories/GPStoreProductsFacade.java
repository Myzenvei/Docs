/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.facades.storeLocator.categories;


import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;

import java.util.List;

import com.gp.commerce.facades.store.data.GPStoreProductsData;


/**
 * Store Product Facade.
 */
public interface GPStoreProductsFacade
{

	/**
	 * Method to get list of StoreProducts for a site.
	 *
	 * @return List<GPStoreProductsData>
	 */
	GPStoreProductsData getStoreProducts();

	/**
	 * Store Location Search.
	 *
	 * @param locationText the location text
	 * @param pageableData the pageable data
	 * @param maxRadius the max radius
	 * @param productCode the product code
	 * @param wishlistUid the wishlist uid
	 * @return StoreFinderSearchPageData
	 */
	StoreFinderSearchPageData<PointOfServiceData> locationSearch(String locationText, PageableData pageableData, double maxRadius,
			final String productCode, final String wishlistUid);

	/**
	 * Position search.
	 *
	 * @param geoPoint the geo point
	 * @param pageableData the pageable data
	 * @param radiusToSearch the radius to search
	 * @param productCode the product code
	 * @param wishlistUid the wishlist uid
	 * @return the store finder search page data
	 */
	StoreFinderSearchPageData<PointOfServiceData> positionSearch(GeoPoint geoPoint, PageableData pageableData,
			double radiusToSearch, String productCode, final String wishlistUid);

	/**
	 * Gets the product code by wishlist uid.
	 *
	 * @param wishlistUid the wishlist uid
	 * @return the product code by wishlist uid
	 */
	List<String> getProductCodeByWishlistUid(String wishlistUid);
}
