/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.services.impl;

import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commerceservices.storefinder.impl.DefaultStoreFinderService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.dao.GPStoreProductDao;
import com.gp.commerce.core.model.StoreProductModel;
import com.gp.commerce.core.services.GPStoreProductService;


/**
 * StoreProduct service implementation
 */
public class GPDefaultStoreProductService extends DefaultStoreFinderService implements GPStoreProductService
{
	private static final Logger LOG = Logger.getLogger(GPDefaultStoreProductService.class);

	@Resource(name = "storeProductDao")
	private GPStoreProductDao storeProductDao;

	@Resource(name = "googleMapsGeoServiceWrapper")
	private GeoWebServiceWrapper geoWebServiceWrapper;

	@Override
	public List<StoreProductModel> getStoreProducts()
	{
		return storeProductDao.getStoreProducts();
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceDistanceData> locationSearch(final BaseStoreModel currentBaseStore,
			final String locationText, final PageableData pageableData, final double maxRadius, final List<String> productCodes)
	{

		final GeoPoint geoPoint = new GeoPoint();

		if (locationText != null && !locationText.isEmpty())
		{
			try
			{
				// Resolve the address to a point
				final GPS resolvedPoint = geoWebServiceWrapper
						.geocodeAddress(generateGeoAddressForSearchQuery(currentBaseStore, locationText));

				geoPoint.setLatitude(resolvedPoint.getDecimalLatitude());
				geoPoint.setLongitude(resolvedPoint.getDecimalLongitude());

				return doSearch(currentBaseStore, locationText, geoPoint, pageableData, Double.valueOf(maxRadius), productCodes);
			}
			catch (final GeoServiceWrapperException ex)
			{
				LOG.info("Failed to resolve location for [" + locationText + "]", ex);
			}
		}

		// Return no results
		return createSearchResult(locationText, geoPoint);

	}
    
	@Override
	public StoreFinderSearchPageData<PointOfServiceDistanceData> positionSearch(final BaseStoreModel currentBaseStore, final GeoPoint geoPoint,
			final PageableData pageableData, final double radiusToSearch, final List<String> productCodes)
	{
		return doSearch(currentBaseStore, null, geoPoint, pageableData, Double.valueOf(radiusToSearch), productCodes);
	}

	protected StoreFinderSearchPageData doSearch(final BaseStoreModel baseStore, final String locationText,
			final GeoPoint centerPoint, final PageableData pageableData, final Double maxRadiusKm, final List<String> productCodes)
	{
		final Collection<PointOfServiceModel> posResults;


		final int resultRangeStart = pageableData.getCurrentPage() * pageableData.getPageSize();
		final int resultRangeEnd = (pageableData.getCurrentPage() + 1) * pageableData.getPageSize();

		if (maxRadiusKm != null)
		{
			posResults = getPointsOfServiceNear(centerPoint, maxRadiusKm.doubleValue(), baseStore, productCodes);
		}
		else
		{
			final Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("baseStore", baseStore);
			paramMap.put("type", PointOfServiceTypeEnum.STORE);
			posResults = getPointOfServiceGenericDao().find(paramMap);
		}

		if (posResults != null)
		{
			// Sort all the POS
			final List orderedResults = calculateDistances(centerPoint, posResults, maxRadiusKm);
			final PaginationData paginationData = createPagination(pageableData, orderedResults.size());
			// Slice the required range window out of the results
			final List orderedResultsWindow = orderedResults.subList(Math.min(orderedResults.size(), resultRangeStart),
					Math.min(orderedResults.size(), resultRangeEnd));

			return createSearchResult(locationText, centerPoint, orderedResultsWindow, paginationData);
		}

		// Return no results
		return createSearchResult(locationText, centerPoint);
	}

	protected Collection<PointOfServiceModel> getPointsOfServiceNear(final GeoPoint centerPoint, final double radiusKm,
			final BaseStoreModel baseStore, final List<String> productCodes) throws PointOfServiceDaoException
	{
		final GPS referenceGps = new DefaultGPS(centerPoint.getLatitude(), centerPoint.getLongitude());
		return storeProductDao.getAllGeocodedPOS(referenceGps, radiusKm, baseStore, productCodes);
	}

	protected List<PointOfServiceDistanceData> calculateDistances(final GeoPoint centerPoint,
			final Collection<PointOfServiceModel> pointsOfService, final Double maxRadiusKm)
	{
		final List<PointOfServiceDistanceData> result = new ArrayList<>();

		for (final PointOfServiceModel pointOfService : pointsOfService)
		{
			final PointOfServiceDistanceData storeFinderResultData = createStoreFinderResultData();
			final Double distance = calculateDistance(centerPoint, pointOfService);
			if (distance.compareTo(maxRadiusKm) <= 0)
			{
				storeFinderResultData.setPointOfService(pointOfService);
				storeFinderResultData.setDistanceKm(distance);
				result.add(storeFinderResultData);
			}
		}

		Collections.sort(result, StoreFinderResultDataComparator.INSTANCE);
		return result;
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceDistanceData> createSearchResult(String locationText, GeoPoint geoPoint) {
		return createSearchResult(locationText, geoPoint, Collections.emptyList(), createPaginationData());
	}

}
