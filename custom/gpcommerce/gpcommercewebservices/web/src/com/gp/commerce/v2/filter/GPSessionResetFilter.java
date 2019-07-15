/*
/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.v2.filter;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;



/**
 * Filter restores the session before login cookies expire
 */
public class GPSessionResetFilter extends AbstractUrlMatchingFilter
{
	private static final Logger LOG = Logger.getLogger(GPSessionResetFilter.class);
	private static final String SESSION_TIME_OUT = "default.session.timeout";
	private static final String GPX_SESSION_TIME_OUT = "gpx.cookie.max.age";
	private static final String PATH = "/";

	private ConfigurationService configurationService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService; 

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		LOG.debug("Inside Webservices GPSessionResetFilter");
		BaseSiteModel site = cmsSiteService.getCurrentSite();
		boolean isGPXSite = null != site && null != site.getUid() && GpcommerceFacadesConstants.GPXPRESS.equalsIgnoreCase(site.getUid()) ? Boolean.TRUE : Boolean.FALSE;

		final Cookie secureCookie = WebUtils.getCookie(request,
						GpcommerceFacadesConstants.ACC_SECURE_GUID_COOKIE);
		final Cookie accessCookie = WebUtils.getCookie(request, GpcommerceFacadesConstants.ACCESS_TOKEN_COOKIE);
		final Cookie refreshCookie = WebUtils.getCookie(request, GpcommerceFacadesConstants.REFRESH_TOKEN_COOKIE);
		if (secureCookie != null && accessCookie != null)
		{
			if (isGPXSite) {
				secureCookie.setMaxAge(getConfigurationService().getConfiguration().getInt(GPX_SESSION_TIME_OUT));
				accessCookie.setMaxAge(getConfigurationService().getConfiguration().getInt(GPX_SESSION_TIME_OUT));
			} 
			else {
				secureCookie.setMaxAge(getConfigurationService().getConfiguration().getInt(SESSION_TIME_OUT));
				accessCookie.setMaxAge(getConfigurationService().getConfiguration().getInt(SESSION_TIME_OUT));
			}	
			secureCookie.setPath(PATH);
			secureCookie.setSecure(true);
			secureCookie.setHttpOnly(true);
			accessCookie.setPath(PATH);
			accessCookie.setSecure(true);
			response.addCookie(secureCookie);
			response.addCookie(accessCookie);
			if (refreshCookie != null)
			{
				if (isGPXSite) {
					refreshCookie.setMaxAge(getConfigurationService().getConfiguration().getInt(GPX_SESSION_TIME_OUT));
				} else {
					refreshCookie.setMaxAge(getConfigurationService().getConfiguration().getInt(SESSION_TIME_OUT));
				}
				refreshCookie.setPath(PATH);
				refreshCookie.setSecure(true);
				response.addCookie(refreshCookie);
			}
		}
		filterChain.doFilter(request, response);
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}