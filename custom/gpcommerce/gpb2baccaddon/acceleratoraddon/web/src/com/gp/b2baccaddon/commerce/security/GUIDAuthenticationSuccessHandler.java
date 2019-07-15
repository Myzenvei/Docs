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
package com.gp.b2baccaddon.commerce.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.b2b.storefront.util.GPAuthenticationToken;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthTokenService;

/**
 * Default implementation of {@link AuthenticationSuccessHandler}
 */
public class GUIDAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private static final Logger LOG = Logger.getLogger(GUIDAuthenticationSuccessHandler.class);
	public static final String REDIRECT_TO_CHECKOUT = "/checkout";
	private GUIDCookieStrategy guidCookieStrategy;
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	private String clientId;
	private String clientSecret;
	private TokenGranter tokenGranter;
	private OAuthTokenService oauthTokenService;
	private CookieGenerator oAuthCookieGenerator;
	private CookieGenerator refreshCookieGenerator;
	private ClientCredentialsTokenGranter clientTokenGranter;
	private CookieGenerator cmmIdCookieGenerator;
	private CustomerFacade customerFacade;
		
	protected static final String CLIENT_ID = "client_id";
	protected static final String CLIENT_SECRET = "client_secret";
	protected static final String GRANT_TYPE = "grant_type";
	protected static final String USERNAME = "username";
	protected static final String PASSWORD = "password";
	protected static final String BASIC_SCOPE = "basic";
	protected static final String CLIENT_CREDENTIALS = "client_credentials";
	protected static final String IS_SUBSCRIBLE = "isSubscribed=true";
	protected static final String CMM_ID = "cmm_id";

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		getGuidCookieStrategy().setCookie(request, response);
		setCMMIDCookie(request, response);
		if (!(authentication instanceof AnonymousAuthenticationToken))
		{
			try
			{
				final OAuth2AccessToken oAuth2AccessToken ;

				LOG.info("in generate token method of the handler");
				if (authentication instanceof GPAuthenticationToken)
				{
					final TokenRequest tokenRequest = new TokenRequest(getTokenRequestParams(request, authentication), "mobile_android",
							Stream.of(BASIC_SCOPE).collect(Collectors.toSet()), CLIENT_CREDENTIALS);
					oAuth2AccessToken = getClientTokenGranter().grant(CLIENT_CREDENTIALS, tokenRequest);
				}

				else {
				final TokenRequest tokenRequest = new TokenRequest(getTokenRequestParams(request, authentication), getClientId(),
				Stream.of(BASIC_SCOPE).collect(Collectors.toSet()), PASSWORD);

				oAuth2AccessToken = getTokenGranter().grant(PASSWORD, tokenRequest);
				}
                setCookie(response, oAuth2AccessToken);
				getOAuthCookieGenerator().addCookie(response, oAuth2AccessToken.getValue());
				getRefreshCookieGenerator().addCookie(response, oAuth2AccessToken.getRefreshToken().getValue());
				LOG.info("generated successfully" + Optional.of(oAuth2AccessToken.getValue()));
			}
			catch (final RuntimeException exception)
			{
				LOG.error("Exception occured in token generation", exception);
			}
		}
		final HttpSession session = request.getSession(true);
		session.removeAttribute("subscribe");
		final Cookie subCartCookie = WebUtils.getCookie(request, "subscrCartId");
		if(null!=subCartCookie)
		{
			session.setAttribute("subscrCart", "true");
		}
		getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
	}

	private void setCMMIDCookie(HttpServletRequest request, HttpServletResponse response) 
	{
		final String customerId = getCustomerFacade().getCurrentCustomer().getCustomerId();
		getCmmIdCookieGenerator().addCookie(response, customerId);
		request.getSession().setAttribute(CMM_ID, customerId);		
	}

	private void setCookie(final HttpServletResponse response, final OAuth2AccessToken oAuth2AccessToken)
	{
		if (null != oAuth2AccessToken)
		{
			final String headerBuffer = "access_token=" + oAuth2AccessToken.getValue() + "; " + "path=/;" + "token_type="
					+ oAuth2AccessToken.getTokenType() + "; " + "refresh_token=" + oAuth2AccessToken.getRefreshToken() + "; "
					+ "expires_in=" + oAuth2AccessToken.getExpiresIn() + "; " + "scope=" + oAuth2AccessToken.getScope() + "; ";
			response.addHeader("Set-Cookie", headerBuffer);
		}

	}

	protected Map<String, String> getTokenRequestParams(final HttpServletRequest request, final Authentication authentication)
	{
		final Map<String, String> requestParameters = new HashMap<>();
		if (authentication instanceof GPAuthenticationToken)
		{
		requestParameters.put(CLIENT_ID,"mobile_android");
		requestParameters.put(CLIENT_SECRET, "secret");
		requestParameters.put(GRANT_TYPE, "client_credentials");
		}
		else
		{
			requestParameters.put(CLIENT_ID, getClientId());
			requestParameters.put(CLIENT_SECRET, getClientSecret());
			requestParameters.put(GRANT_TYPE, PASSWORD);
			requestParameters.put(USERNAME, request.getParameter("j_username"));
			requestParameters.put(PASSWORD, request.getParameter("j_password"));
			requestParameters.put("baseSiteId", request.getParameter("baseSiteId"));
		}
		return requestParameters;
	}

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	/**
	 * @param guidCookieStrategy
	 *           the guidCookieStrategy to set
	 */
	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected AuthenticationSuccessHandler getAuthenticationSuccessHandler()
	{
		return authenticationSuccessHandler;
	}

	/**
	 * @param authenticationSuccessHandler
	 *           the authenticationSuccessHandler to set
	 */
	@Required
	public void setAuthenticationSuccessHandler(final AuthenticationSuccessHandler authenticationSuccessHandler)
	{
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	protected String getClientId()
	{
		return clientId;
	}

	@Required
	public void setClientId(final String clientId)
	{
		this.clientId = clientId;
	}

	protected String getClientSecret()
	{
		return clientSecret;
	}

	@Required
	public void setClientSecret(final String clientSecret)
	{
		this.clientSecret = clientSecret;
	}



	protected TokenGranter getTokenGranter()
	{
		return tokenGranter;
	}

	@Required
	public void setTokenGranter(final TokenGranter tokenGranter)
	{
		this.tokenGranter = tokenGranter;
	}


	protected OAuthTokenService getOauthTokenService()
	{
		return oauthTokenService;
	}

	@Required
	public void setOauthTokenService(final OAuthTokenService oauthTokenService)
	{
		this.oauthTokenService = oauthTokenService;
	}


	public CookieGenerator getOAuthCookieGenerator() {
		return oAuthCookieGenerator;
	}

	public void setOAuthCookieGenerator(final CookieGenerator oAuthCookieGenerator)
	{
		this.oAuthCookieGenerator = oAuthCookieGenerator;
	}

	public void setRefreshCookieGenerator(final CookieGenerator refreshCookieGenerator)
	{
		this.refreshCookieGenerator = refreshCookieGenerator;
	}
	public CookieGenerator getRefreshCookieGenerator() {
		return refreshCookieGenerator;
	}

	public ClientCredentialsTokenGranter getClientTokenGranter() {
		return clientTokenGranter;
	}

	@Required
	public void setClientTokenGranter(final ClientCredentialsTokenGranter clientTokenGranter) {
		this.clientTokenGranter = clientTokenGranter;
	}

	public CookieGenerator getCmmIdCookieGenerator() {
		return cmmIdCookieGenerator;
	}

	public void setCmmIdCookieGenerator(CookieGenerator cmmIdCookieGenerator) {
		this.cmmIdCookieGenerator = cmmIdCookieGenerator;
	}

	public CustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	public void setCustomerFacade(CustomerFacade customerFacade) {
		this.customerFacade = customerFacade;
	}
	
}
