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
package com.gp.commerce.v2.controller;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;
import org.apache.commons.lang3.BooleanUtils;

import com.gp.commerce.core.exceptions.GPCommercePasswordException;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.swagger.ApiBaseSiteIdParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;


@Controller
@RequestMapping(value = "/{baseSiteId}/forgottenpasswordtokens")
@CacheControl(directive = CacheControlDirective.NO_STORE)
@Api(tags = "Forgotten Passwords")
public class ForgottenPasswordsController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(ForgottenPasswordsController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpCustomerFacade;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "userService")
	private GPUserService userService;

	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String ERROR_197 = "197";

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ApiOperation(value = "Generates a token to restore customer's forgotten password.", notes = "Generates a token to restore customer's forgotten password.", authorizations =
	{ @Authorization(value = "oauth2_client_credentials") })
	@ApiBaseSiteIdParam
	public void restorePassword(@PathVariable final String baseSiteId,
			@ApiParam(value = "Customer's user id. Customer user id is case insensitive.", required = true) @RequestParam final String userId)
	{
		final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("restorePassword: uid=" + sanitize(userId));
		}
		if(baseSiteId != null)
		{
			gpCustomerFacade.forgottenPassword(userId.toLowerCase(userService.getCurrentLocale())+uidDelimiter+baseSiteId);
		}
		else
		{
			gpCustomerFacade.forgottenPassword(userId.toLowerCase(userService.getCurrentLocale()));
		}
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "validates token from customer's forgotten password email and resets password", notes = "Gets the generated token to restore customer's reset password.", authorizations =
	{ @Authorization(value = "oauth2_client_credentials") })
	@ApiBaseSiteIdParam
	public UserWsDTO updatePassword(
			@ApiParam(value = "Customer's forgotpassword password token", required = true) @RequestParam(required = true) final String token,
			@ApiParam(value = "Customer's password.", required = true) @RequestParam final String password) throws TokenInvalidatedException

	{
		String user = null;
		final UserWsDTO dto = new UserWsDTO();
		String validtoken=validateToken(token);
		try
		{
			if (!StringUtils.isBlank(validtoken))
			{
				gpCustomerFacade.updatePassword(validtoken, password);
				user = gpCustomerFacade.getUserId(validtoken);

				dto.setUid(user);
			}

		}
		catch (final TokenInvalidatedException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			// This is a temporary fix. Need to be fixed once Exception Advisory class is built
			throw new TokenInvalidatedException(ERROR_197);
		}
		catch (final GPCommercePasswordException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			// This is a temporary fix. Need to be fixed once Exception Advisory class is built
			throw new GPCommercePasswordException("200","New Password cannot be equal to Old Password!");
		}
		return dto;
	}
	
	/**
	 * @param token
	 * @return
	 */
	private String validateToken(String token){
		if(!token.contains("/") || !token.contains("+") || !token.contains("=")) {
			try {
				token = URLDecoder.decode( token, "UTF-8" );
				return token;
			} catch (UnsupportedEncodingException e) {
				throw new SystemException(e.toString(), e);
			}
		}
		return token;
	}

}
