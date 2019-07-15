/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.enums.SelectRoleEnum;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2b.strategies.B2BUserGroupsLookUpStrategy;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

public class GPB2BCustomerReversePopulatorTest {

	private static final String B2BADMINGROUP = "b2badmingroup";
	private static final String B2BCUSTOMERGROUP = "b2bcustomergroup";

	@Mock
	B2BUnitModel b2bUnitModel;
	@Mock
	UserGroupModel existingUserGroupModel;
	@Mock
	UserGroupModel userGroupModel;
	@Mock
	BaseSiteModel baseSite;
	@Mock
	CustomerNameStrategy customerNameStrategy;
	@Mock
	B2BUserGroupsLookUpStrategy b2BUserGroupsLookUpStrategy;
	@Mock
	B2BCommerceB2BUserGroupService b2bCommerceB2BUserGroupService;
	@Mock
	private ModelService modelService;
	@Mock
	private UserService userService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CustomerData source;
	@Mock
	B2BUnitService<B2BUnitModel, UserModel> b2BUnitService;
	@Mock
	private B2BCustomerModel target;

	@InjectMocks
	GPB2BCustomerReversePopulator customerReversePopulator = new GPB2BCustomerReversePopulator();

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		customerReversePopulator.setB2BCommerceB2BUserGroupService(b2bCommerceB2BUserGroupService);
		customerReversePopulator.setB2BUserGroupsLookUpStrategy(b2BUserGroupsLookUpStrategy);
		customerReversePopulator.setB2BUnitService(b2BUnitService);
		customerReversePopulator.setBaseSiteService(baseSiteService);
		customerReversePopulator.setUserService(userService);
		customerReversePopulator.setModelService(modelService);
		customerReversePopulator.setCustomerNameStrategy(customerNameStrategy);

		Mockito.when(source.getEmail()).thenReturn("gpcommerce@gp.com");
		Mockito.when(source.getCellPhone()).thenReturn("3456789012");
		Mockito.when(source.isLeaseSigner()).thenReturn(true);
		Mockito.when(source.getSelectedRole()).thenReturn(B2BADMINGROUP);
		Mockito.when(source.getFirstName()).thenReturn("First");
		Mockito.when(source.getLastName()).thenReturn("Last");
		Mockito.when(source.getRoles()).thenReturn(Collections.emptyList());
		Mockito.when(source.getTitleCode()).thenReturn("Mr");
		Mockito.when(b2BUnitService.getParent(target)).thenReturn(b2bUnitModel);
		Mockito.when(b2BUnitService.getUnitForUid("B2BUnit")).thenReturn(b2bUnitModel);
		Mockito.when(source.getUnit()).thenReturn(Mockito.mock(B2BUnitData.class));
		Mockito.when(source.getUnit().getUid()).thenReturn("B2BUnit");
		Mockito.when(source.getDisplayUid()).thenReturn("CUstomer Uid");

		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSite);
		Mockito.when(target.getGroups()).thenReturn(Collections.singleton(userGroupModel));
		Mockito.when(userService.getUserGroupForUID(B2BADMINGROUP)).thenReturn(userGroupModel);
		Mockito.when(userService.getUserGroupForUID(B2BCUSTOMERGROUP)).thenReturn(userGroupModel);

		Mockito.when(userService.getTitleForCode("Mr")).thenReturn(Mockito.mock(TitleModel.class));
		Mockito.when(b2BUserGroupsLookUpStrategy.getUserGroups()).thenReturn(Collections.emptyList());
		Mockito.when(b2bCommerceB2BUserGroupService.updateUserGroups(b2BUserGroupsLookUpStrategy.getUserGroups(),
				source.getRoles(), target)).thenReturn(Collections.EMPTY_SET);

	}

	@Test
	public void testPopulateForAdmin() {
		customerReversePopulator.populate(source, target);
		Mockito.verify(target).setSelectedRole(Mockito.any());
		Mockito.verify(target).setCellPhone("3456789012");
		Mockito.verify(target).setLeaseSigner(true);
		
	}

	@Test
	public void testPopulateForCustomer() {
		Mockito.when(source.getSelectedRole()).thenReturn(B2BCUSTOMERGROUP);
		customerReversePopulator.populate(source, target);
		Mockito.verify(target).setSelectedRole(Mockito.any());
		Mockito.verify(target).setCellPhone("3456789012");
		Mockito.verify(target).setLeaseSigner(true);
		
	}

}
