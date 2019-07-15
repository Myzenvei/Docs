/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;
/**
 * @author akapandey
 */

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.testframework.Assert;

@UnitTest
public class GPCustomerPopulatorTest {

	private static final String NAME = "john";
	
	@InjectMocks
	GPCustomerPopulator populator = new GPCustomerPopulator();
	
	@Mock
	CustomerModel source;
	CustomerData target = new CustomerData();
	@Mock
	GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(source.getName()).thenReturn(NAME);

	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getName(),gpDefaultCustomerNameStrategy.getName(NAME));
		
	}
}
