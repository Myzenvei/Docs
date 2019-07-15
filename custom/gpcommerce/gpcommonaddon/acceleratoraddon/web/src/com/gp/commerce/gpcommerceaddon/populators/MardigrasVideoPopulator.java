/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.gpcommerceaddon.model.MardigrasVideosComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasVideoComponentData;


import de.hybris.platform.converters.Populator;

public class MardigrasVideoPopulator implements Populator<MardigrasVideosComponentModel, MardigrasVideoComponentData>{
	
	public void populate(MardigrasVideosComponentModel component, MardigrasVideoComponentData videoData) 
	{
		if (null != component.getComponentHeader())
		{
			videoData.setWrapperTitle(component.getComponentHeader());
		}
	}
}
