package com.gp.commerce.core.price.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.job.GPOrderShipmentCronjob;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;

@UnitTest
public class GPDefaultCommercePriceServiceTest {
	
	private ProductModel product ;
	private  GPDefaultEurope1PriceFactory gpPriceFactory ;
	private PriceRow priceRow ;
	private ModelService modelService ;
	private  PriceRowModel prmModel ;
	private  BigDecimal mapPrice ;
	
	@InjectMocks
	GPDefaultCommercePriceService cron = new GPDefaultCommercePriceService();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		product=Mockito.mock(ProductModel.class) ;
		gpPriceFactory=Mockito.mock(GPDefaultEurope1PriceFactory.class) ;
		priceRow=Mockito.mock(PriceRow.class);
		modelService=Mockito.mock(ModelService.class) ;
		prmModel=Mockito.mock(PriceRowModel.class);
		mapPrice= BigDecimal.valueOf(0.0d) ;
		cron.setGpPriceFactory(gpPriceFactory);
		cron.setModelService(modelService);
		
		
	}

	@Test
	public void testMapPriceForProduct() {
		List<PriceRow> productPrices=new ArrayList<>() ;
		productPrices.add(priceRow) ;
	Mockito.when(priceRow.getPK()).thenReturn(PK.fromLong(34567123));
	Mockito.when(gpPriceFactory.getPriceInformationsForProduct(product)).thenReturn(productPrices);
	Mockito.when(modelService.get(priceRow.getPK())).thenReturn(prmModel);
	Mockito.when(prmModel.getMapPrice()).thenReturn(0.0) ;
	cron.getMapPriceForProduct(product);
	}
}
