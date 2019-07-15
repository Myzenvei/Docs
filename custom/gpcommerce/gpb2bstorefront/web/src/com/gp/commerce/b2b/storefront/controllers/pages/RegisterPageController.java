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
 * Register Controller. Handles register for the account flow.
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(RegisterPageController.class);
	private static final String REGISTERPAGE = "registerPage";
	private static final String B2B_MAILER = "B2BMailerCheck";
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	/**
	 * @param model
	 * @throws CMSItemNotFoundException
	 *            This method is used to display registration Page for B2B
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getRegistrationPage(final Model model) throws CMSItemNotFoundException
	{
		LOG.info("Inside RegisterPageController getRegistrationPage()");
		if (!getUserFacade().isAnonymousUser())
		{
			return REDIRECT_PREFIX+"/";
		}

		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(REGISTERPAGE);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);

		final HashMap<String, HashMap<String,String>> states = new HashMap<String,HashMap<String,String>>();
		final Map USMap = new HashMap<>();
		final Map canadaMap = new HashMap<>();
		final Map otherMap = new HashMap<>();
		USMap.put(B2B_MAILER, Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig("addToMarket.us.b2b")));
		canadaMap.put(B2B_MAILER, Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig("addToMarket.ca.b2b")));
		otherMap.put(B2B_MAILER, Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig("addToMarket.other.b2b")));

		states.put("us", (HashMap<String, String>) USMap);
		states.put("ca", (HashMap<String, String>) canadaMap);
		states.put("other", (HashMap<String, String>) otherMap);

		final ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = null;
		try {
			jsonResp = mapperObj.writeValueAsString(states);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		model.addAttribute("userMarketingOption", jsonResp);
		return ControllerConstants.Views.Pages.Account.AccountRegisterPage;
	}
}
