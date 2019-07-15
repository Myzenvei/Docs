/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.b2b.storefront.security;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;


//logout success handler to redirect to login page in case of userid updates like email update, social connect and disconnect.
public class GPUserUpdateStorefrontLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler{

	private GUIDCookieStrategy guidCookieStrategy;
	private List<String> restrictedPages;
	private SessionService sessionService;

	public GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	public List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


}
