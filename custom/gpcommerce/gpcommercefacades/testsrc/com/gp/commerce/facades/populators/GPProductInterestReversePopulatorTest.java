/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;

/**
 * @author akapandey
 */
@UnitTest
public class GPProductInterestReversePopulatorTest {

	private String emailAddress;
	private static final String NOTIFYMEEMAILID = "abc@gp.com";
	
	@InjectMocks
	GPProductInterestReversePopulator populator = new GPProductInterestReversePopulator();
	
	ProductInterestData source = new ProductInterestData();
	ProductInterestModel target = new ProductInterestModel();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source.setEmailAddress(NOTIFYMEEMAILID);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getNotifyMeEmailId(), source.getEmailAddress());
	}
}
