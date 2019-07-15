/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.hook.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.exceptions.GPLowStockException;
import com.gp.commerce.core.exceptions.GPMaximumStockException;
import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPCommerceSizeVariantProductModel;
import com.gp.commerce.core.model.GPCommerceStyleVariantProductModel;
import com.gp.commerce.core.model.GPOrderEntryAttributeModel;
import com.gp.commerce.core.model.GPServiceProductModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;

/**
 * @author dgandabonu
 */
@UnitTest
public class GPCommerceAddToCartMethodHookTest {
	
	@InjectMocks
	private final GPCommerceAddToCartMethodHook gpCommerceAddToCartMethodHook = new GPCommerceAddToCartMethodHook();
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private ProductService productService;
	
	@Mock
	private CommercePriceService commercePriceService;
	
	@Mock
	private CommonI18NService commonI18NService;
	
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Mock
	private I18NService i18NService;
	
	@Before
	public void setUp()
	{
		
		MockitoAnnotations.initMocks(this);
		PriceValue pv1 = new PriceValue("USD", 20.0, true);
		PriceInformation info = new PriceInformation(pv1);
		final CurrencyModel currencyModel = new CurrencyModel();
		currencyModel.setIsocode("USD");
		currencyModel.setSymbol("EN");
		currencyModel.setActive(true); 
		
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("en");
		
		final Locale locale = new Locale("en");
		
		when(modelService.create(GPOrderEntryAttributeModel.class)).thenReturn(new GPOrderEntryAttributeModel());
		when(productService.getProductForCode(Mockito.anyString())).thenReturn(new ProductModel());
		when(commercePriceService.getWebPriceForProduct(Mockito.anyObject())).thenReturn(info);
		when(commonI18NService.getCurrency(Mockito.anyString())).thenReturn(currencyModel);
		when(commonI18NService.getCurrentLanguage()).thenReturn(languageModel);
		when(commerceCommonI18NService.getLocaleForLanguage(Mockito.anyObject())).thenReturn(locale);
	}
	
	@Test
	public void testAfterAddToCart() throws CommerceCartModificationException{
		
		//method call with data
		gpCommerceAddToCartMethodHook.afterAddToCart(getCommerceCartParameters(), getCommerceCartModification()); 
		
		//method call without data
		gpCommerceAddToCartMethodHook.afterAddToCart(new CommerceCartParameter(), getEmptyCartModificationData());
	}

	@Test(expected=GPMaximumStockException.class)
	public void testAfterAddToCartException1() throws CommerceCartModificationException{
		
		CommerceCartModification commerceCartModification=getEmptyCartModificationData();
		commerceCartModification.setStatusCode("maxOrderQuantityExceeded");
		gpCommerceAddToCartMethodHook.afterAddToCart(getCommerceCartParameters(),commerceCartModification );
		
	}
		
	@Test(expected=GPLowStockException.class)
	public void testAfterAddToCartException2() throws CommerceCartModificationException{
		
		CommerceCartModification commerceCartModification=getEmptyCartModificationData();
		commerceCartModification.setStatusCode("lowStock");
		gpCommerceAddToCartMethodHook.afterAddToCart(getCommerceCartParameters(),commerceCartModification );
		
	}
	@Test
	public void testAfterAddToCartForGPCommerceProduct() throws CommerceCartModificationException{
		
		//method call with data
		gpCommerceAddToCartMethodHook.afterAddToCart(getCommerceCartParameters(), getCommerceCartModificationForGPCommerceProduct()); 
		Mockito.verify(productService).getProductForCode("H00023");
	}
	
	private CommerceCartParameter getCommerceCartParameters() {
		
		CommerceCartParameter parameters = new CommerceCartParameter();
		Map<String, String> additionalAttributes = new HashMap<>(1);
		additionalAttributes.put("key1", "value1");
		parameters.setAdditionalAttributes(additionalAttributes);
		return parameters;
	}
	
	private CommerceCartModification getCommerceCartModification() {
		
		CommerceCartModification commerceCartModification = new CommerceCartModification();
		AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();
		GPCommerceSizeVariantProductModel sizeVariantProductModel = new GPCommerceSizeVariantProductModel();
		GPCommerceStyleVariantProductModel styleVariantProductModel = new GPCommerceStyleVariantProductModel();
		GPCommerceProductModel gpCommerceProductModel = new GPCommerceProductModel();
		GPServiceProductModel gpServiceProductModel = new GPServiceProductModel();
		gpServiceProductModel.setCode("H00023");
		List<GPOrderEntryAttributeModel> orderEntryAttributeModelList = new ArrayList<>();
		GPOrderEntryAttributeModel orderEntryAttributeModel = new GPOrderEntryAttributeModel();
		orderEntryAttributeModel.setName("key2");
		orderEntryAttributeModel.setValue("value2"); 
		orderEntryAttributeModelList.add(orderEntryAttributeModel);
		abstractOrderEntryModel.setAdditionalAttributes(orderEntryAttributeModelList);
		sizeVariantProductModel.setBaseProduct(styleVariantProductModel);
		gpCommerceProductModel.setInstallationProduct(gpServiceProductModel);
		gpCommerceProductModel.setGiftWrapProduct(gpServiceProductModel);
		styleVariantProductModel.setBaseProduct(gpCommerceProductModel);
		abstractOrderEntryModel.setProduct(sizeVariantProductModel);
		commerceCartModification.setEntry(abstractOrderEntryModel);
		
		return commerceCartModification;
	}
	
private CommerceCartModification getCommerceCartModificationForGPCommerceProduct() {
		
		CommerceCartModification commerceCartModification = new CommerceCartModification();
		AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();
		GPCommerceSizeVariantProductModel sizeVariantProductModel = new GPCommerceSizeVariantProductModel();
		GPCommerceStyleVariantProductModel styleVariantProductModel = new GPCommerceStyleVariantProductModel();
		GPCommerceProductModel gpCommerceProductModel = new GPCommerceProductModel();
		gpCommerceProductModel.setCode("Code1");
		GPServiceProductModel gpServiceProductModel = new GPServiceProductModel();
		gpServiceProductModel.setCode("H00023");
		List<GPOrderEntryAttributeModel> orderEntryAttributeModelList = new ArrayList<>();
		GPOrderEntryAttributeModel orderEntryAttributeModel = new GPOrderEntryAttributeModel();
		orderEntryAttributeModel.setName("key2");
		orderEntryAttributeModel.setValue("value2"); 
		orderEntryAttributeModelList.add(orderEntryAttributeModel);
		abstractOrderEntryModel.setAdditionalAttributes(orderEntryAttributeModelList);
		sizeVariantProductModel.setBaseProduct(styleVariantProductModel);
		gpCommerceProductModel.setInstallationProduct(gpServiceProductModel);
		gpCommerceProductModel.setGiftWrapProduct(gpServiceProductModel);
		styleVariantProductModel.setBaseProduct(gpCommerceProductModel);
		abstractOrderEntryModel.setProduct(gpCommerceProductModel);
		commerceCartModification.setEntry(abstractOrderEntryModel);
		
		return commerceCartModification;
	}
	
	private CommerceCartModification getEmptyCartModificationData() {
		
		CommerceCartModification commerceCartModification = new CommerceCartModification();
		AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();
		commerceCartModification.setEntry(abstractOrderEntryModel);
		
		return commerceCartModification;
	}
	
}