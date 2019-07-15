/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dao;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.List;

import com.gp.commerce.core.model.StoreProductModel;


/**
 * StoreProduct DAO
 */
public interface GPStoreProductDao
{
	/**
	 * Method to get list of StoreProduct
	 *
	 * @return list of StoreProduct model
	 */
	List<StoreProductModel> getStoreProducts();

	Collection<PointOfServiceModel> getAllGeocodedPOS(GPS referenceGps, double radiusKm, BaseStoreModel baseStore,
			List<String> productCodes);

}
