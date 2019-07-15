/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.stock.converters.populator;

import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.services.BaseStoreService;

import java.text.SimpleDateFormat;

public class GPStockPopulator<SOURCE extends ProductModel, TARGET extends StockData> implements Populator<SOURCE, TARGET>{

	private BaseStoreService baseStoreService;
	private StockService stockService;

	@Override
	public void populate(final ProductModel source, final StockData target) {
		final StockLevelModel stockLevel = stockService.getStockLevel(source, baseStoreService.getCurrentBaseStore().getWarehouses().get(0));

		if (null != stockLevel && null != stockLevel.getNextDeliveryTime()) {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			target.setNextAvailableDate(formatter.format(stockLevel.getNextDeliveryTime()));
		}
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(final StockService stockService) {
		this.stockService = stockService;
	}
}
