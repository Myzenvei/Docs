/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;


public class GPProductNextDeliveryDateValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider{

	private CommerceStockService commerceStockService;
	private StockService stockService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
			throws FieldValueProviderException {
		if (model instanceof PriceRowModel)
		{
			final ProductModel product = ((PriceRowModel) model).getProduct();
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

			if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty()
					&& getCommerceStockService().isStockSystemEnabled(baseSiteModel.getStores().get(0)))
			{
				fieldValues.addAll(createFieldValue(product, indexConfig.getBaseSite().getStores().get(0), indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, null, indexedProperty));
			}
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get stock of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final BaseStoreModel baseStore,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (baseStore != null)
		{
			final StockLevelModel stockLevel = stockService.getStockLevel(product, baseStore.getWarehouses().get(0));

			if (null != stockLevel && null != stockLevel.getNextDeliveryTime())
			{
				final SimpleDateFormat formatter = new SimpleDateFormat(configurationService.getConfiguration().getString("date.format"));
				super.addFieldValues(fieldValues, indexedProperty, null, formatter.format(stockLevel.getNextDeliveryTime()));
			}
		}
		else
		{
			super.addFieldValues(fieldValues, indexedProperty, null, null);
		}

		return fieldValues;
	}

	public CommerceStockService getCommerceStockService() {
		return commerceStockService;
	}
	public void setCommerceStockService(final CommerceStockService commerceStockService) {
		this.commerceStockService = commerceStockService;
	}

	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(final StockService stockService) {
		this.stockService = stockService;
	}
}
