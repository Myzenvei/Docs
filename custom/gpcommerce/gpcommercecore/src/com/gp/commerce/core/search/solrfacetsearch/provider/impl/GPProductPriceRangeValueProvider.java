/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.AbstractMultidimensionalProductFieldValueProvider;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.entity.SolrPriceRange;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;


/**
 * Value Provider for price range of multidimensional products.
 */
public class GPProductPriceRangeValueProvider extends AbstractMultidimensionalProductFieldValueProvider
{
	@Resource(name="priceService")
	private PriceService priceService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if (model instanceof PriceRowModel)
		{
			final Object field = getFieldValue(((PriceRowModel) model).getProduct());
			addFieldValues(fieldValues, indexedProperty, field);
		}
		else
		{
			throw new FieldValueProviderException("Cannot get field for non-product item");
		}

		return fieldValues;
	}

	@Override
	public Object getFieldValue(final ProductModel product)
	{
		String priceRange = null;
		// make sure you have the baseProduct because variantProducts won't have other variants
		final ProductModel baseProduct = getBaseProduct(product);

		final Collection<VariantProductModel> variants = baseProduct.getVariants();
		if (CollectionUtils.isNotEmpty(variants))
		{
			final List<PriceInformation> allPricesInfos = new ArrayList<PriceInformation>();

			// collect all price infos
			for (final VariantProductModel variant : variants)
			{
				allPricesInfos.addAll(priceService.getPriceInformationsForProduct(variant));
			}

			if (!allPricesInfos.isEmpty())
			{
				priceRange = getPriceRangeString(allPricesInfos);
			}
		}

		return priceRange;
	}

	protected String getPriceRangeString(final List<PriceInformation> allPricesInfos)
	{
		Collections.sort(allPricesInfos, PriceRangeComparator.INSTANCE);

		final PriceInformation lowest = allPricesInfos.get(0);
		final PriceInformation highest = allPricesInfos.get(allPricesInfos.size() - 1);
		return SolrPriceRange.buildSolrPropertyFromPriceRows(lowest.getPriceValue().getValue(), lowest.getPriceValue().getCurrencyIso(),
				highest.getPriceValue().getValue(), highest.getPriceValue().getCurrencyIso());
	}

	public static class PriceRangeComparator extends AbstractComparator<PriceInformation>
	{
		public static final PriceRangeComparator INSTANCE = new PriceRangeComparator();

		@Override
		protected int compareInstances(final PriceInformation price1, final PriceInformation price2)
		{
			if (price1 == null || price1.getPriceValue() == null)
			{
				return BEFORE;
			}
			if (price2 == null || price2.getPriceValue() == null)
			{
				return AFTER;
			}

			return Double.compare(price1.getPriceValue().getValue(), price2.getPriceValue().getValue());
		}
	}

}
