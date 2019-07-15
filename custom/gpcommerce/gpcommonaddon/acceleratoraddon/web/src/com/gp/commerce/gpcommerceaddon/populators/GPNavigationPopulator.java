/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPNavData;
import com.gp.commerce.gpcommerceaddon.facades.GPNavLinksData;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

public class GPNavigationPopulator implements Populator<CMSLinkComponentModel, GPNavLinksData>{

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	public void populate(CMSLinkComponentModel linkComponent, GPNavLinksData navLink) 
	{
		navLink.setLinkText(linkComponent.getLinkName(commerceCommonI18NService.getCurrentLocale()));
		navLink.setLinkTo(linkComponent.getUrl());
	}

	public void populate(CMSNavigationNodeModel nav, GPNavData navData) {
		navData.setTitle(nav.getTitle(commerceCommonI18NService.getCurrentLocale()));
		
	}
}
