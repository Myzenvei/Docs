/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.customer.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;

import com.gp.commerce.core.enums.TaxExemptionStatusEnum;
import com.gp.commerce.core.services.impl.DefaultGPCustomerAccountService;
import com.gpintegration.service.impl.GPDefaultCommerceKochAuthService;
import com.gpintegration.service.impl.GPDefaultCommerceSocialAccountService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

@UnitTest
public class GpDefaultCustomerFacadeTest {

	private static final String POWERTOOLS = "powertools";
	private static final String ELECTRONICS = "electronics";
	private static final String TEST_USER_UID = "testUid";

	@InjectMocks
	GpDefaultCustomerFacade gpCustomerFacade=new GpDefaultCustomerFacade();

	@Mock
	ModelService modelService;
	@Mock
	private GPDefaultCommerceSocialAccountService socialAccountService;
	@Mock
	private GPDefaultCommerceKochAuthService gpCommerceKochAuthService;
	@Mock
	UserService userService;
	@Mock
	CustomerNameStrategy strategy;
	@Mock
	CommonI18NService commonI18NService;
	@Mock
	CustomerAccountService customerAccountService;
	@Mock
	private DefaultGPCustomerAccountService gpCustomerAccountService;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;
	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private UserFacade userFacade;
	@Mock
	private Converter<CustomerData, B2BCustomerModel> b2BCustomerReverseConverter;
	@Mock
	private  Converter<UserModel, CustomerData> customerConverter;
	
	@Mock
	private Converter<B2BCustomerModel, CustomerData> b2BCustomerConverter;
	 
	RegisterData data=new RegisterData();
	CustomerModel customer=mock(CustomerModel.class);
	UserModel user= mock(UserModel.class);
	B2BCustomerModel b2BCustomerModel=mock(B2BCustomerModel.class);
	B2BUnitModel b2bUnit=mock(B2BUnitModel.class);
	BaseSiteModel site=mock(BaseSiteModel.class);
	B2BUserGroupModel userGroup=mock(B2BUserGroupModel.class);
	Set<PrincipalGroupModel> groups=new HashSet<>();
	CustomerData custData=new CustomerData();
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		gpCustomerFacade.setModelService(modelService);
		gpCustomerFacade.setUserService(userService);
		gpCustomerFacade.setCustomerNameStrategy(strategy);
		gpCustomerFacade.setCommonI18NService(commonI18NService);
		gpCustomerFacade.setCustomerAccountService(customerAccountService);
		gpCustomerFacade.setB2bUnitService(b2bUnitService);
		gpCustomerFacade.setUserFacade(userFacade);
		gpCustomerFacade.setConfigurationService(configurationService);
		gpCustomerFacade.setSocialAccountService(socialAccountService);
		gpCustomerFacade.setGpCommerceKochAuthService(gpCommerceKochAuthService);

		data.setFirstName("first");
		data.setLastName("last");
		data.setLogin("first.last@name.com");
		data.setTitleCode("mr");
		data.setAddToAffilatedMarketComm(true);
		data.setAddToMarketComm(true);
		data.setCountry("US");
		data.setBaseSiteId(POWERTOOLS);

		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(strategy.getName(data.getFirstName(), data.getLastName())).thenReturn("first Last");
		when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		when(modelService.create(B2BUnitModel.class)).thenReturn(b2bUnit);
		when(modelService.create(B2BUserGroupModel.class)).thenReturn(userGroup);
		when(userService.getCurrentUser()).thenReturn(b2BCustomerModel);
		when(b2BCustomerModel.getGroups()).thenReturn(groups);
		when(b2BCustomerModel.getDefaultB2BUnit()).thenReturn(b2bUnit);
		b2BCustomerModel.setTaxExemptionStatus(TaxExemptionStatusEnum.NOT_SUBMITTED);
		
	}
	
	@Before
	public void setUpCustomerData()
	{
		MockitoAnnotations.initMocks(this);
		
		gpCustomerFacade.setCustomerNameStrategy(strategy);
		
		custData.setFirstName("customerfirstname");
		custData.setLastName("andlastname");
		custData.setCellPhone("9080756435");
		
		when(strategy.getName(custData.getFirstName(), custData.getLastName())).thenReturn("customerfirstname andlastname");
	}

	@Test
	public void testRegisterB2BCustomer() throws DuplicateUidException {
		data.setBaseSiteId(POWERTOOLS);
		data.setLoginType("GPEMPLOYEE");
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString("gp.b2b.unit.site")).thenReturn(POWERTOOLS);
		when(configuration.getString("gp.b2b.default.unit")).thenReturn("default");
		when(modelService.create(B2BCustomerModel.class)).thenReturn(b2BCustomerModel);
		when(modelService.create(B2BUnitModel.class)).thenReturn(b2bUnit);
		when(b2bUnitService.getUnitForUid("default")).thenReturn(b2bUnit);
		when(userService.getUserGroupForUID("b2badmingroup")).thenReturn(userGroup);
		when(userService.getCurrentUser()).thenReturn(user);
		when(socialAccountService.getUserDataFromGoogle(Mockito.anyObject())).thenReturn(data);
		when(gpCommerceKochAuthService.getKochAuthToken(Mockito.anyObject())).thenReturn(data);
		doNothing().when(gpCustomerAccountService).registerB2bUnit(b2BCustomerModel, data.getPassword());

		gpCustomerFacade.register(data);

		assertNotNull(b2BCustomerModel);
	}

	@Test
	public void testRegisterB2CCustomer() throws DuplicateUidException {
		data.setBaseSiteId(ELECTRONICS);
		data.setLoginType("GPEMPLOYEE");
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString("gp.b2b.unit.site")).thenReturn(POWERTOOLS);
		when(modelService.create(CustomerModel.class)).thenReturn(customer);
		doNothing().when(customerAccountService).register(customer, data.getPassword());
		when(socialAccountService.getUserDataFromGoogle(Mockito.anyObject())).thenReturn(data);
		when(gpCommerceKochAuthService.getKochAuthToken(Mockito.anyObject())).thenReturn(data);
		gpCustomerFacade.register(data);
		assertNotNull(customer);
	}

	@Test
	public void testGetUserId()
	{
		final String userId = data.getLogin();
		when(userService.getCurrentUser()).thenReturn(user);
		when(gpCustomerAccountService.getUserId(data.getToken())).thenReturn(userId);

		final String userid = gpCustomerFacade.getUserId(data.getToken());

		assertNotNull(userid);
		assertEquals(userid, userId);
	}
	
	@Test
	public void testGetCustomerTaxExemptionStatus()
	{
		when(userService.getCurrentUser()).thenReturn(b2BCustomerModel);
		when(b2BCustomerModel.getTaxExemptionStatus()).thenReturn(TaxExemptionStatusEnum.NOT_SUBMITTED);

		String taxExemptionStatus	= gpCustomerFacade.getCustomerTaxExemptionStatus();

		assertNotNull(taxExemptionStatus);
	}
	@Test
	public void testCheckTaxExemption()
	{
		when(userService.getCurrentUser()).thenReturn(b2BCustomerModel);
		String taxExemptionStatus	= gpCustomerFacade.getCustomerTaxExemptionStatus();
		assertNotNull(taxExemptionStatus);
	}
	@Test
	public void testForgottenPassword() throws DuplicateUidException {

		when(modelService.create(CustomerModel.class)).thenReturn(customer);
		doNothing().when(gpCustomerAccountService).forgottenPassword(b2BCustomerModel);
		when(userService.getUserForUID(TEST_USER_UID.toLowerCase(), CustomerModel.class)).thenReturn(customer);
		when(customer.getLoginType()).thenReturn("GPEMPLOYEE");
		gpCustomerFacade.forgottenPassword(TEST_USER_UID.toLowerCase());
		verify(customerAccountService).forgottenPassword(customer);
		assertNotNull(b2BCustomerModel);
	}
	@Test(expected = BadCredentialsException.class)
	public void testForgottenPwdInv2()
	{
		customer.setLoginType("GPEMPLOYEE");
		given(userService.getUserForUID(TEST_USER_UID.toLowerCase(), CustomerModel.class)).willThrow(
				new UnknownIdentifierException(""));
		gpCustomerFacade.forgottenPassword(TEST_USER_UID);
	}
	
	@Test
	public void testupdatePersonalDetails() throws DuplicateUidException{
		
		when(gpCustomerAccountService.updatePersonalDetails(custData.getName(), custData)).thenReturn(b2BCustomerModel);
		gpCustomerFacade.setCustomerConverter(customerConverter);
		CustomerData cd = gpCustomerFacade.updatePersonalDetails(custData);
		assertNotNull(cd);
	}
	
	@Test
	public void testDisconnect() throws IllegalArgumentException, DuplicateUidException{
		
		data.setLoginType("GOOGLE");
		when(userService.getCurrentUser()).thenReturn(customer);
		doNothing().when(gpCustomerAccountService).connectOrDisconnect(Mockito.any(CustomerModel.class), Mockito.anyString());
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getString("gpcommercewebservices.user.delimiter")).thenReturn("|");
		gpCustomerFacade.disconnect(data);
	}
	@Test
	public void testConnect() throws DuplicateUidException{
		
		data.setLoginType("GOOGLE");
		when(socialAccountService.getRegisterData(Mockito.any(RegisterData.class))).thenReturn(data);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getString("gpcommercewebservices.user.delimiter")).thenReturn("|");
		when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.doNothing().when(gpCustomerAccountService).connectOrDisconnect(Mockito.any(CustomerModel.class), Mockito.anyString());
		gpCustomerFacade.connect(data);
		
	}
	
	@Test
	public void updateCustomerTest(){
		
		CustomerData customerData = new CustomerData();
		BaseSiteModel siteModel = new BaseSiteModel();
		siteModel.setUid("uid");
		B2BCustomerModel b2bcustomer = new B2BCustomerModel();
		customerData.setFirstName("firstName");
		customerData.setLastName("lastName");
		customerData.setEmail("test@test.com");
		customerData.setUid("uid");
		B2BUnitModel unitmodel = new B2BUnitModel();
		unitmodel.setB2bUnitLevel("L3");
		b2bcustomer.setDefaultB2BUnit(unitmodel);
		b2bcustomer.setCellPhone("91919191919");
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(siteModel);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getString("gpcommercewebservices.user.delimiter")).thenReturn("|");
		when(userService.getUserForUID("uid")).thenReturn(b2bcustomer);
		Mockito.when(b2BCustomerConverter.convert(Mockito.anyObject())).thenReturn(customerData);
		when(b2BCustomerReverseConverter.convert(customerData, b2bcustomer)).thenReturn(b2bcustomer);
		when(modelService.create(B2BCustomerModel.class)).thenReturn(b2bcustomer);
		Mockito.doNothing().when(modelService).save(b2bcustomer);
		CustomerData custData =gpCustomerFacade.updateCustomer(customerData,false);
		assertEquals(custData.getCellPhone(), "91919191919");
	}
	
}
