/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;

import org.springframework.util.Assert;

import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;


/**
 * Populator class to set the email id submitted by the user
 * in the ProductInterestModel from ProductInterestData
 * @author megverma
 *
 */
public class GPProductInterestReversePopulator implements Populator<ProductInterestData, ProductInterestModel>
{
	@Override
	public void populate(final ProductInterestData source, final ProductInterestModel target)
	{
		Assert.notNull(source, GpcommerceFacadesConstants.SOURCE_VALIDATION_ERROR);
		Assert.notNull(target, GpcommerceFacadesConstants.TARGET_VALIDATION_ERROR);

		target.setNotifyMeEmailId(source.getEmailAddress());

	}
}
