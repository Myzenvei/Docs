package com.gp.commerce.facades.permissions.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.services.permission.impl.GPDefaultB2BPermissionService;
import com.gp.commerce.dto.company.data.B2BPermissionDataList;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionTypeData;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import junit.framework.Assert;

@UnitTest
public class GPDefaultB2BPermissionFacadeTest {
	
	@InjectMocks
	GPDefaultB2BPermissionFacade gpDefaultB2BPermissionFacade = new GPDefaultB2BPermissionFacade();
	
	@Mock
	private GPDefaultB2BPermissionService gpb2bPermissionService;
	
	@Mock
	private Converter<B2BPermissionModel, B2BPermissionData> b2BPermissionConverter;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private SearchRestrictionService searchRestrictionService;
	
	@Mock
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;
	
	public GPDefaultB2BPermissionFacadeTest()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void findPermissionsByUnits()
	{
		UserB2BUnitWsDTO userB2BUnitWsDTO = new UserB2BUnitWsDTO();
		List<String> listString = new ArrayList();
		listString.add("testList1");
		userB2BUnitWsDTO.setB2BUnitList(listString);
		B2BUnitModel unit = new B2BUnitModel();
		B2BPermissionModel model = new B2BPermissionModel();
		unit.setId("testList1");
		model.setUnit(unit);
		List<B2BPermissionModel> list = new ArrayList();
		list.add(model);
		Mockito.when(gpb2bPermissionService.findPermissionsByUnits(userB2BUnitWsDTO)).thenReturn(list);
		B2BPermissionData data = new B2BPermissionData();
		List<B2BPermissionData> dataList = new ArrayList();
		dataList.add(data);
		B2BPermissionDataList permissionList = new B2BPermissionDataList();
		permissionList.setPermissions(dataList);
		B2BPermissionDataList permissionListTest = new B2BPermissionDataList();
		Mockito.when(gpDefaultB2BPermissionFacade.findPermissionsByUnits(userB2BUnitWsDTO)).thenReturn(permissionList);
		//Mockito.doNothing().when(b2BPermissionConverter).convert(model,data);
		permissionListTest = gpDefaultB2BPermissionFacade.findPermissionsByUnits(userB2BUnitWsDTO);
		Assert.assertNotNull(permissionListTest.getPermissions());
	}
	
	@Test
	public void addPermissionsToUserGroup()
	{
		String test = "TestPermission";
		String uid = "TestUid";
		List<String> listString = new ArrayList();
		listString.add(test);
		B2BPermissionModel permission = new B2BPermissionModel();
		permission.setCode("TestPermission");
		List<B2BPermissionModel> list = new ArrayList();
		list.add(permission);
		B2BCustomerModel customer = new B2BCustomerModel();
		B2BUserGroupModel  userGroupModel = new B2BUserGroupModel();
		userGroupModel.setPermissions(list);
		Mockito.when(b2BCommerceB2BUserGroupService.getUserGroupForUID(uid,B2BUserGroupModel.class)).thenReturn(userGroupModel);
		B2BPermissionModel testPermission = new B2BPermissionModel();
		permission.setCode("TestPermission");
		Set<B2BPermissionModel> testSet = new HashSet();
		testSet.add(testPermission);
		Mockito.doNothing().when(modelService).save(userGroupModel);
		gpDefaultB2BPermissionFacade.addPermissionsToUserGroup(uid, listString);
	}
	
	@Test
	public void addPermissionsToUser()
	{
		String uid = "testCode";
		List<String> codes = new ArrayList<>();
		codes.add("testCode");
		final List<B2BPermissionModel> permissions = new ArrayList<>();
		B2BPermissionModel model = new B2BPermissionModel();
		model.setCode("testCode");
		permissions.add(model);
		Mockito.when(gpb2bPermissionService.findPermissionsByCodes(codes)).thenReturn(permissions);
		B2BPermissionModel testModel = new B2BPermissionModel();
		testModel.setCode("testCode");
		Set<B2BPermissionModel> testPermission = new HashSet();
		final B2BCustomerModel b2bCustomer = new B2BCustomerModel();
		b2bCustomer.setPermissions(testPermission);
		Mockito.when(userService.getUserForUID(Mockito.anyString(), Mockito.anyObject())).thenReturn(b2bCustomer);
		Mockito.doNothing().when(modelService).save(Mockito.anyObject());
		gpDefaultB2BPermissionFacade.addPermissionsToUser(uid, codes);
	}
}
