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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gp.commerce.storefront.controllers.ControllerConstants;


/**
 * Customer Support Page Controller. Handles customer form page
 */
@Controller
@RequestMapping(value = "/supportticket")
public class GPCustomerSupportPageController extends AbstractPageController
{
	private static final String CONTACT_US = "contactUsForm";
	private static final String CONTACTUSFORM = "/contactusform";

	@RequestMapping(value = CONTACTUSFORM, method = RequestMethod.GET)
	public String getContactUsForm(final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel contactUsPage = getContentPageForLabelOrId(CONTACT_US);
		storeCmsPageInModel(model, contactUsPage);
		setUpMetaDataForContentPage(model, contactUsPage);

		return ControllerConstants.Views.Pages.ACS.GPContactUsFormPage;
	}
}
