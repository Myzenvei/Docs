/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.stock.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collection;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPComponentProductsModel;
import com.gp.commerce.core.model.GPProductKitModel;
import com.gp.commerce.core.model.GPVariantProductKitModel;
import com.gp.commerce.core.product.services.GPProductService;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;
import de.hybris.platform.store.BaseStoreModel;

public class GPCommerceStockServiceImpl extends DefaultCommerceStockService implements CommerceStockService {

	private GPCMSSiteService cmsSiteService;
	
	@Resource(name="gpProductService")
	private GPProductService gpProductService;


	@Override
	public boolean isStockSystemEnabled(final BaseStoreModel baseStore) {
		return !cmsSiteService.isSampleSite() && super.isStockSystemEnabled(baseStore);
	}
	
	
	@Override
	public Long getStockLevelForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be null");

		Collection<StockLevelModel> stockLevels = 
				getStockService().getStockLevels(product, getWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore));
		
		Long componentQuantity =gpProductService.getKitComponentQuantity(product, stockLevels);
		Long stockLevel = getCommerceStockLevelCalculationStrategy().calculateAvailability(stockLevels);
		
		if (null != stockLevel && (product instanceof GPProductKitModel || product instanceof GPVariantProductKitModel) && componentQuantity.intValue()!=0) {
			return stockLevel/componentQuantity;
		}
		
		return stockLevel;
	}

	public GPCMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public void setCmsSiteService(GPCMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

}
