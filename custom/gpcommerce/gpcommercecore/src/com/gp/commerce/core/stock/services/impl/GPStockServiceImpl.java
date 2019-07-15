/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.stock.services.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.gp.commerce.core.exceptions.GPInsufficientStockLevelException;
import com.gp.commerce.core.model.GPComponentProductsModel;
import com.gp.commerce.core.model.GPProductKitModel;
import com.gp.commerce.core.model.GPVariantProductKitModel;
import com.gp.commerce.core.stock.services.GPStockService;

import de.hybris.platform.basecommerce.enums.StockLevelUpdateType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.stock.impl.DefaultStockService;
import de.hybris.platform.store.BaseStoreModel;



public class GPStockServiceImpl extends DefaultStockService implements GPStockService{
	
	private static final Logger LOG = Logger.getLogger(GPStockServiceImpl.class);
	
	@Override
	public void releaseStockForOrder(OrderModel order) {
		WarehouseModel warehouse = getDefaultWarehouseForStore(order.getStore());
		order.getEntries().stream().forEach(entry -> {

			if ("GPProductKit".equalsIgnoreCase(entry.getProduct().getItemtype())) {
				GPProductKitModel kitModel = (GPProductKitModel) entry.getProduct();
				for (GPComponentProductsModel component : kitModel.getKitComponent()) {
					release(component.getKitcomponent(), warehouse,
							(component.getQuantity().intValue() * entry.getQuantity().intValue()),
							StockLevelUpdateType.CUSTOMER_RELEASE.getCode());
				}
			} else if ("GPVariantProductKit".equalsIgnoreCase(entry.getProduct().getItemtype())) {
				GPVariantProductKitModel kitModel = (GPVariantProductKitModel) entry.getProduct();

				for (GPComponentProductsModel component : kitModel.getKitComponent()) {
					release(component.getKitcomponent(), warehouse,
							(component.getQuantity().intValue() * entry.getQuantity().intValue()),
							StockLevelUpdateType.CUSTOMER_RELEASE.getCode());
				}
			} else {
				release(entry.getProduct(), warehouse, entry.getQuantity().intValue(),
						StockLevelUpdateType.CUSTOMER_RELEASE.getCode());
			}

		});
	}

	@Override
	public void reserveStockForOrder(OrderModel order) throws GPInsufficientStockLevelException {
		WarehouseModel warehouse = getDefaultWarehouseForStore(order.getStore());
		
		List<AbstractOrderEntryModel> entries = order.getEntries();
		for (AbstractOrderEntryModel entry : entries) {
			try {

				if ("GPProductKit".equalsIgnoreCase(entry.getProduct().getItemtype())) {
					GPProductKitModel kitModel = (GPProductKitModel) entry.getProduct();
					for (GPComponentProductsModel component : kitModel.getKitComponent()) {
						reserve(component.getKitcomponent(), warehouse,
								(component.getQuantity().intValue() * entry.getQuantity().intValue()),
								StockLevelUpdateType.CUSTOMER_RELEASE.getCode());
					}
				} else if ("GPVariantProductKit".equalsIgnoreCase(entry.getProduct().getItemtype())) {
					GPVariantProductKitModel kitModel = (GPVariantProductKitModel) entry.getProduct();

					for (GPComponentProductsModel component : kitModel.getKitComponent()) {
						reserve(component.getKitcomponent(), warehouse,
								(component.getQuantity().intValue() * entry.getQuantity().intValue()),
								StockLevelUpdateType.CUSTOMER_RELEASE.getCode());
					}
				} else {
					reserve(entry.getProduct(), warehouse, entry.getQuantity().intValue(),
							StockLevelUpdateType.CUSTOMER_RESERVE.getCode());
				}
			} catch (InsufficientStockLevelException e) {
				LOG.error("Error in reserve stock for product " + entry.getProduct().getCode() + " for order '"
						+ order.getCode() + "' ", e);
				throw new GPInsufficientStockLevelException(entry.getProduct().getCode(),
						"Error in reserve stock for product", e);
			}
		}
	}
	
	private WarehouseModel getDefaultWarehouseForStore(BaseStoreModel store) {
		return store.getWarehouses().stream().filter(warehouse -> warehouse.getDefault()).findFirst()
				.orElse(store.getWarehouses().get(0));
	}
}