package com.gp.commerce.core.company.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.company.dao.GPB2BUnitDao;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.user.dao.GPUserDao;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.codegenerator.model.ModelSetterWriter;
import de.hybris.platform.b2b.dao.PrincipalGroupMembersDao;
import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

@UnitTest
public class GPDefaultB2BUnitsServiceTest {

	@Mock
	private DefaultB2BUnitService b2bUnitService;

	@Mock
	private B2BUnitModel b2bUnitModel;

	@Mock
	private ModelService modelService;

	@Mock
	private PrincipalGroupModel principalGroupModel;

	@Mock
	private UserService userService;

	@Mock
	private GPB2BUnitDao gpb2bUnitDao;

	@InjectMocks
	GPDefaultB2BUnitsService gpDefaultB2BUnitsService = new GPDefaultB2BUnitsService();

	@Mock
	private PrincipalGroupMembersDao principalGroupMembersDao;

	@Mock
	protected GPUserDao gPUserDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}



	@Test
	public void disableBranchTest() {

		B2BUnitModel unit = new B2BUnitModel();
		List<B2BBudgetModel> budgets = new ArrayList<>();
		List<B2BUnitModel> units = new ArrayList<>();
		List<AddressModel> addresses = new ArrayList<>();
		AddressModel address = new AddressModel();
		address.setApprovalStatus(GPApprovalEnum.PENDING);
		addresses.add(address);
		B2BBudgetModel bud = new B2BBudgetModel();
		budgets.add(bud);
		unit.setBudgets(budgets);
		Set<B2BPermissionModel> permissions = new HashSet<>();
		B2BPermissionModel permission = new B2BPermissionModel();
		permissions.add(permission);
		unit.setPermissions(permissions);
		Mockito.doNothing().when(modelService).saveAll(budgets);
		Mockito.doNothing().when(modelService).saveAll(permissions);
		Mockito.doNothing().when(modelService).removeAll(Mockito.anyCollectionOf(AddressModel.class));
		Mockito.doNothing().when(modelService).saveAll(Mockito.anyCollection());
		Mockito.doNothing().when(modelService).saveAll(Mockito.anyCollectionOf(B2BUnitModel.class));
		Mockito.when(gPUserDao.getAddressesForB2BUser(units, true)).thenReturn(addresses);
		gpDefaultB2BUnitsService.disableBranch(unit, true);
	}

	@Test
	public void getUnitNodesTest() {
		final B2BCustomerModel employee = new B2BCustomerModel();
		final Set<PrincipalGroupModel> list = new HashSet<>();
		PrincipalGroupModel model = new PrincipalGroupModel();
		list.add(model);
		List<B2BUnitModel> unit = new ArrayList<>();
		employee.setUid("Test123");
		employee.setGroups(list);
		unit = gpDefaultB2BUnitsService.getUnitNodes(employee);
		Assert.assertEquals(0, unit.size());
	}

	@Test
	public void getUnitForUidTest() {
		final String uid = "TestABC";
		B2BUnitModel unit = new B2BUnitModel();
		unit.setUid(uid);
		Mockito.when(userService.getUserGroupForUID(uid, B2BUnitModel.class)).thenReturn(unit);
		unit = gpDefaultB2BUnitsService.getUnitForUid(uid);
		Assert.assertEquals(unit.getUid(), uid);
	}

	@Test
	public void getUsersOfUserGroupTest() {
		final List<B2BUnitModel> units = new ArrayList<>();
		final List<String> userGroupIds = new ArrayList<>();
		final List<B2BCustomerModel> employeesOfGroup = new ArrayList<>();
		final B2BCustomerModel customer = new B2BCustomerModel();
		customer.setUid("Test123");
		employeesOfGroup.add(customer);
		Mockito.when(gpb2bUnitDao.findB2BUnitMembersByGroup(units, userGroupIds)).thenReturn(employeesOfGroup);
		Assert.assertEquals("Test123", employeesOfGroup.get(0).getUid());
	}

	@Test
	public void isB2BAdminTest() {
		boolean testBool;
		final B2BCustomerModel customer = new B2BCustomerModel();
		final Set<PrincipalGroupModel> userGroups = new HashSet<PrincipalGroupModel>();
		PrincipalGroupModel group = new PrincipalGroupModel();
		group.setUid("b2badmingroup");
		userGroups.add(group);
		customer.setGroups(userGroups);
		testBool = gpDefaultB2BUnitsService.isB2BAdmin(customer);
		Assert.assertNotNull(testBool);
	}

	@Test
	public void removeB2BUserGroupFromCustomerGroupsTest() {
		final B2BCustomerModel customer = new B2BCustomerModel();
		final Set<PrincipalGroupModel> groupsWithoutUsergroup = new HashSet<PrincipalGroupModel>();
		final B2BUnitModel defaultB2BUnit = new B2BUnitModel();
		final List<B2BUnitModel> units = new ArrayList<B2BUnitModel>();
		final Set<PrincipalGroupModel> testSet = new HashSet<PrincipalGroupModel>();
		final B2BUnitModel unitModel = new B2BUnitModel();
		unitModel.setUid("testUid");
		PrincipalGroupModel model = new PrincipalGroupModel();
		model.setUid("testUid");
		testSet.add(model);
		customer.setGroups(testSet);
		customer.setDefaultB2BUnit(unitModel);
		Mockito.when(userService.getUserForUID(Mockito.anyString(), Mockito.anyObject())).thenReturn(customer);
		gpDefaultB2BUnitsService.removeB2BUserGroupFromCustomerGroups(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void removeUsergroupFromGroupsTest() {
		Set<PrincipalGroupModel> testModel = new HashSet<PrincipalGroupModel>();
		Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
		PrincipalGroupModel model = new PrincipalGroupModel();
		model.setUid("testUid");
		groups.add(model);
		testModel = gpDefaultB2BUnitsService.removeUsergroupFromGroups(Mockito.anyString(), groups);
		Assert.assertNotNull(testModel);
	}

	@Test
	public void getUsersforB2BUnitTest() {

		B2BUnitModel units = new B2BUnitModel();
		List<B2BCustomerModel> customers = new ArrayList<>();
		String userGroupIds = "test";
		boolean isPrimaryAdmin = true;
		Mockito.when(gpb2bUnitDao.getUsersforB2BUnit(units, userGroupIds, isPrimaryAdmin)).thenReturn(customers);
		Assert.assertNotNull(gpDefaultB2BUnitsService.getUsersforB2BUnit(units, userGroupIds, isPrimaryAdmin));

	}

	@Test
	public void getUnitsWithChildNodesTest() {

		B2BCustomerModel employee = new B2BCustomerModel();
		final Set<PrincipalGroupModel> list = new HashSet<>();
		final Set<PrincipalModel> members = new HashSet<>();
		PrincipalGroupModel model = new PrincipalGroupModel();
		model.setMembers(members);
		list.add(model);
		employee.setGroups(list);
		Assert.assertNotNull(gpDefaultB2BUnitsService.getUnitsWithChildNodes(employee, true));

	}

}
