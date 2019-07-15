/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;
import com.gp.commerce.gpcommerceaddon.model.GPQuplesComponentModel;


/**
 * Quples Component Controller
 */

@Controller("GPQuplesComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPQuplesComponent)
public class GPQuplesComponentController extends AbstractCMSAddOnComponentController<GPQuplesComponentModel>
{

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPQuplesComponentModel component)
	{
		//  Auto-generated method stub

	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPQuplesComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}

}
