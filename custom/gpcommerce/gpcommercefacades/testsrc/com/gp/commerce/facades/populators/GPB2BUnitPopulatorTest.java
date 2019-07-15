/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.user.UserModel;

@UnitTest
public class GPB2BUnitPopulatorTest {

	private static final String ROLE = "role";
	
	@InjectMocks
	private GPB2BUnitPopulator populator = new GPB2BUnitPopulator();
	
	@Mock
	B2BUnitService<CompanyModel, UserModel> b2bUnitService;
	@Mock
	Collection<B2BCustomerModel> b2BCustomers;
	@Mock
	B2BUnitModel source;
	
	B2BUnitData target = new B2BUnitData();
	
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(source.getRole()).thenReturn(ROLE);
		Mockito.when(source.getNoOfEmployees()).thenReturn(200);
		Mockito.when(source.getTypeOfBusiness()).thenReturn("Private");
		Mockito.when(source.getLocName()).thenReturn("USA");
		Mockito.when(source.getUid()).thenReturn("A1990");
		Mockito.when(b2bUnitService.getUsersOfUserGroup(source, "1023", Boolean.FALSE)).thenReturn(b2BCustomers);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		populator.setB2BUnitService(null);
		Assert.assertEquals(target.getRole(), source.getRole());
		Assert.assertEquals(target.getNoOfEmployees(), source.getNoOfEmployees());
		Assert.assertEquals(target.getTypeOfBusiness(), source.getTypeOfBusiness());
	}
	
	
}
