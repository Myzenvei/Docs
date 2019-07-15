/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.stock.converters.com.gp.commerce.facades.stock.converters.populator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.gp.commerce.facades.stock.converters.populator.GPStockPopulator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

@UnitTest
public class GPStockPopulatorTest {
	GPStockPopulator gpStock=new GPStockPopulator();
	

 
	private BaseStoreService baseStoreService;
 
	private StockService stockService;
	@Mock
	private  ProductModel source ;
	@Mock
	private   StockData target ;
	private StockLevelModel  stockLevelModel ;
	private BaseStoreModel  baseStoreModel ;
	private WarehouseModel warehouse ;

	@Before
	public void setUp() throws Exception {
		baseStoreService=Mockito.mock(BaseStoreService.class) ;
		baseStoreModel=Mockito.mock(BaseStoreModel.class) ;
		warehouse=Mockito.mock(WarehouseModel.class) ;
		stockService=Mockito.mock(StockService.class) ;
		stockLevelModel=Mockito.mock(StockLevelModel.class) ;
		target=Mockito.mock(StockData.class) ;
		gpStock.setStockService(stockService);
		gpStock.setBaseStoreService(baseStoreService);
		
	}
	@Test
	public void populateTest() {
		List<WarehouseModel> warehouseList =new ArrayList<>();
		warehouseList.add(warehouse) ;
		Mockito.when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);
		Mockito.when(baseStoreModel.getWarehouses()).thenReturn(warehouseList) ;
		Mockito.when(stockService.getStockLevel(source, baseStoreService.getCurrentBaseStore().getWarehouses().get(0))).thenReturn(stockLevelModel) ;
		Mockito.when(stockLevelModel.getNextDeliveryTime()).thenReturn(new Date()) ;
		gpStock.populate(source, target);
		
	}
}
