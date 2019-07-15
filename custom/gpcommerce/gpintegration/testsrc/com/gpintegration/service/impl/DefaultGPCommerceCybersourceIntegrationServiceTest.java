/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.math.BigDecimal;
import java.util.Currency;

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

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPDataException;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
@PowerMockIgnore({"org.apache.logging.log4j.*"})
public class DefaultGPCommerceCybersourceIntegrationServiceTest {
	@InjectMocks
	private DefaultGPCommerceCybersourceIntegrationService gpCybersourceIntegrationService;
	@Mock
	private ConfigurationService configurationService;
	private CCPaymentInfoData authorizeDetails = new CCPaymentInfoData();
	@Mock
	private CommerceCheckoutParameter cardParameter;
	@Mock
	private Config config;
	@Mock
	private DefaultPaymentServiceImpl defaultPaymentServiceImpl;
	private AddressData address= new AddressData();
	private CountryData country= new CountryData();
	private RegionData region= new RegionData();
	@Mock
	CurrencyModel currency;
	@Mock 
	Currency curr;
	
	private static final String CYB_SRC_ACCESS_KEY = "cybersource.checkout.profile.access_key.";
	private static final String CYB_SRC_PROFILE_ID = "cybersource.checkout.profile.profile_id.";
	private static final String CYB_SRC_SECRET_KEY = "cybersource.checkout.profile.secret_key.";
	private String site="dixie";
	@Before
	public void setup() {
			currency = Mockito.mock(CurrencyModel.class);
			PowerMockito.mockStatic(Config.class);
			Mockito.when(Config.getParameter(GpintegrationConstants.SERVER_URL)).thenReturn("https://ics2wstest.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.148.wsdl");
			Mockito.when(Config.getParameter(GpintegrationConstants.CLIENT_LIB_VERSION)).thenReturn("1.4/1.5.1");
			Mockito.when(Config.getParameter(GpintegrationConstants.TRANSACTION_DECRIPTION)).thenReturn("test");
			
			Mockito.when(Config.getParameter(GpintegrationConstants.MERCHANT_ID+site)).thenReturn("digitalroadmap");
			Mockito.when(Config.getParameter(GpintegrationConstants.MERCHANT_KEY+site)).thenReturn("nx0+fRnraca6Ey9IpIEk4g6dSigoEfDZsY9neMSz4JS3HXz8Cx68Y9PUzrQXAy4Kvuy841c07qMAWf6pUv1MwyLw6IG1WdGSAJ5SqY+vYQvs1GgkSw3ZQPaRXT1x69FHNkw+N1n6r+igiJXdT+IXsffbWww9W4earXwV0AC6B4C68LiL+m1OZKdsfk3cY3OAz5E7BrXfl2a6as1YcaBpQKns0nineSE32OHHd0hR2jaTXJjPsIPGpx3doHDKVUTrEs0ZpmVw70PHhQ2aVBk0+WrbwRtu6r6rHuQJsiSjP6DFbyfqfS6n3q7plQFrt076VYhicyt+/BG3YSBRQgEPAg==");
			
			PowerMockito.mockStatic(System.class);	
			Mockito.when(System.getProperty(GpintegrationConstants.OS_VERSION)).thenReturn("windows");
			Mockito.when(System.getProperty(GpintegrationConstants.JAVA_VENDOR)).thenReturn("1.8.0");
			Mockito.when(System.getProperty(GpintegrationConstants.JAVA_VERSION)).thenReturn("1.8");
			
			Mockito.when(Config.getParameter(CYB_SRC_ACCESS_KEY+site)).thenReturn("3dc205452f8931d1b3dc8a516f92c413");
			Mockito.when(Config.getParameter(CYB_SRC_PROFILE_ID+site)).thenReturn("gpb2bcybsrcdev");
			Mockito.when(Config.getParameter(CYB_SRC_SECRET_KEY+site)).thenReturn("sample");
			
			country.setName("US");
			region.setName("sample");
			address.setCountry(country);
			address.setRegion(region);
			address.setTown("MY CITY");
			address.setEmail("sample@cybersource.com");
			address.setFirstName("sample");
			address.setLastName("dev");
			address.setPhone("02890888888");
			address.setPostalCode("94043");
			address.setTitle("home");
			address.setLine1("1 Card Lane");
			authorizeDetails.setBillingAddress(address);
			Mockito.when(cardParameter.getSecurityCode()).thenReturn("123");
			currency.setDigits(new Integer(2));
			final CartModel cartModel = Mockito.mock(CartModel.class);
			Mockito.when(cardParameter.getCart()).thenReturn(cartModel);
			Mockito.when(cardParameter.getCart().getCurrency()).thenReturn(currency);
		
	}
	@Test(expected=GPDataException.class)
	public void testAuthorizeUsingCybersource() {
		gpCybersourceIntegrationService.authorizeUsingCybersource(authorizeDetails, cardParameter, "dixie",Mockito.anyBoolean(),Mockito.anyBoolean(),Mockito.anyBoolean());
	}
	@Test(expected=GPDataException.class)
	public void testBillCaptureCreditCard() {
		gpCybersourceIntegrationService.billCapture("01000", new BigDecimal("0.0"), curr,"10009101","dixie",1,1);
	}

	@Test(expected=GPDataException.class)
	public void testVoidCaptureCreditCard() {
		gpCybersourceIntegrationService.voidCapture("01000", new BigDecimal("0.0"), curr,"10009101","dixie");
	}
	
	@Test(expected=GPDataException.class)
	public void testReverseAuthCreditCard() {
		gpCybersourceIntegrationService.reverseAuthorization("01000", new BigDecimal("0.0"), curr,"10009101","dixie");
	}

}
