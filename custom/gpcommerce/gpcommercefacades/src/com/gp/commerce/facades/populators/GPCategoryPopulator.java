/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.springframework.util.StringUtils;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Populator;

public class GPCategoryPopulator implements Populator<CategoryModel, CategoryData> {

	
	@Override
	public void populate(final CategoryModel source, final CategoryData target)
	{
		if(!StringUtils.isEmpty(source.getDescription())){
			
			target.setDescription(source.getDescription());
			
		}
		
		if(!StringUtils.isEmpty(source.getItemtype())) {
			
			target.setType(source.getItemtype());
			
		}
	}
	

}
