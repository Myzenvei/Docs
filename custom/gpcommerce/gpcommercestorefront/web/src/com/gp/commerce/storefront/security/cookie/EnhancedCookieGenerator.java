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
package com.gp.commerce.storefront.security.cookie;


import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.CookieGenerator;

import com.sap.security.core.server.csi.util.URLEncoder;


/**
 * Enhanced {@link CookieGenerator} sets additionally header attribute {@value #HEADER_COOKIE}
 */
public class EnhancedCookieGenerator extends CookieGenerator
{
	private static final Logger LOG = Logger.getLogger(EnhancedCookieGenerator.class);

	public static final String HEADER_COOKIE = "Set-Cookie";
	public static final boolean DEFAULT_HTTP_ONLY = false;
	public static final boolean DEFAULT_COOKIE_PATH = true;
	public static final String SESSION_TIME_OUT = "default.session.timeout";

	private boolean useDefaultPath = DEFAULT_COOKIE_PATH;
	private boolean httpOnly = DEFAULT_HTTP_ONLY;

	private ConfigurationService configurationService;


	protected boolean isHttpOnly()
	{
		return httpOnly;
	}

	/**
	 * Marker to choose between only cookie based session and http header as addition
	 */
	public void setHttpOnly(final boolean httpOnly)
	{
		this.httpOnly = httpOnly;
	}

	protected boolean canUseDefaultPath()
	{
		return useDefaultPath;
	}

	/**
	 * Adjusts either dynamic {@link Cookie#setPath(String)} or static assignment. If true a cookie path is calculated by
	 * {@link #setEnhancedCookiePath(Cookie)} method.
	 */
	public void setUseDefaultPath(final boolean useDefaultPath)
	{
		this.useDefaultPath = useDefaultPath;
	}

	@Override
	public void addCookie(final HttpServletResponse response, final String cookieValue)
	{
		Integer cookieMaxAge = this.getCookieMaxAge();
		super.addCookie(new HttpServletResponseWrapper(response) // NOSONAR
		{
			@Override
			public void addCookie(final Cookie cookie)
			{
				if(LOG.isDebugEnabled()){
					LOG.debug("Cookie Name :: "+cookie.getName() +"    Cookie Value "+cookie.getValue());
				}
				try {
					cookie.setValue(URLEncoder.encode(cookie.getValue(), "UTF-8"));
					if(cookieMaxAge==null) {
						cookie.setMaxAge(getConfigurationService().getConfiguration().getInt(SESSION_TIME_OUT));
					}
				} catch (final UnsupportedEncodingException e) {
					LOG.error("Error while encoding the cookie value",e);
				}
				setEnhancedCookiePath(cookie);

				if (isHttpOnly())
				{
					// Custom code to write the cookie including the httpOnly flag
					// StringBuffer cannot be replaced by StringBuilder due to the type required by called function
					final StringBuffer headerBuffer = new StringBuffer(100); // NOSONAR
					ServerCookie.appendCookieValue(headerBuffer, cookie.getVersion(), cookie.getName(), cookie.getValue(),
							cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure(),
							true);
					response.addHeader(HEADER_COOKIE, headerBuffer.toString());
				}
				else
				{
					// Write the cookie as normal
					super.addCookie(cookie);
				}
			}
		}, cookieValue);
	}

	/**
	 * Sets dynamically the {@link Cookie#setPath(String)} value using available
	 * {@link HttpServletRequest#getContextPath()}.
	 */
	protected void setEnhancedCookiePath(final Cookie cookie)
	{
		if (!canUseDefaultPath())
		{
			final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			if (StringUtils.isNotBlank(request.getContextPath()))
			{
				cookie.setPath(request.getContextPath());
			}
		}
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
