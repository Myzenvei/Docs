/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import de.hybris.platform.commercefacades.user.data.RegisterData;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPDefaultCommerceKochAuthServiceTest {

	@Mock
	private GPDefaultCommerceKochAuthService kochAuthService;
	
	@InjectMocks
	private RegisterData registerData;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getKochAuthTokenTest() {
		when(kochAuthService.getKochAuthToken(Mockito.anyObject())).thenReturn(registerData);
		assertNotNull(kochAuthService.getKochAuthToken(registerData));
	}
}
