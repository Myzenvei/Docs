/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

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
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

@UnitTest
public class GPAddressReversePopulatorTest {

	@Mock
	FlexibleSearchService flexibleSearchService;
	@Mock
	CountryModel countryModel;
	@Mock
	CommonI18NService commonI18NService;
	@Mock
	private EnumerationService enumerationService;
	@Mock
	private B2BUnitService b2bUnitService;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private UserService userService;
	@Mock
	RegionData regionData;
	@Mock
	CountryData countryData;
	@Mock
	B2BUnitData b2bUnitData;
	@Mock
	private AddressData source;
	@Mock
	private AddressModel target;

	@InjectMocks
	GPAddressReversePopulator addressReversePopulator = new GPAddressReversePopulator();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		addressReversePopulator.setB2bUnitService(b2bUnitService);
		addressReversePopulator.setCommonI18NService(commonI18NService);
		addressReversePopulator.setConfigurationService(configurationService);
		addressReversePopulator.setEnumerationService(enumerationService);
		addressReversePopulator.setFlexibleSearchService(flexibleSearchService);
		addressReversePopulator.setUserService(userService);
		
		Mockito.when(source.getApprovalStatus()).thenReturn("Active");
		Mockito.when(flexibleSearchService.getModelByExample(Mockito.any())).thenReturn(Mockito.mock(TitleModel.class));
		Mockito.when(source.getUnit()).thenReturn(b2bUnitData);
		Mockito.when(source.getCode()).thenReturn("AddressCode");
		Mockito.when(source.getCompanyName()).thenReturn("Company");
		Mockito.when(source.isBillingAddress()).thenReturn(true);
		Mockito.when(source.getCountry()).thenReturn(countryData);
		Mockito.when(countryData.getIsocode()).thenReturn("US");
		Mockito.when(source.getLastName()).thenReturn("LastName");
		Mockito.when(source.getLine1()).thenReturn("Line1");
		Mockito.when(source.getLine2()).thenReturn("Line2");
		Mockito.when(source.getPhone()).thenReturn("7654567321");
		Mockito.when(source.getPostalCode()).thenReturn("567321");
		Mockito.when(source.isShippingAddress()).thenReturn(true);
		Mockito.when(source.getTown()).thenReturn("Town");
		Mockito.when(source.isVisibleInAddressBook()).thenReturn(true);
		Mockito.when(source.getTitle()).thenReturn("Mr");
		Mockito.when(source.getRegion()).thenReturn(regionData);
		Mockito.when(regionData.getIsocode()).thenReturn("NYC");
		Mockito.when(regionData.getIsocodeShort()).thenReturn("NYC");
		Mockito.when(regionData.getName()).thenReturn("New York City");
		Mockito.when(source.isDefaultAddress()).thenReturn(true);
		Mockito.when(source.isDefaultBillingAddress()).thenReturn(true);
		Mockito.when(source.getPalletShipment()).thenReturn(true);
		Mockito.when(source.getUserId()).thenReturn("CustomerUid");
		Mockito.when(b2bUnitData.getUid()).thenReturn("B2BUid");
		Mockito.when(commonI18NService.getCountry("US")).thenReturn(countryModel);
		Mockito.when(commonI18NService.getRegion(countryModel, "NYC")).thenReturn(Mockito.mock(RegionModel.class));
		
		Mockito.when(userService.getCurrentUser()).thenReturn(Mockito.mock(B2BCustomerModel.class));
		Mockito.when(enumerationService.getEnumerationValue(
				GPApprovalEnum.class.getSimpleName(),"Active")).thenReturn(GPApprovalEnum.ACTIVE);
		Mockito.when( b2bUnitService.getUnitForUid("CustomerUid")).thenReturn(Mockito.mock(B2BUnitModel.class));
		
				
	}

	@Test
	public void testPopulate()
	{
		addressReversePopulator.populate(source, target);
		Mockito.verify(target).setApprovalStatus(Mockito.any());
		Mockito.verify(target).setLastname("LastName");
		Mockito.verify(target).setRegion(Mockito.any());
	}
}
