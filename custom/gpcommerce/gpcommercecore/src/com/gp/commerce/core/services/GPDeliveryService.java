/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.dao.GPDeliveryDao;
import com.gp.commerce.dto.group.dto.DeliveryAddressGroupWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryListWsDTO;
import com.gp.commerce.facade.order.data.SplitEntryWsDTO;

/**
 * This class is for GP Delivery service
 */
public class GPDeliveryService{

	private static final Logger LOG = Logger.getLogger(GPDeliveryService.class);

	private ModelService modelService;
	private GPDeliveryDao gpDeliveryDao ;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;


	/**
	 * Get the delivery cost of the given delivery mode for the given cart or order.
	 * @param deliveryMode
	 * 			the mode of delivery
	 * @param abstractOrder
	 * 			the abstract order
	 * @param deliveryGroupDto
	 * 			the delivery group
	 * @return cost for given delivery mode for the given cart or order.
	 * @throws de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException
	 *  		if fails to get delivery cost for specified order
	 */
	public  Map<String, PriceValue> getDeliveryCostForDeliveryModeAndAbstractOrder(final DeliveryModeModel deliveryMode,
			final AbstractOrderModel abstractOrder, final DeliveryAddressGroupWsDTO deliveryGroupDto) throws Exception {

			validateParameterNotNull(deliveryMode, "deliveryMode model cannot be null");
			validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
			try
			{
				return getCost(abstractOrder,deliveryMode,deliveryGroupDto);
			}
			catch (final JaloDeliveryModeException e)
			{
				LOG.error("Failed to get delivery cost for order: " + abstractOrder.getCode(), e);
				return null;
			}

	}

		/**
		 * Get the delivery cost of the given delivery mode.
		 * @param abstractOrder
		 * 			the abstract order
		 * @param deliveryMode
		 * 			the mode of delivery
		 * @param deliveryGroupDto
		 * 			the delivery group
		 * @return delivery cost of the given delivery mode
		 * @throws Exception on error
		 */
	public Map<String, PriceValue> getCost(final AbstractOrderModel abstractOrder, final DeliveryModeModel deliveryMode,
			final DeliveryAddressGroupWsDTO deliveryGroupDto) throws Exception {

		final CurrencyModel curr = abstractOrder.getCurrency();
		final CountryModel country = abstractOrder.getDeliveryAddress().getCountry();
		final Map<String, Double> productPriceList = new HashMap<>();
		final Map<String, SplitEntryListWsDTO> addressGroupMap = deliveryGroupDto.getDeliveryGroup();
		for (final Entry<String, SplitEntryListWsDTO> address : addressGroupMap.entrySet()) {
			double totalPrice = 0.0;
			final List<SplitEntryWsDTO> splitentry = address.getValue().getSplitEntries();
				for (final SplitEntryWsDTO split : splitentry) {
				totalPrice += split.getPrice().getValue().doubleValue();
			}
			productPriceList.put(address.getKey(), totalPrice);
		}
		final Map<String, PriceValue> pricemap = getDeliveryCostforAddress(productPriceList, curr, country, deliveryMode);
		return pricemap;
	}


	/**
	 * Gets the product delivery cost for specified address.
	 * @param productPriceList
	 * 			the product price list
	 * @param curr
	 * 			the currency
	 * @param country
	 * 			the country
	 * @param deliveryMode
	 * 			the delivery mode
	 * @return the product delivery cost for specified address
	 * @throws Exception on error
	 */
	public Map<String, PriceValue> getDeliveryCostforAddress(final Map<String,Double> productPriceList ,final CurrencyModel curr, final CountryModel country,final DeliveryModeModel deliveryMode) throws Exception{

		final Map<String, PriceValue> delmodePriceMap = new HashMap<>();
		for(final Entry<String, Double> address :	productPriceList.entrySet()) {
		final double totalPrice=address.getValue() ;
		final List<ZoneDeliveryModeValueModel>  deliveryModeValueList =gpDeliveryDao.getDeliveryModeValueList( curr,  country, deliveryMode,totalPrice) ;

		final ZoneDeliveryModeValueModel bestMatch = deliveryModeValueList.get(0);
		final CurrencyModel myCurr = bestMatch.getCurrency();
		final PriceValue priceValue= !curr.equals(myCurr) && myCurr != null? new PriceValue(curr.getIsocode(),
					bestMatch.getValue(),isNetAsPrimitive(bestMatch))
					: new PriceValue(curr.getIsocode(),  getValueAsPrimitive(bestMatch),
							this.isNetAsPrimitive(bestMatch));
		delmodePriceMap.put(address.getKey(), priceValue) ;

		}

		return delmodePriceMap;

	}



	/**
	 * Gets the primitive value
	 * @param bestMatch
	 * 			the best match for delivery
	 * @return primitive value
	 */
	public double getValueAsPrimitive(final ZoneDeliveryModeValueModel bestMatch) {
		final Double value = bestMatch.getValue();
		return value != null ? value.doubleValue() : 0.0D;
	}

	/**
	 * Checks whether the net value is primitive or not
	 * @param bestMatch
	 * 			the best match for delivery
	 * @return boolean value
	 */
	public boolean isNetAsPrimitive(final ZoneDeliveryModeValueModel bestMatch) {
		final Boolean value = bestMatch.getDeliveryMode().getNet() ;
		return value != null ? value.booleanValue() : false;
	}

	/**
	 * Get shipping countries
	 *
	 * @return List<CountryModel>
	 */
	public List<CountryModel> getShippingCountries()
	{
		final Collection<CountryModel> countries = getAllShippingCountries();
		return CollectionUtils.isNotEmpty(countries) ? new ArrayList<CountryModel>(countries) : Collections.emptyList();
	}


	private Collection<CountryModel> getAllShippingCountries()
	{
		final BaseStoreModel store = baseStoreService.getCurrentBaseStore();
		return (store == null || store.getShippingCountries() == null) ? commonI18NService.getAllCountries()
				: store.getShippingCountries();
	}

	public ModelService getModelService() {
		return modelService;
	}


	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}


	public GPDeliveryDao getGpDeliveryDao() {
		return gpDeliveryDao;
	}


	public void setGpDeliveryDao(final GPDeliveryDao gpDeliveryDao) {
		this.gpDeliveryDao = gpDeliveryDao;
	}
}



