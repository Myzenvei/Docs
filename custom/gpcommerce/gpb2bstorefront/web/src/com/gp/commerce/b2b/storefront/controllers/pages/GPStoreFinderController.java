/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/find-a-distributor")
public class GPStoreFinderController extends AbstractPageController
{
	private static final String FIND_A_DISTRIBUTOR = "find-a-distributor";

	@RequestMapping(value = "/home-page", method = RequestMethod.GET)
	public String getDistributorPage(final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel findaDistributorPage = getContentPageForLabelOrId(FIND_A_DISTRIBUTOR);
		storeCmsPageInModel(model, findaDistributorPage);
		setUpMetaDataForContentPage(model, findaDistributorPage);
		model.addAttribute("isPdpPage", "false");
		model.addAttribute("listPage", "/my-account/lists");
		return getViewForPage(model);
	}

	@RequestMapping(value = "/pdp", method = RequestMethod.GET)
	public String getDistributorPageForPdp(final Model model,
			@RequestParam(value = "productCode", required = false) final String productCode) throws CMSItemNotFoundException
	{
		final ContentPageModel findaDistributorPage = getContentPageForLabelOrId(FIND_A_DISTRIBUTOR);
		if (null != productCode)
		{
			model.addAttribute("product", productCode);
			model.addAttribute("isPdpPage", "true");
		}
		storeCmsPageInModel(model, findaDistributorPage);
		setUpMetaDataForContentPage(model, findaDistributorPage);
		return getViewForPage(model);
	}

}
