/*
O * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.controller;

import com.gp.commerce.configurablebundlefacades.dto.BundleTemplateWsDTO;
import com.gp.commerce.facade.configurablebundle.GPBundleFacade;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import io.swagger.annotations.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Web Services Controller to expose the functionality of the
 * {@link de.hybris.platform.commercefacades.product.ProductFacade} and SearchFacade.
 */

@Secured(
		{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT","ROLE_ASAGENTSALESMANAGERGROUP" ,"ROLE_CLIENT","ROLE_GUEST"})
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@Controller
@Api(tags = "BundleTemplates")
@RequestMapping(value = "/{baseSiteId}/bundles")
public class GPBundleDetailsController extends BaseCommerceController
{
	@Resource (name = "gpBundleFacade")
	GPBundleFacade gpBundleFacade;

	@RequestMapping(value="/bundleDetail", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = ".")
	public BundleTemplateWsDTO getBundleDetails(
			@ApiParam(value = "The actual string user searched.") @RequestParam(required = false) final String bundleId,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final BundleTemplateData bundleTemplateData = gpBundleFacade.buildBundleTree(bundleId);
		return getDataMapper().map(bundleTemplateData, BundleTemplateWsDTO.class, fields);
	}
}
