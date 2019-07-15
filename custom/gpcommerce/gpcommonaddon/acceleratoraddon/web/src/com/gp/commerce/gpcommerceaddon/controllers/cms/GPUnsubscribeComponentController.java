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

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultJspIncludePropertiesFacade;
import com.gp.commerce.gpcommerceaddon.model.GPUnsubscribeComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPJspIncludePropertiesDataPopulator;


/**
 * Live Chat Component Controller
 */

@Controller("GPUnsubscribeComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPUnsubscribeComponent)
public class GPUnsubscribeComponentController extends AbstractCMSAddOnComponentController<GPUnsubscribeComponentModel>
{

	@Resource(name = "gpJspIncludePropertiesFacade")
	private GPDefaultJspIncludePropertiesFacade gpJspIncludePropertiesFacade ;


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPUnsubscribeComponentModel component)
	{
		final String jspProps = gpJspIncludePropertiesFacade.populate(component.getUid());
		model.addAttribute("propertiesData", jspProps);
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPUnsubscribeComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}


}
