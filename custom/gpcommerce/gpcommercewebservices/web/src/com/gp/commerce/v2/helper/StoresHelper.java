/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.helper;

import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.gp.commerce.facades.storeLocator.categories.GPStoreProductsFacade;


@Component
public class StoresHelper extends AbstractHelper
{
	private static final double EARTH_PERIMETER = 24901.0;
	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "storeProductFacade")
	private GPStoreProductsFacade storeProductFacade;

	@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'DTO',#query,#latitude,#longitude,#currentPage,#pageSize,#sort,#radius,#accuracy,#productCode,#wishlistUid,#products,#fields)")
	public StoreFinderSearchPageWsDTO locationSearch(final String query, final Double latitude, final Double longitude, //NOSONAR
			final int currentPage, final int pageSize, final String sort, final double radius, final double accuracy,
			final String productCode, final String wishlistUid, final List<String> products, final String fields)
	{
		final StoreFinderSearchPageData<PointOfServiceData> result = locationSearch(query, latitude, longitude, currentPage,
				pageSize, sort, radius, accuracy, productCode, wishlistUid, products);
		return getDataMapper().map(result, StoreFinderSearchPageWsDTO.class, fields);
	}

	@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'Data',#query,#latitude,#longitude,#currentPage,#pageSize,#sort,#radius,#accuracy,#productCode,#wishlistUid,#products)")
	public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String query, final Double latitude, //NOSONAR
			final Double longitude, final int currentPage, final int pageSize, final String sort, final double radius,
			final double accuracy, final String productCode, final String wishlistUid, final List<String> products)
	{
		if (radius > EARTH_PERIMETER)
		{
			throw new RequestParameterException("Radius cannot be greater than Earth's perimeter",
					RequestParameterException.INVALID, "radius");
		}

		final double radiusToSearch = getInKilometres(radius, accuracy);
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		StoreFinderSearchPageData<PointOfServiceData> result = null;
		if (StringUtils.isNotBlank(query))
		{
			result = storeProductFacade.locationSearch(query, pageableData, radiusToSearch, productCode, wishlistUid);
		}
		else if (latitude != null && longitude != null)
		{
			final GeoPoint geoPoint = new GeoPoint();
			geoPoint.setLatitude(latitude.doubleValue());
			geoPoint.setLongitude(longitude.doubleValue());
			result = storeProductFacade.positionSearch(geoPoint, pageableData, radiusToSearch, productCode, wishlistUid);
		}
		else
		{
			result = storeFinderFacade.getAllPointOfServices(pageableData);
		}
		return result;
	}

	@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'storeDetails',#storeId,#fields)")
	public PointOfServiceWsDTO locationDetails(final String storeId, final String fields)
	{
		final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(storeId);
		return getDataMapper().map(pointOfServiceData, PointOfServiceWsDTO.class, fields);
	}

	protected double getInKilometres(final double radius, final double accuracy)
	{
		return (radius + accuracy) * 1.609;
	}

}
