/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */ 
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import junit.framework.Assert;

/**
 * @author dgandabonu
 */
@UnitTest
public class GPProductNextDeliveryDateValueProviderTest {
	
	@InjectMocks
	private final GPProductNextDeliveryDateValueProvider gpProductNextDeliveryDateValueProvider = new GPProductNextDeliveryDateValueProvider();

	@Mock
	private FieldNameProvider fieldNameProvider;
	
	@Mock
	private CommerceStockService commerceStockService;
	
	@Mock
	private StockService stockService;
	
	@Mock
	private ConfigurationService configurationService;
	
	@Mock
	private Configuration configuration;
	
	@Mock
	private IndexedProperty indexedProperty;
	
	@Mock
	private IndexConfig indexConfig;
	
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		List<String> fieldValues = new ArrayList<>();
		fieldValues.add("nextDeliveryDate");
		when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(fieldValues); 
		
	}
	
	
	@Test(expected = FieldValueProviderException.class)
	public void testGetFieldValues() throws FieldValueProviderException {
		
		ProductModel model = new ProductModel();
		BaseSiteModel baseSiteModel = new BaseSiteModel();
		List<BaseStoreModel> baseStoreList = new ArrayList<>(1);
		BaseStoreModel baseStoreModel = new BaseStoreModel();
		List<WarehouseModel> wareHouseList = new ArrayList<>(1);
		WarehouseModel wareHouseModel = new WarehouseModel();
		wareHouseList.add(wareHouseModel);
		baseStoreModel.setWarehouses(wareHouseList);
		baseStoreList.add(baseStoreModel);
		baseSiteModel.setStores(baseStoreList);
		when(indexConfig.getBaseSite()).thenReturn(baseSiteModel);
		when(commerceStockService.isStockSystemEnabled(Mockito.anyObject())).thenReturn(true);
		StockLevelModel stockLevelModel = new StockLevelModel();
		stockLevelModel.setNextDeliveryTime(new Date());
		when(stockService.getStockLevel(Mockito.anyObject(), Mockito.anyObject())).thenReturn(stockLevelModel);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString("date.format")).thenReturn("27-12-2020");
		
		//method call-1
		gpProductNextDeliveryDateValueProvider.getFieldValues(indexConfig, indexedProperty, model);
		
		when(commerceStockService.isStockSystemEnabled(Mockito.anyObject())).thenReturn(false);
		//method call-2
		gpProductNextDeliveryDateValueProvider.getFieldValues(indexConfig, indexedProperty, model);
		
		//method call-3
		gpProductNextDeliveryDateValueProvider.getFieldValues(indexConfig, indexedProperty, new CartModel());
			
	}	
	@Test
	public void testGetStockService() {
		Assert.assertTrue(gpProductNextDeliveryDateValueProvider.getStockService().equals(stockService));
	}
}
