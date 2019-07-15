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
package com.hybris.ymkt.gpsapymktexpressway.clickstream.listners;

import de.hybris.platform.site.BaseSiteService;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import com.hybris.ymkt.common.user.UserContextService;
import com.hybris.ymkt.gpsapymktexpressway.clickstream.services.GPClickStreamService;


public class GPLoginEventListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent>
{
	private static final Logger LOG = LoggerFactory.getLogger(GPLoginEventListener.class);

	private static final String SSOID = "gpnet\\";

	protected BaseSiteService baseSiteService;
	protected GPClickStreamService clickStreamService;
	protected UserContextService userContextService;

	public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent event)
	{
		if (event.getSource() instanceof UsernamePasswordAuthenticationToken)
		{
			final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) event
					.getSource();
			if (usernamePasswordAuthenticationToken.getPrincipal().toString().toLowerCase().contains(SSOID))
			{
				return;
			}

		}



		if (baseSiteService.getCurrentBaseSite() == null)
		{
			return;
		}

		if (userContextService.isIncognitoUser())
		{
			return;
		}

		final String anonymousUserId = userContextService.getAnonymousUserId();
		final String anonymousUserOrigin = userContextService.getAnonymousUserOrigin();
		final String loggedInUserId = userContextService.getUserId();
		final String loggedInUserOrigin = userContextService.getLoggedInUserOrigin();

		LOG.debug("anonymousUserId={} anonymousUserOrigin={} loggedInUserId={} loggedInUserOrigin={}", anonymousUserId,
				anonymousUserOrigin, loggedInUserId, loggedInUserOrigin);

		clickStreamService.linkAnonymousAndLoggedInUsers(anonymousUserId, anonymousUserOrigin, loggedInUserId, loggedInUserOrigin);
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = Objects.requireNonNull(baseSiteService);
	}

	@Required
	public void setClickStreamService(final GPClickStreamService clickStreamService)
	{
		this.clickStreamService = Objects.requireNonNull(clickStreamService);
	}

	@Required
	public void setUserContextService(final UserContextService userContextService)
	{
		this.userContextService = Objects.requireNonNull(userContextService);
	}

}