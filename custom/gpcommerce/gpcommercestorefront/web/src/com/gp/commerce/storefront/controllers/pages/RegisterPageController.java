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
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;

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
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.storefront.controllers.ControllerConstants;

/**
 * Register Controller. Handles register for the account flow.
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterPageController extends AbstractPageController
{
	private static final String COUNTRY_OTHER_LOWERCASE = "other";
	private static final String COUNTRY_CA_LOWERCASE = "ca";
	private static final String COUNTRY_US_LOWERCASE = "us";
	private static final String ADD_TO_MARKET_OTHER_B2C = "addToMarket.other.b2c";
	private static final String ADD_TO_AFFILATED_MARKET_COMM_OTHER_B2C = "addToAffilatedMarketComm.other.b2c";
	private static final String ADD_TO_MARKET_CA_B2C = "addToMarket.ca.b2c";
	private static final String ADD_TO_AFFILATED_MARKET_COMM_CA_B2C = "addToAffilatedMarketComm.ca.b2c";
	private static final String ADD_TO_MARKET_US_B2C = "addToMarket.us.b2c";
	private static final String ADD_TO_AFFILATED_MARKET_COMM_US_B2C = "addToAffilatedMarketComm.us.b2c";
	private static final String B2C_ADD_TO_AFFILATED_MARKET_COMM = "B2CAddToAffilatedMarketComm";
	private static final String B2C_ADD_TO_MARKET = "B2CAddToMarket";
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	private static final Logger LOG = Logger.getLogger(RegisterPageController.class);
	private static final String REGISTERPAGE = "registerPage";
	private static final String KOCHAPI_UI_URI ="gp.koch.auth.token.api.ui.uri";
	private static final String KOCH_CLIENTID ="gp.koch.auth.client.id";
	private static final String CLIENT_ID = "?client_id=";
	private static final String RESPONSE_TYPE = "&response_type=code&scope=openid";
	public static final String REDIRECT_PREFIX = "redirect:";

	/**
	 * @param model
	 * @throws CMSItemNotFoundException
	 *            This method is used to display registration Page for B2C
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getRegistrationPage(final Model model) throws CMSItemNotFoundException
	{
		LOG.info("Inside RegisterPageController getRegistrationPage()");
		if (!userFacade.isAnonymousUser())
		{
			return REDIRECT_PREFIX+"/";
		}

		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(REGISTERPAGE);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);

		final String kochapiuri = configurationService.getConfiguration().getString(KOCHAPI_UI_URI);
		final String kochclientid = configurationService.getConfiguration().getString(KOCH_CLIENTID);

		final String kochurl = kochapiuri + CLIENT_ID + kochclientid + RESPONSE_TYPE;
		model.addAttribute("kochurl", kochurl);
		final Map USMap = new HashMap<>();
		final Map canadaMap = new HashMap<>();
		final Map otherMap = new HashMap<>();
		USMap.put(B2C_ADD_TO_AFFILATED_MARKET_COMM,
				Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(ADD_TO_AFFILATED_MARKET_COMM_US_B2C)));
		USMap.put(B2C_ADD_TO_MARKET, Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(ADD_TO_MARKET_US_B2C)));
		canadaMap.put(B2C_ADD_TO_AFFILATED_MARKET_COMM,
				Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(ADD_TO_AFFILATED_MARKET_COMM_CA_B2C)));
		canadaMap.put(B2C_ADD_TO_MARKET,
				Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(ADD_TO_MARKET_CA_B2C)));
		otherMap.put(B2C_ADD_TO_AFFILATED_MARKET_COMM,
				Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(ADD_TO_AFFILATED_MARKET_COMM_OTHER_B2C)));
		otherMap.put(B2C_ADD_TO_MARKET,
				Boolean.TRUE.toString().equalsIgnoreCase(cmsSiteService.getSiteConfig(ADD_TO_MARKET_OTHER_B2C)));
		final ObjectMapper mapperObj = new ObjectMapper();

		final HashMap<String, HashMap<String,String>> states = new HashMap<String,HashMap<String,String>>();
		states.put(COUNTRY_US_LOWERCASE, (HashMap<String, String>) USMap);
		states.put(COUNTRY_CA_LOWERCASE, (HashMap<String, String>) canadaMap);
		states.put(COUNTRY_OTHER_LOWERCASE, (HashMap<String, String>) otherMap);
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
