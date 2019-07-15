package com.gp.commerce.core.services.usergroups.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.services.usergroups.impl.GPDefaultB2BUserGroupService;
import com.gp.commerce.core.user.dao.GPUserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UnitTest
public class GPDefaultB2BUserGroupServiceTest {

	@InjectMocks
	private final GPDefaultB2BUserGroupService service = new GPDefaultB2BUserGroupService();
    @Mock
    private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;
    @Mock
    private SearchRestrictionService searchRestrictionService;
    @Mock
    private B2BCommerceUnitService b2BCommerceUnitService;
    @Mock
    private PrincipalGroupModel principalGroupModel;
    @Mock
    private B2BUserGroupModel b2bUserGroupModel;
    @Mock
    private B2BCustomerModel b2bCustomerModel;
    @Mock
    private ModelService modelService;
    @Mock
    private B2BUnitModel b2bUnitModel;
    @Mock
    private B2BUnitData b2bUnitData;
    @Mock
    private UserService userService;
    @Mock
    private GPUserDao gpUserDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void updateUserGroupTest() {
		B2BUserGroupData userGroupData = new B2BUserGroupData();
		b2bUnitData.setUid("uid");
		userGroupData.setName("name");
		userGroupData.setUnit(b2bUnitData);
		userGroupData.setUid("uid");
		
		when(b2BCommerceB2BUserGroupService.getUserGroupForUID("uid", B2BUserGroupModel.class)).thenReturn(b2bUserGroupModel);
		when(b2BCommerceUnitService.getUnitForUid(Mockito.anyString())).thenReturn(b2bUnitModel);
		
		doNothing().when(searchRestrictionService).disableSearchRestrictions();
		doNothing().when(modelService).save(b2bUserGroupModel);
		doNothing().when(searchRestrictionService).enableSearchRestrictions();
		
		service.updateUserGroup("uid", userGroupData, false);
		verify(modelService, times(1)).save(b2bUserGroupModel);
		verify(b2BCommerceB2BUserGroupService, times(1)).getUserGroupForUID("uid", B2BUserGroupModel.class);
		verify(b2BCommerceUnitService, times(1)).getUnitForUid(Mockito.anyString());
		verify(searchRestrictionService, times(1)).enableSearchRestrictions();
		verify(modelService, times(0)).create(B2BUserGroupModel.class);
	}
	
	@Test
	public void updateUserGroupTestByCreatingGroup() {
		B2BUserGroupData userGroupData = new B2BUserGroupData();
		b2bUnitData.setUid("uid");
		userGroupData.setName("name");
		userGroupData.setUnit(b2bUnitData);
		userGroupData.setUid("uid");
		
		when(b2BCommerceB2BUserGroupService.getUserGroupForUID("uid", B2BUserGroupModel.class)).thenReturn(null);
		when(b2BCommerceUnitService.getUnitForUid(Mockito.anyString())).thenReturn(b2bUnitModel);
		when(modelService.create(B2BUserGroupModel.class)).thenReturn(b2bUserGroupModel);
		
		doNothing().when(searchRestrictionService).disableSearchRestrictions();
		doNothing().when(modelService).save(b2bUserGroupModel);
		doNothing().when(searchRestrictionService).enableSearchRestrictions();
		
		service.updateUserGroup("uid", userGroupData, true);
		verify(modelService, times(1)).save(b2bUserGroupModel);
		verify(b2BCommerceB2BUserGroupService, times(1)).getUserGroupForUID("uid", B2BUserGroupModel.class);
		verify(b2BCommerceUnitService, times(1)).getUnitForUid(Mockito.anyString());
		verify(searchRestrictionService, times(1)).enableSearchRestrictions();
		verify(modelService, times(1)).create(B2BUserGroupModel.class);
	}
	
	@Test
	public void addUsersToUserGroupTest() {
		List<String> codes = new ArrayList<String>();
		codes.add(Mockito.anyString());
		List<B2BCustomerModel> users = new ArrayList<B2BCustomerModel>();
		users.add(b2bCustomerModel);
		
		when(gpUserDao.findUsersByCodes(codes)).thenReturn(users);
		when(b2BCommerceB2BUserGroupService.getUserGroupForUID("uid", B2BUserGroupModel.class)).thenReturn(b2bUserGroupModel);
		
		doNothing().when(modelService).saveAll(b2bUserGroupModel);
		doNothing().when(searchRestrictionService).enableSearchRestrictions();	
		doNothing().when(searchRestrictionService).disableSearchRestrictions();
		
		service.addUsersToUserGroup("uid", codes);
		verify(b2BCommerceB2BUserGroupService, times(1)).getUserGroupForUID("uid", B2BUserGroupModel.class);
		verify(searchRestrictionService, times(1)).enableSearchRestrictions();
		verify(searchRestrictionService, times(1)).disableSearchRestrictions();
	}
	
	@Test
	public void addUsersToUserGroupTestWithEmptyList() {
		List<String> codes = new ArrayList<String>();
		codes.add(Mockito.anyString());
		List<B2BCustomerModel> users = new ArrayList<B2BCustomerModel>();
		users.add(b2bCustomerModel);
		
		when(gpUserDao.findUsersByCodes(codes)).thenReturn(users);
		when(b2BCommerceB2BUserGroupService.getUserGroupForUID("uid", B2BUserGroupModel.class)).thenReturn(b2bUserGroupModel);
		
		doNothing().when(modelService).saveAll(b2bUserGroupModel);
		doNothing().when(searchRestrictionService).enableSearchRestrictions();	
		doNothing().when(searchRestrictionService).disableSearchRestrictions();
		
		service.addUsersToUserGroup("uid", codes);
		verify(b2BCommerceB2BUserGroupService, times(1)).getUserGroupForUID("uid", B2BUserGroupModel.class);
		verify(searchRestrictionService, times(1)).enableSearchRestrictions();
		verify(searchRestrictionService, times(1)).disableSearchRestrictions();
		assertTrue(b2bUserGroupModel.getMembers().isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void addUserGroupsToUserTest() {
		b2bCustomerModel.setGroups(Mockito.anySet());
		List<String> codes = new ArrayList<String>();
		
		when(userService.getUserForUID("uid", B2BCustomerModel.class)).thenReturn(b2bCustomerModel);
		Set<PrincipalGroupModel> groups = new java.util.HashSet<PrincipalGroupModel>();
		when(b2bCustomerModel.getGroups()).thenReturn(groups);
		
		doNothing().when(modelService).saveAll(b2bCustomerModel);
		doNothing().when(searchRestrictionService).enableSearchRestrictions();
		
		service.addUserGroupsToUser("uid", codes);
		verify(b2bCustomerModel, times(2)).getGroups();
		verify(userService, times(1)).getUserForUID("uid", B2BCustomerModel.class);
		verify(searchRestrictionService, times(1)).enableSearchRestrictions();
		
	}
	
	@Test(expected = GPCommerceBusinessException.class)
	public void throwsGPCommerceBusinessException() {
		B2BUserGroupData userGroupData = new B2BUserGroupData();
		when(b2BCommerceB2BUserGroupService.getUserGroupForUID("uid", B2BUserGroupModel.class)).thenReturn(b2bUserGroupModel);
		when(b2BCommerceUnitService.getUnitForUid(Mockito.anyString())).thenReturn(b2bUnitModel);
		when(modelService.create(B2BUserGroupModel.class)).thenReturn(b2bUserGroupModel);
		doNothing().when(searchRestrictionService).disableSearchRestrictions();
		service.updateUserGroup("uid", userGroupData, true);
	}
	
}
