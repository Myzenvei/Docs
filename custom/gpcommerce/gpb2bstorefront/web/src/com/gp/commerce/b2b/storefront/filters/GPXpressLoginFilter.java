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

package com.gp.commerce.b2b.storefront.filters;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gp.commerce.b2b.storefront.util.GPAuthenticationToken;
import com.gp.commerce.b2b.storefront.util.GPSsoLoginStrategy;
import com.gp.commerce.b2b.storefront.util.GPSsoUtils;
import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;


public class GPXpressLoginFilter extends OncePerRequestFilter{

	private UserService userService;
	private GPSsoLoginStrategy gpSsoLoginStrategy;
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Resource(name = "gpB2BUnitsService")
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	private static final Logger LOG = Logger.getLogger(GPXpressLoginFilter.class);

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException
	{
		LOG.debug("Inside GPXpressLoginFilter");
		if (GPSsoUtils.getSamlCookie(request) != null && null != userService.getCurrentUser() && 
				userService.isAnonymousUser(userService.getCurrentUser()))
		{
			final LoginToken token = new CookieBasedLoginToken(GPSsoUtils.getSamlCookie(request));
			final String userId = token.getUser().getUid();
			LOG.info("SSO User Id " + userId);
			final UserModel userModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Object execute()
				{
					UserModel userModel;
					try
					{
						searchRestrictionService.disableSearchRestrictions();
						userModel = getUserService().getUserForUID(userId);
					}
					finally
					{
						searchRestrictionService.enableSearchRestrictions();
					}
					
					return userModel;
				}

			});
			userService.setCurrentUser(userModel);
			final GPAuthenticationToken gpAuthenticationToken = gpSsoLoginStrategy.login(userId, request, response);
			authenticationSuccessHandler.onAuthenticationSuccess(request, response, gpAuthenticationToken);
			B2BUnitModel unit = ((B2BCustomerModel)userModel).getDefaultB2BUnit();
			if(null != unit)
			{
				sessionService.setAttribute(GpcommerceCoreConstants.SOLD_TO_ID, unit);
				sessionService.setAttribute("soldToRootcategory", unit.getRootCategoryReference().getCode());
				sessionService.setAttribute("soldToUnitId", unit.getUid());

			}
		}
		filterChain.doFilter(request, response);
	}

	public GPSsoLoginStrategy getGpSsoLoginStrategy()
	{
		return gpSsoLoginStrategy;
	}

	@Required
	public void setGpSsoLoginStrategy(final GPSsoLoginStrategy gpSsoLoginStrategy)
	{
		this.gpSsoLoginStrategy = gpSsoLoginStrategy;
	}

	public AuthenticationSuccessHandler getAuthenticationSuccessHandler()
	{
		return authenticationSuccessHandler;
	}

	@Required
	public void setAuthenticationSuccessHandler(final AuthenticationSuccessHandler authenticationSuccessHandler)
	{
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
