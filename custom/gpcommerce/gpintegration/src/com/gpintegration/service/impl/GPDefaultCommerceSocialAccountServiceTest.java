/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import com.gpintegration.service.impl.GPDefaultCommerceSocialAccountService;

public class GPDefaultCommerceSocialAccountServiceTest {
	
	@Mock
	private GPDefaultCommerceSocialAccountService defaultGpCommerceSocialAccountService;

	@InjectMocks
	private RegisterData registerData;
	
	@InjectMocks
	private String socialLoginFlag;
	
	
@Before
public void setUp() throws Exception {
  MockitoAnnotations.initMocks(this);

}

@Test
public void testGetSocialProfileDetails(){
	String userData="{\"name\":\"Rashmi Deloitte\",\"email\":\"rmallya@deloitte.com\",\"birthday\":\"03/30/1996\",\"gender\":\"female\",\"short_name\":\"Rashmi\"}";
	assertNotNull(defaultGpCommerceSocialAccountService.getSocialProfileDetails(userData,socialLoginFlag));
	
	
}

@Test
public void testGetUserDataFromGoogle(){
	when(defaultGpCommerceSocialAccountService.getUserDataFromGoogle(Mockito.anyObject())).thenReturn(registerData);
	assertNotNull(defaultGpCommerceSocialAccountService.getUserDataFromGoogle(registerData));
	
}

@Test
public void testGetUserDataFromFacebook(){
	
	when(defaultGpCommerceSocialAccountService.getUserDataFromFacebook(Mockito.anyObject())).thenReturn(registerData);
	assertNotNull(defaultGpCommerceSocialAccountService.getUserDataFromFacebook(registerData));
}



}
