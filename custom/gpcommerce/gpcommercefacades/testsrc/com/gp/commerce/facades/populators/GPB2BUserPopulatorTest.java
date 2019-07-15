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

import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.user.UserService;

public class GPB2BUserPopulatorTest {
	
	@Mock
	B2BUnitService<B2BUnitModel, B2BCustomerModel> b2BUnitService;
	@Mock
	UserService userService;
	@Mock
	B2BUnitModel b2bUnitModel;
	@Mock
	private B2BCustomerModel source;
	@Mock
	private CustomerData target;
	@InjectMocks
	GPB2BUserPopulator gpb2bUserPopulator=new GPB2BUserPopulator();

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		gpb2bUserPopulator.setB2BUnitService(b2BUnitService);
		//gpb2bUserPopulator.setB2BUserGroupsLookUpStrategy(b2BUserGroupsLookUpStrategy);
		gpb2bUserPopulator.setUserService(userService);
		Mockito.when(source.getUserApprovalStatus()).thenReturn(GPUserApprovalStatusEnum.APPROVED);
		Mockito.when(source.getDefaultB2BUnit()).thenReturn(b2bUnitModel);
		Mockito.when(b2bUnitModel.getB2bUnitLevel()).thenReturn("L2");
		Mockito.when(source.getNewEmail()).thenReturn("email@email.com");
		Mockito.when(source.getUid()).thenReturn("Customer");
		Mockito.when(source.getActive()).thenReturn(true);
		Mockito.when(b2BUnitService.getParent(source)).thenReturn(b2bUnitModel);
		Mockito.when(source.getGroups()).thenReturn(Collections.EMPTY_SET);
		Mockito.when(target.getUid()).thenReturn("Customer");
		
	}
	
	@Test
	public void testPopulate()
	{
		gpb2bUserPopulator.populate(source, target);
		Mockito.verify(target).setUserApprovalStatus(Mockito.anyString());
		Mockito.verify(target).setEdited(Mockito.anyBoolean());
	}
	
	@Test
	public void testPopulateL1User()
	{
		Mockito.when(b2bUnitModel.getB2bUnitLevel()).thenReturn("L1");
		gpb2bUserPopulator.populate(source, target);
		Mockito.verify(target).setUserApprovalStatus(Mockito.anyString());
		Mockito.verify(target).setEdited(Mockito.anyBoolean());

	}
}
