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

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.hybris.platform.acceleratorstorefrontcommons.security.AbstractAcceleratorAuthenticationProvider;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.spring.security.CoreUserDetails;

/**
 * Derived authentication provider supporting additional authentication checks. See
 * {@link de.hybris.platform.spring.security.RejectUserPreAuthenticationChecks}.
 *
 * <ul>
 * <li>prevent login without password for users created via CSCockpit</li>
 * <li>prevent login as user in group admingroup</li>
 * <li>prevent login as user if not authorised for B2B</li>
 * </ul>
 *
 * any login as admin disables SearchRestrictions and therefore no page can be viewed correctly
 */
public class B2BAcceleratorAuthenticationProvider extends AbstractAcceleratorAuthenticationProvider
{
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String MAX_ALLOWED_LOGIN_ATTEMPTS = "gpcommercewebservices.user.max.allowed.login.attempts";
	private static final String BASESITE = "baseSiteId";
	private static final String ERROR_189="errorCode=189";
	private static final String ERROR_190="errorCode=190";
    private static final String ERROR_191="errorCode=191";
    private static final String ERROR_192="errorCode=192";
    private static final String ERROR_193="errorCode=193";
    private static final String ERROR_206="errorCode=206";
	public static final String MASTER_ASM_AGENT = "master.asm.agent.id";

    
	private B2BUserGroupProvider b2bUserGroupProvider;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	
	@Resource(name = "modelService")
	private ModelService modelService;
	private final UserDetailsChecker postAuthenticationChecks = new B2BAcceleratorAuthenticationProvider.DefaultPostAuthenticationChecks(
			null);
	
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		if (Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized()) {
			String siteId;
			if(authentication.getDetails() instanceof Map) {
				Map<String,String> detail = (Map<String,String>)authentication.getDetails();
				siteId = detail.get("baseSiteId");
			}else {
				final GPWebAuthenticationDetails details = (GPWebAuthenticationDetails) authentication.getDetails();
				siteId = details.getSiteId();
			}
			String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
			String userName = authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
			if(null != siteId)
				if (!userName.contains(uidDelimiter)) {
					userName = authentication.getPrincipal() == null ? "NONE_PROVIDED"
							: authentication.getName() + uidDelimiter + siteId;
				}
			int maxAllowedFailedLogin = Integer
					.parseInt(configurationService.getConfiguration().getString(MAX_ALLOWED_LOGIN_ATTEMPTS));
			UserDetails userDetails = null;
			
			try {
				userDetails = this.retrieveUser(userName.toLowerCase());
			} catch (final UsernameNotFoundException arg5) {
				throw new BadCredentialsException(
						this.messages.getMessage(ERROR_193));
			}

			this.getPreAuthenticationChecks().check(userDetails);
			final User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
			final Object credential = authentication.getCredentials();
			
			final String asmAgent = configurationService.getConfiguration().getString(MASTER_ASM_AGENT);
			if(user instanceof Employee )
			{
				EmployeeModel employee = new EmployeeModel();
				employee.setUid(userName);
				EmployeeModel existingUser = flexibleSearchService.getModelByExample(employee);
				if (!user.checkPassword((String) credential)) {
					throw new BadCredentialsException(
							this.messages.getMessage(ERROR_190));
				}
				JaloSession.getCurrentSession().setUser(user);
				return this.createSuccessAuthentication(authentication, userDetails);
				
			} else {
				CustomerModel customerValue = new CustomerModel();
				customerValue.setUid(userDetails.getUsername());
				CustomerModel existingUser = flexibleSearchService.getModelByExample(customerValue);
				if (existingUser instanceof B2BCustomerModel && !(((B2BCustomerModel) existingUser).getActive()))
				{
					throw new BadCredentialsException(this.messages.getMessage(ERROR_206));
				}
				if (existingUser instanceof B2BCustomerModel
						&& "L3".equalsIgnoreCase(((B2BCustomerModel) existingUser).getDefaultB2BUnit().getB2bUnitLevel())
						&& !((B2BCustomerModel) existingUser).getActive())
				{
					throw new BadCredentialsException(this.messages.getMessage(ERROR_189));
				}
			}
			
			if (credential instanceof String) {
				if (!user.checkPassword((String) credential)) {
					if(siteId!=null) {
					// increment FailedLoginCount and allow user to try login for maxAllowedFailedLogin times
					boolean retryLogin = this.incrementFailedLogins((GPUserDetails) userDetails, maxAllowedFailedLogin);
					if(!retryLogin)
						throw new BadCredentialsException(
								this.messages.getMessage(ERROR_190));
					else
						throw new BadCredentialsException(
							this.messages.getMessage(ERROR_191));
					}
					else
						throw new BadCredentialsException(
								this.messages.getMessage(ERROR_192));
				}
				if (siteId != null) {
					// used to reset the FailedLoginCount once user enters valid password
					this.resetFailedLogins((GPUserDetails) userDetails, maxAllowedFailedLogin);
				}
			} else {
				if (!(credential instanceof LoginToken)) {
					throw new BadCredentialsException(
							this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
				}

				if (!user.checkPassword((LoginToken) credential)) {
					throw new BadCredentialsException(
							this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
				}
			}

			this.additionalAuthenticationChecks(userDetails, (AbstractAuthenticationToken) authentication);
			this.postAuthenticationChecks.check(userDetails);
			JaloSession.getCurrentSession().setUser(user);
			return this.createSuccessAuthentication(authentication, userDetails);
		
		}else {
			return this.createSuccessAuthentication(authentication, new CoreUserDetails("systemNotInitialized",
					"systemNotInitialized", true, false, true, true, Collections.EMPTY_LIST, (String) null));
		}
	}
	
	/**
	 * This method is used to increment FailedLoginCount and persist the data
	 * @param userDetails , This has user details uid
	 * @param maxFailedLoginsAllowed, This has maxFailedLoginsAllowed which is from configurable through project.properties
	 * @return
	 */
	private boolean incrementFailedLogins(GPUserDetails userDetails, int maxFailedLoginsAllowed) {
		CustomerModel searchModel = new CustomerModel();
		searchModel.setUid(userDetails.getUsername());
		CustomerModel customerModel = flexibleSearchService.getModelByExample(searchModel);
		int failedLoginsToSet = userDetails.getFailedLoginAttempts() == null ? 1
				: (userDetails.getFailedLoginAttempts().intValue() + 1);
		customerModel.setFailedLoginAttempts(failedLoginsToSet);
		modelService.save(customerModel);
		return failedLoginsToSet >= maxFailedLoginsAllowed;
	}

	/**
	 * This method is used to reset FailedLoginCount to zero and persist the data
	 * @param userDetails
	 */
	private void resetFailedLogins(GPUserDetails userDetails, int maxAllowedFailedLogin) {
		CustomerModel searchModel = new CustomerModel();
		searchModel.setUid(userDetails.getUsername());
		CustomerModel customerModel = flexibleSearchService.getModelByExample(searchModel);
		int failedLogins = userDetails.getFailedLoginAttempts() == null ? 1
				: userDetails.getFailedLoginAttempts().intValue();
		if(failedLogins < maxAllowedFailedLogin)
		{
			customerModel.setFailedLoginAttempts(0);
			modelService.save(customerModel);
		}
		else
			throw new BadCredentialsException(
					this.messages.getMessage("CoreAuthenticationProvider.locked", "Reset credentials"));
	}
	
	
	private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
		private DefaultPostAuthenticationChecks(final Object o) {
		}

		public void check(final UserDetails user) {
			if (!user.isCredentialsNonExpired()) {
				throw new CredentialsExpiredException(B2BAcceleratorAuthenticationProvider.this.messages.getMessage(
						"CoreAuthenticationProvider.credentialsExpired", "User credentials have expired"));
			}
		}
	}
	
	/**
	 * @see de.hybris.platform.acceleratorstorefrontcommons.security.AbstractAcceleratorAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails,
	 *      org.springframework.security.authentication.AbstractAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(final UserDetails details, final AbstractAuthenticationToken authentication)
			throws AuthenticationException
	{
		super.additionalAuthenticationChecks(details, authentication);

		final UserModel userModel = getUserService().getUserForUID(StringUtils.lowerCase(details.getUsername()));
		final UserGroupModel b2bgroup = getUserService().getUserGroupForUID(B2BConstants.B2BGROUP);

		// check if the customer is B2B type
		if (!getUserService().isMemberOfGroup(userModel, b2bgroup))
		{
			throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER_BAD_CREDENTIALS, BAD_CREDENTIALS));
		}

		// check if the customer is authorized
		if (!getB2bUserGroupProvider().isUserAuthorized(details.getUsername()))
		{
			throw new InsufficientAuthenticationException(
					messages.getMessage("checkout.error.invalid.accountType", "You are not allowed to login"));
		}

		// check if the customer account is active
		if (!getB2bUserGroupProvider().isUserEnabled(details.getUsername()))
		{
			throw new DisabledException(
					"User " + details.getUsername() + " is disabled... " + messages.getMessage("text.company.manage.units.disabled"));
		}
	}

	protected B2BUserGroupProvider getB2bUserGroupProvider()
	{
		return b2bUserGroupProvider;
	}

	public void setB2bUserGroupProvider(final B2BUserGroupProvider b2bUserGroupProvider)
	{
		this.b2bUserGroupProvider = b2bUserGroupProvider;
	}
}
