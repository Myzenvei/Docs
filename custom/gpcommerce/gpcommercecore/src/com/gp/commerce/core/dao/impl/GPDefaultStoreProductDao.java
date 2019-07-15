/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.dao.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPStoreProductDao;
import com.gp.commerce.core.model.StoreProductModel;


/**
 * StoreProduct DAO implementation
 */
public class GPDefaultStoreProductDao implements GPStoreProductDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Override
	public List<StoreProductModel> getStoreProducts()
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
				"SELECT {s.pk} FROM {StoreProduct AS s JOIN CMSSITE AS c on {s:site} = {c:PK}} WHERE {c.uid}=?site");
		searchQuery.addQueryParameter(GpcommerceCoreConstants.SITE, cmsSiteService.getCurrentSite().getUid());
		final SearchResult<StoreProductModel> result = flexibleSearchService.search(searchQuery);
		if (CollectionUtils.isNotEmpty(result.getResult()))
		{
			return result.getResult();
		}
		return new ArrayList<>();
	}

	@Override
	public Collection<PointOfServiceModel> getAllGeocodedPOS(final GPS referenceGps, final double radius,
			final BaseStoreModel baseStore, final List<String> productCodes)
	{
		final FlexibleSearchQuery fQuery = buildQuery(referenceGps, radius, baseStore, productCodes);

		final SearchResult<PointOfServiceModel> result = flexibleSearchService.search(fQuery);
		return result.getResult();
	}

	protected FlexibleSearchQuery buildQuery(final GPS center, final double radius, final BaseStoreModel baseStore,
			final List<String> productCodes)
	{
		try
		{
			final List<GPS> corners = GeometryUtils.getSquareOfTolerance(center, radius);
			if ((corners == null) || (corners.isEmpty()) || (corners.size() != 2))
			{
				throw new PointOfServiceDaoException("Could not fetch locations from database. Unexpected neighborhood");
			}
			final Double latMax = Double.valueOf(corners.get(1).getDecimalLatitude());
			final Double lonMax = Double.valueOf(corners.get(1).getDecimalLongitude());
			final Double latMin = Double.valueOf(corners.get(0).getDecimalLatitude());
			final Double lonMin = Double.valueOf(corners.get(0).getDecimalLongitude());
			final StringBuilder query = new StringBuilder();

			if (!productCodes.isEmpty())
			{
				query.append("select {pos.pk} from {Product2B2BUnitRel as rel join Product as p on {p.pk}={rel.source} "
						+ "join PointOfService as pos on {pos.distributor}={rel.target}} where {p.code} in (?productCodes) AND {")
						.append("pos.latitude").append("} is not null AND {").append("pos.longitude").append("} is not null AND {")
						.append("pos.latitude").append("} >= ?latMin AND {").append("pos.latitude").append("} <= ?latMax AND {")
						.append("pos.longitude").append("} >= ?lonMin AND {").append("pos.longitude").append("} " + "<= ?lonMax");

				if (baseStore != null)
				{
					query.append(" AND {").append("pos.baseStore").append("} = ?baseStore");
				}

			}
			else {
				query.append("SELECT {PK} FROM {").append("PointOfService").append("} WHERE {").append("latitude")
						.append("} is not null AND {").append("longitude").append("} is not null AND {").append("latitude")
						.append("} >= ?latMin AND {").append("latitude").append("} <= ?latMax AND {").append("longitude")
						.append("} >= ?lonMin AND {").append("longitude").append("} <= ?lonMax");

				if (baseStore != null)
				{
					query.append(" AND {").append("baseStore").append("} = ?baseStore");
				}

			}

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			fQuery.addQueryParameter("latMax", latMax);
			fQuery.addQueryParameter("latMin", latMin);
			fQuery.addQueryParameter("lonMax", lonMax);
			fQuery.addQueryParameter("lonMin", lonMin);
			if (baseStore != null)
			{
				fQuery.addQueryParameter("baseStore", baseStore);
			}
			if (!productCodes.isEmpty())
			{
				fQuery.addQueryParameter(GpcommerceCoreConstants.PRODUCT_CODES, productCodes);
			}
			return fQuery;
		}
		catch (final GeoLocatorException e)
		{
			throw new PointOfServiceDaoException("Could not fetch locations from database, due to :" + e.getMessage(), e);
		}
	}
}

