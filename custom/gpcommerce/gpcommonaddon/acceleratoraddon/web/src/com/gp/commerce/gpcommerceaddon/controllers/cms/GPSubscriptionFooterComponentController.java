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

import com.gp.commerce.core.model.GPSubscriptionFooterComponentModel;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonWebConstants;
import com.gp.commerce.gpcommerceaddon.controllers.GpcommonaddonControllerConstants;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

/**
 * GP Footer Component Controller
 */

@Controller("GPSubscriptionFooterComponentController")
@RequestMapping(value = GpcommonaddonControllerConstants.Actions.Cms.GPSubscriptionFooterComponent)
public class GPSubscriptionFooterComponentController extends AbstractCMSAddOnComponentController<GPSubscriptionFooterComponentModel>
{

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final GPSubscriptionFooterComponentModel component)
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
	protected String getAddonUiExtensionName(final GPSubscriptionFooterComponentModel component)
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
