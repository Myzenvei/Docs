/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.GPLiveChatComponentData;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultImageAndTextDataFacade;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultJspIncludePropertiesFacade;
import com.gp.commerce.gpcommerceaddon.model.GPLiveChatComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageAndTextDataPopulator;
import com.gp.commerce.gpcommerceaddon.populators.GPJspIncludePropertiesDataPopulator;


/**
 * Live Chat Component Controller
 */

@Controller("GPLiveChatComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPLiveChatComponent)
public class GPLiveChatComponentController extends AbstractCMSAddOnComponentController<GPLiveChatComponentModel>
{

	@Resource(name = "gpImageAndTextDataFacade")
	private GPDefaultImageAndTextDataFacade gpImageAndTextDataFacade;

	@Resource(name = "gpJspIncludePropertiesFacade")
	private GPDefaultJspIncludePropertiesFacade gpJspIncludePropertiesFacade;


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPLiveChatComponentModel component)
	{
		final GPLiveChatComponentData liveChatData = new GPLiveChatComponentData();
		final GPImageAndTextData imageAndTextData = new GPImageAndTextData();
		gpImageAndTextDataFacade.populateImageAndTextData(component, imageAndTextData);
		liveChatData.setImageText(imageAndTextData);
		liveChatData.setJspIncludeProperties(gpJspIncludePropertiesFacade.populate(component.getLiveChat().getUid()));
		model.addAttribute("propertiesData", liveChatData.getJspIncludeProperties());
		model.addAttribute("componentTheme", component.getComponentTheme());
		model.addAttribute("showContactUs", component.getShowContactUs());
		model.addAttribute("imageTextData", GPFunctions.convertToJSON(liveChatData.getImageText()));
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPLiveChatComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}


}
