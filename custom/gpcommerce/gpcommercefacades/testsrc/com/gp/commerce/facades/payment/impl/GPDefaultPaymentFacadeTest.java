/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.payment.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.order.data.CardTypeDataList;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.configuration.Configuration;
import com.gp.commerce.facades.payment.impl.GPDefaultPaymentFacade;
import java.util.List;

@UnitTest
public class GPDefaultPaymentFacadeTest {

	private static final String EXPIRYYEARLIST_MAXCOUNT_PROPERTY = "creditCard.expiryYearList.maxCount";
	private static final String EXPIRYYEARLIST_MAXCOUNT = "10";
	
	@Spy
	@InjectMocks
    private GPDefaultPaymentFacade gpDefaultPaymentFacadeUnderTest;
    
    @Mock
    private ConfigurationService configurationService;
    
    @Mock
	private Configuration configuration;

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
        gpDefaultPaymentFacadeUnderTest = new GPDefaultPaymentFacade();
        gpDefaultPaymentFacadeUnderTest.configurationService=Mockito.mock(ConfigurationService.class);
        ReflectionTestUtils.setField(gpDefaultPaymentFacadeUnderTest, "configurationService", configurationService);
        when(configurationService.getConfiguration()).thenReturn(configuration);
        when(configurationService.getConfiguration().getString(EXPIRYYEARLIST_MAXCOUNT_PROPERTY)).thenReturn("10");
    }

    @Test
    public void testGetCreditCardTypes() {
        // Setup
        final CardTypeDataList expectedResult = null;

        // Run the test
        final CardTypeDataList result = gpDefaultPaymentFacadeUnderTest.getCreditCardTypes();

        // Verify the results
        assertNotEquals(expectedResult, result);
    }

    @Test
    public void testGetExpiryYearList() {
        // Setup
        final List<String> expectedResult = null;

        // Run the test
        final List<String> result = gpDefaultPaymentFacadeUnderTest.getExpiryYearList();

        // Verify the results
        assertNotEquals(expectedResult, result);
    }
    
    @Test
    public void testNullGetExpiryYearList() {
        // Setup
        final List<String> expectedResult = null;
        when(configurationService.getConfiguration().getString(EXPIRYYEARLIST_MAXCOUNT_PROPERTY)).thenReturn(null);

        // Run the test
        final List<String> result = gpDefaultPaymentFacadeUnderTest.getExpiryYearList();

        // Verify the results
        assertNotEquals(expectedResult, result);
    }
}
