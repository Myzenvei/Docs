package com.gp.commerce.core.attributes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.product.dao.GPProductDao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.site.BaseSiteService;

public class ProductAssociatedWithSiteHandlerTest {
	
	
	@InjectMocks
	ProductAssociatedWithSiteHandler handler= new ProductAssociatedWithSiteHandler();
	
	@Mock
	GPProductDao gpProductDao = Mockito.mock(GPProductDao.class);
	
	@Mock
	ProductModel product = Mockito.mock(ProductModel.class);
	
	@Mock
	CMSSiteModel site = Mockito.mock(CMSSiteModel.class);
	
	@Mock
	private BaseSiteService baseSiteService;
	
	@Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGet()
	{
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(site);
		BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		Assert.assertNotNull(currentBaseSite);
		Mockito.when(gpProductDao.isProductAssociatedWithSite(product,site)).thenReturn(true);
		boolean productExists = gpProductDao.isProductAssociatedWithSite(product,(CMSSiteModel) site);
		Assert.assertTrue(productExists);
		Assert.assertTrue(handler.get(product));
		Mockito.when(handler.get(product)).thenThrow(Exception.class);
	}

}
