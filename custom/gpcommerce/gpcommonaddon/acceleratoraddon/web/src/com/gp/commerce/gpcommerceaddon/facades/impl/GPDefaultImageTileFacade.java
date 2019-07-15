/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import com.gp.commerce.gpcommerceaddon.facades.GPImageTileFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTilePopulator;
import com.gp.commerce.core.model.GPImageTileComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;

public class GPDefaultImageTileFacade implements GPImageTileFacade{
	
	private static final Logger LOG = Logger.getLogger(GPDefaultImageTileFacade.class);
	
	@Resource(name = "imageTileDataPopulator")
	private GPImageTilePopulator imageTileDataPopulator;
	
	@Override
	public void populateImageTile(final GPImageTileComponentModel component, final GPImagetileComponentdata gpImagetileComponentdata)
	{
		imageTileDataPopulator.populate(component, gpImagetileComponentdata);
		
	}
	

}
