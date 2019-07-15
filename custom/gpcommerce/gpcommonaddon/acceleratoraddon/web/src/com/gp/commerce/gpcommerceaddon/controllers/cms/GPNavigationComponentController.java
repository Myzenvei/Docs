/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
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

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.facades.GPNavLinksData;
import com.gp.commerce.gpcommerceaddon.facades.GPNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultNavigationFacade;
import com.gp.commerce.gpcommerceaddon.facades.GPNavData;
import com.gp.commerce.gpcommerceaddon.model.GPNavigationComponentModel;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;


/**
 * GP Navigation Controller
 */

@Controller("GPNavigationComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPNavigationComponent)
public class GPNavigationComponentController extends AbstractCMSAddOnComponentController<GPNavigationComponentModel>
{
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name ="gpNavigationFacade")
	private GPDefaultNavigationFacade gpNavigationFacade;
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPNavigationComponentModel component)
	{
		final GPNavigationComponentData navComponent = new GPNavigationComponentData();
		final List<CMSNavigationNodeModel> navs = component.getNavigationNode().getChildren();
		final List<GPNavData> columns = new ArrayList<>();
		for (final CMSNavigationNodeModel nav : navs)
		{
			final GPNavData navData = new GPNavData();
			final List<GPNavLinksData> navLinks = new ArrayList<>();
			final List<CMSNavigationNodeModel> subNavs = nav.getChildren();
			for (final CMSNavigationNodeModel nav1 : subNavs)
			{
				for (final CMSNavigationEntryModel nav2 : nav1.getEntries())
				{
					final CMSLinkComponentModel linkComponent = (CMSLinkComponentModel) nav2.getItem();
					if (linkComponent != null) {
						final GPNavLinksData navLink = new GPNavLinksData();
						gpNavigationFacade.populateNavLinksData(linkComponent,navLink);
						
						navLinks.add(navLink);
					}
				}
			}
			gpNavigationFacade.populateNavData(nav,navData);
			
			navData.setLinks(navLinks);
			columns.add(navData);
		}
		navComponent.setColumns(columns);
		model.addAttribute("navigationData", GPFunctions.convertObjectToJSON(navComponent.getColumns()));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPNavigationComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}


}
