/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

public interface GPXpressHeaderNavigationFacade {
	public GpXpressHeaderMenuOptionData getMyListsData(final CMSLinkComponentModel myList);
}
