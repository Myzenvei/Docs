/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.gpintegration.utils.GPCybersourceMerchantAliasManager;
import com.gpintegration.utils.GPSignaturePojo;
import com.gpintegration.utils.GPSignatureResponseDTO;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import static org.junit.Assert.*;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
/**
 * @author vdannina
 *
 */
@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class )
@PowerMockIgnore({"org.apache.logging.log4j.*","javax.crypto.*"})
public class DefaultGPCyberSourceSignatureServiceTest {
	private GPSignaturePojo signatureData;
	@InjectMocks 
	private DefaultGPCyberSourceSignatureService gpCyberSourceSignatureService;
	@Mock
	GPSignatureResponseDTO response;
	@Mock
	GPCybersourceMerchantAliasManager cybersourceMerchantAliasManager;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration config;
	
	@Mock
	private Logger logger;
	private static final String site = "dixie";
	
	private static final String CYB_SRC_CREATE_TOKEN_URL = "cybersource.signature.create.response.url";
	private static final String CYB_SRC_UPDATE_TOKEN_URL = "cybersource.signature.update.response.url";
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String CYB_SRC_ACCESS_KEY = "cybersource.checkout.profile.access_key.";
	private static final String CYB_SRC_PROFILE_ID = "cybersource.checkout.profile.profile_id.";
	private static final String CYB_SRC_SECRET_KEY = "cybersource.checkout.profile.secret_key.";
	
	@Before
	public void setup() {
		signatureData = new GPSignaturePojo();
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter(CYB_SRC_CREATE_TOKEN_URL)).thenReturn("https://testsecureacceptance.cybersource.com/silent/token/create");
		Mockito.when(Config.getParameter(CYB_SRC_UPDATE_TOKEN_URL)).thenReturn("https://testsecureacceptance.cybersource.com/silent/token/update");
		Mockito.when(Config.getParameter(CYB_SRC_ACCESS_KEY+site)).thenReturn("3dc205452f8931d1b3dc8a516f92c413");
		Mockito.when(Config.getParameter(CYB_SRC_PROFILE_ID+site)).thenReturn("gpb2bcybsrcdev");
		Mockito.when(Config.getParameter(CYB_SRC_SECRET_KEY+site)).thenReturn("1f9532fa862d481fb17e458db3f76bf8a32510885f77466b8f52bad72c0622cd687c188abb6a42edaa759d49099de98b5a480d5db21f48b7a5f4b92678337399f35a59197d4946b8918f61f8550a1651bf17f79e0b1c4e0a85e1bb6b4964c00c22275a2b4e2d4739a9d4364142d254fd3f6a19505338458ba6e59f195209631a");
		Mockito.when(Config.getParameter(BASESITE_DELIMITER)).thenReturn("|");
		Mockito.when(configurationService.getConfiguration()).thenReturn(config);
		Mockito.when(config.getString(BASESITE_DELIMITER)+site).thenReturn("|");
		
		signatureData.setBillToAddressCity("My City");
		signatureData.setBillToAddressLine1("CA");
		signatureData.setBillToAddressCountry("US");
		
		signatureData.setBillToAddressPostalCode("94043");
		signatureData.setBillToEmail("sample@cybersource.com");
		signatureData.setBillToPhone("02890888888");
		
		signatureData.setBillToSurname("O'Drien");
		signatureData.setBillToForename("sample");
		signatureData.setPaymentMethod("card");
		
		signatureData.setTransactionType("create_payment_token");
		signatureData.setUnsignedFieldNames("card_type,card_number,card_expiry_date");
		signatureData.setSignedFieldNames("access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency,payment_method,bill_to_forename,bill_to_surname,bill_to_email,bill_to_phone,bill_to_address_line1,bill_to_address_city,bill_to_address_state,bill_to_address_country,bill_to_address_postal_code");
		
		signatureData.setAmount("0.00");
	}
	
	
	@Test
	public void testGetTheSignature() {
		 response = gpCyberSourceSignatureService.getTheSignature(signatureData, site);
		 assertNotNull(response.getSignature());
		 assertEquals(response.getAccess_key(),Config.getParameter(CYB_SRC_ACCESS_KEY+site));
		 assertNotNull(response.getSigned_date_time());
		 assertEquals(response.getCyber_source_url(),Config.getParameter(CYB_SRC_CREATE_TOKEN_URL));
		 
	}

}
