/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.gpcommerceaddon.model.GPIndustrialSolutionsComponentModel;

/**
 * @author minal
 *
 */
public interface GPIndustrialSolutionsFacade {
	public void populateImageTileData(GPIndustrialSolutionsComponentModel component,
			GPImagetileComponentdata gpImagetileComponentdata);

}
