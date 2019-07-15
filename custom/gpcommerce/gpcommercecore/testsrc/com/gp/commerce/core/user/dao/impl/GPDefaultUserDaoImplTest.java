package com.gp.commerce.core.user.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.core.user.dao.GPUserDao;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.site.BaseSiteService;

@IntegrationTest
public class GPDefaultUserDaoImplTest extends ServicelayerTest{
	
	private static final String SITE_ID = "testgppro";
	@Resource(name="gpUserDao")
	private GPUserDao userDao;
	
	@Resource
	private BaseSiteService baseSiteService; 
	
	@Before
	public void setUp() throws Exception
	{
		importCsv("/gpcommercecore/test/testMarketingPreferences.impex", "utf-8");
	}

	@Test
	public void testGetAllPreferencesForSite()
	{
		List<MarketingPreferenceModel> sitePreferences = userDao.getMarketingPreferencesForSite((CMSSiteModel)baseSiteService.getBaseSiteForUID(SITE_ID));
		Assert.assertNotNull(sitePreferences);
		Assert.assertEquals(2, sitePreferences.size());
	}
}
