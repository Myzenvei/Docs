/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.storefront.security;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.acceleratorstorefrontcommons.security.StorefrontAuthenticationSuccessHandler;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.webservicescommons.oauth2.token.OAuthTokenService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;


/**
 * Default implementation of {@link AuthenticationSuccessHandler}
 */
public class GUIDAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private static final String BASE_SITE_ID = "baseSiteId";

	private static final String CART = "cart";

	private static final String CHECKOUT = "checkout";

	private static final String FLOW = "flow";
	
	private static final String SHARE_FAV = "/sharefavourite/?listName=";

	private static final Logger LOG = Logger.getLogger(GUIDAuthenticationSuccessHandler.class);

	private GUIDCookieStrategy guidCookieStrategy;
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	private AuthenticationSuccessHandler authenticationCheckoutSuccessHandler;
	private AuthenticationSuccessHandler authenticationCartSuccessHandler;
	private AuthenticationSuccessHandler authenticationShareListSuccessHandler;
	
	private String clientId;
	private String clientSecret;
	private TokenGranter tokenGranter;
	private OAuthTokenService oauthTokenService;

	protected static final String CLIENT_ID = "client_id";
	protected static final String CLIENT_SECRET = "client_secret";
	protected static final String GRANT_TYPE = "grant_type";
	protected static final String USERNAME = "username";
	protected static final String PASSWORD = "password";
	protected static final String BASIC_SCOPE = "basic";
	protected static final String CMM_ID = "cmm_id";
	
	private CookieGenerator oAuthCookieGenerator;
	private CookieGenerator refreshCookieGenerator;
	private CookieGenerator cmmIdCookieGenerator;
	private CustomerFacade customerFacade;
	
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
				LOG.info("in generate token method of the handler");
				final TokenRequest tokenRequest = new TokenRequest(getTokenRequestParams(request, authentication), getClientId(),
						Stream.of(BASIC_SCOPE).collect(Collectors.toSet()), PASSWORD);
				final OAuth2AccessToken oAuth2AccessToken = getTokenGranter().grant(PASSWORD, tokenRequest);
				setCookie(response,oAuth2AccessToken);
				if(null!=oAuth2AccessToken)
				{
				getOAuthCookieGenerator().addCookie(response, oAuth2AccessToken.getValue());
				getRefreshCookieGenerator().addCookie(response, oAuth2AccessToken.getRefreshToken().getValue());
				if(LOG.isDebugEnabled()){
					LOG.debug("generated successfully" + Optional.of(oAuth2AccessToken.getValue()));
				}
				}
			}
			catch (final RuntimeException exception)
			{
				LOG.error(exception);
			}
		}
		final HttpSession session = request.getSession(true);
		session.removeAttribute("subscribe");
		final Cookie subCartCookie = WebUtils.getCookie(request, "subscrCartId");
		if(null!=subCartCookie) {
			session.setAttribute("subscrCart", "true");
		}
		if (null != request.getCookies()) {
			Cookie cookieValue = Arrays.asList(request.getCookies()).stream()
					.filter(cookie -> FLOW.equalsIgnoreCase(cookie.getName())).findFirst().orElse(null);
			Cookie cookieValueForShare = Arrays.asList(request.getCookies()).stream()
					.filter(cookie -> "share".equalsIgnoreCase(cookie.getName())).findFirst().orElse(null);
			if (null != cookieValue && CHECKOUT.equalsIgnoreCase(cookieValue.getValue()) && null == subCartCookie ) {
				cookieValue.setMaxAge(0);
				cookieValue.setPath("/");
				response.addCookie(cookieValue);
				getAuthenticationCheckoutSuccessHandler().onAuthenticationSuccess(request, response, authentication);
			}else if (null != cookieValue && CART.equalsIgnoreCase(cookieValue.getValue())) {
				cookieValue.setMaxAge(0);
				cookieValue.setPath("/");
				response.addCookie(cookieValue);
				getAuthenticationCartSuccessHandler().onAuthenticationSuccess(request, response, authentication);
			}else if (null != cookieValueForShare && null != cookieValueForShare.getValue()) {
				StorefrontAuthenticationSuccessHandler auth = (StorefrontAuthenticationSuccessHandler) getAuthenticationShareListSuccessHandler();
				auth.setDefaultTargetUrl(SHARE_FAV+cookieValueForShare.getValue());
				cookieValueForShare.setMaxAge(0);
				cookieValueForShare.setPath("/");
				response.addCookie(cookieValueForShare);
				getAuthenticationShareListSuccessHandler().onAuthenticationSuccess(request, response, authentication);

			}else {
				getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
			}
		} else {
			getAuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);
		}
	
	}
	
	private void setCookie(HttpServletResponse response, OAuth2AccessToken oAuth2AccessToken) {
		if(null != oAuth2AccessToken) {
			final String headerBuffer = 
			"access_token="+oAuth2AccessToken.getValue()+"; " + "path=/;" + "token_type="+oAuth2AccessToken.getTokenType()+"; " + "refresh_token="+oAuth2AccessToken.getRefreshToken()+"; " +
			"expires_in="+oAuth2AccessToken.getExpiresIn()+"; " + "scope="+oAuth2AccessToken.getScope()+"; ";
			response.addHeader("Set-Cookie", headerBuffer);
		}
		
	}

	protected Map<String, String> getTokenRequestParams(final HttpServletRequest request, final Authentication authentication)
	{
		final Map<String, String> requestParameters = new HashMap<>();
		requestParameters.put(CLIENT_ID, getClientId());
		requestParameters.put(CLIENT_SECRET, getClientSecret());
		requestParameters.put(GRANT_TYPE, PASSWORD);
		if(USERNAME.equalsIgnoreCase(request.getParameter("j_username"))){
			requestParameters.put(USERNAME, authentication.getPrincipal().toString());
		}else {
			requestParameters.put(USERNAME, (String) request.getParameter("j_username"));
		}
		requestParameters.put(PASSWORD, (String) request.getParameter("j_password"));
		requestParameters.put(BASE_SITE_ID, request.getParameter(BASE_SITE_ID));
		return requestParameters;
	}

	private void setCMMIDCookie(HttpServletRequest request, HttpServletResponse response) 
	{
		final String customerId = getCustomerFacade().getCurrentCustomer().getCustomerId();
		getCmmIdCookieGenerator().addCookie(response, customerId);
		request.getSession().setAttribute(CMM_ID, customerId);		
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
	
	public void setOAuthCookieGenerator(CookieGenerator oAuthCookieGenerator) {
		this.oAuthCookieGenerator = oAuthCookieGenerator;
	}
	
	public void setRefreshCookieGenerator(CookieGenerator refreshCookieGenerator) {
		this.refreshCookieGenerator = refreshCookieGenerator;
	}
	public CookieGenerator getRefreshCookieGenerator() {
		return refreshCookieGenerator;
	}
	
	public AuthenticationSuccessHandler getAuthenticationCheckoutSuccessHandler() {
		return authenticationCheckoutSuccessHandler;
	}
	
	public void setAuthenticationCheckoutSuccessHandler(
			AuthenticationSuccessHandler authenticationCheckoutSuccessHandler) {
		this.authenticationCheckoutSuccessHandler = authenticationCheckoutSuccessHandler;
	}
	
	public AuthenticationSuccessHandler getAuthenticationCartSuccessHandler() {
		return authenticationCartSuccessHandler;
	}
	
	public void setAuthenticationCartSuccessHandler(AuthenticationSuccessHandler authenticationCartSuccessHandler) {
		this.authenticationCartSuccessHandler = authenticationCartSuccessHandler;
	}

	public AuthenticationSuccessHandler getAuthenticationShareListSuccessHandler() {
		return authenticationShareListSuccessHandler;
	}

	public void setAuthenticationShareListSuccessHandler(
			AuthenticationSuccessHandler authenticationShareListSuccessHandler) {
		this.authenticationShareListSuccessHandler = authenticationShareListSuccessHandler;
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
