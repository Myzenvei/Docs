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

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.facades.wishlist.GPWishlistFacade;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import com.gp.commerce.core.util.GPFunctions;

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

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "wishlistFacade")
	private GPWishlistFacade wishlistFacade;


	public GPWishlistFacade getWishlistFacade() {
		return wishlistFacade;
	}

	public void setWishlistFacade(final GPWishlistFacade wishlistFacade) {
		this.wishlistFacade = wishlistFacade;
	}

	private static final String LOGOUT = "logout";
	private static final String ACCOUNT_CONFIRMATION_SIGNOUT_TITLE = "account.confirmation.signout.title";
	private static final String ACCOUNT_CONFIRMATION_CLOSE_TITLE = "account.confirmation.close.title";
	private static final String HOME_PAGE = "gpHomePage";
	private static final String SOCIAL_AGGR_START_DATE_KEY = "socialaggrstartdate.b2b";
	private static final String SOCIAL_AGGR_START_DATE = "startDate";
	private static final String SOCIAL_AGGR_END_DATE_KEY = "socialaggrenddate.b2b";
	private static final String SOCIAL_AGGR_END_DATE = "endDate";
	private static final String SOCIAL_AGGR_FEEDID_KEY = "socialaggrfeedId.b2b";
	private static final String SOCIAL_AGGR_FEEDID = "feedId";
	private static final String SOCIAL_AGGR_DATA_PER_KEY = "socialaggrdataPer.b2b";
	private static final String SOCIAL_AGGR_DATA_PER = "dataPer";
	private static final String SOCIAL_AGGR_PAGES_KEY = "socialaggrpages.b2b";
	private static final String SOCIAL_AGGR_PAGES = "pages";
	private static final String SOCIAL_AGGR_TRUNCATE_KEY = "socialaggrtruncate.b2b";
	private static final String SOCIAL_AGGR_TRUNCATE = "truncate";
	private static final String SOCIAL_AGGR_GUTTER_KEY = "socialaggrgutter.b2b";
	private static final String SOCIAL_AGGR_GUTTER = "gutter";
	private static final String SOCIAL_AGGR_COLUMNS_KEY = "socialaggrcolumns.b2b";
	private static final String SOCIAL_AGGR_COLUMNS = "columns";
	private static final String SOCIAL_AGGR_INTERVAL_KEY = "socialaggrinterval.b2b";
	private static final String SOCIAL_AGGR_INTERVAL = "interval";
	private static final String SOCIAL_AGGR_FILTER_KEY = "socialaggrfilter.b2b";
	private static final String SOCIAL_AGGR_FILTER = "filter";
	private static final String SOCIAL_AGGR_STYLE_KEY = "socialaggrstyle.b2b";
	private static final String SOCIAL_AGGR_STYLE = "style";
	private static final String SOCIAL_AGGR_OVERLAY_KEY = "socialaggroverlay.b2b";
	private static final String SOCIAL_AGGR_OVERLAY = "overlay";
	private static final String LISTDETAILS_CMS_PAGE ="gplistdetailspage";
	private static final String PRECURATED_LIST = "PRE_CURATED_LIST";
	private static final String USER_LIST= "USER_LIST";
	private static final String IS_PRECURATED = "isPrecuratedList";
	private static final String CONTACT_US = "contactUsForm";
	private static final String SHARED_LIST = "isSharedList";
	private static final String IS_SHARED_CART = "isSharedCart";
	private static final String SHARE_CART_LIST = "SHARE_CART_LIST";

	private static final String CANONICAL_URL = "canonicalUrl";

	private static final String FIND_A_DISTRIBUTOR = "find-a-distributor";
	private static final String DISPENSER = "dispenserReplacementTicket";

	@RequestMapping(method = RequestMethod.GET)
	public String home(@RequestParam(value = WebConstants.CLOSE_ACCOUNT, defaultValue = "false") final boolean closeAcc,
			@RequestParam(value = LOGOUT, defaultValue = "false") final boolean logout, final Model model,
			final RedirectAttributes redirectModel, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
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
		final String redirectionUrlKey =  GPFunctions.targetUrlFromSessionAttribute(request);
		if (StringUtils.isNotBlank(redirectionUrlKey) && StringUtils.equalsIgnoreCase(FIND_A_DISTRIBUTOR, redirectionUrlKey))
		{
			return REDIRECT_PREFIX + "/find-a-distributor/home-page";
		}
		
		HttpSession session=request.getSession(true);
		

		if(!userService.isAnonymousUser(userService.getCurrentUser()) && null!=session.getAttribute("subscReferer"))
		{
			String refererURL=(String) session.getAttribute("subscReferer");
			session.removeAttribute("subscReferer");
			return REDIRECT_PREFIX+ refererURL;
		}
		
		if(userService.isAnonymousUser(userService.getCurrentUser()) && null!=session.getAttribute("subscReferer")) 
		{
			session.removeAttribute("subscReferer");
		}
		
		if (baseSiteService.getCurrentBaseSite().getUid().equalsIgnoreCase(GpcommerceFacadesConstants.GPXPRESS)) {
			Cookie cookie = WebUtils.getCookie(request,
					GpcommerceFacadesConstants.RELAY_ENABLED);
			if(null != cookie && cookie.getValue().equalsIgnoreCase(Boolean.TRUE.toString())) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
				return ControllerConstants.Views.Pages.Misc.MiscSuccessPage;
			}
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
		model.addAttribute("channel", baseSiteService.getCurrentBaseSite().getChannel());

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

		fieldsMap.put(SOCIAL_AGGR_START_DATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_START_DATE_KEY));
		fieldsMap.put(SOCIAL_AGGR_END_DATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_END_DATE_KEY));
		fieldsMap.put(SOCIAL_AGGR_FEEDID,cmsSiteService.getSiteConfig(SOCIAL_AGGR_FEEDID_KEY));
		fieldsMap.put(SOCIAL_AGGR_DATA_PER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_DATA_PER_KEY));
		fieldsMap.put(SOCIAL_AGGR_PAGES,cmsSiteService.getSiteConfig(SOCIAL_AGGR_PAGES_KEY));
		fieldsMap.put(SOCIAL_AGGR_TRUNCATE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_TRUNCATE_KEY));
		fieldsMap.put(SOCIAL_AGGR_GUTTER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_GUTTER_KEY));
		fieldsMap.put(SOCIAL_AGGR_COLUMNS,cmsSiteService.getSiteConfig(SOCIAL_AGGR_COLUMNS_KEY));
		fieldsMap.put(SOCIAL_AGGR_INTERVAL,cmsSiteService.getSiteConfig(SOCIAL_AGGR_INTERVAL_KEY));
		fieldsMap.put(SOCIAL_AGGR_FILTER,cmsSiteService.getSiteConfig(SOCIAL_AGGR_FILTER_KEY));
		fieldsMap.put(SOCIAL_AGGR_STYLE,cmsSiteService.getSiteConfig(SOCIAL_AGGR_STYLE_KEY));
		fieldsMap.put(SOCIAL_AGGR_OVERLAY,cmsSiteService.getSiteConfig(SOCIAL_AGGR_OVERLAY_KEY));

		final ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = null;
		try {
			jsonResp = mapperObj.writeValueAsString(fieldsMap);
		} catch (final IOException e) {
			LOG.error(e.getMessage(),e);
		}

		model.addAttribute("socialAggrOption", jsonResp);
	}

	@RequestMapping(value = "/productlist", method = RequestMethod.GET)
	public String getListDetails(final Model model,@RequestParam(required = true) final String listName, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final ContentPageModel listDetailCmsPage = getContentPageForLabelOrId(LISTDETAILS_CMS_PAGE);
		storeCmsPageInModel(model, listDetailCmsPage);
		setUpMetaDataForContentPage(model, listDetailCmsPage);
		if (StringUtils.isNotBlank(listDetailCmsPage.getCanonicalUrl()))
		{
			model.addAttribute(CANONICAL_URL, listDetailCmsPage.getCanonicalUrl());
		}
		else
		{
			model.addAttribute(CANONICAL_URL, request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, listDetailCmsPage.getSeoIndex());
		WishlistData wishlistData = new WishlistData();
		wishlistData = wishlistFacade.getPrecuratedlist(listName);
		if(null != wishlistData && StringUtils.isNotEmpty(wishlistData.getWishlistType()) && !USER_LIST.equalsIgnoreCase(wishlistData.getWishlistType()) && PRECURATED_LIST.equalsIgnoreCase(wishlistData.getWishlistType())){
			model.addAttribute(IS_PRECURATED, Boolean.TRUE);
		}else {
			model.addAttribute(IS_PRECURATED, Boolean.FALSE);
		}
		model.addAttribute(SHARED_LIST, Boolean.FALSE);
		model.addAttribute(IS_SHARED_CART, Boolean.FALSE);
		return ControllerConstants.Views.Pages.Account.gpListDetailsPage;
	}

	/**
	 * @param model
	 * @param listName
	 *           Name of the wishlist
	 * @return
	 *       Page
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/sharelist", method = RequestMethod.GET)
	public String getShareListDetails(final Model model,@RequestParam(required = true) final String listName, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final ContentPageModel listDetailCmsPage = getContentPageForLabelOrId(LISTDETAILS_CMS_PAGE);

		storeCmsPageInModel(model, listDetailCmsPage);
		setUpMetaDataForContentPage(model, listDetailCmsPage);
		if (StringUtils.isNotBlank(listDetailCmsPage.getCanonicalUrl()))
		{
			model.addAttribute(CANONICAL_URL, listDetailCmsPage.getCanonicalUrl());
		}
		else
		{
			model.addAttribute(CANONICAL_URL, request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, listDetailCmsPage.getSeoIndex());

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
			model.addAttribute(CANONICAL_URL,request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, contactUsPage.getSeoIndex());
		return ControllerConstants.Views.Pages.ACS.GPContactUsFormPage;
	}
	
	@RequestMapping(value="/dispenserreplaceticket")
	public String getDispenserReplaceTicket(final Model model) throws CMSItemNotFoundException
	{
		LOG.info("Inside DispenserReplacementTicketController getDispenserReplaceTicket()");

		final ContentPageModel contactUSCMSPage = getContentPageForLabelOrId(DISPENSER);
		storeCmsPageInModel(model, contactUSCMSPage);
		setUpMetaDataForContentPage(model, contactUSCMSPage);
		return ControllerConstants.Views.Pages.Account.AccountDispenserTicketPage;
	}
}
