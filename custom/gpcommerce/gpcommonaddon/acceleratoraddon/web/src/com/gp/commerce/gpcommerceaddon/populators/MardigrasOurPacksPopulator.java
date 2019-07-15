/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.gpcommerceaddon.model.MardigrasOurPacksComponentModel;

import com.gp.commercegpcommerceaddon.facades.MardigrasVideoComponentData;


public class MardigrasOurPacksPopulator {
	
	public MardigrasVideoComponentData populate(MardigrasOurPacksComponentModel component, MardigrasVideoComponentData ourPacksData) 
	{
		if (null != component.getComponentHeader())
		{
			ourPacksData.setWrapperTitle(component.getComponentHeader());
		}
		return ourPacksData;
	}
}
