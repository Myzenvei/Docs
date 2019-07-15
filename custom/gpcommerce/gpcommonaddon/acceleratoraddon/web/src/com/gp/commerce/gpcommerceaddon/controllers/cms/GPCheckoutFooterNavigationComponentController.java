/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultCheckoutFooterNavigationFacade;
import com.gp.commerce.gpcommerceaddon.model.GPCheckoutFooterNavigationComponentModel;

/**
 * GP Checkout Footer Navigation Controller
 */

@Controller("GPCheckoutFooterNavigationComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPCheckoutFooterNavigationComponent)
public class GPCheckoutFooterNavigationComponentController extends AbstractCMSAddOnComponentController<GPCheckoutFooterNavigationComponentModel>
{
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Resource(name = "gpCheckoutFooterNavigationFacade")
	private GPDefaultCheckoutFooterNavigationFacade gpCheckoutFooterNavigationFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPCheckoutFooterNavigationComponentModel component)
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
						gpCheckoutFooterNavigationFacade.populateFooterLinksData(linkComponent, footerLink);
						
						footerLinks.add(footerLink);
					}
				}
			}
			gpCheckoutFooterNavigationFacade.populateFooterData(nav,footerData);
			footerData.setLinks(footerLinks);
			columns.add(footerData);
		}
		
		footerComponent.setColumns(columns);
		gpCheckoutFooterNavigationFacade.populateFooterComponent(component, footerComponent);
		
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
	protected String getAddonUiExtensionName(final GPCheckoutFooterNavigationComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}

	/**
	 * 
	 * @param copyRightText
	 * @return the Text which contain the current year with Copyright Symbol
	 */
	protected String getCopyRightYear(String notice)
	{
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear(); 		
		// Creating Copyright year so that it will change automatically
		String copyrightText = "\u00a9"+ " "+year+" "+notice;
		return copyrightText;
	}
}
