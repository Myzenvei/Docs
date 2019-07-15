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
package com.gp.commerce.b2b.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.core.services.GPCMSSiteService;
/**
 * Lease Page Controller
 */
@Controller
@RequestMapping(value = "/lease")
public class LeasePageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(LeasePageController.class);
	private static final String LEASEPAGE = "leasePage";

	/**
	 * @param model
	 * @throws CMSItemNotFoundException
	 *            This method is used to display Lease Page for B2B
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getLeasePage(final Model model) throws CMSItemNotFoundException
	{
		LOG.info("Inside LeasePageController getLeasePage()");

		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(LEASEPAGE);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);
		return ControllerConstants.Views.Pages.Account.AccountLeasePage;
	}
}
