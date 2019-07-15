/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.configuration.Configuration;
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

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.user.impl.GPAddCustomerToSfdcResponse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class )
@PowerMockIgnore({"org.apache.logging.log4j.*"})

public class GPDefaultCommerceSCPIServiceTest {

		@InjectMocks
		private GPDefaultCommerceSCPIService gpCommerceSCPIService;
		
		
		@Mock
		private CustomerModel customerModel;
		
		@Mock
		private B2BCustomerModel b2bcustomerModel;
		
		@Mock
		private ConfigurationService configurationService;

		@Mock
		private Configuration config;
		
		@Mock
		private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
		
		@Mock
		private B2BUnitModel b2bUnit;
		
		@Mock
		AddressModel address = new AddressModel();
		
		@Mock
		CountryModel country;
		
		
		@Before
		public void setUp() {
			Mockito.when(customerModel.getUid()).thenReturn("sample@gmail.com|dixie");
			Mockito.when(customerModel.getDisplayName()).thenReturn("guest");
			Mockito.when(customerModel.getName()).thenReturn("don john");
			Mockito.when(b2bcustomerModel.getUid()).thenReturn("sample@gmail.com|dixie");
			Mockito.when(b2bcustomerModel.getDisplayName()).thenReturn("guest");
			Mockito.when(b2bcustomerModel.getName()).thenReturn("don john");
			Collection<AddressModel> addresses = new ArrayList<AddressModel>();
			country = new CountryModel();
			address.setContactAddress(true);
			address.setTown("Peach Tree");
			country.setIsocode("US");
			address.setCountry(country);
			address.setDistrict("Califonia");
			address.setStreetname("301");
			address.setPhone1("0123456789");
			address.setCellphone("0123456789");
			addresses.add(address);
			Mockito.when(customerModel.getAddresses()).thenReturn(addresses);
			Mockito.when(b2bcustomerModel.getDefaultB2BUnit()).thenReturn(b2bUnit);
			Mockito.when(b2bUnit.getAddresses()).thenReturn(addresses);
			PowerMockito.mockStatic(Config.class);
			Mockito.when(configurationService.getConfiguration()).thenReturn(config);
			Mockito.when(config.getString(GpintegrationConstants.SCPI_ENDPOINT)).thenReturn("https://l250153-iflmap.hcisbp.us3.hana.ondemand.com:443/http/customertosfdc");
			Mockito.when(config.getString(GpintegrationConstants.SCPI_USERNAME)).thenReturn("S0019592881");
			Mockito.when(config.getString(GpintegrationConstants.SCPI_PASSWORD)).thenReturn("/(6&J+80");
			final String[] names = new String[]
					{ "don", "john" };
					given(gpDefaultCustomerNameStrategy.splitName(customerModel.getName())).willReturn(names);
					given(gpDefaultCustomerNameStrategy.splitName(b2bcustomerModel.getName())).willReturn(names);
		}
		
		@Test
		public void addCustomerToSfdcTest() {
			GPAddCustomerToSfdcResponse response = gpCommerceSCPIService.addCustomerToSfdc(customerModel);
			assertEquals(response.getContactResponse().getErrorCode(),"MISSING_ARGUMENT");
			assertEquals(response.getContactResponse().getErrorMessage(),"Source_Id__c not specified");
			
			
		}
		@Test
		public void addB2bCustomerToSfdcTest() {
			GPAddCustomerToSfdcResponse response = gpCommerceSCPIService.addCustomerToSfdc(b2bcustomerModel);
			assertEquals(response.getContactResponse().getErrorCode(),"MISSING_ARGUMENT");
			assertEquals(response.getContactResponse().getErrorMessage(),"Source_Id__c not specified");
		}
		@Test
		public void addCustomerToSfdcNotGuestTest() {
			Mockito.when(b2bcustomerModel.getDisplayName()).thenReturn("user");
			GPAddCustomerToSfdcResponse response = gpCommerceSCPIService.addCustomerToSfdc(customerModel);
			assertEquals(response.getContactResponse().getErrorCode(),"MISSING_ARGUMENT");
			assertEquals(response.getContactResponse().getErrorMessage(),"Source_Id__c not specified");
			
			
		}
		@Test
		public void addB2bCustomerToSfdcNotGuestTest() {
			Mockito.when(b2bcustomerModel.getDisplayName()).thenReturn("user");
			GPAddCustomerToSfdcResponse response = gpCommerceSCPIService.addCustomerToSfdc(b2bcustomerModel);
			assertEquals(response.getContactResponse().getErrorCode(),"MISSING_ARGUMENT");
			assertEquals(response.getContactResponse().getErrorMessage(),"Source_Id__c not specified");
		}
		@Test
		public void addCustomerToSfdcFailTest() {
			Mockito.when(config.getString(GpintegrationConstants.SCPI_PASSWORD)).thenReturn("/(6&J+8");
			GPAddCustomerToSfdcResponse response = gpCommerceSCPIService.addCustomerToSfdc(customerModel);
			assertNull(response);
			
		}
		@Test
		public void addB2bCustomerToSfdcFailTest() {
			Mockito.when(config.getString(GpintegrationConstants.SCPI_PASSWORD)).thenReturn("/(6&J+8");
			GPAddCustomerToSfdcResponse response = gpCommerceSCPIService.addCustomerToSfdc(b2bcustomerModel);
			assertNull(response);
		}
	

}
