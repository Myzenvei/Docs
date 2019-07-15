/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hook.impl;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import javax.annotation.Generated;
import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.price.service.GPCommercePriceService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.servicelayer.model.ModelService;

@UnitTest
public class GPCommerceCartCalculationMethodHookTest {
	
	@Mock
	private GPCommercePriceService gpDefaultCommercePriceService;
	@Mock    
	private ModelService modelService;
	@Mock 
	CommerceCartParameter parameter;
	@Mock
	CartEntryModel entryModel;
	@Mock
	ProductModel productModel;
	@Mock
	CartModel cartModel;
	@Mock    
	private CalculationService calculationService;

	@InjectMocks
	GPCommerceCartCalculationMethodHook cartCalculationMethodHook;

	@Before
	public void setUp() throws Exception {

		cartCalculationMethodHook=createTestSubject();
		MockitoAnnotations.initMocks(this);
		cartCalculationMethodHook.setCalculationService(calculationService);
		cartCalculationMethodHook.setGpDefaultCommercePriceService(gpDefaultCommercePriceService);
		cartCalculationMethodHook.setModelService(modelService);
		Mockito.when(parameter.getCart()).thenReturn(cartModel);
		Mockito.when(cartModel.getEntries()).thenReturn(Collections.singletonList(entryModel));
		Mockito.when(entryModel.getProduct()).thenReturn(productModel);
		Mockito.when(gpDefaultCommercePriceService.getMapPriceForProduct(productModel)).thenReturn(Double.valueOf("11.45"));
	}

	private GPCommerceCartCalculationMethodHook createTestSubject() {
		return new GPCommerceCartCalculationMethodHook();
	}

	@Test
	public void testAfterCalculate() throws Exception {
		cartCalculationMethodHook.afterCalculate(parameter);
		assertTrue(cartCalculationMethodHook.getCalculationService().equals(calculationService));
		assertTrue(cartCalculationMethodHook.getModelService().equals(modelService));
		assertTrue(cartCalculationMethodHook.getGpDefaultCommercePriceService().equals(gpDefaultCommercePriceService));
		
	}

	@Test
	public void testBeforeCalculate() throws Exception {
		
		cartCalculationMethodHook.beforeCalculate(parameter);
		Mockito.verify(gpDefaultCommercePriceService).getMapPriceForProduct(productModel);
	}
}