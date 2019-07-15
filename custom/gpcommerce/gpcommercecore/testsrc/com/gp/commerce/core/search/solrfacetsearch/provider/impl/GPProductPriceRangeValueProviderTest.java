/*******************************************************************************
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *******************************************************************************/
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author dgandabonu
 */
@UnitTest
public class GPProductPriceRangeValueProviderTest {
	
	
	@InjectMocks
	private final GPProductPriceRangeValueProvider gpProductPriceRangeValueProvider = new GPProductPriceRangeValueProvider();

	@Mock
	private FieldNameProvider fieldNameProvider;

	@Mock
	private IndexedProperty indexedProperty;
	
	@Mock
	private IndexConfig indexConfig;
	
	@Mock
	private PriceService priceService;
	
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);	
	}
	
	@Test
	public void testGetFieldValues() throws FieldValueProviderException {
		
		ProductModel model = new ProductModel();
		List<VariantProductModel> variants = new ArrayList<>();
		VariantProductModel variantProductModel = new VariantProductModel();
		variantProductModel.setBaseProduct(model);
		variants.add(variantProductModel);
		model.setVariants(variants);
		
		final List<PriceInformation> allPricesInfos = new ArrayList<PriceInformation>();
		
		PriceValue priceValue = new PriceValue("USD", 200.00D,true);
		PriceInformation priceInfo1 = new PriceInformation(priceValue);
		
		PriceValue priceValue2 = new PriceValue("USD", 300.00D,true);
		PriceInformation priceInfo2 = new PriceInformation(priceValue2);
		
		allPricesInfos.add(priceInfo1);
		allPricesInfos.add(priceInfo2);
		
		when(priceService.getPriceInformationsForProduct(variantProductModel)).thenReturn(allPricesInfos);
		
		//method call-1
		gpProductPriceRangeValueProvider.getFieldValue(model);
		
		final List<PriceInformation> allPricesInfos2 = new ArrayList<PriceInformation>();
		when(priceService.getPriceInformationsForProduct(variantProductModel)).thenReturn(allPricesInfos2);
		
		//method call-2
		gpProductPriceRangeValueProvider.getFieldValue(model);
		
		ProductModel model2 = new ProductModel();
		
		//method call-3
		gpProductPriceRangeValueProvider.getFieldValue(model2);
		
	}

}
