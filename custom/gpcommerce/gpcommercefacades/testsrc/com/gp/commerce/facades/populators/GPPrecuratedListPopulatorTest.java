/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.services.impl.DefaultGPCMSSiteService;
import com.gp.commerce.facades.component.data.GPPreCurateddata;
import com.gp.commerce.facades.data.WishlistData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

/**
 * @author akapandey
 */
@UnitTest
public class GPPrecuratedListPopulatorTest {
	
	@InjectMocks
	GPPrecuratedListPopulator populator = new GPPrecuratedListPopulator();
	
	@Mock
	SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	@Mock
	private DefaultGPCMSSiteService defaultGpCMSSiteService;
	@Mock
	WishlistData source;
	GPPreCurateddata target = new GPPreCurateddata();
	@Mock
	BaseSiteModel baseSiteModel;
	@Mock
	BaseSiteService baseSiteService;
	@Mock
	ConfigurationService configurationService;
	@Mock
	Configuration cfg;
	private String bannerImageD;
	String key = "/productlist";
	

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		populator.setDefaultGpCMSSiteService(defaultGpCMSSiteService);
		populator.setSiteBaseUrlResolutionService(siteBaseUrlResolutionService);
		source.setBannerImageD(bannerImageD);
		Mockito.when(source.getWishlistUid()).thenReturn("201");
		Mockito.when(source.getCmssite()).thenReturn("dixie");
		Mockito.when(defaultGpCMSSiteService.getBaseSiteService()).thenReturn(baseSiteService);
		Mockito.when(baseSiteService.getBaseSiteForUID(source.getCmssite())).thenReturn(baseSiteModel);
		Mockito.when(configurationService.getConfiguration()).thenReturn(cfg);
		Mockito.when(cfg.getString("precurated.prepend.link")).thenReturn(key);
		Mockito.when(siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteModel,  StringUtils.EMPTY, true, key)).thenReturn(key);
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source,target) ;
		Assert.assertEquals(target.getCuratedImageUrl(), source.getBannerImageD());
		Assert.assertEquals(target.getCuratedLinkUrl(),key+"/?listName="+source.getWishlistUid());
		
	}

}
