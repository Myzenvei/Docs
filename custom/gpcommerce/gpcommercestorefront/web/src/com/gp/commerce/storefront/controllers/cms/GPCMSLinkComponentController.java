/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.storefront.controllers.ControllerConstants;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;

@Controller("GPCMSLinkComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.GPCMSLinkComponent)
public class GPCMSLinkComponentController extends AbstractAcceleratorCMSComponentController<CMSLinkComponentModel> 
{
	@Override
	protected void fillModel(HttpServletRequest request, Model model, CMSLinkComponentModel component)
	{
		String linkDetails = GPFunctions.convertToJSON(component);
		model.addAttribute("linkDetails", linkDetails);
	}

}
