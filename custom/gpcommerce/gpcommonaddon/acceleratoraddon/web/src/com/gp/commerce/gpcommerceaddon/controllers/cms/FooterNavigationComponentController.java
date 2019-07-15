/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.FooterNavigationComponentModel;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.impl.DefaultFooterNavigationFacade;
/**
 * GP Footer Navigation Controller
 */

@Controller("FooterNavigationComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.FooterNavigationComponent)
public class FooterNavigationComponentController extends AbstractCMSAddOnComponentController<FooterNavigationComponentModel>
{
	private static final String GPX_SITE = "gpxpress";
	private static final String GPX_CPRT = "Copyright 1997-";

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService siteService;
	
	@Resource(name = "footerNavigationFacade")
	private DefaultFooterNavigationFacade footerNavigationFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final FooterNavigationComponentModel component)
	{
		final GPFooterNavigationComponentData footerComponent = new GPFooterNavigationComponentData();
		final List<CMSNavigationNodeModel> footerNavs = component.getNavigationNode().getChildren();
		final List<GPFooterData> columns = new ArrayList<>();
		for (final CMSNavigationNodeModel nav : footerNavs)
		{
			final GPFooterData footerData = new GPFooterData();
			final List<GPFooterLinksData> footerLinks = new ArrayList<>();
			final List<CMSNavigationNodeModel> subNavs = nav.getChildren();
			for (final CMSNavigationNodeModel nav1 : subNavs)
			{
				for (final CMSNavigationEntryModel nav2 : nav1.getEntries())
				{
					final CMSLinkComponentModel linkComponent = (CMSLinkComponentModel) nav2.getItem();
					if (linkComponent != null) {
						final GPFooterLinksData footerLink = new GPFooterLinksData();
						footerNavigationFacade.populateFooterLink(linkComponent,footerLink);
						
						footerLinks.add(footerLink);
					}
				}
			}
			footerNavigationFacade.populateTitle(nav, footerData);
			
			footerData.setLinks(footerLinks);
			columns.add(footerData);
		}
		footerComponent.setColumns(columns);
		footerNavigationFacade.populateFooterComponent(component, footerComponent);
		
		model.addAttribute("footerData", GPFunctions.convertObjectToJSON(footerComponent));
		model.addAttribute("companyText", getCopyRightYear(component.getNotice(commerceCommonI18NService.getCurrentLocale())));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final FooterNavigationComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}

	/**
	 *
	 * @param copyRightText
	 * @return the Text which contain the current year with Copyright Symbol
	 */
	protected String getCopyRightYear(final String notice)
	{
		final LocalDate currentDate = LocalDate.now();
		final int year = currentDate.getYear();
		String copyrightText = null;
		// Creating Copyright year so that it will change automatically
		if (siteService.getCurrentSite().getUid().equalsIgnoreCase(GPX_SITE))
		{
			copyrightText = GPX_CPRT + year + " " + notice;
		} else {
			copyrightText = "\u00a9" + " " + year + " " + notice;
		}

		return copyrightText;
	}

}
