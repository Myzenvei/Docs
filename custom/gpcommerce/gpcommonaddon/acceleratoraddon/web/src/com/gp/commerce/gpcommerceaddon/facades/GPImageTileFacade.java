/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;


import com.gp.commerce.core.model.GPImageTileComponentModel;

import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
public interface GPImageTileFacade {
		
	public void populateImageTile(final GPImageTileComponentModel component, final GPImagetileComponentdata gpImagetileComponentdata);
	

}
