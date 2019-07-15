/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPProductSolutionCMSComponentModel;
import com.gp.commerce.core.model.GPTabbedImageTileComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GpProductSolutionComponentData;
import com.gp.commerce.gpcommerceaddon.facades.GPProductSolutionFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPProductSolutionPopulator;

public class DefaultGPProductSolutionFacade implements GPProductSolutionFacade {

	@Resource(name ="gpProductSolutionPopulator")
	private GPProductSolutionPopulator gpProductSolutionPopulator;

	@Override
	public GpProductSolutionComponentData populateProductSolution(GPProductSolutionCMSComponentModel component,
			GpProductSolutionComponentData gpProductSolutionComponentData) {
		
		return gpProductSolutionPopulator.populateProductSolution(component, gpProductSolutionComponentData);
	}
	
	@Override
	public void populateTabbedImageTile(GPTabbedImageTileComponentModel gpTabbedImageTileComponentModel,
			GPImagetileComponentdata tab) {
		
		gpProductSolutionPopulator.populateTabbedImageTile(gpTabbedImageTileComponentModel,tab);
	}
}
