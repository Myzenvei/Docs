/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.FooterNavigationFacade;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.populators.FooterNavigationPopulator;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;

/**
 * @author minal
 *
 */
public class DefaultFooterNavigationFacade implements FooterNavigationFacade {

	@Resource(name = "footerNavigationPopulator")
	private FooterNavigationPopulator footerNavigationPopulator;

	public void populateFooterLink(CMSLinkComponentModel linkComponent, GPFooterLinksData footerLink) {
		footerNavigationPopulator.populate(linkComponent,footerLink);
		
	}

	public void populateFooterComponent(FooterNavigationComponentModel component,
			GPFooterNavigationComponentData footerComponent) {
		footerNavigationPopulator.populate(component, footerComponent);
		
	}

	public void populateTitle(CMSNavigationNodeModel nav, GPFooterData footerData) {
		footerNavigationPopulator.populate(nav, footerData);
		
	}
}
