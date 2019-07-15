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

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.storefront.controllers.ControllerConstants;
import com.gp.gpassistedservicesstorefront.constants.GpassistedservicesstorefrontConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.commerce.facades.process.email.context.ShareCartEmailContext;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;


/**
 * Controller for home page
 */
@Controller
@RequestMapping("/")
public class HomePageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(HomePageController.class);

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;


	private static final String LOGOUT = "logout";
	private static final String ACCOUNT_CONFIRMATION_SIGNOUT_TITLE = "account.confirmation.signout.title";
	private static final String ACCOUNT_CONFIRMATION_CLOSE_TITLE = "account.confirmation.close.title";
	private static final String KOCHAPI_UI_URI ="gp.koch.auth.token.api.ui.uri";
	private static final String KOCH_CLIENTID ="gp.koch.auth.client.id";
	private static final String CLIENT_ID = "?client_id=";
	private static final String RESPONSE_TYPE = "&response_type=code&scope=openid";
	private static final String LOGIN_EMPLOYEESTORE_DEFAULTPAGE_REDIRECT = "gpcommercestorefront.employeestore.basesiteID";
	private static final String HOME_PAGE = "gpHomePage";
	private static final String SOCIAL_AGGR_START_DATE_B2C_KEY = "socialaggrstartdate.b2c";
	private static final String SOCIAL_AGGR_START_DATE = "startDate";
	private static final String SOCIAL_AGGR_END_DATE_B2C_KEY = "socialaggrenddate.b2c";
	private static final String SOCIAL_AGGR_END_DATE = "endDate";
	private static final String SOCIAL_AGGR_FEEDID_B2C_KEY = "socialaggrfeedId.b2c";
	private static final String SOCIAL_AGGR_FEEDID = "feedId";
	private static final String SOCIAL_AGGR_TRUNCATE_KEY = "socialaggrtruncate.b2c";
	private static final String SOCIAL_AGGR_TRUNCATE = "truncate";
	private static final String SOCIAL_AGGR_GUTTER_KEY = "socialaggrgutter.b2c";
	private static final String SOCIAL_AGGR_GUTTER = "gutter";
	private static final String SOCIAL_AGGR_COLUMNS_KEY = "socialaggrcolumns.b2c";
	private static final String SOCIAL_AGGR_COLUMNS = "columns";
	private static final String SOCIAL_AGGR_INTERVAL_KEY = "socialaggrinterval.b2c";
	private static final String SOCIAL_AGGR_INTERVAL = "interval";
	private static final String SOCIAL_AGGR_FILTER_KEY = "socialaggrfilter.b2c";
	private static final String SOCIAL_AGGR_FILTER = "filter";
	private static final String SOCIAL_AGGR_STYLE_KEY = "socialaggrstyle.b2c";
	private static final String SOCIAL_AGGR_STYLE = "style";
	private static final String SOCIAL_AGGR_OVERLAY_KEY = "socialaggroverlay.b2c";
	private static final String SOCIAL_AGGR_OVERLAY = "overlay";
	private static final String SOCIAL_AGGR_DATAPER = "dataPer";
	private static final String SOCIAL_AGGR_DATAPER_KEY = "socialaggdataper.b2c";
	

	private static final String CONTACT_US = "contactUsForm";
	private static final String LISTDETAILS_CMS_PAGE ="gplistdetailspage";
	private static final String IS_PRECURATED = "isPrecuratedList";
	private static final String SHARED_LIST = "isSharedList";
	private static final String SHARE_CART_LIST = "SHARE_CART_LIST";
	private static final String IS_SHARED_CART = "isSharedCart";

	private static final String CANONICAL_URL = "canonicalUrl";

	@RequestMapping(method = RequestMethod.GET)
	public String home(@RequestParam(value = WebConstants.CLOSE_ACCOUNT, defaultValue = "false") final boolean closeAcc,
			@RequestParam(value = LOGOUT, defaultValue = "false") final boolean logout, final Model model,
			final RedirectAttributes redirectModel, final HttpServletRequest request) throws CMSItemNotFoundException

	{

		if (logout)
		{
			String message = ACCOUNT_CONFIRMATION_SIGNOUT_TITLE;
			if (closeAcc)
			{
				message = ACCOUNT_CONFIRMATION_CLOSE_TITLE;
			}
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, message);
			return REDIRECT_PREFIX + ROOT;
		}

		final ContentPageModel homePage = getContentPageForLabelOrId(HOME_PAGE);
		storeCmsPageInModel(model, homePage);
		setUpMetaDataForContentPage(model, homePage);
		updatePageTitle(model, homePage);
		if (StringUtils.isNotBlank(homePage.getCanonicalUrl()))
		{
			model.addAttribute(CANONICAL_URL, homePage.getCanonicalUrl());
		}
		else
		{
			model.addAttribute(CANONICAL_URL, request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, homePage.getSeoIndex());
		final String kochapiuri = configurationService.getConfiguration().getString(KOCHAPI_UI_URI);
		final String kochclientid = configurationService.getConfiguration().getString(KOCH_CLIENTID);

		final String kochurl = kochapiuri + CLIENT_ID + kochclientid + RESPONSE_TYPE;
		model.addAttribute("kochurl", kochurl);

		model.addAttribute("channel", baseSiteService.getCurrentBaseSite().getChannel());
		final String asmRequestParam = request.getParameter(GpassistedservicesstorefrontConstants.ASM_REQUEST_PARAM);
		if(userFacade.isAnonymousUser()) {
			if(baseSiteService.getCurrentBaseSite().getUid().equals(configurationService.getConfiguration().getString(LOGIN_EMPLOYEESTORE_DEFAULTPAGE_REDIRECT)) && asmRequestParam == null) {
				return REDIRECT_PREFIX+"/login";
			}
		}

		setUpSocialAggrData(model);

		return getViewForPage(model);
	}

	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveContentPageTitle(cmsPage.getTitle()));
	}

	/**
	 * Method to set up social aggregator data
	 * @param model
	 */
	private void setUpSocialAggrData(final Model model) {

		final Map fieldsMap = new HashMap<>();

		fieldsMap.put(SOCIAL_AGGR_START_DATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_START_DATE_B2C_KEY));
		fieldsMap.put(SOCIAL_AGGR_END_DATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_END_DATE_B2C_KEY));
		fieldsMap.put(SOCIAL_AGGR_FEEDID,cmsSiteService.getSiteConfig(SOCIAL_AGGR_FEEDID_B2C_KEY));
		fieldsMap.put(SOCIAL_AGGR_TRUNCATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_TRUNCATE_KEY));
		fieldsMap.put(SOCIAL_AGGR_GUTTER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_GUTTER_KEY));
		fieldsMap.put(SOCIAL_AGGR_COLUMNS,cmsSiteService.getSiteConfig(SOCIAL_AGGR_COLUMNS_KEY));
		fieldsMap.put(SOCIAL_AGGR_INTERVAL,cmsSiteService.getSiteConfig(SOCIAL_AGGR_INTERVAL_KEY));
		fieldsMap.put(SOCIAL_AGGR_FILTER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_FILTER_KEY));
		fieldsMap.put(SOCIAL_AGGR_STYLE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_STYLE_KEY));
		fieldsMap.put(SOCIAL_AGGR_OVERLAY,cmsSiteService.getSiteConfig(SOCIAL_AGGR_OVERLAY_KEY));
		fieldsMap.put(SOCIAL_AGGR_DATAPER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_DATAPER_KEY));


		final ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = null;
		try {
			jsonResp = mapperObj.writeValueAsString(fieldsMap);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}

		model.addAttribute("socialAggrOption", jsonResp);
	}

	@RequestMapping(value = "/contact-us")
	public String getContactUsForm(final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final ContentPageModel contactUsPage = getContentPageForLabelOrId(CONTACT_US);
		if (contactUsPage.getExpirationDate() != null && contactUsPage.getExpirationDate().before(new Date())
				&& contactUsPage.getExpiredPageRedirectUrl() != null)
		{
			return REDIRECT_PREFIX + contactUsPage.getExpiredPageRedirectUrl();
		}
		storeCmsPageInModel(model, contactUsPage);
		setUpMetaDataForContentPage(model, contactUsPage);
		if (StringUtils.isNotBlank(contactUsPage.getCanonicalUrl()))
		{
			model.addAttribute(CANONICAL_URL, contactUsPage.getCanonicalUrl());
		}
		else
		{
			model.addAttribute(CANONICAL_URL, request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, contactUsPage.getSeoIndex());
		return ControllerConstants.Views.Pages.ACS.GPContactUsFormPage;
	}

	@RequestMapping(value = "/sharefavourite", method = RequestMethod.GET)
	public String getShareListDetails(final Model model, @RequestParam(required = true) final String listName,
			final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final ContentPageModel listDetailsPage = getContentPageForLabelOrId(LISTDETAILS_CMS_PAGE);
		storeCmsPageInModel(model, listDetailsPage);
		setUpMetaDataForContentPage(model, listDetailsPage);
		if (StringUtils.isNotBlank(listDetailsPage.getCanonicalUrl()))
		{
			model.addAttribute(CANONICAL_URL, listDetailsPage.getCanonicalUrl());
		}
		else
		{
			model.addAttribute(CANONICAL_URL, request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, listDetailsPage.getSeoIndex());
		WishlistData wishlistData = new WishlistData();
		wishlistData = wishlistFacade.getSharedlist(listName);
		model.addAttribute(IS_PRECURATED, Boolean.FALSE);
		if(null != wishlistData && StringUtils.isNotEmpty(wishlistData.getWishlistType()) && SHARE_CART_LIST.equalsIgnoreCase(wishlistData.getWishlistType()))
		{
			model.addAttribute(IS_SHARED_CART, Boolean.TRUE);
			model.addAttribute(SHARED_LIST, Boolean.FALSE);
		}
		else
		{
			model.addAttribute(SHARED_LIST, Boolean.TRUE);
			model.addAttribute(IS_SHARED_CART, Boolean.FALSE);
		}
		return ControllerConstants.Views.Pages.Account.gpListDetailsPage;
	}

}
