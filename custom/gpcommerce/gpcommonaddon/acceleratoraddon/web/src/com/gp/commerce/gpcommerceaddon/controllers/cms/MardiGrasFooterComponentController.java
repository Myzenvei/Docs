/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterData;
import com.gp.commerce.gpcommerceaddon.facades.GPFooterNavigationComponentData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultFooterDataFacade;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasFooterComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPFooterDataPopulator;


/**
 * Mardi Gras Footer Navigation Controller
 */

@Controller("MardiGrasFooterComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.MardiGrasFooterComponent)
public class MardiGrasFooterComponentController extends AbstractCMSAddOnComponentController<MardiGrasFooterComponentModel>
{

	@Resource(name = "gpFooterDataFacade")
	private GPDefaultFooterDataFacade gpFooterDataFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MardiGrasFooterComponentModel component)
	{
		final GPFooterNavigationComponentData footerComponent = new GPFooterNavigationComponentData();
		final List<GPFooterData> footerDataList = new ArrayList<>();
		gpFooterDataFacade.populateFooterData(component, footerDataList);
		footerComponent.setColumns(footerDataList);
		if (component.getFooterLogo() != null && component.getFooterLogo().getMedia() != null)
		{
			footerComponent.setFooterLogo(component.getFooterLogo().getMedia().getURL());
			footerComponent.setFooterLogoAltText(component.getFooterLogo().getMedia().getAltText());
			footerComponent.setFooterLogoUrl(component.getFooterLogo().getUrl());
		}
		model.addAttribute("footerData", GPFunctions.convertObjectToJSON(footerComponent));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final MardiGrasFooterComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}


}
