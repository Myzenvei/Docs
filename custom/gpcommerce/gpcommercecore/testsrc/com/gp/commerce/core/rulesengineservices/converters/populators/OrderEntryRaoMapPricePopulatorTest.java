/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.converters.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

import com.gp.commerce.core.rulesengineservices.converters.populators.OrderEntryRaoMapPricePopulator;

public class OrderEntryRaoMapPricePopulatorTest {

	@InjectMocks
    private OrderEntryRaoMapPricePopulator orderEntryRaoMapPricePopulatorUnderTest;

	@Mock
    private AbstractOrderEntryModel source;
	@Mock
    private OrderEntryRAO target;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        orderEntryRaoMapPricePopulatorUnderTest = new OrderEntryRaoMapPricePopulator();
        Mockito.when(source.getMapPrice()).thenReturn(10.0);
    }

    @Test
    public void testPopulate() throws Exception {

        orderEntryRaoMapPricePopulatorUnderTest.populate(source, target);
        assertEquals(Double.valueOf(10.d),target.getMapPrice());
    }
}
