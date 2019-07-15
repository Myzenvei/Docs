/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.helpers;

import de.hybris.platform.basecommerce.enums.DistanceUnit;
import de.hybris.platform.commerceservices.storefinder.helpers.impl.DefaultDistanceHelper;
import de.hybris.platform.store.BaseStoreModel;

import java.text.DecimalFormat;


/**
 * GP distace helper inorder change the decimal format into 2
 */
public class GPDefaultDistanceHelper extends DefaultDistanceHelper
{
	@Override
	public String getDistanceStringForStore(final BaseStoreModel baseStoreModel, final double distanceInKm)
	{
		String distanceString = "";
		DistanceUnit distanceUnit = DistanceUnit.MILES;
		if (baseStoreModel.getStorelocatorDistanceUnit() != null)
		{
			distanceUnit = baseStoreModel.getStorelocatorDistanceUnit();
		}
		final DecimalFormat distanceFormat = new DecimalFormat("###,###.##");
		distanceString = distanceFormat.format(getDistance(distanceUnit, distanceInKm).doubleValue()) + " "
				+ getDistanceUnit(distanceUnit);
		return distanceString;
	}
}
