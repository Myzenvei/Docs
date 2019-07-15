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
package com.gp.commerce.storefront.security;

import de.hybris.platform.acceleratorstorefrontcommons.security.AbstractAcceleratorAuthenticationProvider;
import de.hybris.platform.commercefacades.user.data.KochAuthData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.core.Constants;
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
import de.hybris.platform.spring.security.CoreUserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gp.commerce.spring.security.GPUserDetails;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPKochAuthException;
import com.gpintegration.service.impl.GPDefaultCommerceKochAuthService;
import com.gpintegration.service.impl.GPDefaultCommerceSocialAccountService;
import com.gp.commerce.storefront.filters.GPWebAuthenticationDetails;
import com.gp.commerce.core.enums.GPLoginType;


/**
 * Derived authentication provider supporting additional authentication checks. See
 * {@link de.hybris.platform.spring.security.RejectUserPreAuthenticationChecks}.
 *
 * <ul>
 * <li>prevent login without password for users created via CSCockpit</li>
 * <li>prevent login as user in group admingroup</li>
 * </ul>
 *
 * any login as admin disables SearchRestrictions and therefore no page can be viewed correctly
 */
public class AcceleratorAuthenticationProvider extends AbstractAcceleratorAuthenticationProvider
{
	private static final String NONE_PROVIDED = "NONE_PROVIDED";
	private static final String SYSTEM_NOT_INITIALIZED = "systemNotInitialized";
	private static final String ROLE_ADMIN_GROUP = "ROLE_" + Constants.USER.ADMIN_USERGROUP.toUpperCase();
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String MAX_ALLOWED_LOGIN_ATTEMPTS = "gpcommercewebservices.user.max.allowed.login.attempts";
	private static final String BASESITE = "baseSiteId";
	private static final String LOGINTYPE = "loginType";
	private static final String TOKEN = "token";
	private static final String KOCH_ACCESS_TOKEN = "kochAuthAccessToken";
	private static final String KOCH_ID_TOKEN = "kochAuthIdToken";
	private static final String KOCH_REFRESH_TOKEN= "kochAuthRefreshToken";
	private static final String KOCH_AUTH_TS = "kochAuthTS";
	private static final String KOCH_EMAILID ="kochEmailId";
	private static final String ERROR_190="errorCode=190";
	private static final String ERROR_191="errorCode=191";
	private static final String ERROR_192="errorCode=192";
	private static final String ERROR_193="errorCode=193";
	private static final String ERROR_194="errorCode=194";
	private static final String ERROR_199="errorCode=199";
    private static final Logger LOG = Logger.getLogger(AcceleratorAuthenticationProvider.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	
	@Resource(name = "defaultWsUserDetailsService")
	private UserDetailsService defaultWsUserDetailsService;
	
	@Resource(name = "modelService")
	private ModelService modelService;

	private GPDefaultCommerceKochAuthService kochAuthService;
	
	private GPDefaultCommerceSocialAccountService socialAccountService;
	
	private GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ROLE_ADMIN_GROUP);
	
	private final UserDetailsChecker postAuthenticationChecks = new AcceleratorAuthenticationProvider.DefaultPostAuthenticationChecks(null);

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		if (Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized()) {
			String siteId;
			String loginType;
			String token;
			String kochAuthAccessToken;
			String kochAuthIdToken;
			String kochAuthRefreshToken;
			String kochAuthTS;
			String kochEmailId;
			
			if(authentication.getDetails() instanceof Map) {
				Map<String,String> detail = (Map<String,String>)authentication.getDetails();
				siteId = detail.get(BASESITE);
				loginType = detail.get(LOGINTYPE);
				token = detail.get(TOKEN);
				kochAuthAccessToken = detail.get(KOCH_ACCESS_TOKEN);
				kochAuthIdToken = detail.get(KOCH_ID_TOKEN);
				kochAuthRefreshToken = detail.get(KOCH_REFRESH_TOKEN);
				kochAuthTS = detail.get(KOCH_AUTH_TS);
				kochEmailId = detail.get(KOCH_EMAILID);
			}else {
				final GPWebAuthenticationDetails details = (GPWebAuthenticationDetails) authentication.getDetails();
				siteId = details.getSiteId();
				loginType = details.getLoginType();
				token = details.getToken();
				kochAuthAccessToken = details.getKochAuthAccessToken();
				kochAuthIdToken = details.getKochAuthIdToken();
				kochAuthRefreshToken = details.getKochAuthRefreshToken();
				kochAuthTS = details.getKochAuthTS();
				kochEmailId = details.getKochEmailId();
			}
			String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
			String userName = authentication.getPrincipal() == null ? NONE_PROVIDED : authentication.getName();
			if(null != siteId && !userName.contains(uidDelimiter)){
					userName = authentication.getPrincipal() == null ? NONE_PROVIDED
							: authentication.getName() + uidDelimiter + siteId;
			}
			int maxAllowedFailedLogin = Integer
					.parseInt(configurationService.getConfiguration().getString(MAX_ALLOWED_LOGIN_ATTEMPTS));
			UserDetails userDetails = null;
			if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(), loginType) || StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(), loginType)) {
				RegisterData registerData=new RegisterData();
				registerData.setToken(token);
				registerData.setLoginType(loginType);
				registerData=socialAccountService.getRegisterData(registerData);
				userName=registerData.getLogin()+ uidDelimiter + siteId;
			}
			try {
				userDetails = this.retrieveGPUser(userName.toLowerCase());
			} catch (final UsernameNotFoundException arg5) {
				throw new BadCredentialsException(
						this.messages.getMessage(ERROR_193),arg5);
			}

			this.getPreAuthenticationChecks().check(userDetails);
			final User user = UserManager.getInstance().getUserByLogin(userDetails.getUsername());
			
			// Added code for Employee login 30 days check
			if (StringUtils.equalsIgnoreCase(GPLoginType.GPEMPLOYEE.getCode(), loginType)) {
				
				if(!StringUtils.isEmpty(kochAuthAccessToken) && !StringUtils.isEmpty(kochAuthIdToken) && !StringUtils.isEmpty(kochAuthRefreshToken) && !StringUtils.isEmpty(kochAuthTS) && !StringUtils.isEmpty(kochEmailId)) {
					KochAuthData kochAuthData = new KochAuthData();
					kochAuthData.setAccessToken(kochAuthAccessToken);
					kochAuthData.setIdToken(kochAuthIdToken);
					kochAuthData.setRefreshToken(kochAuthRefreshToken);
					kochAuthData.setModifiedTimeStamp(kochAuthTS);
					kochAuthData.setKochEmailId(kochEmailId);
					updateKochAuthDetails((GPUserDetails) userDetails, kochAuthData);
				}
				if (!isAuthTSExpired((GPUserDetails) userDetails, loginType)) {
					throw new BadCredentialsException(
							this.messages.getMessage(ERROR_194));
				}
			}
			
			final Object credential = authentication.getCredentials();
			if (credential instanceof String) {
				if (!user.checkPassword((String) credential)) {
					if(siteId!=null) {
					// increment FailedLoginCount and allow user to try login for maxAllowedFailedLogin times
					boolean retryLogin = this.incrementFailedLogins((GPUserDetails) userDetails, maxAllowedFailedLogin);
					if(!retryLogin){
						throw new BadCredentialsException(
								this.messages.getMessage(ERROR_190));
					}
					else{
						throw new BadCredentialsException(
							this.messages.getMessage(ERROR_191));
					}
					}
					else{
						throw new BadCredentialsException(
								this.messages.getMessage(ERROR_192));
					}
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
			try {
				JaloSession.getCurrentSession().setUser(user);
			} catch(Exception e) {
				LOG.debug("Authorization issue in in setting user", e);
			}
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
		else{
			throw new BadCredentialsException(
					this.messages.getMessage("CoreAuthenticationProvider.locked", "Reset credentials"));
		}
	}
	
	/**
	 * This method is used to reset FailedLoginCount to zero and persist the data
	 * @param userDetails
	 */
	private void updateKochAuthDetails(GPUserDetails userDetails, KochAuthData kochAuthData) {
		CustomerModel searchModel = new CustomerModel();
		searchModel.setUid(userDetails.getUsername());
		CustomerModel customerModel = flexibleSearchService.getModelByExample(searchModel);
			if(null != customerModel.getKochEmailId()) {
				if(customerModel.getKochEmailId().equalsIgnoreCase(kochAuthData.getKochEmailId())) {
					customerModel.setKochAuthAccessToken(kochAuthData.getAccessToken());
					customerModel.setKochAuthIdToken(kochAuthData.getIdToken());
					customerModel.setKochAuthRefreshToken(kochAuthData.getRefreshToken());
					customerModel.setKochAuthTS(kochAuthData.getModifiedTimeStamp());
					customerModel.setKochEmailId(kochAuthData.getKochEmailId());
					modelService.save(customerModel);
				} else {
					throw new BadCredentialsException(
							this.messages.getMessage(ERROR_199));
				}
			}
	}
	

	 protected final GPUserDetails retrieveGPUser(String username) throws AuthenticationException {
	        GPUserDetails loadedUser;
	        try {
	            loadedUser = (GPUserDetails) this.defaultWsUserDetailsService.loadUserByUsername(username);
	        } catch (DataAccessException var4) {
	            throw new AuthenticationServiceException(var4.getMessage(), var4);
	        }

	        if(loadedUser == null) {
	            throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
	        } else {
	            return loadedUser;
	        }
	    }
	/**
	 * This methid is used to check the kochAuth expiry 
	 * @param userDetails
	 * @param loginType
	 * @return
	 */
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
				throw new CredentialsExpiredException(AcceleratorAuthenticationProvider.this.messages.getMessage(
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

	public void setAdminGroup(final String adminGroup)
	{
		if (StringUtils.isBlank(adminGroup))
		{
			adminAuthority = null;
		}
		else
		{
			adminAuthority = new SimpleGrantedAuthority(adminGroup);
		}
	}

	protected GrantedAuthority getAdminAuthority()
	{
		return adminAuthority;
	}
	
}
