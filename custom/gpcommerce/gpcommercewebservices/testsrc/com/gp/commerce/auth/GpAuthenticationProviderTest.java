package com.gp.commerce.auth;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.gpintegration.service.impl.GPDefaultCommerceSocialAccountService;

@UnitTest
public class GpAuthenticationProviderTest {
    @InjectMocks
	GpAuthenticationProvider gpAuthenticationProvider = new GpAuthenticationProvider();
    @Mock
    private ConfigurationService configurationService;
    @Mock
    Configuration configuration;
	@Mock
	private GPDefaultCommerceSocialAccountService socialAccountService;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
    
	@Test
	public void authenticateTest(){
		
		
		Authentication authentication =Mockito.mock(Authentication.class);
		Map<String, String> details=new HashMap<>();
		RegisterData registerData= new RegisterData();
		String credentials = new String();;
		registerData.setLogin("login");
		details.put("baseSiteId", "gppro");
		details.put("loginType", "GOOGLE");
		details.put("token", "test");
		
		Mockito.when(authentication.getCredentials()).thenReturn(credentials);
		Mockito.when(authentication.getDetails()).thenReturn(details);
		Mockito.when(authentication.getPrincipal()).thenReturn("test");
		Mockito.when(authentication.getName()).thenReturn("test");
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString("gpcommercewebservices.user.delimiter")).thenReturn("|");
		Mockito.when(configuration.getString("gpcommercewebservices.user.max.allowed.login.attempts")).thenReturn("3");
		Mockito.when(socialAccountService.getRegisterData(Mockito.any(RegisterData.class))).thenReturn(registerData);
		gpAuthenticationProvider.authenticate(authentication);
		
	}
	
	
}
