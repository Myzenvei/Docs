package com.gp.commerce.core.services.permission.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.dao.impl.DefaultGPB2BPermissionDao;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import com.gp.commerce.core.services.permission.impl.GPDefaultB2BPermissionService;

@UnitTest
public class GPDefaultB2BPermissionServiceTest {
	
	@InjectMocks
	GPDefaultB2BPermissionService gpDefaultB2BPermissionService = new GPDefaultB2BPermissionService();
	
	@Mock
	DefaultGPB2BPermissionDao defaultGPB2BPermissionDao;
	private B2BPermissionModel model  ;
	private UserB2BUnitWsDTO userB2BUnitWsDTO ;
	@Mock
	private UserService userService;
	private B2BCustomerModel b2bCustomer ;
	@Mock
	private  SearchRestrictionService searchRestrictionService;
	@Mock
	private ModelService modelService ;
	@Mock
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService ;
	@Mock
	private B2BUserGroupModel b2BUserGroupModel ;
	
	public GPDefaultB2BPermissionServiceTest()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Before
	public void setUp() throws Exception {
		model=	Mockito.mock(B2BPermissionModel.class) ;
		userB2BUnitWsDTO=Mockito.mock(UserB2BUnitWsDTO.class);
		b2bCustomer=Mockito.mock(B2BCustomerModel.class);
		gpDefaultB2BPermissionService.setDefaultGPB2BPermissionDao(defaultGPB2BPermissionDao);
		ReflectionTestUtils.setField(gpDefaultB2BPermissionService, "userService", userService);
		ReflectionTestUtils.setField(gpDefaultB2BPermissionService, "modelService", modelService);

	}
	
	@Test
	public void findPermissionsByUnitsTest(){
	 
		List<String> listString = new ArrayList();
		listString.add("testList1");
		userB2BUnitWsDTO.setB2BUnitList(listString);
		B2BUnitModel unit = new B2BUnitModel();
		unit.setId("testList1");
		List<B2BPermissionModel> list = new ArrayList<>();
		list.add(model);
		Mockito.when(defaultGPB2BPermissionDao.findPermissionsByUnits(userB2BUnitWsDTO)).thenReturn(list);
		List<B2BPermissionModel> permission=gpDefaultB2BPermissionService.findPermissionsByUnits(userB2BUnitWsDTO);
		Assert.assertEquals(model,permission.get(0));
}
	
	@Test
	public void findPermissionsByCodesTest(){
		List<String> listString = new ArrayList<>();
		listString.add("testString");
		B2BUnitModel unit = new B2BUnitModel();
		unit.setId("testString");
		B2BPermissionModel model = new B2BPermissionModel();
		model.setUnit(unit);
		List<B2BPermissionModel> list = new ArrayList<>();
		list.add(model);
		List<B2BPermissionModel> permission = new ArrayList<>();
		Mockito.when(defaultGPB2BPermissionDao.findPermissionsByCodes(listString)).thenReturn(list) ;
		List<B2BPermissionModel> permissionForCode=gpDefaultB2BPermissionService.findPermissionsByCodes(listString);
		Assert.assertEquals(model,permissionForCode.get(0));
		 
	}
	
	@Test
	public void addPermissionsToUserTest() {
		List<String> list = new ArrayList<>();
		list.add("code");
		Mockito.when(userService.getUserForUID(Mockito.anyString() ,Mockito.anyObject())).thenReturn(b2bCustomer) ;
		gpDefaultB2BPermissionService.addPermissionsToUser(Mockito.anyString(), list);
	}
	
	@Test
	public void addPermissionsToUserGroupTest() {
		List<String> list = new ArrayList<>();
		list.add("code");
		Mockito.when(b2BCommerceB2BUserGroupService.getUserGroupForUID(Mockito.anyString(),Mockito.anyObject())).thenReturn(b2BUserGroupModel);
		gpDefaultB2BPermissionService.addPermissionsToUserGroup(Mockito.anyString(), list);
	}
}
