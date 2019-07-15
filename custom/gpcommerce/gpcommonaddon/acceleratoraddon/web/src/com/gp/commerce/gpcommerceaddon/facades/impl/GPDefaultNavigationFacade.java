/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPNavData;
import com.gp.commerce.gpcommerceaddon.facades.GPNavLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPNavigationFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPNavigationPopulator;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;

public class GPDefaultNavigationFacade implements GPNavigationFacade{
	
	@Resource(name = "gpNavigationPopulator")
	private GPNavigationPopulator gpNavigationPopulator;

	public void populateNavLinksData(CMSLinkComponentModel linkComponent, GPNavLinksData navLink) {
		gpNavigationPopulator.populate(linkComponent, navLink);
		
	}

	public void populateNavData(CMSNavigationNodeModel nav, GPNavData navData) {
		gpNavigationPopulator.populate(nav, navData);
		
	}
	
	
}
