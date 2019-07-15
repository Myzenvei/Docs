/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.b2binitialdata.dataimport.impl;

import static org.mockito.Mockito.when;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

@UnitTest
public class GpB2BCoreDataImportServiceTest {
	
	@InjectMocks
	private final GpB2BCoreDataImportService gpB2BCoreDataImportService = new GpB2BCoreDataImportService();
	
	@Mock
	ConfigurationService configurationService;
	
	@Mock
	private Configuration configuration;
	
	@Mock
	SetupImpexService setupImpexService;
	
	@Before
	public void setUp()
	{
		Registry.activateMasterTenant();
		MockitoAnnotations.initMocks(this);	
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getBoolean("setup.siteoverride", false)).thenReturn(true);
	
	}
	
	@Test
	public void testImportStore() {
		
		//method call-1
		gpB2BCoreDataImportService.importStore("gpb2binitialdata", "gppro", "gpUSProductCatalog");
		
		when(configuration.getBoolean("setup.siteoverride", false)).thenReturn(false);
		
		//method call-2
		gpB2BCoreDataImportService.importStore("gpb2binitialdata", "gppro", "gpUSProductCatalog");
	}
}
