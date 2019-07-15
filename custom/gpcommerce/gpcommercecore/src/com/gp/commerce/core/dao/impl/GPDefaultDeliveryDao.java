/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPDeliveryDao;

public class GPDefaultDeliveryDao implements GPDeliveryDao  {

	private static final String AMOUNT = "amount";

	private static final String CURRENCY = "curr";

	private static final String DELIVERY_MODE = "deliveryMode";

	private FlexibleSearchService flexibleSearchService;

	private static final String RETRIEVE_DELIVERYMODE_PRICE= "	SELECT {v.pk} FROM {ZoneDeliveryModeValue AS v JOIN ZoneCountryRelation AS z2cRel ON {v.zone}={z2cRel.source} } "
		+ "WHERE {v.deliveryMode} = ?deliveryMode AND {v.currency} = ?curr AND {v.minimum} <= ?amount AND "
		+ "{z2cRel.target} = ?country ORDER BY {v.minimum} DESC "  ;



	@Override
	public List<ZoneDeliveryModeValueModel> getDeliveryModeValueList(final CurrencyModel curr, final CountryModel country,
			final DeliveryModeModel deliveryMode, final double totalPrice) throws Exception {
		final Map<String, Object> params = new HashMap<>();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_DELIVERYMODE_PRICE);
		params.put(DELIVERY_MODE, deliveryMode);
		params.put(CURRENCY, curr);
		params.put(GpcommerceCoreConstants.COUNTRY, country);
		params.put(AMOUNT, new Double(totalPrice));
		searchQuery.addQueryParameters(params);
		final SearchResult<ZoneDeliveryModeValueModel> searchResult =getFlexibleSearchService().search(searchQuery);
		List<ZoneDeliveryModeValueModel>  deliveryModeValueList= searchResult.getResult();
		if (deliveryModeValueList.isEmpty() && !curr.getBase().booleanValue()
				&& C2LManager.getInstance().getBaseCurrency() != null) {
			params.put(CURRENCY, C2LManager.getInstance().getBaseCurrency());
			final SearchResult<ZoneDeliveryModeValueModel> searchResultModified =getFlexibleSearchService().search(searchQuery);
			deliveryModeValueList = searchResultModified.getResult() ;
		}

		if (deliveryModeValueList.isEmpty()) {
			throw new Exception("no delivery price defined for mode " + this
					+ ", country " + country + ", currency " + curr + " and amount " );
		}
		return deliveryModeValueList ;
	}



	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}



	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

}
