/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.gpcommerceaddon.model.GPIndustrialSolutionsComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPIndustrialSolutionsPopulator;

/**
 * @author minal
 *
 */
public class GPDefaultIndustrialSolutionsFacade {
	
	@Resource(name ="gpIndustrialSolutionsPopulator")
	private GPIndustrialSolutionsPopulator gpIndustrialSolutionsPopulator;

	public void populateImageTileData(GPIndustrialSolutionsComponentModel component,
			GPImagetileComponentdata gpImagetileComponentdata) {
		gpIndustrialSolutionsPopulator.populate(component,gpImagetileComponentdata);
		
	}

}
