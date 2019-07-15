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
package com.gp.commerce.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gp.commerce.storefront.controllers.ControllerConstants;


/**
 * Redirect Controller. Handles redirect for the account flow.
 */
@Controller
@RequestMapping(value = "/redirect")
public class RedirectPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(RedirectPageController.class);
	private static final String REDIRECTPAGE = "redirectPage";

	/**
	 * @param model
	 * @throws CMSItemNotFoundException
	 *            This method is used to display redirect page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getRedirectPage(final Model model) throws CMSItemNotFoundException
	{
		LOG.info("Inside RedirectPageController getRegistrationPage()");
		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(REDIRECTPAGE);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);

		return ControllerConstants.Views.Pages.Redirect.RedirectPage;
	}
}
