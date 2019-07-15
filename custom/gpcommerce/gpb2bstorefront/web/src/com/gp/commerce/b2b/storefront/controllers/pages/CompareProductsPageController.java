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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import org.springframework.web.bind.annotation.RequestParam;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import java.util.List;

import javax.annotation.Resource;
import com.gp.commerce.core.util.GPFunctions;

@Controller
@RequestMapping("/compare")
public class CompareProductsPageController extends AbstractPageController
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CompareProductsPageController.class);
	
	private static final String COMPARE_PRODUCTS_CMS_PAGE = "compareProductsPage";
	private static final String LAST_LINK_CLASS = "active";
	
	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;
	
	@RequestMapping(method = RequestMethod.GET)
	public String getComparePage(@RequestParam(required = true) final String productCodes,final Model model) throws CMSItemNotFoundException
	{		
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(COMPARE_PRODUCTS_CMS_PAGE);
		storeCmsPageInModel(model, pageForRequest);
		List<Breadcrumb> compareBreadCrumb = productBreadcrumbBuilder.getBreadcrumbs(productCodes.substring(0,productCodes.indexOf(":")));	
		compareBreadCrumb.remove(compareBreadCrumb.size()-1);
		compareBreadCrumb.add(new Breadcrumb("#", getMessageSource().getMessage("text.link.comparePage", null,
            getI18nService().getCurrentLocale()), LAST_LINK_CLASS));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				GPFunctions.convertToJSON(compareBreadCrumb));
		setUpMetaDataForContentPage(model, pageForRequest);
		return getViewForPage(model);
	}	
	
}