/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.controllers.cms;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.commerce.core.model.GPFooterComponentModel;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;

/**
 * GP Footer Component Controller
 */

@Controller("GPFooterComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPFooterComponent)
public class GPFooterComponentController extends AbstractCMSAddOnComponentController<GPFooterComponentModel>
{

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPFooterComponentModel component)
	{
		 component.setCopyrightYear(getCopyRightYear());
	}

	/**
	 * Gets the addon ui extension name.
	 *
	 * @param component
	 *           for component
	 * @return the extension name
	 */
	@Override
	protected String getAddonUiExtensionName(final GPFooterComponentModel component)
	{
		return GpcommonaddonWebConstants.EXTENSION_NAME;
	}

	/**
	 * 
	 * @return the current/copyright year string
	 */
	protected String getCopyRightYear()
	{
		return String.valueOf(LocalDate.now().getYear());
	}
	
}
