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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.core.util.JsonHelper;
import com.gp.commerce.facade.catalog.GpCatalogFacade;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.util.Config;

@Controller
@RequestMapping("/crossReference")
public class CrossReferencePageController extends AbstractPageController
{
	private static final Set<CatalogOption> OPTIONS;

	static
	{
		OPTIONS = getOptions();
	}
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CrossReferencePageController.class);
	
	private static final String CROSS_REFERENCE_CMS_PAGE = "crossReferencePage";
	private static final String GP_CATALOG_ONLINE_VERSION = "gp.catalog.online.version";
	private static final String GP_CATALOG_ID = "gp.catalog.id";
	private static final String  CROSS_REFERENCE_CATEGORIES = "gp.cross.reference.categories";
	
	@Resource(name = "gpDefaultCatalogFacade")
	private GpCatalogFacade gpCatalogFacade;
	
	@RequestMapping(method = RequestMethod.GET)
	public String getCrossReferencePage(final HttpServletRequest request,final Model model) throws CMSItemNotFoundException
	{		
		final ContentPageModel pageForRequest = getContentPageForLabelOrId(CROSS_REFERENCE_CMS_PAGE);
		final PageOption page = PageOption.createForPageNumberAndPageSize(0, 10);
		String catalogId = Config.getParameter(GP_CATALOG_ID);
		String catalogVersion = Config.getParameter(GP_CATALOG_ONLINE_VERSION);
		String categoryId = Config.getParameter(CROSS_REFERENCE_CATEGORIES);
		
		final CategoryHierarchyData categoryHierarchyData = gpCatalogFacade.getCategoryById(catalogId, catalogVersion, categoryId,	page, OPTIONS);
		gpCatalogFacade.sortCategoriesByOrder(categoryHierarchyData);
		
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
		model.addAttribute("crossReferenceCategories",JsonHelper.getJson(categoryHierarchyData));
		
		return ControllerConstants.Views.Pages.Product.CrossReference;
	}	
	
	

	protected static Set<CatalogOption> getOptions()
	{
		final Set<CatalogOption> opts = new HashSet<>();
		opts.add(CatalogOption.BASIC);
		opts.add(CatalogOption.CATEGORIES);
		opts.add(CatalogOption.SUBCATEGORIES);
		return opts;
	}
}