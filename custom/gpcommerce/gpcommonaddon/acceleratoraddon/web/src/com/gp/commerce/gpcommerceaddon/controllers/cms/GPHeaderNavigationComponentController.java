/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPHeaderNavigationComponentModel;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.user.GPUserFacade;
import com.gp.commerce.gpcommerceaddon.facades.GPHeaderNavigationComponentData;


/**
 * Controller for  GPHeaderNavigationComponent
 */
@Controller("GPHeaderNavigationComponentController")
@RequestMapping(value = "/view/" + GPHeaderNavigationComponentModel._TYPECODE + "Controller")
public class GPHeaderNavigationComponentController extends AbstractCMSAddOnComponentController<GPHeaderNavigationComponentModel>
{
	@Resource(name = "gpUserFacade")
	private GPUserFacade gpUserFacade;


	/**
	 * Gets the complete json of header navigation component.
	 *
	 * @param component
	 *           for component
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPHeaderNavigationComponentModel component)
	{
		final GPHeaderNavigationComponentData gpHeaderNavigation = gpUserFacade.getHeaderData(request, component, model);
		model.addAttribute("headerSection", GPFunctions.convertToJSON(gpHeaderNavigation));

	}


}
