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
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/cludosearchresults")
public class CludoSearchPageController extends AbstractPageController {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CludoSearchPageController.class);

	private static final String CLUDO_SEARCH_CMS_PAGE = "cludoSearchPage";
	private static final String CLUDO_SEARCH_SITE_KEY = "cludo.site.key";
	private static final String CLUDO_SEARCH_URL = "cludo.search.url";
	private static final String CLUDO_AUTOSUGGEST_URL = "cludo.autosuggest.url";

	@RequestMapping(method = RequestMethod.GET)
	public String getContentSearchPage(final Model model) throws CMSItemNotFoundException {
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(CLUDO_SEARCH_CMS_PAGE);
		model.addAttribute("cludoSiteKey", Config.getParameter(CLUDO_SEARCH_SITE_KEY));
		model.addAttribute("cludoSearchURL", Config.getParameter(CLUDO_SEARCH_URL));
		model.addAttribute("cludoAutoSuggestURL", Config.getParameter(CLUDO_AUTOSUGGEST_URL));
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
		return getViewForPage(model);
	}

}
