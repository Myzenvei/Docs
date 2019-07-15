/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPCheckoutFooterNavigationFacade;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.model.GPCheckoutFooterNavigationComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPCheckoutFooterNavigationPopulator;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;

/**
 * @author minal
 *
 */
public class GPDefaultCheckoutFooterNavigationFacade implements GPCheckoutFooterNavigationFacade{
	
	@Resource(name = "gpCheckoutFooterNavigationPopulator")
	private GPCheckoutFooterNavigationPopulator gpCheckoutFooterNavigationPopulator;

	public void populateFooterLinksData(CMSLinkComponentModel linkComponent, GPFooterLinksData footerLink) {
		
		gpCheckoutFooterNavigationPopulator.populate(linkComponent, footerLink);
	}

	public void populateFooterData(CMSNavigationNodeModel nav, GPFooterData footerData) {
		gpCheckoutFooterNavigationPopulator.populate(nav, footerData);
		
	}

	public void populateFooterComponent(GPCheckoutFooterNavigationComponentModel component,
			GPFooterNavigationComponentData footerComponent) {
		gpCheckoutFooterNavigationPopulator.populate(component, footerComponent);
		
	}

}
