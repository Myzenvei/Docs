/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPImageLinkComponentModel;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

public interface MardigrasHeaderNavigationFacade {
	
	public MardigrasMenuOptionData getSocialIconData(final GPImageLinkComponentModel socialIcon);
	
	public MardigrasMenuOptionData getSocialLinkData(final CMSLinkComponentModel socialLink);
}
