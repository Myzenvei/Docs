/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.apache.commons.configuration.Configuration;

import static org.mockito.BDDMockito.given;

import com.gp.commerce.core.services.GPCustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.config.impl.DefaultConfigurationService;
import de.hybris.platform.util.mail.MailUtils;
import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPCustomerEmailResolutionServiceTest {

    @InjectMocks
    private GPCustomerEmailResolutionService service = new GPCustomerEmailResolutionService();
 
    @Mock
    private ConfigurationService configurationService;
     
    @Mock
    private Configuration configuration;
    
    
    private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
     
    @Before
    public void setUp() throws Exception {
         MockitoAnnotations.initMocks(this);
         service.setConfigurationService(configurationService);
         
        given(configurationService.getConfiguration()).willReturn(configuration);
    }
    
    @Test
    public void testGetConfigurationService()
    {
        Assert.assertNotNull(service.getConfigurationService());
    }
    
    @Test
    public void testGetEmailForCustomerRegistered()
    {
        CustomerModel customerModel = Mockito.mock(CustomerModel.class);
        String emailAfterProcessing = "abc@def.com";
        
        given(configuration.getString(BASESITE_DELIMITER)).willReturn("|");
        Mockito.when(customerModel.getType()).thenReturn(CustomerType.REGISTERED);
        Mockito.when(customerModel.getOriginalUid()).thenReturn(emailAfterProcessing);
        
        Assert.assertNotNull(service.getEmailForCustomer(customerModel));
    }
    
    @Test
    public void testGetEmailForCustomerGuest()
    {
        CustomerModel customerModel = Mockito.mock(CustomerModel.class);
        String emailAfterProcessing = "abc@def.com";
        
        given(configuration.getString(BASESITE_DELIMITER)).willReturn("|");
        Mockito.when(customerModel.getType()).thenReturn(CustomerType.GUEST);
        Mockito.when(customerModel.getUid()).thenReturn(emailAfterProcessing);
        
        Assert.assertNotNull(service.getEmailForCustomer(customerModel));
    }
    
    @Test
    public void testGetEmailForCustomerRegisteredNullCase()
    {
        CustomerModel customerModel = Mockito.mock(CustomerModel.class);
        String emailAfterProcessing = "abc@def.com";
        
        given(configuration.getString(BASESITE_DELIMITER)).willReturn("|");
        Mockito.when(customerModel.getType()).thenReturn(CustomerType.REGISTERED);
        Mockito.when(customerModel.getOriginalUid()).thenReturn(null);
        
        Assert.assertNull(service.getEmailForCustomer(customerModel));
    }
    
    @Test
    public void testGetEmailForCustomerGuestNullCase()
    {
        CustomerModel customerModel = Mockito.mock(CustomerModel.class);
        String emailAfterProcessing = "abc@def.com";
        
        given(configuration.getString(BASESITE_DELIMITER)).willReturn("|");
        Mockito.when(customerModel.getType()).thenReturn(CustomerType.GUEST);
        Mockito.when(customerModel.getUid()).thenReturn(null);
        
        Assert.assertNull(service.getEmailForCustomer(customerModel));
    }
    
    @Test(expected=Exception.class)
    public void testGetEmailForCustomerRegisteredExceptionCase()
    {
        CustomerModel customerModel = Mockito.mock(CustomerModel.class);
        String emailAfterProcessing = "@def.com";
        
        given(configuration.getString(BASESITE_DELIMITER)).willReturn("|");
        Mockito.when(customerModel.getType()).thenReturn(CustomerType.REGISTERED);
        Mockito.when(customerModel.getOriginalUid()).thenReturn(emailAfterProcessing);
        
        Assert.assertNull(service.getEmailForCustomer(customerModel));
    }
}

