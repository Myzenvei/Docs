package com.gp.ymkt.common.services;

	import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.SiteMapConfigModel;
import de.hybris.platform.acceleratorservices.model.SiteMapLanguageCurrencyModel;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;

@UnitTest
public class GPYmktProductServiceTest {
	

		@InjectMocks
		GPYmktProductService service = new GPYmktProductService();
		
		@Mock
		CMSSiteService cmsSiteService;
		
		@Before
		public void setUp() throws Exception {
			MockitoAnnotations.initMocks(this);
		}

		@Test
		public void testGetProductImageURL() {
			ProductModel productModel =  Mockito.mock(ProductModel.class);
			MediaModel media = Mockito.mock(MediaModel.class);
			
			Mockito.when(media.getURL()).thenReturn("media/jkkldjflkdjkldj");
			Mockito.when(productModel.getPicture()).thenReturn(media);
			productModel.setPicture(media);
			Optional<String> url = service.getProductImageURL(productModel);
			Assert.assertEquals("media/jkkldjflkdjkldj", url.get());
			url = service.getProductImageURL(productModel);
			Mockito.when(media.getURL()).thenReturn(null);
			Mockito.when(productModel.getPicture()).thenReturn(media);
			url = service.getProductImageURL(productModel);
			Assert.assertEquals(Optional.empty(), url);


		}
		
		@Test
		public void testGetProductURL() {
			ProductModel productModel =  Mockito.mock(ProductModel.class);
			CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
			SiteMapConfigModel siteMapConfig = Mockito.mock(SiteMapConfigModel.class);
			Mockito.when(site.getSiteMapConfig()).thenReturn(siteMapConfig);
			SiteMapLanguageCurrencyModel siteMapLanguageCurrencyModel = Mockito.mock(SiteMapLanguageCurrencyModel.class);
			CurrencyModel currencyModel = Mockito.mock(CurrencyModel.class);
			currencyModel.setIsocode("USD");
			Mockito.when(siteMapLanguageCurrencyModel.getCurrency()).thenReturn(currencyModel);
			List<SiteMapLanguageCurrencyModel> siteMapList = Arrays.asList(siteMapLanguageCurrencyModel);
			Mockito.when(siteMapConfig.getSiteMapLanguageCurrencies()).thenReturn(siteMapList);
		    SiteBaseUrlResolutionService siteBaseUrlResolutionService = Mockito.mock(SiteBaseUrlResolutionService.class);
		   	UrlResolver<ProductModel> productModelUrlResolver = Mockito.mock(UrlResolver.class);
		   	service.setSiteBaseUrlResolutionService(siteBaseUrlResolutionService);
		   	service.setProductModelUrlResolver(productModelUrlResolver);
		   	Mockito.when(siteBaseUrlResolutionService.getWebsiteUrlForSite(Mockito.any(CMSSiteModel.class), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn("/p/343");
			String url = service.getProductURL(site, productModel);
			Assert.assertEquals("/p/343", url);
		}




}
