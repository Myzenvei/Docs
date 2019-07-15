/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.delivery.dao.impl;

import de.hybris.platform.commerceservices.delivery.dao.impl.DefaultCountryZoneDeliveryModeDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.link.Link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.gp.commerce.core.dao.GPCartDao;
import com.gp.commerce.core.model.ShippingRestrictionModel;

public class GPCountryZoneDeliveryModeDao extends DefaultCountryZoneDeliveryModeDao{

	private static final String ZONE_COUNTRY_RELATION = "ZoneCountryRelation";
	private static final String STORE_TO_DELIVERY_MODE_RELATION = "BaseStore2DeliveryModeRel";
	private static final String DELIVERY_COUNTRY = "deliveryCountry";
	private static final String CURRENCY = "currency";
	private static final String NET = "net";
	private static final String ACTVE = "active";
	private static final String STORE = "store";

	private GPCartDao gpCartDao ;

	@Override
	public Collection<DeliveryModeModel> findDeliveryModes(final AbstractOrderModel abstractOrder)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {zdm:").append(ItemModel.PK).append("}");
		query.append(" FROM { ").append(ZoneDeliveryModeValueModel._TYPECODE).append(" AS val");
		query.append(" JOIN ").append(ZoneDeliveryModeModel._TYPECODE).append(" AS zdm");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={zdm:").append(ItemModel.PK)
				.append('}');
		query.append(" JOIN ").append(ZONE_COUNTRY_RELATION).append(" AS z2c");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.ZONE).append("}={z2c:").append(Link.SOURCE).append('}');
		query.append(" JOIN ").append(STORE_TO_DELIVERY_MODE_RELATION).append(" AS s2d");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={s2d:").append(Link.TARGET).append('}');
		query.append(" } WHERE {val:").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?currency");
		query.append(" AND {z2c:").append(Link.TARGET).append("}=?deliveryCountry");
		query.append(" AND {s2d:").append(Link.SOURCE).append("}=?store");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.NET).append("}=?net");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.ACTIVE).append("}=?active");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(DELIVERY_COUNTRY, abstractOrder.getDeliveryAddress().getCountry());
		params.put(CURRENCY, abstractOrder.getCurrency());
		params.put(NET, abstractOrder.getNet());
		params.put(ACTVE, Boolean.TRUE);
		params.put(STORE, abstractOrder.getStore());

		Collection<DeliveryModeModel> deliveryModes= doSearch(query.toString(), params, DeliveryModeModel.class);
		final List<String> restrictedCode = getRestrictedCode(abstractOrder);
		if(CollectionUtils.isNotEmpty(restrictedCode)) {
			deliveryModes = deliveryModes.stream().filter(d -> !restrictedCode.contains(d.getRestrictedCode())).collect(Collectors.toList());
		}
		return deliveryModes;
	}

	private List<String> getRestrictedCode(final AbstractOrderModel abstractOrder) {
		final List<String> restrictedCode = new ArrayList<>();
		for (final AbstractOrderEntryModel entry : abstractOrder.getEntries()) {
			final List<ShippingRestrictionModel> shippingRestriction = getGpCartDao().fetchShippingRestrictions(
					entry.getProduct().getCode(), abstractOrder.getDeliveryAddress().getCountry().getIsocode(),
					abstractOrder.getDeliveryAddress().getRegion().getIsocodeShort());
			if (CollectionUtils.isNotEmpty(shippingRestriction)) {
				for (final ShippingRestrictionModel code : shippingRestriction) {
					if (null != code.getDeliveryMode()) {
						restrictedCode.add(code.getDeliveryMode());
					}
				}
			}
		}
		return restrictedCode;
	}

	public GPCartDao getGpCartDao() {
		return gpCartDao;
	}

	public void setGpCartDao(final GPCartDao gpCartDao) {
		this.gpCartDao = gpCartDao;
	}

}
