/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.usergroups.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.services.usergroups.impl.GPDefaultB2BUserGroupService;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class GPDefaultB2BUserGroupFacadeTest {

	@InjectMocks
	GPDefaultB2BUserGroupFacade gpDefaultB2BUserGroupFacade = new GPDefaultB2BUserGroupFacade();

	@Mock
	private CustomerData currentUser = null;
	@Mock
	CustomerModel user;
	@Mock
	private UserModel invited;
	@Mock
	private SearchRestrictionService searchRestrictionService;
	
	@Mock
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Mock
	private UserService userService;

	@Mock
	private ModelService modelService;

	@Mock
	private EventService eventService;

	@Mock
	private Converter<UserModel, CustomerData> customerConverter;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private GPDefaultB2BUserGroupService gpb2buserGroupService;

	@Mock
	private Converter<B2BUserGroupModel, B2BUserGroupData> b2BUserGroupConverter;

	@Mock
	private GPDefaultB2BUserGroupService gpDefaultB2BUserGroupService;

	@Mock
	private B2BCommerceUnitService b2BCommerceUnitService;
	@Mock
	private GPEmailItemEvent item;
	@Mock
	private B2BCustomerModel invCustomer;
	@Mock
	private BaseStoreModel baseStore = new BaseStoreModel();
	@Mock
	private BaseSiteModel baseSite;
	@Mock
	private LanguageModel language;
	@Mock
	private CurrencyModel currency = new CurrencyModel();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(gpDefaultB2BUserGroupFacade, "userService", userService);
	}
	
	@Test
	public void findUserGroupsByUnitsTest() {

		List<String> list = new ArrayList();
		list.add("TestString");
		UserB2BUnitWsDTO wsDto = new UserB2BUnitWsDTO();
		wsDto.setB2BUnitList(list);
		B2BUserGroupModel model = new B2BUserGroupModel();
		List<B2BUserGroupModel> userList = new ArrayList();
		userList.add(model);
		B2BUserGroupData data = new B2BUserGroupData();
		data.setUid("TestString");
		List<B2BUserGroupData> userData = new ArrayList();
		userData.add(data);
		Mockito.when(gpb2buserGroupService.findUserGroupsByUnits(wsDto)).thenReturn(userList);
		Mockito.when(b2BUserGroupConverter.convert(Mockito.anyObject(), Mockito.anyObject())).thenReturn(data);

		gpDefaultB2BUserGroupFacade.findUserGroupsByUnits(wsDto);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void addUserGroupsToUserTest() {
		List<String> testList = new ArrayList<>();
		testList.add("testList");
		final List<B2BUserGroupModel> userGroupModels = new ArrayList();
		B2BUserGroupModel model = new B2BUserGroupModel();
		model.setUid("testUid");
		userGroupModels.add(model);
		Mockito.when(gpDefaultB2BUserGroupService.findB2BUserGroupsByCodes(testList)).thenReturn(userGroupModels);
		final B2BCustomerModel customer = new B2BCustomerModel();
		PrincipalGroupModel testGroup = new PrincipalGroupModel();
		testGroup.setUid("testUid");
		Set<PrincipalGroupModel> testModel = new HashSet();
		testModel.add(testGroup);
		customer.setGroups(testModel);
		Mockito.when(userService.getUserForUID(Mockito.anyString(), Mockito.anyObject())).thenReturn(customer);
		Mockito.doNothing().when(modelService).saveAll(customer);
		gpDefaultB2BUserGroupFacade.addUserGroupsToUser(Mockito.anyString(), Mockito.anyList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addUsersToUserGroupTest() {
		List<String> testList = new ArrayList<>();
		testList.add("testList");
		PrincipalGroupModel testGroup = new PrincipalGroupModel();
		testGroup.setUid("testUid");
		Set<PrincipalGroupModel> testModel = new HashSet();
		testModel.add(testGroup);

		final List<B2BCustomerModel> users = new ArrayList<>();
		B2BCustomerModel customer = new B2BCustomerModel();
		customer.setUid("testUid");
		customer.setGroups(testModel);
		users.add(customer);
		final B2BUserGroupModel userGroupModel = new B2BUserGroupModel();
		Mockito.when(b2BCommerceB2BUserGroupService.getUserGroupForUID(Mockito.anyString(), Mockito.anyObject()))
				.thenReturn(userGroupModel);
		Mockito.doNothing().when(modelService).saveAll(customer);
		gpDefaultB2BUserGroupFacade.addUsersToUserGroup(Mockito.anyString(), Mockito.anyList());
	}

	@Test
	public void testInviteUser()
	{
		final String invitedUser="User1";
		final CustomerData currentUser=Mockito.mock(CustomerData.class);
		final B2BCustomerModel b2bCustomerModel=Mockito.mock(B2BCustomerModel.class);
		Mockito.when(userService.getUserForUID(invitedUser)).thenReturn(b2bCustomerModel);
		Mockito.when(b2bCustomerModel.getEmail()).thenReturn("email@gp.com");
		Mockito.when(userService.getCurrentUser()).thenReturn(b2bCustomerModel);
		Mockito.when(b2bCustomerModel.getSite()).thenReturn(baseSite);
		Mockito.when(baseSite.getStores()).thenReturn(Collections.singletonList(baseStore));
		Mockito.when(commonI18NService.getCurrentLanguage()).thenReturn(language);
		Mockito.when(commonI18NService.getCurrentCurrency()).thenReturn(currency);
		
		gpDefaultB2BUserGroupFacade.inviteUser(invitedUser, currentUser);
		Mockito.verify(eventService).publishEvent(Mockito.any());
		
	}

	@Test
	public void updateUserGroupFalseTest() {

		B2BUserGroupModel userGroupmodel = new B2BUserGroupModel();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) userGroupmodel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		B2BUserGroupData userGroupData = new B2BUserGroupData();
		B2BUnitModel unitModel = new B2BUnitModel();
		B2BUnitData unitdata = new B2BUnitData();
		userGroupData.setName("testname");
		userGroupData.setUid("testuid");
		userGroupData.setUnit(unitdata);
		Mockito.when(b2BCommerceB2BUserGroupService.getUserGroupForUID(Mockito.anyString(), Mockito.anyObject()))
				.thenReturn(userGroupmodel);
		Mockito.doNothing().when(searchRestrictionService).disableSearchRestrictions();
		Mockito.doNothing().when(searchRestrictionService).enableSearchRestrictions();
		Mockito.when(b2BCommerceUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unitModel);
		Mockito.doNothing().when(modelService).saveAll(userGroupmodel);
		gpDefaultB2BUserGroupFacade.updateUserGroup("groupuid", userGroupData, false);

	}

	@Test
	public void updateUserGroupTrueTest() {

		B2BUserGroupModel userGroupmodel = new B2BUserGroupModel();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		B2BUserGroupModel newCreated = new B2BUserGroupModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) newCreated.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		B2BUserGroupData userGroupData = new B2BUserGroupData();
		userGroupmodel = null;
		B2BUnitModel unitModel = new B2BUnitModel();
		B2BUnitData unitdata = new B2BUnitData();
		userGroupData.setName("testname");
		userGroupData.setUid("testuid");
		userGroupData.setUnit(unitdata);
		Mockito.when(b2BCommerceUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unitModel);
		Mockito.doNothing().when(searchRestrictionService).disableSearchRestrictions();
		Mockito.doNothing().when(searchRestrictionService).enableSearchRestrictions();
		Mockito.when(modelService.create(B2BUserGroupModel.class)).thenReturn(newCreated);
		Mockito.when(b2BCommerceB2BUserGroupService.getUserGroupForUID(Mockito.anyString(), Mockito.anyObject()))
				.thenReturn(userGroupmodel);
		Mockito.doNothing().when(modelService).saveAll(newCreated);
		gpDefaultB2BUserGroupFacade.updateUserGroup("groupuid", userGroupData, true);

	}

	private GPDefaultB2BUserGroupFacade createTestSubject() {
		return new GPDefaultB2BUserGroupFacade();
	}

	

}
