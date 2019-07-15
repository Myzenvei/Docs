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
package com.gp.commerce.sso.utils;

import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.sso.RedirectionController;
import com.gp.commerce.sso.constants.GpsinglesignonConstants;


/**
 *
 */
public class DefaultSAMLUtil implements SAMLUtil
{
	private final static Logger LOGGER = Logger.getLogger(DefaultSAMLUtil.class);

	@Override
	public String getUserId(final SAMLCredential credential)
	{
		String userId = "";

		final String userIdAttributeKey = StringUtils.defaultIfEmpty(Config.getParameter(GpsinglesignonConstants.SSO_USERID_KEY),
				null);

		if (null != userIdAttributeKey)
		{
			userId = null != credential.getAttributeAsString(userIdAttributeKey) ? credential
					.getAttributeAsString(userIdAttributeKey) : "";
		}

		return userId;
	}

	@Override
	public String getUserName(final SAMLCredential credential)
	{
		String firstName = "";
		String lastName = "";
		String userName = "";

		final String firstNameAttributeKey = StringUtils.defaultIfEmpty(
				Config.getParameter(GpsinglesignonConstants.SSO_FIRSTNAME_KEY), null);

		final String lastNameAttributeKey = StringUtils.defaultIfEmpty(
				Config.getParameter(GpsinglesignonConstants.SSO_LASTNAME_KEY), null);

		if (null != firstNameAttributeKey)
		{

			firstName = null != credential.getAttributeAsString(firstNameAttributeKey) ? credential
					.getAttributeAsString(firstNameAttributeKey) : "";
		}

		if (null != lastNameAttributeKey)
		{
			lastName = null != credential.getAttributeAsString(lastNameAttributeKey) ? credential
					.getAttributeAsString(lastNameAttributeKey) : "";
		}

		userName = firstName + " " + lastName;

		return userName;
	}

	@Override
	public String getCustomAttribute(final SAMLCredential credential, final String attributeName)
	{
		String customerAttrValue = "";

		if (null != attributeName)
		{
			customerAttrValue = null != credential.getAttributeAsString(attributeName) ? credential
					.getAttributeAsString(attributeName) : "";
		}

		return customerAttrValue;
	}

	@Override
	public List<String> getCustomAttributes(final SAMLCredential credential, final String attributeName)
	{
		final List<String> groups = new ArrayList<>();

		if (null != attributeName)
		{
			final String[] samlGroups = credential.getAttributeAsStringArray(attributeName);
			if (ArrayUtils.isNotEmpty(samlGroups))
			{
				final List<String> values = Arrays.stream(samlGroups).map(s -> s.split(",")).flatMap(Arrays::stream)
						.collect(Collectors.toList());
				groups.addAll(values);
			}
		}
		return groups;
	}

	@Override
	public void setCookie(final HttpServletResponse response, final HttpServletRequest request,final String cookieName,final String cookieValue)
	{
		final Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(60 * 60 * 24);
		cookie.setPath(GpsinglesignonConstants.DEFAULT_COOKIE_PATH);
		cookie.setSecure(true);
		response.addCookie(cookie);
	}

	@Override
	public void expireCookies(final HttpServletResponse response, final HttpServletRequest request, final String soldTo) {
		final Cookie soldToCookie = WebUtils.getCookie(request, GpsinglesignonConstants.SOLD_TO);
		final Cookie sessionCookie = WebUtils.getCookie(request, GpsinglesignonConstants.SESSION_COOKIE);
		final Cookie relayCookie = WebUtils.getCookie(request, GpsinglesignonConstants.RELAY_ENABLED);
		final Cookie samlCookie = WebUtils.getCookie(request, GpsinglesignonConstants.SSO_DEFAULT_COOKIE_NAME);
		if(null != relayCookie) {
			LOGGER.debug("Removing relay cookie" + relayCookie);
			relayCookie.setPath(GpsinglesignonConstants.DEFAULT_COOKIE_PATH);	
			relayCookie.setMaxAge(0);
			response.addCookie(relayCookie);
		}
		if(null != samlCookie) {
			LOGGER.debug("Removing SAML cookie" + samlCookie);
			samlCookie.setPath(GpsinglesignonConstants.DEFAULT_COOKIE_PATH);	
			samlCookie.setMaxAge(0);
			response.addCookie(samlCookie);
		}
		LOGGER.debug("JSESSION ID Before " + sessionCookie + "SOLD TO Before " + soldToCookie);		
		if (null != soldToCookie && !StringUtils.equals(soldToCookie.getValue(), soldTo) && null != sessionCookie)
		{
			LOGGER.debug("JSESSION ID After " + sessionCookie.getValue() + "SOLD TO After " + soldToCookie.getValue());	
			sessionCookie.setPath("/gp");
			sessionCookie.setMaxAge(0);
			response.addCookie(sessionCookie);
			LOGGER.debug("Invalidating current session");	
		}

	}
}

