/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPXpressHeaderNavigationFacade;
import com.gp.commerce.gpcommerceaddon.facades.GpXpressHeaderMenuOptionData;
import com.gp.commerce.gpcommerceaddon.populators.GPXpressHeaderNavigationPopulator;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

public class DefaultGPXpressHeaderNavigationFacade implements GPXpressHeaderNavigationFacade{

	@Resource(name ="gpXpressHeaderNavigationPopulator")
	private GPXpressHeaderNavigationPopulator gpXpressHeaderNavigationPopulator;

	@Override
	public GpXpressHeaderMenuOptionData getMyListsData(CMSLinkComponentModel myList) {
		
		return gpXpressHeaderNavigationPopulator.getMyListsData(myList);
	}
}
