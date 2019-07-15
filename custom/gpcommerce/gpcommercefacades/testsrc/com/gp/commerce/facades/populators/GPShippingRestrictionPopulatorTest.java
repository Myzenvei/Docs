/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.facade.order.data.ShippingRestrictionsData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;

@UnitTest
public class GPShippingRestrictionPopulatorTest {

	public static final String COUNTRYISOCODE = "CountryIsoCode";
	public static final String REGION = "Region";
	
	@InjectMocks
	GPShippingRestrictionPopulator populator = new GPShippingRestrictionPopulator();
	ShippingRestrictionModel source = new ShippingRestrictionModel();
	ShippingRestrictionsData target = new ShippingRestrictionsData();
	CountryWsDTO country = new CountryWsDTO();
	RegionWsDTO region = new RegionWsDTO();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setCountryIsoCode(COUNTRYISOCODE);
		source.setRegion(REGION);
		source.setProductCode("Product123");
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getCountry().getIsocode(), source.getCountryIsoCode());
		Assert.assertEquals(target.getRegion().getIsocode(), source.getRegion());
		Assert.assertEquals(target.getProductCode(), source.getProductCode());
	}
}
