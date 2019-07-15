/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.b2b.storefront.filters;

import de.hybris.platform.cms2.misc.UrlUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.impl.DefaultConfigurationService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


public class FrameFilter extends GenericFilterBean
{

	private static final String IFRAME_ENABLED_SITES = "iFrameEnabledSitesHostURL";
	private static final String URL_STRIP_CHARACTER = "/";
	private static final String X_FRAME_OPTIONS = "X-Frame-Options";
	private static final String CSV_DELIMIITER = ",";

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException
	{
		filterChain.doFilter(servletRequest, new HttpServletResponseWrapper((HttpServletResponse) servletResponse)
		{
			@Override
			public void setHeader(final String name, final String value)
			{
				final HttpServletRequest request = ((HttpServletRequest) servletRequest);
				String hostUrl = UrlUtils.extractHostInformationFromRequest(request);
				hostUrl = StringUtils.strip(hostUrl, URL_STRIP_CHARACTER);

				final String iFrameEnabledSitesHostValue = Registry.getApplicationContext().getBean(DefaultConfigurationService.class)
						.getConfiguration().getString(IFRAME_ENABLED_SITES);
				final String[] IframeEnabledSiteHostURLs = iFrameEnabledSitesHostValue.split(CSV_DELIMIITER);

				for (String iFrameEnabledSitesHost : IframeEnabledSiteHostURLs)
				{
					iFrameEnabledSitesHost = StringUtils.strip(iFrameEnabledSitesHost, URL_STRIP_CHARACTER);
					if (hostUrl.equalsIgnoreCase(iFrameEnabledSitesHost))
					{
						if (!name.equalsIgnoreCase(X_FRAME_OPTIONS))
						{
							super.setHeader(name, value);
						}
					}

				}


			}
		});
	}
}
