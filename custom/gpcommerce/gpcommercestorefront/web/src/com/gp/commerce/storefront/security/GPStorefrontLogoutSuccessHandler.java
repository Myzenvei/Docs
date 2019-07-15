/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.storefront.security;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;


public class GPStorefrontLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{

	private static final Logger LOG = Logger.getLogger(GPStorefrontLogoutSuccessHandler.class);
	public static final String CLOSE_ACCOUNT_PARAM = "&closeAcc=true";
	public static final String LOGOUT_COOKIE = "logoutFlow";

	private GUIDCookieStrategy guidCookieStrategy;
	private GPUserUpdateStorefrontLogoutSuccessHandler userUpdateLogoutSuccessHandler;
	private List<String> restrictedPages;
	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;
	private SessionService sessionService;

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		boolean userUpdate = false;
		if (null != request.getCookies())
		{
			final Cookie cookieValue = Arrays.asList(request.getCookies()).stream()
					.filter(cookie -> LOGOUT_COOKIE.equalsIgnoreCase(cookie.getName())).findFirst().orElse(null);
			if (null != cookieValue)
			{
				userUpdate = true;
			}
		}
		try {
			removeCookies(request,response);
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("Error while deleting the cookies on logout"+e);
		}
		getGuidCookieStrategy().deleteCookie(request, response);
		getSessionService().removeAttribute(WebConstants.USER_CONSENTS);

		// Delegate to default redirect behavior
		super.setUseReferer(false);
		if (userUpdate)
		{
			getUserUpdateLogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
		}
		else
		{
			super.onLogoutSuccess(request, response, authentication);
		}
	}

	private void removeCookies(final HttpServletRequest request,final HttpServletResponse response) throws MalformedURLException, CMSItemNotFoundException {
		final String cookies = Config.getParameter("gp.commerce.cookies");
		for(final String cookieName:cookies.split(",")) {
			final Cookie cookie=new Cookie(cookieName, "");
			cookie.setSecure(true);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

	}

	@Override
	protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		String targetUrl = super.determineTargetUrl(request, response);

		for (final String restrictedPage : getRestrictedPages())
		{
			// When logging out from a restricted page, return user to homepage.
			if (targetUrl.contains(restrictedPage))
			{
				targetUrl = super.getDefaultTargetUrl();
			}
		}

		// For closing an account, we need to append the closeAcc query string to the target url to display the close account message in the homepage.
		if (StringUtils.isNotBlank(request.getParameter(WebConstants.CLOSE_ACCOUNT)))
		{
			targetUrl = targetUrl + CLOSE_ACCOUNT_PARAM;
		}
		return targetUrl;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public GPUserUpdateStorefrontLogoutSuccessHandler getUserUpdateLogoutSuccessHandler()
	{
		return userUpdateLogoutSuccessHandler;
	}

	public void setUserUpdateLogoutSuccessHandler(final GPUserUpdateStorefrontLogoutSuccessHandler userUpdateLogoutSuccessHandler)
	{
		this.userUpdateLogoutSuccessHandler = userUpdateLogoutSuccessHandler;
	}
}
