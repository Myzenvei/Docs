/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.b2b.storefront.util;


import de.hybris.platform.util.Config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

public class GPSsoUtils {

	private GPSsoUtils()
	{
	}

	public static final String SSO_COOKIE_NAME = "sso.gp.cookie.name";

	/**
	 * Erase SAML sso cookie from browser.
	 *
	 * @param response
	 */
	public static void eraseSamlCookie(final HttpServletResponse response)
	{
		final String cookieName = Config.getParameter(SSO_COOKIE_NAME);
		if (cookieName != null)
		{
			final Cookie cookie = new Cookie(cookieName, "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
		}
	}

	/**
	 * Get SAML sso cookie.
	 *
	 * @param request
	 * @return saml SSO cookie if persist
	 */
	public static Cookie getSamlCookie(final HttpServletRequest request)
	{
		final String cookieName = Config.getParameter(SSO_COOKIE_NAME);
		final Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			final Cookie[] var3 = cookies;
			final int var4 = cookies.length;

			for (int var5 = 0; var5 < var4; ++var5)
			{
				final Cookie cookie = var3[var5];
			}
		}
		return cookieName != null ? WebUtils.getCookie(request, cookieName) : null;
	}
}
