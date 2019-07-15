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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gp.commerce.core.util.GPFunctions;
import com.gp.gpassistedservicesstorefront.constants.GpassistedservicesstorefrontConstants;
/**
 * Controller for a category page
 */
@Controller
@RequestMapping(value = "/category")
public class CategoryPageController extends AbstractCategoryPageController {
	private static final String LOGIN_URL = "/login";
	private static final String CANONICAL_URL = "canonicalUrl";
	private static final String CATEGORY_CODE = "categoryCode";
	protected static final String TITLE_WORD_SEPARATOR = " | ";
	public static final String REDIRECT_PREFIX = "redirect:";
	private static final String LOGIN_EMPLOYEESTORE_DEFAULTPAGE_REDIRECT = "gpcommercestorefront.employeestore.basesiteID";
	private static final String CATEGORY_DESCRIPTION = "categoryDescription";
	
	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder breadcrumbBuilder;
	
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;
	
	@Resource
	SessionService sessionService;
	
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
    public String category(@PathVariable(CATEGORY_CODE) final String categoryCode, // NOSONAR
                           @RequestParam(value = "query", required = false) final String searchQuery,
                           @RequestParam(value = "page", defaultValue = "0") final int page,
                           @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                           @RequestParam(value = "sort", required = false) final String sortCode, final Model model,
                           final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException {
		final CategoryModel category = getCommerceCategoryService().getCategoryForCode(categoryCode);
		sessionService.setAttribute("currentCategoryCode", category);
		final CategoryPageModel categoryPage = getCategoryPage(category);
		storeCmsPageInModel(model, categoryPage);
        final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();
		final String builder = StringUtils.isNotEmpty(category.getName())?category.getName() + TITLE_WORD_SEPARATOR
						+currentSite.getName():currentSite.getName();
		model.addAttribute(CMS_PAGE_TITLE, builder);
		model.addAttribute(CATEGORY_CODE, categoryCode);
		model.addAttribute(CATEGORY_DESCRIPTION, category.getDescription());
		if (StringUtils.isNotBlank(category.getCanonicalUrl()))
		{
			model.addAttribute(CANONICAL_URL, category.getCanonicalUrl());
		}
		else
		{
			model.addAttribute(CANONICAL_URL, request.getRequestURL());
		}
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, category.getSeoIndex());
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				GPFunctions.convertToJSON(breadcrumbBuilder.getBreadcrumbs(categoryCode, null, false)));
		String metaKeywords = null;
		if (CollectionUtils.isNotEmpty(category.getKeywords()))
		{
			metaKeywords = MetaSanitizerUtil.sanitizeKeywords(
					category.getKeywords().stream().map(keyword -> keyword.getKeyword()).collect(Collectors.toSet()));
		}
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, GPFunctions.convertToJSON(getSearchBreadcrumbBuilder().getBreadcrumbs(categoryCode, null, true)));
		final String asmRequestParam = request.getParameter(GpassistedservicesstorefrontConstants.ASM_REQUEST_PARAM);
		if(userFacade.isAnonymousUser()) {
			if(baseSiteService.getCurrentBaseSite().getUid().equals(configurationService.getConfiguration().getString(LOGIN_EMPLOYEESTORE_DEFAULTPAGE_REDIRECT)) && asmRequestParam == null) {
				return REDIRECT_PREFIX+LOGIN_URL;
			}
		}
		return getViewPage(categoryPage);
    }

    @ResponseBody
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
    public FacetRefinement<SearchStateData> getFacets(@PathVariable(CATEGORY_CODE) final String categoryCode,
                                                      @RequestParam(value = "query", required = false) final String searchQuery,
                                                      @RequestParam(value = "page", defaultValue = "0") final int page,
                                                      @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                                      @RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException {
        return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
    }

    @ResponseBody
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
    public SearchResultsData<ProductData> getResults(@PathVariable(CATEGORY_CODE) final String categoryCode,
                                                     @RequestParam(value = "query", required = false) final String searchQuery,
                                                     @RequestParam(value = "page", defaultValue = "0") final int page,
                                                     @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                                     @RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException {
        return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
    }
}