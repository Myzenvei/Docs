/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.spring.security;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.spring.security.CoreUserDetails;

@UnitTest
public class GPUserDetailsTest {

	@InjectMocks
	private GPUserDetails gpUserDetails = new GPUserDetails(null, null);
	@InjectMocks
	private GPUserDetails gpUser = new GPUserDetails(null, null, false, false, false, false, null, null, null);

	private Integer failedLoginAttempts;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testGPUserDetails1() {
		CoreUserDetails coreUserDetails = Mockito.mock(CoreUserDetails.class);
		Collection<GrantedAuthority> authorities = new HashSet();
		GrantedAuthority grantedAuthority = Mockito.mock(GrantedAuthority.class);
		authorities.add(grantedAuthority);
		GPUserDetails spy = (GPUserDetails) Mockito
				.spy((CoreUserDetails) new GPUserDetails(coreUserDetails, failedLoginAttempts));
	}

	@Test
	public void testGPUserDetails() {
		GrantedAuthority grantedAuthority = Mockito.mock(GrantedAuthority.class);
		Collection<GrantedAuthority> authorities = new HashSet();
		authorities.add(grantedAuthority);
		GPUserDetails spy = Mockito.spy(new GPUserDetails(Mockito.anyString(), Mockito.anyString(), false, false, false,
				false, authorities, Mockito.anyString(), failedLoginAttempts));
	}

}
