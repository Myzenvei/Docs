/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.gp.commerce.core.model.StoreProductModel;
import com.gp.commerce.facades.store.data.GPStoreProductData;


/**
 * Populate Store Product data
 */
public class GPStoreProductsPopulator implements Populator<StoreProductModel, GPStoreProductData>
{

	@Override
	public void populate(final StoreProductModel source, final GPStoreProductData target) throws ConversionException
	{
		target.setCode(source.getCode());
		target.setDescription(source.getDescription());
	}
}
