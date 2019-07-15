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
package com.gp.commerce.v2.filter;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.gp.commerce.asm.AssistedServiceAgentLoginStrategy;
import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.facade.assistedservice.GPAssistedServiceFacade;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBlockedException;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;


/**
 * Filter that puts user from the requested url into the session.
 */
public class UserMatchingFilter extends AbstractUrlMatchingFilter
{
	private static final String ASAGENT = "asagent";
	private static final String JSESSIONID = "JSESSIONID";
	private static final String ACCESS_IS_DENIED = "Access is denied";
	private static final String UID = "UID";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_CUSTOMERGROUP = "ROLE_CUSTOMERGROUP";
	public static final String ROLE_CUSTOMERMANAGERGROUP = "ROLE_CUSTOMERMANAGERGROUP";

	public static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String CURRENT_USER = "current";
	private static final String ANONYMOUS_USER = "anonymous";
	private static final String ACTING_USER_UID = "ACTING_USER_UID";
	private static final Logger LOG = Logger.getLogger(UserMatchingFilter.class);
	
	public static final String ASM_AGENT_CUSTOMER_NAME ="asmAgentCustomerName";
	public static final String ASM_CUSTOMERID= "asmCustomerId";
	public static final String ASM_CARTID= "asmCartId";
	public static final String ROLE_ASAGENTGROUP = "ROLE_ASAGENTGROUP"; 
	public static final String ROLE_ASAGENTSALESGROUP ="ROLE_ASAGENTSALESGROUP";
	public static final String ROLE_ASAGENTSALESMANAGERGROUP ="ROLE_ASAGENTSALESMANAGERGROUP";
	public static final String AS_SAVED_ON_AGENTLOGIN_CUSTOMER = AssistedServiceFacade.class.getName() + "_saved_customer";
	public static final String MASTER_ASM_AGENT = "master.asm.agent.id";
	public static final String MASTER_ASM_AGENT_PWD = "master.asm.agent.password";
	private static final String USER_ID = "userId";
	
	private String regexp;
	private UserService userService;
	private SessionService sessionService;
	private GPAssistedServiceFacade gpAssistedServiceFacade;
	private AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy;
	private CartService cartService;
	private ConfigurationService configurationService;
	
	@Resource(name = "gpB2BUnitsService")
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		final Authentication auth = getAuth();
		final String userFromHeader = request.getHeader(USER_ID);
		if (hasRole(ROLE_CUSTOMERGROUP, auth) || hasRole(ROLE_CUSTOMERMANAGERGROUP,auth))
		{
			getSessionService().setAttribute(ACTING_USER_UID, auth.getPrincipal());
		}
		
		if(hasRole(ROLE_ASAGENTGROUP, auth) || hasRole(ROLE_ASAGENTSALESGROUP, auth) || hasRole(ROLE_ASAGENTSALESMANAGERGROUP, auth))
		{		
			LOG.debug("User has ASM Role"+userService.getCurrentUser().getUid());
			setCustomerForASM(request, response);
		}
		else
		{
			final String userID = getValue(request, regexp);
			if (userID == null)
			{
				if (hasRole(ROLE_CUSTOMERGROUP, auth) || hasRole(ROLE_CUSTOMERMANAGERGROUP, auth))
				{
					setCurrentUser((String) auth.getPrincipal());
				} else {
					checkAnonymousUser(userFromHeader);
				}
				
			}
			else if(userID.equals(CURRENT_USER) && null != userFromHeader && StringUtils.isNotBlank(userFromHeader))
			{
				setCurrentUser(userService.getUserForUID(userFromHeader));
			}
			else if (userID.equals(ANONYMOUS_USER))
			{
				setCurrentUser(userService.getAnonymousUser());
				getSessionService().setAttribute(ACTING_USER_UID, ANONYMOUS_USER);
			}
			else if (hasRole(ROLE_TRUSTED_CLIENT, auth) || hasRole(ROLE_CUSTOMERMANAGERGROUP, auth))
			{
				setCurrentUser(userID);
			}
			else if (hasRole(ROLE_CUSTOMERGROUP, auth))
			{
				if (userID.equals(CURRENT_USER) || userID.equals(auth.getPrincipal()))
				{
					setCurrentUser((String) auth.getPrincipal());
				}
				else
				{
					throw new AccessDeniedException(ACCESS_IS_DENIED);
				}
			}
			else
			{
				// could not match any authorized role
				throw new AccessDeniedException(ACCESS_IS_DENIED);
			}
		}


		final UserModel customerModel = userService.getCurrentUser();
		if (customerModel instanceof B2BCustomerModel)
		{
			final B2BCustomerModel customer = (B2BCustomerModel) customerModel;
			ThreadContext.put(UID, customer.getUid());
		}
		soldToIdCheck(request, customerModel);
		final String sessionId = retrieveJsessionIdFromRequest();
		ThreadContext.put(JSESSIONID, sessionId);
		
		filterChain.doFilter(request, response);
		
		LOG.debug("User after filter "+userService.getCurrentUser().getUid());
		try
		{
			ThreadContext.remove(UID);
			ThreadContext.remove(JSESSIONID);
		}
		catch (Exception ex)
		{
			LOG.error("Exception occured while removeing thread context parameters" + ex.getMessage(),ex);
		}
	}

	private void checkAnonymousUser(final String userFromHeader) {
		if (null != userFromHeader && StringUtils.isNotBlank(userFromHeader)) 
		{
			setCurrentUser(userService.getUserForUID(userFromHeader));
		}
		else 
		{
			// fallback to anonymous
			setCurrentUser(userService.getAnonymousUser());
		}
	}

	private void soldToIdCheck(HttpServletRequest request, UserModel customer) {
		if (customer instanceof B2BCustomerModel)
		{
			String soldTo = request.getHeader(GpcommerceCoreConstants.SOLD_TO_ID);
			if(null != baseSiteService.getCurrentBaseSite()  && GpcommerceCoreConstants.GPXPRESS.equalsIgnoreCase(baseSiteService.getCurrentBaseSite().getUid())){
				final String userFromHeader = request.getHeader(USER_ID);
				getSessionService().setAttribute(ACTING_USER_UID, null != userFromHeader ? userFromHeader : GpcommerceCoreConstants.EMPTY_STRING);
			}
			if(null != soldTo && !soldTo.isEmpty()) {
				List<B2BUnitModel> units = gpB2BUnitsService.getUnitNodes((B2BCustomerModel)customer);
				boolean isSoldToIdPresent = false;
				if(CollectionUtils.isNotEmpty(units)){
					for (B2BUnitModel unit : units) {
						
						if(unit.getUid().equalsIgnoreCase(soldTo))
						{
							isSoldToIdPresent = true;
							getSessionService().setAttribute(GpcommerceCoreConstants.SOLD_TO_ID, unit);
							getSessionService().setAttribute("soldToRootcategory", unit.getRootCategoryReference().getCode());
							getSessionService().setAttribute("soldToUnitId", unit.getUid());
							break;
						}
					} 
					if (!isSoldToIdPresent) {
						throw new AccessDeniedException("Cannot find soldToId  in the system");
					}
				} 
			}
		}
		if(null == getSessionService().getAttribute(GpcommerceCoreConstants.SOLD_TO_ID))
		{
			getSessionService().setAttribute("soldToRootcategory", "DEFAULT");
			getSessionService().setAttribute("soldToUnitId", "DEFAULT");
		}
	}

	private void setCustomerForASM(final HttpServletRequest request, final HttpServletResponse response) {
		String asmCustomerId = null;
		String asmCartId = null;
		String asmAgentCustomerName = null;
		
		final Cookie asmAgentCustomerNameCookie = WebUtils.getCookie(request, ASM_AGENT_CUSTOMER_NAME);
		final Cookie asmCustomerIdCookie = WebUtils.getCookie(request, ASM_CUSTOMERID);
		final Cookie asmCartIdCookie = WebUtils.getCookie(request, ASM_CARTID);
		if (asmAgentCustomerNameCookie != null)
		{
			asmAgentCustomerName = asmAgentCustomerNameCookie.getValue();
		}		
		if (asmCustomerIdCookie != null)
		{
			asmCustomerId = asmCustomerIdCookie.getValue();
		}
		if (asmCartIdCookie != null)
		{
			asmCartId = asmCartIdCookie.getValue();
		}
		if (asmCartId!=null && asmAgentCustomerName!= null) {
			try
			{
				LOG.debug("After ASM cookie check "+userService.getCurrentUser().getUid()+":Cookie values"+asmCustomerId+":"+asmCartId);
				asmCustomerId = StringUtils.isBlank(asmCustomerId)?"anonymous":asmCustomerId;
					
				getGpAssistedServiceFacade().loginAssistedServiceAgent(asmAgentCustomerName);
				getGpAssistedServiceFacade().emulateCustomer(asmCustomerId, asmCartId);
				UserModel currentUser = getUserService().getCurrentUser();
				if (!getGpAssistedServiceFacade().isGPAssistedServiceAgent(currentUser))
				{
					getSessionService().getCurrentSession().setAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER, currentUser);
				}
				getGpAssistedServiceFacade().emulateAfterLogin();
				final CartModel cart = getCartService().getSessionCart();
				if(null!=cart && null!=asmCartIdCookie)
				{
				asmCartIdCookie.setValue(cart.getCode());
				}
				
				if(LOG.isDebugEnabled()){
					LOG.debug("Before leaving ASM emualte "+userService.getCurrentUser().getUid());
				}
			}
			catch (final Exception e)
			{
				LOG.debug("Exception occured during ASM emualte", e);
			}
		}
	}

	protected Authentication getAuth()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected String getRegexp()
	{
		return regexp;
	}

	@Required
	public void setRegexp(final String regexp)
	{
		this.regexp = regexp;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
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


	public GPAssistedServiceFacade getGpAssistedServiceFacade() {
		return gpAssistedServiceFacade;
	}

	public void setGpAssistedServiceFacade(GPAssistedServiceFacade gpAssistedServiceFacade) {
		this.gpAssistedServiceFacade = gpAssistedServiceFacade;
	}

	protected boolean hasRole(final String role, final Authentication auth)
	{
		if (auth != null)
		{
			for (final GrantedAuthority ga : auth.getAuthorities())
			{
				if (ga.getAuthority().equals(role))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void setCurrentUser(final String uid)
	{
		try
		{
			final UserModel userModel = userService.getUserForUID(uid);
			userService.setCurrentUser(userModel);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.debug(ex.getMessage());
			throw ex;
		}
	}

	protected void setCurrentUser(final UserModel user)
	{
		userService.setCurrentUser(user);
	}
	
	private String retrieveJsessionIdFromRequest()
	{
		RequestAttributes requestAttributes = null;
		HttpServletRequest request = null;
		try
		{
			requestAttributes = RequestContextHolder.currentRequestAttributes();
			if (requestAttributes != null)
			{
				final ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
				request = attributes.getRequest();
				final Cookie[] cookies = request.getCookies();
				if (ArrayUtils.isNotEmpty(cookies))
				{
					for (final Cookie cookie : cookies)
					{
						if (cookie.getName().equalsIgnoreCase(JSESSIONID))
						{
							return cookie.getValue();
						}
					}
				}
				return new String();
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while retrieveJsessionIdFromRequest" + e.getMessage(),e);
		}
		return new String();
	}

	public AssistedServiceAgentLoginStrategy getAssistedServiceAgentLoginStrategy() {
		return assistedServiceAgentLoginStrategy;
	}

	public void setAssistedServiceAgentLoginStrategy(AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy) {
		this.assistedServiceAgentLoginStrategy = assistedServiceAgentLoginStrategy;
	}

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
