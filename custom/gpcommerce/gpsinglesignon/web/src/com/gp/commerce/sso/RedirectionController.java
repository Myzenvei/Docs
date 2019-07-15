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
package com.gp.commerce.sso;

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

import com.gp.commerce.sso.constants.GpsinglesignonConstants;
import com.gp.commerce.sso.utils.DefaultSAMLUtil;
import com.gp.commerce.sso.utils.SAMLUtil;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.util.Config;


/**
 * RedirectionController
 */
@Controller
@RequestMapping("/saml/**")
public class RedirectionController
{

	private final static Logger LOGGER = Logger.getLogger(RedirectionController.class);

	@Resource(name = "customSSOUserService")
	private GPSSOService userService;

	private final SAMLUtil samlUil = new DefaultSAMLUtil();

	@RequestMapping(method = RequestMethod.GET)
	public String redirect(final HttpServletResponse response, final HttpServletRequest request)
	{
		
		final SAMLCredential credential = (SAMLCredential) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		
		String soldTo = samlUil.getCustomAttribute(credential, GpsinglesignonConstants.SOLD_TO_URL);
		LOGGER.debug("SoldTo from SAML" + soldTo);		
	
		samlUil.expireCookies(response, request, soldTo);
		
		final Collection<String> roles = new ArrayList<String>();

		final UserModel user;

		String referenceURL = StringUtils.substringAfter(request.getServletPath(), "/saml/");

		if (!StringUtils.isEmpty(request.getQueryString()))
		{
			referenceURL += request.getQueryString().isEmpty() ? "" : "?" + request.getQueryString();
		}

		try
		{
			String userRole = samlUil.getCustomAttribute(credential, GpsinglesignonConstants.USER_ROLE_URL);
			
			LOGGER.debug("User role " + userRole);
			roles.add(GpsinglesignonConstants.ROLE);
			
			final String userId = credential.getNameID().getValue()
					.substring(credential.getNameID().getValue().lastIndexOf('\\') + 1);
			
			String approvalSampleStatus = samlUil.getCustomAttribute(credential, GpsinglesignonConstants.APP_SAMPLE_STATUS);
			LOGGER.debug("Approved sample status from SAML " + approvalSampleStatus);
			
			String email = samlUil.getCustomAttribute(credential, GpsinglesignonConstants.EMAIL);
			LOGGER.debug("Users Email " + email);
			
			user = userService.getOrCreateSSOUser(userId, samlUil.getUserName(credential), roles,
					soldTo, email, approvalSampleStatus.equalsIgnoreCase(Boolean.TRUE.toString()) ? Boolean.TRUE : Boolean.FALSE);
			
			// Setting the Soldto & role Cookie
			samlUil.setCookie(response, request, GpsinglesignonConstants.SOLD_TO, soldTo);
			samlUil.setCookie(response, request, GpsinglesignonConstants.USER_ID, userId);
			storeTokenFromSaml(response, user);

			final String redirectURL = StringUtils.defaultIfEmpty(Config.getParameter(GpsinglesignonConstants.SSO_REDIRECT_URL),
					GpsinglesignonConstants.DEFAULT_REDIRECT_URL);
			
			final String relayEnabled = Config.getParameter(GpsinglesignonConstants.SSO_RELAY_ENABLED);
			
			LOGGER.info("Relay state from SAML" + credential.getRelayState());		
			
			if (relayEnabled.equalsIgnoreCase(Boolean.TRUE.toString()) && credential.getRelayState() != null && !credential.getRelayState().isEmpty()) {
				samlUil.setCookie(response, request, GpsinglesignonConstants.RELAY_ENABLED, Boolean.TRUE.toString());
			}
			
			return GpsinglesignonConstants.REDIRECT_PREFIX + redirectURL + referenceURL;
		}
		catch (final IllegalArgumentException e)
		{
			LOGGER.error("GPXpress SSO error", e);
		}

		catch (final Exception e)
		{
			LOGGER.error("GPXpress SSO error", e);
		}

		return "/error";
	}

	public void storeTokenFromSaml(final HttpServletResponse response, final UserModel user)
	{
		try
		{

			final String cookiePath = StringUtils.defaultIfEmpty(Config.getParameter(GpsinglesignonConstants.SSO_COOKIE_PATH),
					GpsinglesignonConstants.DEFAULT_COOKIE_PATH);

			final String cookieMaxAgeStr = StringUtils.defaultIfEmpty(
					Config.getParameter(GpsinglesignonConstants.SSO_COOKIE_MAX_AGE),
					String.valueOf(GpsinglesignonConstants.DEFAULT_COOKIE_MAX_AGE));

			int cookieMaxAge;

			if (!NumberUtils.isNumber(cookieMaxAgeStr))
			{
				cookieMaxAge = GpsinglesignonConstants.DEFAULT_COOKIE_MAX_AGE;
			}
			else
			{
				cookieMaxAge = Integer.valueOf(cookieMaxAgeStr).intValue();
			}

			UserManager.getInstance().storeLoginTokenCookie(
					//
					StringUtils.defaultIfEmpty(Config.getParameter(GpsinglesignonConstants.SSO_COOKIE_NAME),
							GpsinglesignonConstants.SSO_DEFAULT_COOKIE_NAME), // cookie name
					user.getUid(), // user id
					"en", // language iso code
					null, // credentials to check later
					cookiePath, // cookie path
					StringUtils.defaultIfEmpty(Config.getParameter(GpsinglesignonConstants.SSO_COOKIE_DOMAIN),
							GpsinglesignonConstants.SSO_DEFAULT_COOKIE_DOMAIN), // cookie domain
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
