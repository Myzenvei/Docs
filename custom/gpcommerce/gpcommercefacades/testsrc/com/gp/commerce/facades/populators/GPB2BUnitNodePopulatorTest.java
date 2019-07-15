/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class GPB2BUnitNodePopulatorTest {
	
	@Mock
	Converter<B2BCustomerModel, CustomerData> b2bCustomerConverter;
	@Mock
	Converter<AddressModel, AddressData> addressConverter;
	@Mock
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2BUnitService;
	@Mock
	AddressModel addressModel;
	@Mock
	private B2BUnitModel source;
	
	Collection<B2BCustomerModel> administrators=new ArrayList<>();
	@Mock
	private B2BUnitNodeData target;
	
	@InjectMocks
	GPB2BUnitNodePopulator gpb2bUnitNodePopulator=new GPB2BUnitNodePopulator();


	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		gpb2bUnitNodePopulator.setAddressConverter(addressConverter);
		gpb2bUnitNodePopulator.setB2BCustomerConverter(b2bCustomerConverter);
		gpb2bUnitNodePopulator.setB2BUnitService(b2BUnitService);
		Mockito.when(source.getUid()).thenReturn("User1");
		Mockito.when(source.getRole()).thenReturn("Role1");
		Mockito.when(source.getName()).thenReturn("User1");
		Mockito.when(source.getAddresses()).thenReturn(Collections.singletonList(addressModel));
		Mockito.when(source.getMembers()).thenReturn(Collections.EMPTY_SET);
		Mockito.when(b2BUnitService.getParent(source)).thenReturn(source);
		Mockito.when(addressConverter.convert(addressModel)).thenReturn(Mockito.mock(AddressData.class));
		Mockito.when(b2BUnitService.getUsersOfUserGroup(source,
				B2BConstants.B2BADMINGROUP, false)).thenReturn(Collections.singletonList(Mockito.mock(B2BCustomerModel.class)));
		Mockito.when(b2BUnitService.getUsersOfUserGroup(source,
				B2BConstants.B2BCUSTOMERGROUP, false)).thenReturn(Collections.singletonList(Mockito.mock(B2BCustomerModel.class)));
		
	}
	@Test
	public void testPopulate()
	{
		gpb2bUnitNodePopulator.populate(source, target);
		Mockito.verify(target).setAddresses(Mockito.anyList());
		Mockito.verify(target).setRole("Role1");
		Mockito.verify(target).setParentName("User1");
	}
}
