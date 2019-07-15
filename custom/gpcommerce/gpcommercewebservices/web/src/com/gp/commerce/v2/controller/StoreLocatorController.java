/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.controller;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gp.commerce.dto.company.data.CategoriesListWsDTO;
import com.gp.commerce.dto.company.data.UserListWsDTO;
import com.gp.commerce.facades.store.data.GPStoreCategoriesListData;
import com.gp.commerce.facades.storeLocator.categories.impl.GPDefaultStoreCategoriesFacadeImpl;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Secured(
{ "ROLE_CUSTOMERGROUP", "ROLE_GUEST",  "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT"})
@Controller
@RequestMapping("/{baseSiteId}/productCategory")
public class StoreLocatorController extends BaseController {

	@Resource(name = "gpStoreCategoriesFacade")
	private GPDefaultStoreCategoriesFacadeImpl gpStoreCategoriesFacade;

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	@ApiBaseSiteIdParam
	@ResponseBody
	public CategoriesListWsDTO getStoreCategoriesList(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) {
		GPStoreCategoriesListData categories = gpStoreCategoriesFacade.getStoreCategories();

		return getDataMapper().map(categories, CategoriesListWsDTO.class, fields);

	}

}
