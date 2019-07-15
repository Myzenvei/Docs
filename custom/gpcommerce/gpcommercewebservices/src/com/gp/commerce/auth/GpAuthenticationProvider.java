/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.auth;

import java.util.Collections;
import java.util.Map;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gp.commerce.spring.security.GPUserDetails;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;
import de.hybris.platform.spring.security.CoreUserDetails;

import com.gpintegration.service.impl.GPDefaultCommerceKochAuthService;
import com.gpintegration.service.impl.GPDefaultCommerceSocialAccountService;

import de.hybris.platform.commercefacades.user.data.KochAuthData;
import de.hybris.platform.commercefacades.user.data.RegisterData;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPKochAuthException;
import com.gp.commerce.core.enums.GPLoginType;
import com.gp.commerce.core.services.GPUserService;

/**
 * @author vdannina
 *
 */
public class GpAuthenticationProvider extends CoreAuthenticationProvider{
	private static final String SYSTEM_NOT_INITIALIZED = "systemNotInitialized";
	private static final String NONE_PROVIDED = "NONE_PROVIDED";
	private static final String LOGIN_TYPE = "loginType";
	private static final String BASE_SITE_ID = "baseSiteId";
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String MAX_ALLOWED_LOGIN_ATTEMPTS = "gpcommercewebservices.user.max.allowed.login.attempts";
	private static final String BASESITE = BASE_SITE_ID;
	private static final String KOCH_ACCESS_TOKEN = "kochAuthAccessToken";
	private static final String KOCH_ID_TOKEN = "kochAuthIdToken";
	private static final String KOCH_REFRESH_TOKEN= "kochAuthRefreshToken";
	private static final String KOCH_AUTH_TS = "kochAuthTS";
	private static final String KOCH_EMAILID = "kochEmailId";
	
	private static final Logger LOG = Logger.getLogger(GpAuthenticationProvider.class);
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	
	@Resource(name = "defaultWsUserDetailsService")
	private UserDetailsService defaultWsUserDetailsService;
	
	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Resource(name = "userService")
	private GPUserService userService;

	private GPDefaultCommerceKochAuthService kochAuthService;
	
	private final UserDetailsChecker postAuthenticationChecks = new GpAuthenticationProvider.DefaultPostAuthenticationChecks(null);
	
	private GPDefaultCommerceSocialAccountService socialAccountService;
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		if (Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized()) {
			final Map<String, String> details = (Map<String, String>) authentication.getDetails();
			String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
			String userName = authentication.getPrincipal() == null ? NONE_PROVIDED : authentication.getName();
			if(details.get(BASESITE)!=null)
			{
				userName = authentication.getPrincipal() == null ? NONE_PROVIDED
					: authentication.getName() + uidDelimiter + details.get(BASE_SITE_ID);
			}
			int maxAllowedFailedLogin = Integer
					.parseInt(configurationService.getConfiguration().getString(MAX_ALLOWED_LOGIN_ATTEMPTS));
			UserDetails userDetails = null;
			if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(), details.get(LOGIN_TYPE)) || StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(), details.get(LOGIN_TYPE))) {
				RegisterData registerData=new RegisterData();
				registerData.setToken(details.get("token"));
				registerData.setLoginType(details.get(LOGIN_TYPE));
				registerData=socialAccountService.getRegisterData(registerData);
				userName=registerData.getLogin()+ uidDelimiter + details.get(BASE_SITE_ID);
			}
			try {
				userDetails = this.retrieveUser(userName.toLowerCase(userService.getCurrentLocale()));
			} catch (final UsernameNotFoundException arg5) {
				throw new BadCredentialsException(
						this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"), arg5);
			}

			this.getPreAuthenticationChecks().check(userDetails);
			final User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
			
			// Added code for Employee login 30 days check
			if (StringUtils.equalsIgnoreCase(GPLoginType.GPEMPLOYEE.getCode(), details.get(LOGIN_TYPE))) {
				if(!StringUtils.isEmpty(details.get(KOCH_ACCESS_TOKEN)) && !StringUtils.isEmpty(details.get(KOCH_ID_TOKEN)) && !StringUtils.isEmpty(details.get(KOCH_REFRESH_TOKEN)) && !StringUtils.isEmpty(details.get(KOCH_AUTH_TS)) && !StringUtils.isEmpty(details.get(KOCH_EMAILID))) {
					if(LOG.isDebugEnabled()){
						LOG.debug("Inside Koch Auth flow change check GPAuthenticationProvider");
					}
					updateKochAuthDetails((GPUserDetails) userDetails, details);
				}
				if (!isAuthTSExpired((GPUserDetails) userDetails, details.get(LOGIN_TYPE))) {
					if(LOG.isDebugEnabled()){
						LOG.debug("Inside the login check of more than 30 days");
					}
					throw new GPKochAuthException("AuthTS expired please authenticate the employee through Koch Auth");
				}
			}
			
			final Object credential = authentication.getCredentials();
			if (credential instanceof String) {
				if (!user.checkPassword((String) credential)) {
					if(details.get(BASESITE)!=null) {
					// increment FailedLoginCount and allow user to try login for maxAllowedFailedLogin times
					boolean retryLogin = this.incrementFailedLogins((GPUserDetails) userDetails, maxAllowedFailedLogin);
						if(!retryLogin)
							{
								throw new BadCredentialsException(
										this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
							}
						else
							{
								throw new BadCredentialsException(
							this.messages.getMessage("CoreAuthenticationProvider.locked", "Reset credentials"));
							}
					}
					else
					{
						throw new BadCredentialsException(
								this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
					}
				}
				if (details.get(BASESITE) != null) {
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
		} else {
			return this.createSuccessAuthentication(authentication, new CoreUserDetails(SYSTEM_NOT_INITIALIZED,
					SYSTEM_NOT_INITIALIZED, true, false, true, true, Collections.emptyList(), (String) null));
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
		{
			throw new BadCredentialsException(
					this.messages.getMessage("CoreAuthenticationProvider.locked", "Reset credentials"));
		}
	}
	
	/**
	 * This method is used to reset FailedLoginCount to zero and persist the data
	 * @param userDetails
	 */
	
	private void updateKochAuthDetails(GPUserDetails userDetails, Map<String, String> kochAuthTokenMap) {
		CustomerModel searchModel = new CustomerModel();
		searchModel.setUid(userDetails.getUsername());
		CustomerModel customerModel = flexibleSearchService.getModelByExample(searchModel);
		if(null != customerModel.getKochEmailId()) {
			if(customerModel.getKochEmailId().equalsIgnoreCase(kochAuthTokenMap.get(KOCH_EMAILID))) {
				if(LOG.isDebugEnabled()){
					LOG.debug("Inside the login check of registered and login same employee");
				}
				customerModel.setKochAuthAccessToken(kochAuthTokenMap.get(KOCH_ACCESS_TOKEN));
				customerModel.setKochAuthIdToken(kochAuthTokenMap.get(KOCH_ID_TOKEN));
				customerModel.setKochAuthRefreshToken(kochAuthTokenMap.get(KOCH_REFRESH_TOKEN));
				customerModel.setKochAuthTS(kochAuthTokenMap.get(KOCH_AUTH_TS));
				customerModel.setKochEmailId(kochAuthTokenMap.get(KOCH_EMAILID));
				modelService.save(customerModel);
			} else {
				LOG.error("Registered and login user are not same.");
				throw new GPKochAuthException("Registered and login user are not same.");
			}
		}
	}
	

	private boolean isAuthTSExpired(GPUserDetails userDetails, String loginType) {
		CustomerModel customerModel = new CustomerModel();
		customerModel.setUid(userDetails.getUsername());
		CustomerModel customerDetail = flexibleSearchService.getModelByExample(customerModel);
		String authTS = customerDetail.getKochAuthTS();
		Date authTSDate = null;
		if(null != authTS) {
			authTSDate = GpintegrationConstants.convertStringToDate(authTS);
		}
		Date currentDate = new Date();
		return currentDate.compareTo(authTSDate) < 0;
	}
	
	private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
		private DefaultPostAuthenticationChecks(final Object o) {
		}

		public void check(final UserDetails user) {
			if (!user.isCredentialsNonExpired()) {
				throw new CredentialsExpiredException(GpAuthenticationProvider.this.messages.getMessage(
						"CoreAuthenticationProvider.credentialsExpired", "User credentials have expired"));
			}
		}
	}

	public GPDefaultCommerceSocialAccountService getSocialAccountService() {
		return socialAccountService;
	}

	public void setSocialAccountService(GPDefaultCommerceSocialAccountService socialAccountService) {
		this.socialAccountService = socialAccountService;
	}
	
	public GPDefaultCommerceKochAuthService getKochAuthService() {
		return kochAuthService;
	}

	public void setKochAuthService(GPDefaultCommerceKochAuthService kochAuthService) {
		this.kochAuthService = kochAuthService;
	}
}