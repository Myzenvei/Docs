/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.b2binitialdata.dataimport.impl;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.servicelayer.config.ConfigurationService;

@UnitTest
public class GpB2BSampleDataImportServiceTest {
	
	@InjectMocks
	private final GpB2BSampleDataImportService gpB2BSampleDataImportService = new GpB2BSampleDataImportService();
	
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
		 
		final Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("customerreview.searchrestrictions.create", new String[] { "true" });
		final SystemSetupContext ctx = new SystemSetupContext(params, Type.ESSENTIAL, Process.ALL, "gpb2binitialdata");
		
		gpB2BSampleDataImportService.importCommerceOrgData(ctx);
	}
	
	
}
