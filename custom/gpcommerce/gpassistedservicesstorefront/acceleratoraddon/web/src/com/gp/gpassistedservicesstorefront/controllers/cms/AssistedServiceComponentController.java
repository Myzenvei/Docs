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
package com.gp.gpassistedservicesstorefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedserviceservices.utils.CustomerEmulationParams;

import com.gp.gpassistedservicesstorefront.constants.GpassistedservicesstorefrontConstants;
import com.gp.gpassistedservicesstorefront.redirect.AssistedServiceRedirectStrategy;
import com.gp.gpassistedservicesstorefront.security.AssistedServiceAgentAuthoritiesManager;
import com.gp.gpassistedservicesstorefront.security.impl.AssistedServiceAgentLoginStrategy;
import com.gp.gpassistedservicesstorefront.security.impl.AssistedServiceAgentLogoutStrategy;
import com.gp.gpassistedservicesstorefront.security.impl.AssistedServiceAuthenticationToken;
import com.gp.gpassistedservicesstorefront.util.SubscriptionFacadeReflectionWrapper;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.CookieGenerator;

import com.sap.security.core.server.csi.XSSEncoder;


@Controller
@RequestMapping(value = "/assisted-service")
public class AssistedServiceComponentController extends AbstractController
{
	private static final String ASSISTED_SERVICE_COMPONENT = "addon:/gpassistedservicesstorefront/cms/asm/assistedServiceComponent";

	private static final String ASM_MESSAGE_ATTRIBUTE = "asm_message";
	private static final String ASM_REDIRECT_URL_ATTRIBUTE = "redirect_url";
	private static final String ASM_ALERT_CLASS = "asm_alert_class";
	private static final String ENABLE_360_VIEW = "enable360View";
	private static final String OAUTH_CLIENT_ID = "oauth.client.id";
	private static final String OAUTH_CLIENT_SECRET = "oauth.client.secret";
	private static final String SESSION_CART_KEY = "cart";

	protected static final String USERNAME = "username";
	protected static final String PASSWORD = "password";
	protected static final String CLIENT_ID = "client_id";
	protected static final String CLIENT_SECRET = "client_secret";
	protected static final String GRANT_TYPE = "grant_type";
	protected static final String BASIC_SCOPE = "basic";
	private static final String OAUTH_COOKIE="oauth";
	
	private Map<String, Object> asmMap;
	
	private static final Logger LOG = Logger.getLogger(AssistedServiceComponentController.class);

	@Resource(name = "assistedServiceFacade")
	private AssistedServiceFacade assistedServiceFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "assistedServiceAgentLoginStrategy")
	private AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy;

	@Resource(name = "assistedServiceAgentLogoutStrategy")
	private AssistedServiceAgentLogoutStrategy assistedServiceAgentLogoutStrategy;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "subscriptionFacadeWrapper")
	private SubscriptionFacadeReflectionWrapper subscriptionFacadeWrapper;

	@Resource(name = "assistedServiceRedirectStrategy")
	private AssistedServiceRedirectStrategy assistedServiceRedirectStrategy;

	@Resource(name = "assistedServiceAgentAuthoritiesManager")
	private AssistedServiceAgentAuthoritiesManager authoritiesManager;
	
	@Resource(name = "asmOAuthCookieGenerator")
	private CookieGenerator asmOAuthCookieGenerator;
	
	@Resource(name = "asmRefreshCookieGenerator")
	private CookieGenerator asmRefreshCookieGenerator;

	@Resource(name = "asmAgentCustomerNameCookieGenerator")
	private CookieGenerator asmAgentCustomerNameCookieGenerator;
	
	@Resource(name = "asmCustomerIdCookieGenerator")
	private CookieGenerator asmCustomerIdCookieGenerator;
	
	@Resource(name = "asmCartIdCookieGenerator")
	private CookieGenerator asmCartIdCookieGenerator;
	
	@Resource(name = "asmAnonymousGuidCookieGenerator")
	private CookieGenerator asmAnonymousGuidCookieGenerator;
	
	@Resource(name = "asmAnonymousGuidUserCookieGenerator")
	private CookieGenerator asmAnonymousGuidUserCookieGenerator;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "gpResourceOwnerPasswordTokenGranter")
	private TokenGranter tokenGranter; 
	

	@RequestMapping(value = "/quit", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String quitAssistedServiceMode(final Model model, final HttpServletRequest request, final HttpServletResponse response)
	{
		if(assistedServiceFacade.isAssistedServiceAgentLoggedIn()){
			endEmulateCustomer(model, response);
		}
       assistedServiceFacade.quitAssistedServiceMode();
       assistedServiceAgentLogoutStrategy.logout(request);
       asmAgentCustomerNameCookieGenerator.removeCookie(response);
       asmCartIdCookieGenerator.removeCookie(response);
       asmAnonymousGuidCookieGenerator.removeCookie(response);
       asmCustomerIdCookieGenerator.removeCookie(response);
       model.addAttribute("closeAsm", "close");
       return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAssistedServiceAgent(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, @RequestParam("username") final String username,
			@RequestParam("password") final String password)
	{
		try
		{
			assistedServiceFacade.loginAssistedServiceAgent(username, password);
			assistedServiceAgentLoginStrategy.login(username, request, response);
			asmAgentCustomerNameCookieGenerator.addCookie(response, username);
			asmCartIdCookieGenerator.removeCookie(response);
			// asmAnonymousGuidCookieGenerator.removeCookie(response);
			asmCustomerIdCookieGenerator.removeCookie(response);

			setOAuthToken(request, response);
			CustomerEmulationParams savedEmulationParams = assistedServiceFacade.getAsmSession()
					.getSavedEmulationData();
			assistedServiceFacade.emulateAfterLogin();

			refreshSpringSecurityToken();
			setSessionTimeout();
			if (null != savedEmulationParams && null != savedEmulationParams.getUserId()) {
				asmCustomerIdCookieGenerator.addCookie(response, savedEmulationParams.getUserId());
			}
			if (null != savedEmulationParams && null != savedEmulationParams.getCartId()) {
				asmCartIdCookieGenerator.addCookie(response, savedEmulationParams.getCartId());
			} else if (null != savedEmulationParams) {
				asmCartIdCookieGenerator.addCookie(response, cartService.getSessionCart().getCode());
			}
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, assistedServiceRedirectStrategy.getRedirectPath());
			model.addAttribute("agentId", assistedServiceFacade.getAsmSession().getAgent().getUid());
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			model.addAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			model.addAttribute("username", this.encodeValue(username));
			LOG.debug("Error occured during ASM Login"+username, e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		   return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/logoutasm", method = RequestMethod.POST)
	public String logoutAssistedServiceAgent(final Model model, final HttpServletRequest request, final HttpServletResponse response)
	{
		endEmulateCustomer(model, response);
		try
		{
			assistedServiceFacade.logoutAssistedServiceAgent();
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessage());
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		model.addAttribute("customerReload", "reload");
		assistedServiceAgentLogoutStrategy.logout(request);
		asmAgentCustomerNameCookieGenerator.removeCookie(response);
		asmCartIdCookieGenerator.removeCookie(response);
		asmAnonymousGuidCookieGenerator.removeCookie(response);
		asmCustomerIdCookieGenerator.removeCookie(response);
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/personify-customer", method = RequestMethod.POST)
	public String emulateCustomer(final Model model, @RequestParam("customerId") final String customerId,
			@RequestParam("customerName") final String customerName, @RequestParam("cartId") final String cartId, final HttpServletRequest request,
			final HttpServletResponse response)
	{

		String redirectTo = emulateCustomer(model, customerId, customerName, cartId);
		CartModel cart = (CartModel) asmMap.get(SESSION_CART_KEY);
		if (StringUtils.isEmpty(customerId) && cartId != null && asmMap != null) {
			if (cart != null) {
				asmAnonymousGuidCookieGenerator.addCookie(response, cart.getGuid());
				asmAnonymousGuidUserCookieGenerator.addCookie(response,"anonymous");
			}
		}
		else if(!StringUtils.isEmpty(customerId) && cartId != null) {
			asmAnonymousGuidUserCookieGenerator.addCookie(response,"current");
		}
		asmCustomerIdCookieGenerator.addCookie(response, customerId);
		asmCartIdCookieGenerator.addCookie(response, cart.getCode());
		return redirectTo;
	}
	
	public String emulateCustomer(final Model model, @RequestParam("customerId") final String customerId,
			@RequestParam("customerName") final String customerName, @RequestParam("cartId") final String cartId)
	{
		try
		{
			assistedServiceFacade.emulateCustomer(customerId, cartId);
			refreshSpringSecurityToken();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, assistedServiceRedirectStrategy.getRedirectPath());
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			model.addAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			model.addAttribute("customerId", this.encodeValue(customerId));
			model.addAttribute("cartId", this.encodeValue(cartId));
			model.addAttribute("customerName", this.encodeValue(customerName));
			LOG.debug(e.getMessage(), e);
		}
		asmMap = assistedServiceFacade.getAssistedServiceSessionAttributes();
		model.addAllAttributes(asmMap);
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/emulate", method = RequestMethod.GET)
	public String emulateCustomerByLink(final RedirectAttributes redirectAttrs,
			@RequestParam(value = "customerId", required = false) final String customerId,
			@RequestParam(value = "cartId", required = false) final String cartId,
			@RequestParam(value = "orderId", required = false) final String orderId,
			@RequestParam(value = "fwd", required = false) final String fwd,
			@RequestParam(value = "enable360View", required = false) final boolean enable360View,
			final Model model, final HttpServletRequest request,
						final HttpServletResponse response)
	{
		try
		{
			// launch AS mode in case it has not been launched yet
			if (!assistedServiceFacade.isAssistedServiceModeLaunched())
			{
				LOG.debug("ASM launched after link-emulate request");
				assistedServiceFacade.launchAssistedServiceMode();
			}

			if (assistedServiceFacade.isAssistedServiceAgentLoggedIn())
			{
				assistedServiceFacade.stopEmulateCustomer();
				refreshSpringSecurityToken();
				LOG.debug(String.format("Previous emulation for customerId:[%s] has been stopped.",
						userService.getCurrentUser().getUid()));
			}

			//only set the flash attribute if this value is true and will only happen in case 360 view is initiated from customer list
			if (false != enable360View)
			{
				redirectAttrs.addFlashAttribute(ENABLE_360_VIEW, Boolean.valueOf(enable360View));
			}

			assistedServiceFacade.emulateCustomer(customerId, cartId, orderId);
			LOG.debug(String.format(
					"Link-emulate request successfuly started an emulation with parameters: customerId:[%s], cartId:[%s]", customerId,
					cartId));
			refreshSpringSecurityToken();
			asmMap = assistedServiceFacade.getAssistedServiceSessionAttributes();
			model.addAllAttributes(asmMap);
			CartModel cart = (CartModel) asmMap.get(SESSION_CART_KEY);
			asmCustomerIdCookieGenerator.addCookie(response, customerId);
			asmCartIdCookieGenerator.addCookie(response,  cart.getCode());
			if(!StringUtils.isEmpty(customerId) && cartId != null) {
				asmAnonymousGuidUserCookieGenerator.addCookie(response,"current");
				}
			
			model.addAttribute("agentId",assistedServiceFacade.getAsmSession().getAgent().getUid());
			return REDIRECT_PREFIX + assistedServiceRedirectStrategy.getRedirectPath();
		}
		catch (final AssistedServiceException e)
		{
			LOG.debug(e.getMessage(), e);
			redirectAttrs.addFlashAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			redirectAttrs.addFlashAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			redirectAttrs.addFlashAttribute("customerId", this.encodeValue(customerId));
			redirectAttrs.addFlashAttribute("customerName", this.encodeValue(customerId));
			redirectAttrs.addFlashAttribute("cartId", this.encodeValue(cartId));
			assistedServiceFacade.getAsmSession().setForwardUrl(fwd);
		}
		return REDIRECT_PREFIX + assistedServiceRedirectStrategy.getErrorRedirectPath();
	}

	@RequestMapping(value = "/create-account", method = RequestMethod.POST)
	public String createCustomer(final Model model, @RequestParam("customerId") final String customerId,
			@RequestParam("customerName") final String customerName)
	{
		String redirectTo = ASSISTED_SERVICE_COMPONENT;
		try
		{
			assistedServiceFacade.createCustomer(customerId, customerName);
			// customerId is lower cased when user registered using customer account service
			final String customerIdLowerCased = customerId.toLowerCase();
			final CartModel sessionCart = cartService.getSessionCart();
			if (sessionCart != null && userService.isAnonymousUser(sessionCart.getUser())
					&& CollectionUtils.isNotEmpty(sessionCart.getEntries()))
			{
				// if there's already an anonymous cart with entries - bind it to customer
				bindCart(customerIdLowerCased, null, model);
				redirectTo = emulateCustomer(model, customerIdLowerCased, null, cartService.getSessionCart().getCode());
			}
			else
			{
				redirectTo = emulateCustomer(model, customerIdLowerCased, null, null);
			}
			try
			{
				subscriptionFacadeWrapper.updateProfile(new HashMap<String, String>());
			}
			catch (final Exception ex)
			{
				LOG.error("Subscription profile updating failed", ex);
				throw new AssistedServiceException("Subscription profile updating failed", ex);
			}
		}
		catch (final AssistedServiceException e)
		{
			if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate"))
			{
				model.addAttribute(ASM_MESSAGE_ATTRIBUTE, "asm.createCustomer.duplicate.error");
				model.addAttribute(ASM_ALERT_CLASS, "ASM_alert_customer ASM_alert_create_new");
			}
			else
			{
				model.addAttribute(ASM_MESSAGE_ATTRIBUTE, "asm.createCustomer.error");
			}
			model.addAttribute("customerId", this.encodeValue(customerId));
			model.addAttribute("customerName", this.encodeValue(customerName + ", " + customerId));
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return redirectTo;
	}

	@RequestMapping(value = "/personify-stop", method = RequestMethod.POST)
	public String endEmulateCustomer(final Model model, final HttpServletResponse response)
	{
		authoritiesManager.restoreInitialAuthorities();
		assistedServiceFacade.stopEmulateCustomer();
		refreshSpringSecurityToken();
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, "/");
		asmCustomerIdCookieGenerator.removeCookie(response);
		asmCartIdCookieGenerator.removeCookie(response);
		//asmAnonymousGuidCookieGenerator.removeCookie(response);
		asmAnonymousGuidUserCookieGenerator.addCookie(response, "current");
		return ASSISTED_SERVICE_COMPONENT;
	}

    @RequestMapping(value = "/resetSession", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String resetSession(final Model model)
    {
        refreshSpringSecurityToken();
        model.addAttribute("customerReload", "reload");
        model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
        return ASSISTED_SERVICE_COMPONENT;
    }

	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	@ResponseBody
	public String autocomplete(@RequestParam("customerId") final String customerId,
			@RequestParam("callback") final String callback)
	{
		final StringBuilder autocompleteResult = new StringBuilder();
		try
		{
			final List<CustomerData> customers = assistedServiceFacade.getSuggestedCustomerList(customerId);

			// because of jsonp callback parameter - I have to construct JSON manually (
			autocompleteResult.append(this.encodeValue(callback)).append("([");
			if (CollectionUtils.isNotEmpty(customers))
			{
				for (final CustomerData customer : customers)
				{
					final CustomerModel customerModel = (CustomerModel) userService.getUserForUID(customer.getUid()); // TODO REFACTOR THAT
					try
					{
						autocompleteResult.append(getCustomerJSON(customerModel));
					}
					catch (final UnsupportedEncodingException e)
					{
						LOG.error("Error occured during encoding customer data: " + customer.getUid(), e);
					}
					final Collection<CartData> carts = assistedServiceFacade.getCartListForCustomer(customerModel);
					if (CollectionUtils.isNotEmpty(carts))
					{
						autocompleteResult.append(", carts:[");
						final Collection<CartData> cartsForCustomer = carts;
						for (final CartData cart : cartsForCustomer)
						{
							autocompleteResult.append("\"").append(cart.getCode()).append("\",");
						}
						autocompleteResult.deleteCharAt(autocompleteResult.length() - 1);
						autocompleteResult.append("]");
					}
					autocompleteResult.append("},");
				}
				autocompleteResult.deleteCharAt(autocompleteResult.length() - 1);
			}
			else
			{
				autocompleteResult.append("{label:\"No results found\", value: \"\" }");
			}
			autocompleteResult.append("])");
		}
		catch (final AssistedServiceException e)
		{
			// just do nothing and return empty string
			LOG.debug(e.getMessage(), e);
		}
		return autocompleteResult.toString();
	}

	protected String getCustomerJSON(final CustomerModel customer) throws UnsupportedEncodingException
	{
		final String cardNumber = customer.getDefaultPaymentInfo() instanceof CreditCardPaymentInfoModel
				? ((CreditCardPaymentInfoModel) customer.getDefaultPaymentInfo()).getNumber() : null;
		final String last4Digits = cardNumber == null ? "----"
				: cardNumber.subSequence(cardNumber.length() >= 4 ? cardNumber.length() - 4 : 0, cardNumber.length()).toString();
		final String formattedCreationDate = customer.getCreationtime() != null
				? new SimpleDateFormat("dd/MM/YYYY").format(customer.getCreationtime()) : "--/--/----";
		return String.format("{email:'%s',value:'%s',date:'%s',card:'%s'", XSSEncoder.encodeJavaScript(customer.getUid()),
				XSSEncoder.encodeJavaScript(customer.getName()), formattedCreationDate, last4Digits);
	}

	@RequestMapping(value = "/bind-cart", method = RequestMethod.POST)
	public String bindCart(@RequestParam(value = "customerId", required = false) final String customerId,
			@RequestParam(value = "cartId", required = false) final String cartId, final Model model)
	{
		try
		{
			assistedServiceFacade.bindCustomerToCart(customerId, cartId);
			refreshSpringSecurityToken();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, "/");
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessage());
			model.addAttribute("customerId", this.encodeValue(customerId));
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)
	public String addToCartEventHandler(final Model model)
	{
		try
		{
			// since cart isn't empty anymore - emulate mode should be on
			assistedServiceFacade.emulateCustomer(userService.getCurrentUser().getUid(), cartService.getSessionCart().getCode());
		}
		catch (final AssistedServiceException e)
		{
			LOG.debug(e.getMessage(), e);
			return null; // there will be 'page not found' response in case of exception
		}
		return refresh(model);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public String refresh(final Model model)
	{
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}


	protected void setSessionTimeout()
	{
		JaloSession.getCurrentSession().setTimeout(assistedServiceFacade.getAssistedServiceSessionTimeout());
		// since agent is logged in - change session timeout to the value from properties
		((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession()
				.setMaxInactiveInterval(assistedServiceFacade.getAssistedServiceSessionTimeout()); // in seconds
	}

	/**
	 * This method should be called after any facade method where user substitution may occur
	 */
	protected void refreshSpringSecurityToken()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AssistedServiceAuthenticationToken)
		{
			final UserModel currentUser = userService.getCurrentUser();
			if (currentUser == null || userService.isAnonymousUser(currentUser) || isASAgent(currentUser))
			{
				((AssistedServiceAuthenticationToken) authentication).setEmulating(false);
			}
			else
			{
				((AssistedServiceAuthenticationToken) authentication).setEmulating(true);
				authoritiesManager.addCustomerAuthoritiesToAgent(currentUser.getUid());
			}
		}
	}

	protected boolean isASAgent(final UserModel currentUser)
	{
		final Set<UserGroupModel> userGroups = userService.getAllUserGroupsForUser(currentUser);
		for (final UserGroupModel userGroup : userGroups)
		{
			if (GpassistedservicesstorefrontConstants.AS_AGENT_GROUP_UID.equals(userGroup.getUid()))
			{
				return true;
			}
		}
		return false;
	}

	protected String encodeValue(final String inputValue)
	{
		final String trimmedInputValue = StringUtils.isEmpty(inputValue) ? "" : inputValue.trim();

		try
		{
			return XSSEncoder.encodeHTML(trimmedInputValue);
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("Error occured during encoding the input value: " + inputValue, e);
		}
		return null;
	}
	
	protected void setOAuthToken(final HttpServletRequest request, final HttpServletResponse response)
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AssistedServiceAuthenticationToken)
		{
			try
			{
				LOG.info("in generate token method of the handler");
				final TokenRequest tokenRequest = new TokenRequest(getTokenRequestParams(request, authentication), getClientId(),
						Stream.of(BASIC_SCOPE).collect(Collectors.toSet()), PASSWORD);
				final OAuth2AccessToken oAuth2AccessToken = tokenGranter.grant(PASSWORD, tokenRequest);
				setCookie(response,oAuth2AccessToken);
				asmOAuthCookieGenerator.addCookie(response, oAuth2AccessToken.getValue());
				asmRefreshCookieGenerator.addCookie(response, oAuth2AccessToken.getRefreshToken().getValue());
				if(LOG.isDebugEnabled()){
					LOG.debug("generated successfully" + Optional.of(oAuth2AccessToken.getValue()));
				};
			}
			catch (final RuntimeException exception)
			{
				LOG.error("Exception occured in setOAuthToken", exception);
			}
		}
	} 
	
	protected Map<String, String> getTokenRequestParams(final HttpServletRequest request, final Authentication authentication)
	{
		final Map<String, String> requestParameters = new HashMap<>();
		requestParameters.put(CLIENT_ID, getClientId());
		requestParameters.put(CLIENT_SECRET, configurationService.getConfiguration().getString(OAUTH_CLIENT_SECRET));
		requestParameters.put(GRANT_TYPE, PASSWORD);
		if("username".equalsIgnoreCase(request.getParameter("username"))){
			requestParameters.put(USERNAME, authentication.getPrincipal().toString());
		}else {
			requestParameters.put(USERNAME, (String) request.getParameter("username"));
		}
		requestParameters.put(PASSWORD, (String) request.getParameter("password"));
		// requestParameters.put("baseSiteId", request.getParameter("baseSiteId")); //OOTB
		//requestParameters.put("baseSiteId", customerId.substring(customerId.indexOf("|")));
		return requestParameters;
	}
	
	private void setCookie(HttpServletResponse response, OAuth2AccessToken oAuth2AccessToken) {
		if(null != oAuth2AccessToken) {
			final StringBuffer headerBuffer = new StringBuffer();
			headerBuffer.append("access_token="+oAuth2AccessToken.getValue()+"; ").append("path=/;").append("token_type="+oAuth2AccessToken.getTokenType()+"; ").append("refresh_token="+oAuth2AccessToken.getRefreshToken()+"; ")
			.append("expires_in="+oAuth2AccessToken.getExpiresIn()+"; ").append("scope="+oAuth2AccessToken.getScope()+"; ");
			response.addHeader("Set-Cookie", headerBuffer.toString());
		}
		
	}
	
	protected String getClientId()
	{
		return configurationService.getConfiguration().getString(OAUTH_CLIENT_ID);
	}

}
