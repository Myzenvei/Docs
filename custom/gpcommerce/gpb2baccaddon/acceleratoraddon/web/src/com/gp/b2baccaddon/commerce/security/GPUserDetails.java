/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.b2baccaddon.commerce.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import de.hybris.platform.spring.security.CoreUserDetails;

/**
 * @author vdannina
 *
 */
public class GPUserDetails extends CoreUserDetails{
	 private final Integer failedLoginAttempts;
	    
	    /**
	     * @param username
	     * @param password
	     * @param enabled
	     * @param accountNonExpired
	     * @param credentialsNonExpired
	     * @param accountNonLocked
	     * @param authorities
	     * @param iso
	     * @param failedLoginAttempts
	     */
	    public GPUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities, String iso, Integer failedLoginAttempts){
	        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities,iso);
	        this.failedLoginAttempts = failedLoginAttempts;
	    }
	    /**
	     * Construct to set the additional field failedLoginAttempts
	     * @param coreUserDetails
	     * @param failedLoginAttempts
	     */
	    public GPUserDetails(CoreUserDetails coreUserDetails,Integer failedLoginAttempts){
	        super(coreUserDetails.getUsername(),coreUserDetails.getPassword(),coreUserDetails.isEnabled(),coreUserDetails.isAccountNonExpired(),coreUserDetails.isCredentialsNonExpired(),coreUserDetails.isAccountNonLocked(),coreUserDetails.getAuthorities(),coreUserDetails.getLanguageISO());
	        this.failedLoginAttempts = failedLoginAttempts;
	    }

	    /**
	     * @return get the failedLoginAttempts 
	     */
	    public Integer getFailedLoginAttempts() {
	        return failedLoginAttempts;
	    }

}
