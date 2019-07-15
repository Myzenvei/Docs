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

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gp.commerce.b2b.storefront.controllers.ControllerConstants;
import com.gp.commerce.core.util.GPFunctions;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;


/**
 * Controller defines routes to render pages for business org modules within My Company section.
 */
@Controller
@RequestMapping("/businessorg")
public class GPBusinessOrgPageController extends AbstractPageController
{
	

	private static final String BREADCRUMBS_ATTR = "breadcrumbs";
	private static final String UNITS_CMS_PAGE ="gpunitspage";
	private static final String USERGROUPS_CMS_PAGE ="gpusergroupspage";
	private static final String USERS_CMS_PAGE ="gpuserspage";
	private static final String PERMISSIONS_CMS_PAGE ="gppermissionspage";
	private static final String ADDRESSES_CMS_PAGE ="gpaddressespage";
	private static final String PERMISSIONS_DETAILS_CMS_PAGE ="gppermissiondetailspage";
	private static final String USERS_CMS_DETAILS_PAGE ="gpuserdetailspage";
	private static final String USERGROUPS_DETAILS_CMS_PAGE ="gpusergroupdetailspage";
	private static final String UNITS_DETAILS_CMS_PAGE ="gpunitdetailspage";

	private static final String ORDERSAPPROVAL_CMS_PAGE = "gpordersapprovalpage";
	private static final String ORDERSAPPROVAL_DETAILS_CMS_PAGE = "gporderapprovaldetailspage";


	
	//Breadcrumb page navigating Urls
	private static final String BUSINESSORG_PERMISSIONS_PAGE_URL = "/businessorg/permissions";
	private static final String BUSINESSORG_UNITS_PAGE_URL = "/businessorg/units";
	private static final String BUSINESSORG_USERS_PAGE_URL = "/businessorg/users";
	private static final String BUSINESSORG_USERGROUPS_PAGE_URL = "/businessorg/usergroups";
	private static final String BUSINESSORG_ORDERS_PAGE_URL = "/businessorg/ordersapproval";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;
	
	@RequestMapping(value = "/permissions", method = RequestMethod.GET)
	public String managePermissions(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(PERMISSIONS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PERMISSIONS_CMS_PAGE));
		
		//Setting page breadcrumb display name to Model
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.permissions")));


		return ControllerConstants.Views.Pages.BusinessOrg.gpPermissions;
	}
	
	@RequestMapping(value = "/permissions/details", method = RequestMethod.GET)
	public String getPermissionDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(PERMISSIONS_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PERMISSIONS_DETAILS_CMS_PAGE));
		
		//To populate breadcrumb display names to the pages . 
		populateBreadCrumbs("text.account.permissions", "text.account.permissiondetails", model ,BUSINESSORG_PERMISSIONS_PAGE_URL);
  //Added
		return ControllerConstants.Views.Pages.BusinessOrg.gpPermissionsDetails;
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String manageUsers(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(USERS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(USERS_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.users")));
		return ControllerConstants.Views.Pages.BusinessOrg.gpUsers;
	}
	
	
	@RequestMapping(value = "/users/details", method = RequestMethod.GET)
	public String getUserDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(USERS_CMS_DETAILS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(USERS_CMS_DETAILS_PAGE));
		populateBreadCrumbs("text.account.users", "text.account.userdetails", model ,BUSINESSORG_USERS_PAGE_URL);
		return ControllerConstants.Views.Pages.BusinessOrg.gpUsersDetails;
	}
	
	
	@RequestMapping(value = "/usergroups", method = RequestMethod.GET)
	public String manageUsergroups(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(USERGROUPS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(USERGROUPS_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.usergroups")));

		return ControllerConstants.Views.Pages.BusinessOrg.gpUserGroups;
	}
	
	
	@RequestMapping(value = "/usergroups/details", method = RequestMethod.GET)
	public String getUsergroupDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(USERGROUPS_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(USERGROUPS_DETAILS_CMS_PAGE));
		populateBreadCrumbs("text.account.usergroups", "text.account.usergroupdetails", model ,BUSINESSORG_USERGROUPS_PAGE_URL);
		return ControllerConstants.Views.Pages.BusinessOrg.gpUserGroupsDetails;
	}
	
	
	
	@RequestMapping(value = "/units", method = RequestMethod.GET)
	public String manageUnits(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(UNITS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UNITS_CMS_PAGE));
		
		model.addAttribute(BREADCRUMBS_ATTR, 
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.units")));

		return ControllerConstants.Views.Pages.BusinessOrg.gpUnitsPage;
	}
	
	
	@RequestMapping(value = "/units/details", method = RequestMethod.GET)
	public String getUnitDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(UNITS_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UNITS_DETAILS_CMS_PAGE));
		populateBreadCrumbs("text.account.units", "text.account.unitdetails", model ,BUSINESSORG_UNITS_PAGE_URL);
		return ControllerConstants.Views.Pages.BusinessOrg.gpUnitsDetailsPage;
	}
	
	
	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	public String manageAddresses(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESSES_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESSES_CMS_PAGE));
		return ControllerConstants.Views.Pages.BusinessOrg.gpAddresses;
	}

	@RequestMapping(value = "/ordersapproval", method = RequestMethod.GET)
	public String manageOrdersApproval(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDERSAPPROVAL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDERSAPPROVAL_CMS_PAGE));
		model.addAttribute(BREADCRUMBS_ATTR,
				GPFunctions.convertToJSON(accountBreadcrumbBuilder.getBreadcrumbs("text.account.units")));

		return ControllerConstants.Views.Pages.BusinessOrg.gpOrdersApprovalPage;
	}


	@RequestMapping(value = "/orderapproval/{ordercode}", method = RequestMethod.GET)
	public String getOrderApprovalDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", defaultValue = "code") final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDERSAPPROVAL_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDERSAPPROVAL_DETAILS_CMS_PAGE));
		populateBreadCrumbs("text.account.ordersapproval", "text.account.orderapprovaldetails", model, BUSINESSORG_ORDERS_PAGE_URL);
		return ControllerConstants.Views.Pages.BusinessOrg.gpOrderApprovalDetailsPage;
	}


	/**
	 * @param listKey 
	 *           - listKey is the property name defined in message.properties which contains the Display name of the breadcrumb
	 * @param detailsKey
	 *           - detailsKey is the property name defined in message.properties which contains the second Display name of breadcrumb
	 * @param model
	 * @param pageUrl
	 *           - pageUrl is the url to which page should be navigated upon clicking the breadcrumb
	 */
	private void populateBreadCrumbs(final String listKey, final String detailsKey,final Model model ,final String pageUrl) {
		
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb(pageUrl, getMessageSource().getMessage(listKey, null,
                getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage(detailsKey, null,
                getI18nService().getCurrentLocale()), null));
		model.addAttribute(BREADCRUMBS_ATTR, GPFunctions.convertToJSON(breadcrumbs));
	}

}
