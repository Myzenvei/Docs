/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.b2b.storefront.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

@Controller
@RequestMapping("/bundle")
public class GPBundlePageController extends AbstractPageController
{

	protected static final String BUNDLE_CMS_PAGE = "bundle-setup";

	@RequestMapping(method = RequestMethod.GET)
	public String getBundlePage(final Model model) throws CMSItemNotFoundException
	{
		setupBundleCMSPage(model);
		return ControllerConstants.Views.Pages.Product.BundleCMSPage;
	}

	protected void setupBundleCMSPage(final Model model) throws CMSItemNotFoundException {
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(BUNDLE_CMS_PAGE);
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
	}
}