/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.storefront.controllers.cms;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.storefront.controllers.ControllerConstants;
import com.gp.commerce.core.model.GPCategoryAuthoringComponentModel;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.session.SessionService;

@Controller("GPCategoryAuthoringComponentController")
@RequestMapping(value = ControllerConstants.Actions.Cms.GPCategoryAuthoringComponent)
public class GPCategoryAuthoringComponentController extends AbstractAcceleratorCMSComponentController<GPCategoryAuthoringComponentModel>{
	
	@Resource(name="sessionService")
	SessionService sessionService;

	@Override
	protected void fillModel(HttpServletRequest request, Model model, GPCategoryAuthoringComponentModel component) {
		CategoryModel category = sessionService.getAttribute("currentCategoryCode");
		
		if(category != null && category.getAuthoringContent() != null) {
		component.setAuthoringContent(category.getAuthoringContent());
		sessionService.removeAttribute("currentCategoryCode");
		}
	}


}
