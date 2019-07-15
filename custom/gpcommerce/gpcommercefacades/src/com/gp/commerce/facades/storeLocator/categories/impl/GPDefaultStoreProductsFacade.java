/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.facades.storeLocator.categories.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.gp.commerce.core.model.StoreProductModel;
import com.gp.commerce.core.services.GPStoreProductService;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.data.WishlistEntryData;
import com.gp.commerce.facades.populators.GPStoreProductsPopulator;
import com.gp.commerce.facades.store.data.GPStoreProductData;
import com.gp.commerce.facades.store.data.GPStoreProductsData;
import com.gp.commerce.facades.storeLocator.categories.GPStoreProductsFacade;
import com.gp.commerce.facades.wishlist.impl.GPDefaultWishlistFacade;


/**
 * Store Locator Facade Implementation
 */
public class GPDefaultStoreProductsFacade implements GPStoreProductsFacade
{
	@Resource(name = "storeProductService")
	private GPStoreProductService storeProductService;

	@Resource(name = "storeProductPopulator")
	private GPStoreProductsPopulator storeProductPopulator;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "searchPagePointOfServiceDistanceConverter")
	private Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> searchPagePointOfServiceDistanceConverter;

	@Resource(name = "wishlistFacade")
	private GPDefaultWishlistFacade wishlistFacade;

	@Override
	public GPStoreProductsData getStoreProducts()
	{
		final List<StoreProductModel> products = storeProductService.getStoreProducts();
		final GPStoreProductsData storeProducts = new GPStoreProductsData();
		final List<GPStoreProductData> productList = new ArrayList<>();
		for (final StoreProductModel product : products)
		{
			final GPStoreProductData productData = new GPStoreProductData();
			storeProductPopulator.populate(product, productData);
			productList.add(productData);
		}
		storeProducts.setGpStoreProducts(productList);
		return storeProducts;
	}

	public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String locationText, final PageableData pageableData,
			final double maxRadius, final String productCode, final String wishlistUid)
	{
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		final List<String> productCodes = new ArrayList<>();
		if (StringUtils.isNotBlank(wishlistUid))
		{
			productCodes.addAll(getProductCodeByWishlistUid(wishlistUid));
			if (productCodes.isEmpty())
			{
				// Return no results
				return searchPagePointOfServiceDistanceConverter.convert(storeProductService.createSearchResult(locationText, new GeoPoint()));
			}
		}
		else if (StringUtils.isNotBlank(productCode))
		{
			productCodes.add(productCode);
		}
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = storeProductService
				.locationSearch(currentBaseStore, locationText, pageableData, maxRadius, productCodes);
		return searchPagePointOfServiceDistanceConverter.convert(searchPageData);
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceData> positionSearch(final GeoPoint geoPoint, final PageableData pageableData,
			final double radiusToSearch, final String productCode, final String wishlistUid)
	{
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		final List<String> productCodes = new ArrayList<>();
		if (StringUtils.isNotBlank(wishlistUid))
		{
			productCodes.addAll(getProductCodeByWishlistUid(wishlistUid));
			if (productCodes.isEmpty())
			{
				// Return no results
				return searchPagePointOfServiceDistanceConverter.convert(storeProductService.createSearchResult(null, new GeoPoint()));
			}
		}
		else if (StringUtils.isNotBlank(productCode))
		{
			productCodes.add(productCode);
		}
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = storeProductService
				.positionSearch(currentBaseStore, geoPoint, pageableData, radiusToSearch, productCodes);
		return searchPagePointOfServiceDistanceConverter.convert(searchPageData);
	}

	@Override
	public List<String> getProductCodeByWishlistUid(final String wishlistUid)
	{
		final List<String> productCodes = new ArrayList<>();
		final WishlistData wishlistData = wishlistFacade.getWishlist(wishlistUid, null, null,false);
		if (null != wishlistData)
		{
			final List<WishlistEntryData> entryData = wishlistData.getWishlistEntries();
			for (final WishlistEntryData data : entryData)
			{
				if (null != data.getProduct())
				{
					productCodes.add(data.getProduct().getCode());
				}
			}
		}
		return productCodes;
	}
}
