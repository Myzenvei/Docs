/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPProductSolutionCMSComponentModel;
import com.gp.commerce.core.model.GPTabbedImageTileComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GpProductSolutionComponentData;

public interface GPProductSolutionFacade {

	public GpProductSolutionComponentData populateProductSolution(GPProductSolutionCMSComponentModel component, GpProductSolutionComponentData gpProductSolutionComponentData);
	
	public void populateTabbedImageTile(GPTabbedImageTileComponentModel gpTabbedImageTileComponentModel,
			GPImagetileComponentdata tab);
}
