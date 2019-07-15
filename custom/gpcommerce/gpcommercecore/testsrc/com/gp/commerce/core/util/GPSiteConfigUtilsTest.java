package com.gp.commerce.core.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.util.Config;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
@PowerMockIgnore("org.apache.logging.log4j.*") 
public class GPSiteConfigUtilsTest
{
	@Mock
	private Config config;
	@Mock
	private DecimalFormat decimalFormat;
	
	@Before
	public void setup() throws Exception {

		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetSiteConfig()
	{
		 CMSSiteModel cmsSite=new CMSSiteModel();
		final Map<String, String> siteConfig=new HashMap<>();
		siteConfig.put("KEY", "VALUE");
		cmsSite.setSiteConfig(siteConfig);
		Assert.assertTrue(GPSiteConfigUtils.getSiteConfig(cmsSite, "KEY").equalsIgnoreCase("VALUE"));
	}
	
	@Test
	public void testGetSiteEnvConfig()
	{
		CMSSiteModel cmsSite = new CMSSiteModel();
		final Map<String, String> siteEnvConfig = new HashMap<>();
		siteEnvConfig.put("KEY", "VALUE");
		cmsSite.setSiteEnvConfig(siteEnvConfig);
		Assert.assertTrue(GPSiteConfigUtils.getSiteEnvConfig(cmsSite, "KEY").equalsIgnoreCase("VALUE"));
	}
	
	@Test
	public void testGetDecimalFormatValue()
	{
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter("permissions.thersholdvalue.decimal.format")).thenReturn("##.00");
		String decimalValue = GPSiteConfigUtils.getDecimalFormatValue(12.3698);
		Assert.assertTrue(Double.parseDouble(decimalValue)==12.37);
	}
	
	@Test
	public void testisB2BSite()
	{
		BaseSiteModel site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2B);
		Assert.assertTrue(GPSiteConfigUtils.isB2BSite(site));
	}
	
	
}
