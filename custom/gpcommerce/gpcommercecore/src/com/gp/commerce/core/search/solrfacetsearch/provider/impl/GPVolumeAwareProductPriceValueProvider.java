/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.gp.commerce.core.price.service.GPFilteredPriceContainer;
import com.gp.commerce.core.price.service.impl.GPFilteredPriceContainerImpl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

public class GPVolumeAwareProductPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	
	private static final Logger LOG = Logger.getLogger(GPVolumeAwareProductPriceValueProvider.class.getName());

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<>();
		try
		{
			checkModel(model);

			final PriceRowModel inputPriceRow = (PriceRowModel) model;

			if (inputPriceRow.getSalePrice() != null && inputPriceRow.getSalePrice() == Boolean.TRUE) {
				LOG.info(inputPriceRow.getPk() + "**" + inputPriceRow.getPrice());
				addPriceFieldValues(indexConfig, indexedProperty, fieldValues, inputPriceRow);

			}
			// list pricerow
			else if (((inputPriceRow.getSalePrice() == null)
					|| (inputPriceRow.getSalePrice() != null && inputPriceRow.getSalePrice() == Boolean.FALSE))
					&& ((inputPriceRow.getMinAdvertisedPrice() == null)
							|| (inputPriceRow.getMinAdvertisedPrice() != null
									&& inputPriceRow.getMinAdvertisedPrice() == Boolean.FALSE))) {
				processListPriceRow(indexConfig, indexedProperty, inputPriceRow, fieldValues);

			} else {
				// map pricerow
				throw new FieldValueProviderException(
						"Cannot index price from minAdvertisedPrice row" + inputPriceRow.getPk());

			}
		}
		catch (final Exception e)
		{
			LOG.error("Failed to generate volume price", e);
			throw new FieldValueProviderException(
					"Cannot evaluate " + indexedProperty.getName() + " using " + this.getClass().getName(), e);
		}

		return fieldValues;
	}

	/**
	 * 
	 * @param indexConfig
	 * @param indexedProperty
	 * @param inputPriceRow
	 * @throws FieldValueProviderException 
	 */
	private void processListPriceRow(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final PriceRowModel inputPriceRow, List<FieldValue> fieldValues) throws FieldValueProviderException
	{
		// list pricerow
		boolean hasUg = inputPriceRow.getUg() != null ? true : false;
		boolean hasGPUserPriceGroup = inputPriceRow.getGpUserPriceGroup() != null ? true : false;

		if (hasGPUserPriceGroup && !hasUg)
		{
			LOG.error("Price row incorrectly configured with GpUserPriceGroup without Ug");
		}
		else
		{
			GPFilteredPriceContainer gpPriceContainer = new GPFilteredPriceContainerImpl();
			if (inputPriceRow.getProduct() != null)
			{
				List<PriceRowModel> priceRowList = new ArrayList<PriceRowModel>(inputPriceRow.getProduct().getEurope1Prices());
				priceRowList.sort(Comparator.comparing(PriceRowModel::getPrice));

				for (PriceRowModel priceRow : priceRowList)
				{
					gpPriceContainer.addPriceRow(priceRow);
				}
				gpPriceContainer.filterPrices();
				List<PriceRowModel> allSalePrices = gpPriceContainer.getAllSalePrices();
				PriceRowModel topMatchingPriceRow = getTopMatchingPriceRow(inputPriceRow, allSalePrices);
				addPriceFieldValues(indexConfig, indexedProperty, fieldValues, topMatchingPriceRow);

			}
		}
	}

	private void addPriceFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			List<FieldValue> fieldValues, PriceRowModel topMatchingPriceRow) throws FieldValueProviderException {
		// index the range and price for the identified price row
		if (indexConfig.getCurrencies().isEmpty())
		{
			processPricesWithEmptyCurrencies(indexedProperty, fieldValues, topMatchingPriceRow);
		}
		else
		{
			for (final CurrencyModel currency : indexConfig.getCurrencies())
			{
				processPricesForCurrency(indexedProperty, fieldValues, currency, topMatchingPriceRow);
			}
		}
	}
	
	protected PriceRowModel getTopMatchingPriceRow(PriceRowModel inputPriceRow, List<PriceRowModel> allSalePrices) {
		
		PriceRowModel topPriceRow = null;
		PriceRowModel[] priceRowsByPriority = getPriceRowsByPriority(inputPriceRow, allSalePrices);
		for(PriceRowModel priceRow: priceRowsByPriority) {
			if(priceRow!=null) {
				topPriceRow = priceRow;
				break;
			}
		}  
		if(topPriceRow != null) {
			LOG.debug("PK"+inputPriceRow.getPk()+" price"+inputPriceRow.getPrice()+" ** top matching pricerow"+topPriceRow.getPrice());
		} else {
			topPriceRow = inputPriceRow;
			LOG.debug("PK"+inputPriceRow.getPk()+"returning input price row");
		}
		return topPriceRow;
	}
	
	protected PriceRowModel[] getPriceRowsByPriority(PriceRowModel inputPriceRow, List<PriceRowModel> allSalePrices)
	{
		PriceRowModel priceRowArr[] = new PriceRowModel[4];
		
		boolean hasUg = inputPriceRow.getUg() != null ? true : false;
		boolean hasGPUserPriceGroup = inputPriceRow.getGpUserPriceGroup() != null ? true : false;
		
		for (PriceRowModel currentListPriceRow : allSalePrices)
		{
			boolean hasCurrUg = currentListPriceRow.getUg() != null ? true : false;
			boolean hasCurrGPUserPriceGroup = currentListPriceRow.getGpUserPriceGroup() != null ? true : false;
			
			//case for logged in
			if ((hasUg && hasGPUserPriceGroup ) && (hasCurrUg && hasCurrGPUserPriceGroup) 
					&& currentListPriceRow.getUg().equals(inputPriceRow.getUg()) 
					&& currentListPriceRow.getGpUserPriceGroup().equals(inputPriceRow.getGpUserPriceGroup()))
			{
				priceRowArr[0] = currentListPriceRow;
				LOG.debug("inputPriceRow price "+inputPriceRow.getPrice()+ "\n priceRowArr[0] "+priceRowArr[0] );
				break;
			}//below 2 for anonymous with site specific
			else if((hasUg && !hasGPUserPriceGroup) && (hasCurrUg && !hasCurrGPUserPriceGroup) 
					&& currentListPriceRow.getUg().equals(inputPriceRow.getUg())
					&& priceRowArr[1] == null) 
			{
					priceRowArr[1] = currentListPriceRow;
					LOG.debug("priceRowArr[1] "+priceRowArr[1] );
					break;
			}
			else if((hasUg && !hasGPUserPriceGroup) && (!hasCurrUg && !hasCurrGPUserPriceGroup)
					&& priceRowArr[2] == null)
			{
					priceRowArr[2] = currentListPriceRow;
					LOG.debug("priceRowArr[2] "+priceRowArr[2] );
					break;
			}// overall default case
			else if((!hasUg && !hasGPUserPriceGroup) && (!hasCurrUg && !hasCurrGPUserPriceGroup))
			{
				if(priceRowArr[3] == null)
					priceRowArr[3] = currentListPriceRow;
				LOG.debug("priceRowArr[3] "+priceRowArr[3] );
			}
		}
		return priceRowArr;
	}
	
	
	protected void processPricesForCurrency(final IndexedProperty indexedProperty, final Collection<FieldValue> fieldValues,
			final CurrencyModel currency, final PriceRowModel price) throws FieldValueProviderException
	{
		if (null != price)
		{
			List<String> rangeNameList;
			final Double value = price.getPrice();
			rangeNameList = getRangeNameList(indexedProperty, value, currency.getIsocode());
			final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty,
					currency.getIsocode().toLowerCase());
			addFieldValues(fieldValues, rangeNameList, value, fieldNames);
		}
	}
	
	protected void processPricesWithEmptyCurrencies(final IndexedProperty indexedProperty,
			final Collection<FieldValue> fieldValues, final PriceRowModel price) throws FieldValueProviderException
	{
		if (null != price)
		{
			List<String> rangeNameList;
			final Double value = price.getPrice();
			rangeNameList = getRangeNameList(indexedProperty, value);
			final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty,
					price.getCurrency().getIsocode());
			addFieldValues(fieldValues, rangeNameList, value, fieldNames);
		}
	}

	protected void addFieldValues(final Collection<FieldValue> fieldValues, final List<String> rangeNameList, final Double value,
			final Collection<String> fieldNames)
	{
		for (final String fieldName : fieldNames)
		{
			if (rangeNameList.isEmpty())
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
			else
			{
				for (final String rangeName : rangeNameList)
				{
					fieldValues.add(new FieldValue(fieldName, rangeName == null ? value : rangeName));
				}
			}
		}
	}


	/**
	 * Checks whether the instance is PriceRow model
	 * 
	 * @param model
	 * @throws FieldValueProviderException
	 */
	protected void checkModel(final Object model) throws FieldValueProviderException
	{
		if (!(model instanceof PriceRowModel))
		{
			throw new FieldValueProviderException("Cannot evaluate price of non-price item");
		}
	}

	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}
