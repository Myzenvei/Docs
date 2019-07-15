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


import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ContentPageBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import com.gp.commerce.core.util.GPFunctions;


/**
 * Error handler to show a CMS managed error page. This is the catch-all controller that handles all GET requests that
 * are not handled by other controllers.
 */
@Controller
//@RequestMapping()
public class DefaultPageController extends AbstractPageController
{
	private static final String ERROR_CMS_PAGE = "notFound";
	private static final String CANONICAL_URL = "canonicalUrl";

	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	private static final String FIND_A_DISTRIBUTOR = "find-a-distributor";

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "contentPageBreadcrumbBuilder")
	private ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;


	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		final String redirectionUrlKey = GPFunctions.targetUrlFromSessionAttribute(request);
		if (StringUtils.isNotBlank(redirectionUrlKey) && StringUtils.equalsIgnoreCase(FIND_A_DISTRIBUTOR, redirectionUrlKey))
		{
			return REDIRECT_PREFIX + "/find-a-distributor/home-page";
		}
		// Check for CMS Page where label or id is like /page
		final ContentPageModel pageForRequest = getContentPageForRequest(request);
		if (pageForRequest != null)
		{
			if (pageForRequest.getExpirationDate() != null && pageForRequest.getExpirationDate().before(new Date())
					&& pageForRequest.getExpiredPageRedirectUrl() != null)
			{
				return REDIRECT_PREFIX + pageForRequest.getExpiredPageRedirectUrl();
			}
			storeCmsPageInModel(model, pageForRequest);
			setUpMetaDataForContentPage(model, pageForRequest);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, contentPageBreadcrumbBuilder.getBreadcrumbs(pageForRequest));
			if (StringUtils.isNotBlank(pageForRequest.getCanonicalUrl()))
			{
				model.addAttribute(CANONICAL_URL, pageForRequest.getCanonicalUrl());
			}
			else
			{
				model.addAttribute(CANONICAL_URL, request.getRequestURL());
			}
			model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, pageForRequest.getSeoIndex());
			return getViewForPage(pageForRequest);
		}

		// No page found - display the notFound page with error from controller
		storeCmsPageInModel(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		GlobalMessages.addErrorMessage(model, "system.error.page.not.found");

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
	}

	/**
	 * Lookup the CMS Content Page for this request.
	 *
	 * @param request
	 *           The request
	 * @return the CMS content page
	 */
	protected ContentPageModel getContentPageForRequest(final HttpServletRequest request)
	{
		// Get the path for this request.
		// Note that the path begins with a '/'
		final String lookupPathForRequest = urlPathHelper.getLookupPathForRequest(request);

		try
		{
			// Lookup the CMS Content Page by label. Note that the label value must begin with a '/'.
			return getCmsPageService().getPageForLabel(lookupPathForRequest);
		}
		catch (final CMSItemNotFoundException ignore)
		{
			// Ignore exception
		}
		return null;
	}
}
