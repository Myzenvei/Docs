/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.util.PriceValue;

/**
 * @author dgandabonu
 */
@UnitTest
public class ProductVolumePricesProviderTest {

	@InjectMocks
	private final ProductVolumePricesProvider productVolumePricesProvider = new ProductVolumePricesProvider();
	
	@Mock
	private FieldNameProvider fieldNameProvider;
	
	@Mock
	private PriceService priceService;
	
	@Mock
	private IndexedProperty indexedProperty;
	
	@Mock
	private IndexConfig indexConfig;
	
	@Mock
	I18NService i18nService;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);	
		
		List<String> fieldValues = new ArrayList<>();
		fieldValues.add("volume");
		when(fieldNameProvider.getFieldNames(indexedProperty, "usd")).thenReturn(fieldValues);
		
		List<CurrencyModel> currencies = new ArrayList<>(1);
		CurrencyModel currency = new CurrencyModel();
		currency.setIsocode("USD");
		currencies.add(currency);
		when(indexConfig.getCurrencies()).thenReturn(currencies);
		when(i18nService.getCurrentCurrency()).thenReturn(currency);
		
	}
	
	@Test
	public void testGetFieldValues() throws FieldValueProviderException {
		
		ProductModel model = new ProductModel();
		List<PriceInformation> priceInfoList = new ArrayList<>(1);
		PriceValue priceValue = new PriceValue("USD", 200.00D,true);
		Map<String, Long> valueMap = new HashMap<>(1);
		valueMap.put("minqtd", 300L);
		PriceInformation priceInfo = new PriceInformation(valueMap,priceValue);
		priceInfoList.add(priceInfo);
		when(priceService.getPriceInformationsForProduct(model)).thenReturn(priceInfoList); 
		
		//method call-1
		productVolumePricesProvider.getFieldValues(indexConfig, indexedProperty, model);
		
		
		List<PriceInformation> priceInfoList2 = new ArrayList<>(2);
		PriceValue priceValue2 = new PriceValue("USD", 200.00D,true);
		Map<String, Long> valueMap2 = new HashMap<>(1);
		valueMap2.put("minqtd", 400L);
		PriceValue priceValue3 = new PriceValue("USD", 200.00D,true);
		Map<String, Long> valueMap3 = new HashMap<>(1);
		valueMap3.put("minqtd", 500L);
		
		PriceInformation priceInfo2 = new PriceInformation(valueMap2,priceValue2);
		PriceInformation priceInfo3 = new PriceInformation(valueMap3,priceValue3);
		priceInfoList2.add(priceInfo2);
		priceInfoList2.add(priceInfo3);
		when(priceService.getPriceInformationsForProduct(model)).thenReturn(priceInfoList2); 
		
		//method call-2
		productVolumePricesProvider.getFieldValues(indexConfig, indexedProperty, model);
	}
}
