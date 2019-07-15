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

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;


public class GPProductWeblistPriceValueProvider extends GPPropertyFieldValueProvider implements FieldValueProvider
{


	private FieldNameProvider fieldNameProvider;
	
	private static final Logger LOG = Logger.getLogger(GPProductWeblistPriceValueProvider.class.getName());

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final List<FieldValue> fieldValues = new ArrayList<>();
		try
		{
			checkModel(model);

			final PriceRowModel inputPriceRow = (PriceRowModel) model;
			// list pricerow
			if (((inputPriceRow.getSalePrice() == null)
					|| (inputPriceRow.getSalePrice() != null && inputPriceRow.getSalePrice() == Boolean.FALSE))
							&& ((inputPriceRow.getMinAdvertisedPrice() == null)
					|| (inputPriceRow.getMinAdvertisedPrice() != null && inputPriceRow.getMinAdvertisedPrice() == Boolean.FALSE)))
			{
				LOG.debug(inputPriceRow.getPk()+"**"+inputPriceRow.getPrice());
				addFieldValues(inputPriceRow, indexedProperty, fieldValues);

			}
			else if (inputPriceRow.getSalePrice() != null && inputPriceRow.getSalePrice())
			{
				processSalePriceRow(indexedProperty, inputPriceRow, fieldValues);
			}
			else
			{
				// map pricerow
				throw new FieldValueProviderException("Cannot index price from minAdvertisedPrice row" + inputPriceRow.getPk());

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
	 */
	private void processSalePriceRow(final IndexedProperty indexedProperty,
			final PriceRowModel inputPriceRow, List<FieldValue> fieldValues)
	{
		// sale pricerow
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
				List<PriceRowModel> allListPrices = gpPriceContainer.getAllListPrices();
				PriceRowModel topMatchingPriceRow = getTopMatchingPriceRow(inputPriceRow, allListPrices);
				addFieldValues(topMatchingPriceRow, indexedProperty, fieldValues);

			}
		}
	}
	
	protected PriceRowModel getTopMatchingPriceRow(PriceRowModel inputPriceRow, List<PriceRowModel> allListPrices) {
		PriceRowModel topPriceRow = null;
		PriceRowModel[] priceRowsByPriority = getPriceRowsByPriority(inputPriceRow, allListPrices);
		for(PriceRowModel priceRow: priceRowsByPriority) {
			if(priceRow!=null) {
				topPriceRow = priceRow;
				break;
			}
		}  
		if(topPriceRow != null) {
			LOG.debug("PK"+inputPriceRow.getPk()+" price"+inputPriceRow.getPrice()+" ** top matching pricerow"+topPriceRow.getPrice());
		} 
		return topPriceRow;
	}
	
	protected PriceRowModel[] getPriceRowsByPriority(PriceRowModel inputPriceRow, List<PriceRowModel> allListPrices)
	{
		PriceRowModel priceRowArr[] = new PriceRowModel[6];
		
		boolean hasUg = inputPriceRow.getUg() != null ? true : false;
		boolean hasGPUserPriceGroup = inputPriceRow.getGpUserPriceGroup() != null ? true : false;
		
		for (PriceRowModel currentListPriceRow : allListPrices)
		{
			boolean hasCurrUg = currentListPriceRow.getUg() != null ? true : false;
			boolean hasCurrGPUserPriceGroup = currentListPriceRow.getGpUserPriceGroup() != null ? true : false;
			
			//below 3 are for logged in case
			if ((hasUg && hasGPUserPriceGroup) && (hasCurrUg && hasCurrGPUserPriceGroup) 
					&& currentListPriceRow.getUg().equals(inputPriceRow.getUg()) 
					&& currentListPriceRow.getGpUserPriceGroup().equals(inputPriceRow.getGpUserPriceGroup()))
					
			{
				priceRowArr[0] = currentListPriceRow;
				LOG.debug("inputPriceRow price "+inputPriceRow.getPrice()+ "\n priceRow1 "+priceRowArr[0]);
				break;
			}
			else if ((hasUg && hasGPUserPriceGroup ) && (hasCurrUg && !hasCurrGPUserPriceGroup) 
					&& currentListPriceRow.getUg().equals(inputPriceRow.getUg())
					&& priceRowArr[1] == null)
			{
				priceRowArr[1] = currentListPriceRow;
				LOG.debug("inputPriceRow price "+inputPriceRow.getPrice()+ "\n priceRow2 "+priceRowArr[1]);
			}
			else if ((hasUg && hasGPUserPriceGroup) && (!hasCurrUg && !hasCurrGPUserPriceGroup) 
					&& priceRowArr[2] == null)
			{
				priceRowArr[2] = currentListPriceRow;
				LOG.debug("inputPriceRow price "+inputPriceRow.getPrice()+ "\n priceRow3 "+priceRowArr[2]);
				break;
			}//next 3 are for anonymous case
			else if((hasUg && !hasGPUserPriceGroup) && (hasCurrUg && !hasCurrGPUserPriceGroup) 
					&& currentListPriceRow.getUg().equals(inputPriceRow.getUg())
					&& priceRowArr[3] == null) 
			{
					priceRowArr[3] = currentListPriceRow;
					LOG.debug("priceRow4 "+priceRowArr[3] );
					break;
			}
			else if((hasUg && !hasGPUserPriceGroup) && (!hasCurrUg && !hasCurrGPUserPriceGroup) 
					&& priceRowArr[4] == null) 
			{
				priceRowArr[4] = currentListPriceRow;
				LOG.debug("priceRow5 "+priceRowArr[4] );
					break;
			}
			else if((!hasUg && !hasGPUserPriceGroup) && (!hasCurrUg && !hasCurrGPUserPriceGroup)
					&& priceRowArr[5] == null)
			{
				priceRowArr[5] = currentListPriceRow;
				LOG.debug("priceRow6 "+priceRowArr[5] );
				break;
			}
		}
		return priceRowArr;
	}
	
	
	

	/**
	 * Add Field values
	 * 
	 * @param priceRow
	 * @param indexConfig
	 * @param indexedProperty
	 */
	protected void addFieldValues(final PriceRowModel priceRow,
			final IndexedProperty indexedProperty, List<FieldValue> fieldValues)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,null);
		for (final String fieldName : fieldNames) {
			FieldValue fieldValue = createFieldValue(priceRow, fieldName);
			if(null != fieldValue)
			{
				fieldValues.add(fieldValue);
			}
		}
	}

	/**
	 * create Field value
	 * 
	 * @param product
	 * @param indexedProperty
	 * @param currency
	 * @return fieldValues
	 */
	protected FieldValue createFieldValue(final PriceRowModel priceRow, String fieldName)
	{
		FieldValue fieldValue = null;
		if (null != priceRow && null != priceRow.getPrice())
		{
			LOG.debug("List Price row added :" + priceRow.getPrice() + "price row PK" + priceRow.getPk() + " product code"
					+ priceRow.getProduct().getCode());
			fieldValue = new FieldValue(fieldName, priceRow.getPrice());
		}
		return fieldValue;
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
