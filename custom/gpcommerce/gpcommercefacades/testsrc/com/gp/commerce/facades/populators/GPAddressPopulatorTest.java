/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.enums.GPApprovalEnum;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class GPAddressPopulatorTest {
	
	
	@Mock
	B2BCustomerModel customer;
	@Mock
	B2BUnitModel b2bUnitModel;
	@Mock
	CountryModel countryModel;
	@Mock
	RegionModel regionModel;
	@Mock
	private Converter<AddressModel, StringBuilder> defaultAddressFormatConverter;

	@Mock
	private AddressModel source;
	@Mock
	private AddressData target;
	
	private Map<String, Converter<AddressModel, StringBuilder>> addressFormatConverterMap;
	
	@InjectMocks
	GPAddressPopulator addressPopulator=new GPAddressPopulator();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		addressFormatConverterMap=new HashMap<>();
		addressFormatConverterMap.put("US", defaultAddressFormatConverter);
		addressPopulator.setAddressFormatConverterMap(addressFormatConverterMap);
		addressPopulator.setDefaultAddressFormatConverter(defaultAddressFormatConverter);
		
		Mockito.when(source.getAppartment()).thenReturn("Appartment");
		Mockito.when(source.getApprovalStatus()).thenReturn(GPApprovalEnum.ACTIVE);
		Mockito.when(source.getB2bUnit()).thenReturn(b2bUnitModel);
		Mockito.when(source.getBillingAddress()).thenReturn(true);
		Mockito.when(source.getBuilding()).thenReturn("Building");
		Mockito.when(source.getCellphone()).thenReturn("4455667781");
		Mockito.when(source.getCode()).thenReturn("AddressCode");
		Mockito.when(source.getCompany()).thenReturn("Company");
		Mockito.when(source.getContactAddress()).thenReturn(true);
		Mockito.when(source.getCountry()).thenReturn(countryModel);
		Mockito.when(countryModel.getIsocode()).thenReturn("US");
		Mockito.when(defaultAddressFormatConverter.convert(source)).thenReturn(new StringBuilder("Formatted String"));
		Mockito.when(source.getDepartment()).thenReturn("Department");
		Mockito.when(source.getDistrict()).thenReturn("District");
		Mockito.when(source.getGender()).thenReturn(Gender.MALE);
		Mockito.when(source.getLastname()).thenReturn("LastName");
		Mockito.when(source.getMiddlename()).thenReturn("MiddleName");
		Mockito.when(source.getLine1()).thenReturn("Line1");
		Mockito.when(source.getLine2()).thenReturn("Line2");
		Mockito.when(source.getPhone1()).thenReturn("7654567321");
		Mockito.when(source.getPhone2()).thenReturn("7654567321");
		Mockito.when(source.getPostalcode()).thenReturn("567321");
		Mockito.when(source.getShippingAddress()).thenReturn(true);
		Mockito.when(source.getTown()).thenReturn("Town");
		Mockito.when(source.getPk()).thenReturn(PK.fromLong(12345673452L));
		Mockito.when(source.getVisibleInAddressBook()).thenReturn(true);
		Mockito.when(source.getTitle()).thenReturn(Mockito.mock(TitleModel.class));
		Mockito.when(source.getTitle().getName()).thenReturn("Mr");
		Mockito.when(source.getTitle().getCode()).thenReturn("Mr");
		Mockito.when(source.getRegion()).thenReturn(regionModel);
		Mockito.when(regionModel.getIsocode()).thenReturn("NYC");
		Mockito.when(regionModel.getIsocodeShort()).thenReturn("NYC");
		Mockito.when(regionModel.getName()).thenReturn("New York City");
		Mockito.when(regionModel.getCountry()).thenReturn(countryModel);	
		Mockito.when(source.getVisibleInAddressBook()).thenReturn(true);
		Mockito.when(source.getVisibleInAddressBook()).thenReturn(true);
		Mockito.when(source.getPalletShipment()).thenReturn(true);
		Mockito.when(source.getOwner()).thenReturn(customer);
		Mockito.when(customer.getUid()).thenReturn("CustomerUid");
		Mockito.when(b2bUnitModel.getActive()).thenReturn(true);
		Mockito.when(b2bUnitModel.getUid()).thenReturn("B2BUid");
		Mockito.when(b2bUnitModel.getLocName()).thenReturn("B2BUid");
		Mockito.when(source.getApprovalStatus()).thenReturn(GPApprovalEnum.ACTIVE);
		
		
		
	}
	
	@Test
	public void testPopulate()
	{
		addressPopulator.populate(source, target);
		Mockito.verify(target).setUserId("CustomerUid");
		Mockito.verify(target).setUnit(Mockito.any());
		Mockito.verify(target).setRegion(Mockito.any());
	}

}
