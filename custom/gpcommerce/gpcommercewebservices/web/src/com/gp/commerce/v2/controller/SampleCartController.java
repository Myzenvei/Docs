/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.controller;

import javax.annotation.Resource;
import javax.ws.rs.Produces;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.dto.cart.CartStatusDTO;
import com.gp.commerce.dto.samplecart.SampleCartDeleteSuccessDTO;
import com.gp.commerce.strategies.GPSampleCartDeleteStrategy;

import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/{baseSiteId}/sampleCarts")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
@Api(tags = "SampleCarts")
public class SampleCartController extends BaseCommerceController {

	@Resource
	GPSampleCartDeleteStrategy gpSampleCartDeleteStrategy;

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/deleteCart", method = RequestMethod.POST)
	@ResponseBody
	@Produces({"application/json"})
	@ApiOperation(value = "Deletes a sample cart with a given cart id.", notes = "Deletes a sample cart with a given cart id.")
	@ResponseStatus(HttpStatus.OK)
	public SampleCartDeleteSuccessDTO deleteSampleCart(
			@ApiParam(value = "CartStatusDTO", required = true) @RequestBody final CartStatusDTO cartStatusDto) {
		SampleCartDeleteSuccessDTO successDto=new SampleCartDeleteSuccessDTO();
		successDto.setSuccess(gpSampleCartDeleteStrategy.removeSampleCart(cartStatusDto.getCartId(), cartStatusDto.getStatus()));
		successDto.setMessage(String.format("Cart with id %s deleted successfully", cartStatusDto.getCartId()));
	    return successDto;
	  }


}
