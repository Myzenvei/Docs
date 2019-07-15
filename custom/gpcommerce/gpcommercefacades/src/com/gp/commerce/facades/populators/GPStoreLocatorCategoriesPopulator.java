/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import com.gp.commerce.facades.store.data.GPStoreCategoriesData;
import com.gp.commerce.core.model.ProductCategoriesModel;

public class GPStoreLocatorCategoriesPopulator implements Populator<ProductCategoriesModel, GPStoreCategoriesData> {

	@Override
	public void populate(ProductCategoriesModel source, GPStoreCategoriesData target) throws ConversionException {
		
		target.setGroupId(source.getGroupId());
		target.setGroupName(source.getGroupName());
		
	}
}
