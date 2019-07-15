/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;

public interface FooterNavigationFacade {
	public void populateFooterLink(CMSLinkComponentModel linkComponent, GPFooterLinksData footerLink);
	public void populateFooterComponent(FooterNavigationComponentModel component,
			GPFooterNavigationComponentData footerComponent);
	public void populateTitle(CMSNavigationNodeModel nav, GPFooterData footerData);

}
