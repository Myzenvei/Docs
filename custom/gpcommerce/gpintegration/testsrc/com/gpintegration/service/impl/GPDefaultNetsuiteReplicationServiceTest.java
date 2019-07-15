package com.gpintegration.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

@UnitTest
public class GPDefaultNetsuiteReplicationServiceTest {
	
	@InjectMocks
	private GPDefaultNetsuiteReplicationService netsuiteReplicationService;
	
	@Mock
	ConfigurationService configurationService;
	
	@Mock
	Configuration configuration;
	
	@Mock
	Logger log;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		netsuiteReplicationService = new GPDefaultNetsuiteReplicationService();
		netsuiteReplicationService.setConfigurationService(configurationService);
		given(configurationService.getConfiguration()).willReturn(configuration);
		given(configuration.getString("gp.netsuite.service.customer.uri")).willReturn("https://rest.netsuite.com/app/site/hosting/restlet.nl?script=384&deploy=1");
		given(configuration.getString("gp.netsuite.service.order.uri")).willReturn("https://rest.netsuite.com/app/site/hosting/restlet.nl?script=385&deploy=1");
		given(configuration.getString("gp.netsuite.oauth.consumer.key")).willReturn("694ecbccd72128f1f3619eec6d6e0ff8ba642f04dd726fe79f070a23753a85d1");
		given(configuration.getString("gp.netsuite.oauth.access.token")).willReturn("99bdbf13d734105b40bcab35b96a2f656d33d4d1a9c29f772c611a8fb153d0a0");
		given(configuration.getString("gp.netsuite.service.customer.order.url")).willReturn("https://rest.netsuite.com/app/site/hosting/restlet.nl");
		given(configuration.getString("gp.netsuite.oauth.consumer.secret")).willReturn("3b2ed40c39ed580b6a11dba1cf5a84bdbe88bb193f8fcb8a8570caf07feeb37c");
		given(configuration.getString("gp.netsuite.oauth.token.secret")).willReturn("de800f568126991fd6c71ba197aed271129ab5249eddf5734aa4276412f720fd");
        given(configuration.getString("gp.netsuite.oauth.realm")).willReturn("4807301_SB1");
	}
	
	@Test
    public void testEncode() {
        String inputText = "POST"+"&"+"https://rest.netsuite.com/app/site/hosting/restlet.nl";
        String encodingType = "UTF-8";
        String expectedEncodedString = "POST%26https%3A%2F%2Frest.netsuite.com%2Fapp%2Fsite%2Fhosting%2Frestlet.nl";
        String encodedValue = netsuiteReplicationService.encode(inputText, encodingType);
        assertEquals(expectedEncodedString, encodedValue);
    }
	
	@Test
    public void testComputeOAuthSignature() throws UnsupportedEncodingException, GeneralSecurityException {
        String consumerSecret = "3b2ed40c39ed580b6a11dba1cf5a84bdbe88bb193f8fcb8a8570caf07feeb37c";
        String tokenSecret = "de800f568126991fd6c71ba197aed271129ab5249eddf5734aa4276412f720fd";
        String algorithmName = "HmacSHA1";
        String computedHash = netsuiteReplicationService.computeOAuthSignature("sampleBaseString", consumerSecret+"&"+tokenSecret, algorithmName);
        assertEquals("i+JAZeWRgajSl12qrZkQt8EFfVY=", computedHash);
    }
	
	@Test
    public void testFormAuthorizeHeader() throws Exception {
        String test = netsuiteReplicationService.formAuthorizeHeader("POST", "1", "384");
        assertNotNull(test);
    }

}
