/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.price.service.impl.GPDefaultEurope1PriceFactory;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;

/**
 * @author dgandabonu
 */
@UnitTest
public class GPProductWeblistPriceValueProviderTest {
	
	@InjectMocks
	private final GPProductWeblistPriceValueProvider gpProductWeblistPriceValueProvider = new GPProductWeblistPriceValueProvider();

	@Mock
	private FieldNameProvider fieldNameProvider;

	@Mock
	private IndexedProperty indexedProperty;
	
	@Mock
	private IndexConfig indexConfig;
	
	@Mock
	GPDefaultEurope1PriceFactory gpPriceService;
	
	@Mock
	ModelService modelService;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);	
		List<String> fieldValues = new ArrayList<>();
		fieldValues.add("webListPrice");
		when(fieldNameProvider.getFieldNames(indexedProperty, "usd")).thenReturn(fieldValues); 
	}
	
	@Test
	public void testGetFieldValues() throws FieldValueProviderException {
		
		ProductModel model = new ProductModel();
		List<CurrencyModel> currencies = new ArrayList<>(1);
		CurrencyModel currency = new CurrencyModel();
		currency.setIsocode("USD");
		currencies.add(currency);
		when(indexConfig.getCurrencies()).thenReturn(currencies);
		
		List<PriceRow> productPrices = new ArrayList<>(1);
		PriceRow priceRow = new PriceRow();
		productPrices.add(priceRow);
		PriceRowModel priceModel = new PriceRowModel();
		priceModel.setPrice(200.00D);
		priceModel.setWeblistPrice(220.00D);
		when(gpPriceService.getPriceInformationsForProduct(model)).thenReturn(productPrices);
		when(modelService.get(priceRow.getPK())).thenReturn(priceModel);
		
		//method call-1
		gpProductWeblistPriceValueProvider.getFieldValues(indexConfig, indexedProperty, model);
		
		when(gpPriceService.getPriceInformationsForProduct(model)).thenReturn(new ArrayList<PriceRow>());
		
		//method call-2
		gpProductWeblistPriceValueProvider.getFieldValues(indexConfig, indexedProperty, model);
		
		//method call-3
		gpProductWeblistPriceValueProvider.getFieldValues(indexConfig, indexedProperty, new CartModel());
		
		
		List<PriceRow> priceRows = new ArrayList<>(1);
		PriceRow priceRow2 = new PriceRow();
		priceRows.add(priceRow2);
		PriceRowModel priceModel2 = new PriceRowModel();
		priceModel2.setPrice(220.00D);
		priceModel2.setWeblistPrice(200.00D);
		when(gpPriceService.getPriceInformationsForProduct(model)).thenReturn(priceRows);
		when(modelService.get(priceRow2.getPK())).thenReturn(priceModel2);
	
		//method call-4
		gpProductWeblistPriceValueProvider.getFieldValues(indexConfig, indexedProperty, model);
		
	}

}
