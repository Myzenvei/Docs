package com.gp.commerce.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.gp.commerce.core.services.impl.DefaultGPCMSSiteService;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(GPSiteConfigUtils.class )
public class DefaultGPCMSSiteServiceTest {

	@InjectMocks
	private DefaultGPCMSSiteService service = new DefaultGPCMSSiteService();
	
	private static final Logger LOG = Logger.getLogger(DefaultGPCMSSiteServiceTest.class);
	
	@Mock
	private CMSSiteModel cmsSite;
	@Mock
	BaseSiteService baseSiteService;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		service.setBaseSiteService(baseSiteService);
		PowerMockito.mockStatic(GPSiteConfigUtils.class);
		Mockito.when(GPSiteConfigUtils.getSiteConfig(cmsSite, "KEY")).thenReturn("SiteConfig");
		Mockito.when(GPSiteConfigUtils.getSiteEnvConfig(cmsSite, "KEY")).thenReturn("SiteEnvConfig");
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(cmsSite);
		
	}
	
	@Test
	public void testGetSiteConfig()
	{
		Assert.assertTrue(service.getSiteConfig("KEY").equalsIgnoreCase("SiteConfig"));

	}
	
	@Test
	public void testGetSiteEnvConfig()
	{
		Assert.assertTrue(service.getSiteEnvConfig("KEY").equalsIgnoreCase("SiteEnvConfig"));

	}
		
}

	