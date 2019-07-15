/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.gpcommerceaddon.model.GPCheckoutFooterNavigationComponentModel;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;

public interface GPCheckoutFooterNavigationFacade {
	public void populateFooterLinksData(CMSLinkComponentModel linkComponent, GPFooterLinksData footerLink);
	public void populateFooterData(CMSNavigationNodeModel nav, GPFooterData footerData);
	public void populateFooterComponent(GPCheckoutFooterNavigationComponentModel component,
			GPFooterNavigationComponentData footerComponent) ;
}
