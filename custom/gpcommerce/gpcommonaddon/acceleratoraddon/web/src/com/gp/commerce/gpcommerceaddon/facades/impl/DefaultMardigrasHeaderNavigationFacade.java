/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasHeaderNavigationFacade;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasMenuOptionData;
import com.gp.commerce.gpcommerceaddon.populators.MardigrasHeaderNavigationPopulator;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

public class DefaultMardigrasHeaderNavigationFacade implements MardigrasHeaderNavigationFacade
{
	@Resource(name = "mardigrasHeaderNavigationPopulator")
	private MardigrasHeaderNavigationPopulator mardigrasHeaderNavigationPopulator;
	
	@Override
	public MardigrasMenuOptionData getSocialIconData(GPImageLinkComponentModel socialIcon) {
		
		return mardigrasHeaderNavigationPopulator.getSocialIconData(socialIcon);
	}

	@Override
	public MardigrasMenuOptionData getSocialLinkData(CMSLinkComponentModel socialLink) {
		
		return mardigrasHeaderNavigationPopulator.getSocialLinkData(socialLink);
	}

}
