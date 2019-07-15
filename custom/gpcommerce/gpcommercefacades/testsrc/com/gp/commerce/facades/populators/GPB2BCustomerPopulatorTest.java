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
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2b.strategies.B2BUserGroupsLookUpStrategy;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

@UnitTest
public class GPB2BCustomerPopulatorTest {
	
	@Mock
	private B2BUnitService<B2BUnitModel, UserModel> b2bUnitService;

	@Mock
	private Converter<AddressModel, AddressData> addressConverter;
	
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private Converter<CurrencyModel, CurrencyData> currencyConverter;
	@Mock
	private B2BUserGroupsLookUpStrategy b2BUserGroupsLookUpStrategy;
	@Mock
	B2BUnitModel b2bUnitModel;
	@Mock
	B2BUnitData b2bUnitData;
	@Mock
	private B2BCustomerModel source; 
	@Mock
	private CustomerData target;
	@Mock
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	@InjectMocks
	GPB2BCustomerPopulator gpb2bCustomerPopulator=new GPB2BCustomerPopulator();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		gpb2bCustomerPopulator.setB2bUnitService(b2bUnitService);
		gpb2bCustomerPopulator.setCommonI18NService(commonI18NService);
		gpb2bCustomerPopulator.setB2BUserGroupsLookUpStrategy(b2BUserGroupsLookUpStrategy);
		gpb2bCustomerPopulator.setCurrencyConverter(currencyConverter);
		ReflectionTestUtils.setField(gpb2bCustomerPopulator, "gpDefaultCustomerNameStrategy", gpDefaultCustomerNameStrategy);
		ReflectionTestUtils.setField(gpb2bCustomerPopulator, "addressConverter", addressConverter);
		
		Mockito.when(source.getCellPhone()).thenReturn("897867564");
		Mockito.when(source.isLeaseSigner()).thenReturn(true);
		Mockito.when(source.getName()).thenReturn("Name");
		Mockito.when(source.getUid()).thenReturn("CustomerUid");
		Mockito.when(source.getActive()).thenReturn(true);
		Mockito.when(source.getTitle()).thenReturn(Mockito.mock(TitleModel.class));
		Mockito.when(source.getTitle().getCode()).thenReturn("Mr");
		Mockito.when(gpDefaultCustomerNameStrategy.getName("Name")).thenReturn("Name");
		Mockito.when(target.getUnit()).thenReturn(b2bUnitData);
		Mockito.when(b2bUnitService.getParent(source)).thenReturn(b2bUnitModel);
		Mockito.when(b2bUnitModel.getB2bUnitLevel()).thenReturn("Level1");
		Mockito.when(b2bUnitModel.getRole()).thenReturn("Role1");
		AddressModel address=Mockito.mock(AddressModel.class);
		Mockito.when(source.getAddresses()).thenReturn(Collections.singleton(address));
		Mockito.when(b2bUnitModel.getAddresses()).thenReturn(Collections.singleton(address));
		Mockito.when(addressConverter.convert(address)).thenReturn(Mockito.mock(AddressData.class));
		
	}
	
	@Test
	public void testPopulate()
	{
		gpb2bCustomerPopulator.populate(source, target);
		Mockito.verify(target).setUnit(b2bUnitData);
		Mockito.verify(target).setCellPhone("897867564");;
		Mockito.verify(b2bUnitData).setAddresses(Mockito.anyList());
	}

}
