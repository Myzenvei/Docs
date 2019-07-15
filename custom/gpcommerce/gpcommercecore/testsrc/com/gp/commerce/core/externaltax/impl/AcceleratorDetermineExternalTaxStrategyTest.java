/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.externaltax.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Verify;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;

import static org.junit.Assert.assertEquals;
import com.gp.commerce.core.externaltax.impl.AcceleratorDetermineExternalTaxStrategy;

/**
 * @author rbadisa
 *
 */
public class AcceleratorDetermineExternalTaxStrategyTest {

	@InjectMocks
    private AcceleratorDetermineExternalTaxStrategy acceleratorDetermineExternalTaxStrategyUnderTest;
    @Mock
    private AbstractOrderModel abstractOrder;
    @Mock
    private DeliveryModeModel deliveryMode;
    @Mock
    private AddressModel address;
    @Rule
     public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	acceleratorDetermineExternalTaxStrategyUnderTest =new AcceleratorDetermineExternalTaxStrategy();
        Mockito.when(abstractOrder.getNet()).thenReturn(Boolean.TRUE);
        Mockito.when(abstractOrder.getDeliveryMode()).thenReturn(deliveryMode);
        Mockito.when(abstractOrder.getDeliveryAddress()).thenReturn(address);
        assertEquals(Boolean.TRUE,acceleratorDetermineExternalTaxStrategyUnderTest.shouldCalculateExternalTaxes(abstractOrder));
    }

    /**
     * Test to check when the abstract order is not null to calculate external taxes
     */
    @Test
    public void testShouldCalculateExternalTaxes() {
        // Setup
        final boolean expectedResult = true;

        // Run the test
        final boolean result = acceleratorDetermineExternalTaxStrategyUnderTest.shouldCalculateExternalTaxes(abstractOrder);

        // Verify the results
        assertEquals(expectedResult, result);
    }
    
    
    /**
     * Test to check when the abstract order is null
     */
    @Test(expected = IllegalStateException.class)
    public void testShouldCalculateExternalTaxesWhenAbstractOrderNull() {
        // Setup
        final AbstractOrderModel abstractOrder = null;
        //thrown.expect(IllegalStateException.class);
        final boolean result = acceleratorDetermineExternalTaxStrategyUnderTest.shouldCalculateExternalTaxes(abstractOrder);
    }
    
    /**
     * Test to check when the abstract order is not null and expected is false
     */
    @Test
    public void testShouldCalculateExternalTaxesWhenAbstractOrderNotNull() {
    	final boolean expectedResult = false;
    	 Mockito.when(abstractOrder.getNet()).thenReturn(Boolean.FALSE);
    	
        // Run the test
        final boolean result = acceleratorDetermineExternalTaxStrategyUnderTest.shouldCalculateExternalTaxes(abstractOrder);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
