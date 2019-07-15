/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.b2b.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.core.model.GPCartSuggestionComponentModel;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;

@Controller("GPCartSuggestionComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.GPCartSuggestionComponent)
public class GPCartSuggestionComponentController extends AbstractCMSComponentController<GPCartSuggestionComponentModel>{

	@Override
	protected void fillModel(HttpServletRequest request, Model model, GPCartSuggestionComponentModel component) {
		model.addAttribute("title", component.getTitle());
		model.addAttribute("uid", component.getUid());
	}

	@Override
	protected String getView(GPCartSuggestionComponentModel component) {

		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(GPCartSuggestionComponentModel._TYPECODE);
	}

}
