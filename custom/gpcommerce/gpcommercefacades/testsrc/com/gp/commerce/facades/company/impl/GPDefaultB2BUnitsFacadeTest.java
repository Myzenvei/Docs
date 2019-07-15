package com.gp.commerce.facades.company.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.company.B2BCommerceUserService;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.organization.services.OrgUnitHierarchyService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import static org.mockito.BDDMockito.given;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.company.services.GPB2BUnitsService;

@UnitTest
public class GPDefaultB2BUnitsFacadeTest {

	@InjectMocks
	GPDefaultB2BUnitsFacade gpDefaultB2BUnitsFacade = new GPDefaultB2BUnitsFacade();

	@Mock
	protected UserService userService;

	@Mock
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@Mock
	protected B2BUnitService<B2BUnitModel, UserModel> b2bUnitService;

	@Mock
	protected ModelService modelService;

	@Mock
	protected B2BCommerceUserService b2BCommerceUserService;

	@Mock
	protected Populator<CustomerData, B2BCustomerModel> customerReversePopulator;

	@Mock
	protected B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Mock
	protected Converter<B2BUnitModel, B2BUnitNodeData> unitTreeConverter;

	@Mock
	protected Converter<CustomerModel, CustomerData> b2BCustomerConverter;

	@Mock
	protected Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;

	@Mock
	protected OrgUnitHierarchyService orgUnitHierarchyService;

	@Mock
	private Configuration configuration;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	protected Converter<B2BUnitData, B2BUnitModel> b2BUnitReverseConverter;

	@Mock
	protected B2BCommerceUnitService b2BCommerceUnitService;

	@Mock
	protected SessionService sessionService;

	B2BUnitModel unitModel = mock(B2BUnitModel.class);

	B2BCustomerModel b2bCustomer = mock(B2BCustomerModel.class);

	@Mock
	private Session session;

	String unitUid = "abcd";

	public GPDefaultB2BUnitsFacadeTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void disableUnitTest() {
		final B2BUnitModel unit = new B2BUnitModel();
		unit.setUid("testUser");
		Mockito.when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		Mockito.doNothing().when(gpB2BUnitsService).disableBranch(unit, Boolean.FALSE);
		gpDefaultB2BUnitsFacade.disableUnit(Mockito.anyString(), Boolean.FALSE);
		Assert.assertEquals("testUser", unit.getUid());
	}

	@Test
	public void addUnitToUserTest() {
		final B2BCustomerModel customer = new B2BCustomerModel();
		final B2BUnitModel unit = new B2BUnitModel();
		final B2BUnitNodeData nodeData = new B2BUnitNodeData();
		final String unitId = "testUnit";
		final String userId = "testUser";
		unit.setUid("testUser");
		Mockito.when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.doNothing().when(b2bUnitService).addMember(unit, customer);
		Mockito.doNothing().when(modelService).save(customer);
		Mockito.when(unitTreeConverter.convert(Mockito.anyObject())).thenReturn(nodeData);
		gpDefaultB2BUnitsFacade.addUnitToUser(unitId, userId);
		Assert.assertEquals("testUser", unit.getUid());
	}

	@Test
	public void removeUnitFromUserTest() {
		final B2BUnitModel unit = new B2BUnitModel();
		unit.setUid("testUser");
		final B2BCustomerModel customer = new B2BCustomerModel();
		customer.setUid("testUser");
		customer.setCustomerID("testCustomer123");
		Mockito.when(userService.getUserForUID(Mockito.anyString())).thenReturn(customer);
		Mockito.doNothing().when(gpB2BUnitsService).removeB2BUserGroupFromCustomerGroups(customer.getCustomerID(),
				unit.getUid());
		gpDefaultB2BUnitsFacade.removeUnitFromUser(unit.getUid(), customer.getUid());
		Assert.assertEquals("testUser", customer.getUid());
	}

	@Test
	public void addUserToUnitTest() {
		final B2BUnitModel unit = new B2BUnitModel();
		unit.setUid("testUser");
		final B2BCustomerModel customer = new B2BCustomerModel();
		customer.setUid("testUser");
		Set<PrincipalGroupModel> testModel = new HashSet();
		PrincipalGroupModel testGroup = new PrincipalGroupModel();
		testModel.add(testGroup);
		customer.setGroups(testModel);
		final CustomerData customerData = new CustomerData();
		final List<String> roles = new ArrayList<>();
		roles.add("b2bcustomergroup");
		customerData.setRoles(roles);
		Mockito.when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		Mockito.when(userService.getUserForUID(Mockito.anyString())).thenReturn(customer);
		Mockito.doNothing().when(b2bCustomerService).addMember(customer, unit);
		Mockito.doNothing().when(modelService).save(unit);
		Mockito.when(b2BCustomerConverter.convert(Mockito.anyObject())).thenReturn(customerData);
		gpDefaultB2BUnitsFacade.addUserToUnit(unit.getUid(), customerData);
	}

	

	@Test
	public void getUsersOfUserGroupTest() {
		
		final B2BCustomerModel currentUser = new B2BCustomerModel();
		final List<B2BUnitModel> units = new ArrayList<>();
		final List<B2BCustomerModel> b2BCustomers = new ArrayList<>();
		b2BCustomers.add(currentUser);
		Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);
		Mockito.when(gpB2BUnitsService.getUnitsWithChildNodes(currentUser,true)).thenReturn(units);
		Mockito.when(gpB2BUnitsService.getUsersOfUserGroup(Mockito.anyList(),Mockito.anyList())).thenReturn(b2BCustomers);
		List<CustomerData> customers = gpDefaultB2BUnitsFacade.getUsersOfUserGroup();
		Mockito.verify(b2BCustomerConverter).convert(currentUser);
	}

	@Test
	public void getUnitForUid() {
		final String unitUid = "testUid";
		final B2BUnitModel unitModel = new B2BUnitModel();
		final B2BUnitData unit = new B2BUnitData();
		unit.setUid(unitUid);
		B2BUnitData testData = new B2BUnitData();
		testData.setUnit(unit);
		final CustomerData testCust = new CustomerData();
		testCust.setUid("testUid");
		final Collection<CustomerData> admins = new ArrayList<>();
		admins.add(testCust);
		unit.setAdministrators(admins);
		final Collection<CustomerData> customers = new ArrayList<>();
		customers.add(testCust);
		unit.setCustomers(customers);
		Mockito.when(b2bUnitService.getUnitForUid(unitUid)).thenReturn(unitModel);
		Mockito.when(b2bUnitConverter.convert(unitModel)).thenReturn(unit);
		testData = gpDefaultB2BUnitsFacade.getUnitForUid(unitUid);
		Assert.assertEquals("testUid", testData.getUid());
	}

	@Test
	public void getUnitsForUser() {
		final String userId = "testUid";
		final B2BCustomerModel currentUser =  Mockito.mock(B2BCustomerModel.class);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		B2BUnitNodeData node =Mockito.mock(B2BUnitNodeData.class);
		Mockito.when(node.getId()).thenReturn("UnitID");
		List<B2BUnitNodeData> testNodeData = new ArrayList<>();
		testNodeData.add(node);
		Mockito.when(unit.getUid()).thenReturn("testUid");
		Mockito.when(unit.getActive()).thenReturn(true);
		Mockito.when(unit.getId()).thenReturn("testid");
		final List<B2BUnitModel> units = new ArrayList<>();
		units.add(unit);
		final List<B2BUnitModel> activeUnits = new ArrayList<>();
		activeUnits.add(unit);
		Mockito.when(userService.getUserForUID(userId)).thenReturn(currentUser);
		Mockito.when(gpB2BUnitsService.getUnitNodes(currentUser)).thenReturn(units);
		Mockito.when(unitTreeConverter.convert(unit)).thenReturn(node);
		
		Assert.assertTrue(gpDefaultB2BUnitsFacade.getUnitsForUser(userId).contains(node));
	}

	@Test
	public void updateOrCreateBusinessUnitTestForNewModel() {
		B2BUnitNodeData b2BUnitNodeData = new B2BUnitNodeData();
		B2BUnitModel unitModel = new B2BUnitModel();
		String originalUid = null;
		B2BUnitData b2BUnitData = new B2BUnitData();
		b2BUnitData.setName("testName");
		B2BUnitModel parentunitAfter = new B2BUnitModel();
		parentunitAfter.setB2bUnitLevel("L2");
		unitModel.setB2bUnitLevel("L2");
		when(modelService.create(B2BUnitModel.class)).thenReturn(unitModel);
		when(b2BUnitReverseConverter.convert(Mockito.anyObject())).thenReturn(unitModel);
		when(b2BCommerceUnitService.getParentUnit(unitModel)).thenReturn(parentunitAfter);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getBoolean("commerceservices.org.unit.path.generation.enabled",
				true)).thenReturn(false);
		Mockito.when(userService.getCurrentUser()).thenReturn(b2bCustomer);
		when(sessionService.getCurrentSession()).thenReturn(session);
		// Mockito.doAnswer((i) ->{return null;}
		// ).when(b2bUnitService).updateBranchInSession(sessionService.getCurrentSession(),b2bCustomer);
		Mockito.doNothing().when(b2bUnitService).updateBranchInSession(session, b2bCustomer);
		Mockito.when(unitTreeConverter.convert(Mockito.anyObject())).thenReturn(b2BUnitNodeData);
		b2BUnitNodeData = gpDefaultB2BUnitsFacade.updateOrCreateBusinessUnit(originalUid, b2BUnitData);
		assertEquals(b2BUnitNodeData,b2BUnitNodeData);

	}

	@Test
	public void updateOrCreateBusinessUnitTest() {
		B2BUnitData b2BUnitData = new B2BUnitData();
		b2BUnitData.setName("testName");
		final B2BUnitModel unit = new B2BUnitModel();
		final B2BUnitModel parentModelBefore = new B2BUnitModel();
		parentModelBefore.setB2bUnitLevel("L2");
		final B2BUnitModel b2bafter = new B2BUnitModel();
		b2bafter.setB2bUnitLevel("L2");
		final B2BUnitNodeData nodeData = new B2BUnitNodeData();
		unit.setUid("L2");
		String originalUid = "testUser";
		B2BUnitModel parentUnit = new B2BUnitModel();
		when(gpB2BUnitsService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		when(b2BCommerceUnitService.getParentUnit(unit)).thenReturn(parentModelBefore);
		when(b2BUnitReverseConverter.convert(Mockito.any(B2BUnitData.class), Mockito.any(B2BUnitModel.class)))
				.thenReturn(b2bafter);
		when(b2BCommerceUnitService.getParentUnit(b2bafter)).thenReturn(parentModelBefore);
		when(b2BUnitReverseConverter.convert(Mockito.anyObject())).thenReturn(unit);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configurationService.getConfiguration().getBoolean("commerceservices.org.unit.path.generation.enabled",
				true)).thenReturn(true);
		Mockito.doNothing().when(orgUnitHierarchyService).saveChangesAndUpdateUnitPath(unit);
		Mockito.doNothing().when(modelService).save(unit);
		gpDefaultB2BUnitsFacade.updateOrCreateBusinessUnit(originalUid, b2BUnitData);
	}

	@Test
	public void getUnitNodestest() {
		final B2BCustomerModel currentUser = new B2BCustomerModel();
		final List<B2BUnitModel> units = new ArrayList<>();
		final B2BUnitModel unit=new B2BUnitModel();
		units.add(unit);
		final B2BUnitNodeData unitNodes = new B2BUnitNodeData();
		List<B2BUnitNodeData> testNodeData = new ArrayList<>();
		unitNodes.setActive(true);
		unitNodes.setId("Id1");
		currentUser.setUid("test@test.com");
		when(userService.getUserForUID("test@test.com")).thenReturn(currentUser);
		when(gpB2BUnitsService.getUnitsWithChildNodes(currentUser, false)).thenReturn(units);
		Mockito.when(unitTreeConverter.convert(unit)).thenReturn(unitNodes);
		Assert.assertTrue(gpDefaultB2BUnitsFacade.getUnitNodes("test@test.com", true).contains(unitNodes));
	}

	@Test
	public void getUsersOfUserGroupForUnitTest() {

		CustomerData customerData=Mockito.mock(CustomerData.class);
		final B2BCustomerModel currentUser = Mockito.mock(B2BCustomerModel.class);
		final List<B2BCustomerModel> b2BCustomers = new ArrayList<>();
		b2BCustomers.add(currentUser);
		when(userService.getCurrentUser()).thenReturn(currentUser);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		when(unit.getUid()).thenReturn("testUser");
		when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		when(gpB2BUnitsService.getUsersOfUserGroup(Mockito.anyList(),Mockito.anyList())).thenReturn(b2BCustomers);
		when(b2BCustomerConverter.convert(currentUser)).thenReturn(customerData);
		
		Assert.assertTrue(gpDefaultB2BUnitsFacade.getUsersOfUserGroupForUnit(unitUid, "testgroup").contains(customerData));

	}
	

	@Test
	public void getUsersOfUserGroupTestForNullRole() {

		CustomerData customerData=Mockito.mock(CustomerData.class);
		final B2BCustomerModel currentUser = Mockito.mock(B2BCustomerModel.class);
		final List<B2BCustomerModel> b2BCustomers = new ArrayList<>();
		b2BCustomers.add(currentUser);
		when(userService.getCurrentUser()).thenReturn(currentUser);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		when(unit.getUid()).thenReturn("testUser");
		when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		when(gpB2BUnitsService.getUsersOfUserGroup(Mockito.anyList(),Mockito.anyList())).thenReturn(b2BCustomers);
		when(b2BCustomerConverter.convert(currentUser)).thenReturn(customerData);
		
		Assert.assertTrue(gpDefaultB2BUnitsFacade.getUsersOfUserGroupForUnit(unitUid,null).contains(customerData));

	}
	

	@Test
	public void getUsersOfUserGroupTestForB2BGroupRole() {

		CustomerData customerData=Mockito.mock(CustomerData.class);
		final B2BCustomerModel currentUser = Mockito.mock(B2BCustomerModel.class);
		final List<B2BCustomerModel> b2BCustomers = new ArrayList<>();
		b2BCustomers.add(currentUser);
		when(userService.getCurrentUser()).thenReturn(currentUser);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		when(unit.getUid()).thenReturn("testUser");
		when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		when(gpB2BUnitsService.getUsersOfUserGroup(Mockito.anyList(),Mockito.anyList())).thenReturn(b2BCustomers);
		when(b2BCustomerConverter.convert(currentUser)).thenReturn(customerData);
		
		Assert.assertTrue(gpDefaultB2BUnitsFacade.getUsersOfUserGroupForUnit(unitUid,B2BConstants.B2BCUSTOMERGROUP).contains(customerData));

	}
	
	@Test
	public void getUsersOfUserGroupTestForB2BAdminGroup() {

		CustomerData customerData=Mockito.mock(CustomerData.class);
		final B2BCustomerModel currentUser = Mockito.mock(B2BCustomerModel.class);
		final List<B2BCustomerModel> b2BCustomers = new ArrayList<>();
		b2BCustomers.add(currentUser);
		when(userService.getCurrentUser()).thenReturn(currentUser);
		final B2BUnitModel unit = Mockito.mock(B2BUnitModel.class);
		when(unit.getUid()).thenReturn("testUser");
		when(b2bUnitService.getUnitForUid(Mockito.anyString())).thenReturn(unit);
		when(gpB2BUnitsService.getUsersOfUserGroup(Mockito.anyList(),Mockito.anyList())).thenReturn(b2BCustomers);
		when(b2BCustomerConverter.convert(currentUser)).thenReturn(customerData);
		
		Assert.assertTrue(gpDefaultB2BUnitsFacade.getUsersOfUserGroupForUnit(unitUid,B2BConstants.B2BADMINGROUP).contains(customerData));

	}
	
	@Test
	public void testRemoveB2BUserGroupFromCustomerGroups()
	{
		gpDefaultB2BUnitsFacade.removeB2BUserGroupFromCustomerGroups("customer", "customergroup");
		Mockito.verify(gpB2BUnitsService).removeB2BUserGroupFromCustomerGroups("customer", "customergroup");
		
	}

}
