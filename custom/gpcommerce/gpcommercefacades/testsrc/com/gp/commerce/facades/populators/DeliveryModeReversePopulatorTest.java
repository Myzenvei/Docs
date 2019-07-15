/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

/**
 * @author akapandey
 */
@UnitTest
public class DeliveryModeReversePopulatorTest {

	@InjectMocks
	DeliveryModeReversePopulator populator = new DeliveryModeReversePopulator();
	DeliveryModeData source = new DeliveryModeData();
	DeliveryModeModel target = new DeliveryModeModel();
	PriceData pd=new PriceData();
	
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		pd.setFormattedValue("12.44");
		pd.setValue(BigDecimal.valueOf(12.44));
		source.setCode("10199");
		source.setDeliveryCost(pd);
		source.setDeliveryPrice("200");
		source.setDeliveryFormattedPrice(200.00);
	}
	
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getDeliveryPrice(),pd.getFormattedValue());
		Assert.assertTrue(target.getDeliveryFormattedPrice()==pd.getValue().doubleValue());

	}
}
