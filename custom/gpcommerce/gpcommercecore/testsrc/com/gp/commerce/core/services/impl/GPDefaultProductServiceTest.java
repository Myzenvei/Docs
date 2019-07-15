package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.GPVariantColorCodesModel;
import com.gp.commerce.core.model.GPAttributeConfigModel;
import com.gp.commerce.core.product.dao.impl.GPDefaultProductDao;
import com.gp.commerce.core.product.services.impl.GPDefaultProductService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

@UnitTest
public class GPDefaultProductServiceTest {
	private static final String ASSETCODE = "camera";
	
	@InjectMocks
	private GPDefaultProductService service= new GPDefaultProductService();

	@Mock
	Configuration config;
	
	@Mock
	private GPDefaultProductDao gpProductDao;
	
	@Mock 
	CMSSiteService cmssite;
	
	@Mock
	CMSSiteModel cmsSiteModel;
	
	@Mock
	ConfigurationService configurationService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		service.setCmsSiteService(cmssite);
		BDDMockito.given(cmssite.getCurrentSite()).willReturn(cmsSiteModel);
		BDDMockito.given(cmsSiteModel.getUid()).willReturn("vanityfair");
	}

	@Test
	public void testGetAttributesOfProduct() {
		GPAttributeConfigModel gpAttributeConfigModel = new GPAttributeConfigModel();
		gpAttributeConfigModel.setCmsSite(cmsSiteModel);
		gpAttributeConfigModel.setAssetCode("camera");
		gpAttributeConfigModel.setName("compatible memory cards");
		gpAttributeConfigModel.setClassificationCode(656);
		gpAttributeConfigModel.setCompareEnabled(true);
		gpAttributeConfigModel.setSearchEnabled(false);
		gpAttributeConfigModel.setDetailEnabled(false);
		List<GPAttributeConfigModel> attributeList = new ArrayList<>();				
		attributeList.add(gpAttributeConfigModel);
		when(configurationService.getConfiguration()).thenReturn(config);
		when(config.getString("product.compare.pagetype")).thenReturn("PDP_COMPARE");
		when(gpProductDao.getAttributeList("vanityfair", ASSETCODE)).thenReturn(attributeList);	
		List<String> productAttributes= service.getAttributesOfProduct(ASSETCODE, "PDP_COMPARE");
		assertEquals("compatible memory cards, 656",productAttributes.get(0));

	}
	
	@Test
	public void testFetchAllColorCodes()
	{
		final GPVariantColorCodesModel colorCodeModel = new GPVariantColorCodesModel();
		colorCodeModel.setColorName("red");
		colorCodeModel.setColorCode("#12345");
		final List<GPVariantColorCodesModel> list = new ArrayList<>();
		list.add(colorCodeModel);

		when(gpProductDao.getAllColorCodes()).thenReturn(list);

		assertTrue(service.fetchAllColorCodes().get("red").equals("#12345"));

	}

}
