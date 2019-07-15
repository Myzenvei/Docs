/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GeneratedGpcommerceCoreConstants.Enumerations.MaterialStatusEnum;


/**
 * This ValueProvider will provide the product stock level status. The stock level count changes so frequently that it
 * is not sensible to index the count directly, but rather to map the count to a status (or band) and then index the
 * status.
 */
public class GPProductObseleteIndicatorValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private CommerceStockService commerceStockService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PriceRowModel)
		{
			final ProductModel product = ((PriceRowModel) model).getProduct();
			
			boolean isObseleteOutOfStock = false;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
				
			if(MaterialStatusEnum.OBSOLETE.equalsIgnoreCase(product.getMaterialStatus().getCode()))
			{
				final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

				if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty()
						&& getCommerceStockService().isStockSystemEnabled(baseSiteModel.getStores().get(0)))
				{
					BaseStoreModel baseStore = baseSiteModel.getStores().get(0);
					
					if (baseStore != null)
					{
						final StockLevelStatus stockLevelStatus = getProductStockLevelStatus(product, baseStore);
						if (stockLevelStatus != null)
						{
							if(StockLevelStatus.OUTOFSTOCK.getCode().equalsIgnoreCase(stockLevelStatus.getCode()))
							{
								isObseleteOutOfStock = true;
							}
						}
					}
				}
			}
			
			fieldValues.addAll(createFieldValue(product, isObseleteOutOfStock, indexedProperty));
			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get materialStatus and stock of non-product item");
		}
		
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final boolean isObseleteOutOfStock,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();


		addFieldValues(fieldValues, indexedProperty, isObseleteOutOfStock);

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected StockLevelStatus getProductStockLevelStatus(final ProductModel product, final BaseStoreModel baseStore)
	{
		return getCommerceStockService().getStockLevelStatusForProductAndBaseStore(product, baseStore);
	}
}
