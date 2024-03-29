/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gp.commerce.constants.GpssobackofficeConstants;
import com.gp.commerce.sso.GPBackOfficeSSOService;
import com.gp.commerce.utils.DefaultSAMLUtil;
import com.gp.commerce.utils.SAMLUtil;


/**
 *
 */
@Controller
@RequestMapping("/saml/**")
public class RedirectionController
{

	private final static Logger LOGGER = Logger.getLogger(RedirectionController.class);

	@Resource(name = "gpCustomBackofficeSSOService")
	private GPBackOfficeSSOService userService;

	private final SAMLUtil samlUil = new DefaultSAMLUtil();

	@RequestMapping(method = RequestMethod.GET)
	public String redirect(final HttpServletResponse response, final HttpServletRequest request)
	{

		final SAMLCredential credential = (SAMLCredential) SecurityContextHolder.getContext().getAuthentication().getCredentials();

		final Collection<String> roles = new ArrayList<String>();

		final UserModel user;

		String referenceURL = StringUtils.substringAfter(request.getServletPath(), "/saml/");

		if (!StringUtils.isEmpty(request.getQueryString()))
		{
			referenceURL += request.getQueryString().isEmpty() ? "" : "?" + request.getQueryString();
		}

		try
		{
			roles.addAll(samlUil.getCustomAttributes(credential,
					StringUtils.defaultIfEmpty(Config.getParameter(GpssobackofficeConstants.SSO_USERGROUP_KEY), "usergroup")));

			user = userService.getOrCreateSSOUser(samlUil.getUserId(credential).toLowerCase(), samlUil.getUserName(credential),
					roles);

			storeTokenFromSaml(response, user);

			//continue to the redirection and token will be generated only in case the user has valid group
			final String redirectURL = StringUtils.defaultIfEmpty(Config.getParameter(GpssobackofficeConstants.SSO_REDIRECT_URL),
					GpssobackofficeConstants.DEFAULT_REDIRECT_URL);

			return GpssobackofficeConstants.REDIRECT_PREFIX + redirectURL + referenceURL;

		}
		catch (final IllegalArgumentException e)
		{
			//the user is not belonging to any valid group
			LOGGER.error(e);
		}

		catch (final Exception e)
		{
			//something went wrong and we need to log that
			LOGGER.error(e);
		}

		return "/error";
	}

	public void storeTokenFromSaml(final HttpServletResponse response, final UserModel user)
	{
		try
		{

			final String cookiePath = StringUtils.defaultIfEmpty(Config.getParameter(GpssobackofficeConstants.SSO_COOKIE_PATH),
					GpssobackofficeConstants.DEFAULT_COOKIE_PATH);

			final String cookieMaxAgeStr = StringUtils.defaultIfEmpty(
					Config.getParameter(GpssobackofficeConstants.SSO_COOKIE_MAX_AGE),
					String.valueOf(GpssobackofficeConstants.DEFAULT_COOKIE_MAX_AGE));

			int cookieMaxAge;

			if (!NumberUtils.isNumber(cookieMaxAgeStr))
			{
				cookieMaxAge = GpssobackofficeConstants.DEFAULT_COOKIE_MAX_AGE;
			}
			else
			{
				cookieMaxAge = Integer.valueOf(cookieMaxAgeStr).intValue();
			}

			UserManager.getInstance().storeLoginTokenCookie(
					//
					StringUtils.defaultIfEmpty(Config.getParameter(GpssobackofficeConstants.SSO_COOKIE_NAME),
							GpssobackofficeConstants.SSO_DEFAULT_COOKIE_NAME), // cookie name
					user.getUid(), // user id
					"en", // language iso code
					null, // credentials to check later
					cookiePath, // cookie path
					StringUtils.defaultIfEmpty(Config.getParameter(GpssobackofficeConstants.SSO_COOKIE_DOMAIN),
							GpssobackofficeConstants.SSO_DEFAULT_COOKIE_DOMAIN), // cookie domain
					true, // secure cookie
					cookieMaxAge, // max age in seconds
					response);
		}
		catch (final EJBPasswordEncoderNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
}
