/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import com.gp.commerce.gpcommerceaddon.facades.GpXpressHeaderMenuOptionData;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

public class GPXpressHeaderNavigationPopulator {
	public GpXpressHeaderMenuOptionData getMyListsData(final CMSLinkComponentModel myList)
	{
		final GpXpressHeaderMenuOptionData linkData = new GpXpressHeaderMenuOptionData();
		if (null != myList.getUrl())
		{
			linkData.setLinkURL(myList.getUrl());
			linkData.setLinkText(myList.getName());
		}
		return linkData;
		
	}
}
