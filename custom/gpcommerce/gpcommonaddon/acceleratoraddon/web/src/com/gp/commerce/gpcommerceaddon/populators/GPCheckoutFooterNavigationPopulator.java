/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.model.GPCheckoutFooterNavigationComponentModel;

import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

/**
 * @author minal
 *
 */
public class GPCheckoutFooterNavigationPopulator implements Populator<CMSLinkComponentModel, GPFooterLinksData>{
	
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	public void populate(CMSLinkComponentModel linkComponent, GPFooterLinksData footerLink) {
		
		footerLink.setLinkText(linkComponent.getLinkName(commerceCommonI18NService.getCurrentLocale()));
		footerLink.setLinkTo(linkComponent.getUrl());
		footerLink.setExternal(linkComponent.isExternal());
		if (linkComponent.getTarget() != null)
		{
			footerLink.setNewWindow(
					linkComponent.getTarget().compareTo(LinkTargets.NEWWINDOW) == 0 ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	public void populate(CMSNavigationNodeModel nav, GPFooterData footerData) {
		footerData.setTitle(nav.getTitle(commerceCommonI18NService.getCurrentLocale()));
		
	}

	public void populate(GPCheckoutFooterNavigationComponentModel component,
			GPFooterNavigationComponentData footerComponent) {
		if (component.getFooterLogo() != null && component.getFooterLogo().getMedia() != null)
		{
			footerComponent.setFooterLogo(component.getFooterLogo().getMedia().getURL());
			footerComponent.setFooterLogoAltText(component.getFooterLogo().getMedia().getAltText());
			footerComponent.setFooterLogoUrl(component.getFooterLogo().getUrl());
		}
		
	}

}
