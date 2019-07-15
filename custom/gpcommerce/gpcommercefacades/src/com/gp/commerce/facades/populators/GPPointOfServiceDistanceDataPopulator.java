/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.commercefacades.storefinder.converters.populator.PointOfServiceDistanceDataPopulator;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.helpers.DistanceHelper;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;


/**
 * Point of service populator to use gp helper
 */
public class GPPointOfServiceDistanceDataPopulator extends PointOfServiceDistanceDataPopulator
{
	@Resource(name = "gpDefaultDistanceHelper")
	private DistanceHelper gpDefaultDistanceHelper;

	@Override
	public void populate(final PointOfServiceDistanceData source, final PointOfServiceData target) throws ConversionException
	{
		if (source != null)
		{
			final String formattedDistance = gpDefaultDistanceHelper
					.getDistanceStringForStore(source.getPointOfService().getBaseStore(), source.getDistanceKm());
			target.setFormattedDistance(formattedDistance);
		}
	}

}
