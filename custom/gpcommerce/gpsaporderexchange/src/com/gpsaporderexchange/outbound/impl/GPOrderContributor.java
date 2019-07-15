/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchange.outbound.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.gpsaporderexchange.constants.GPOrderCsvColumns;


/**
 * @author Siddharth Jain
 *
 */

/**
 * Builds the Row map for the CSV files for the Order
 */
public class GPOrderContributor implements RawItemContributor<ConsignmentModel>
{

	public static final String ONE_DAY_DELIVERY = "1-Day";
	public static final String NEXT_DAY_DELIVERY = "Next";
	public static final String TWO_DAY_DELIVERY = "2-Day";

	private final Set<String> columns = new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID, OrderCsvColumns.DATE,
			OrderCsvColumns.ORDER_CURRENCY_ISO_CODE, OrderCsvColumns.PAYMENT_MODE, OrderCsvColumns.DELIVERY_MODE,
			OrderCsvColumns.BASE_STORE, GPOrderCsvColumns.GPDIVISION, GPOrderCsvColumns.EMAIL_ID, GPOrderCsvColumns.SHIP_TYPE,
			GPOrderCsvColumns.SUBSCRIPTION_FLAG));

	private Map<String, String> batchIdAttributes;


	@Override
	public Set<String> getColumns()
	{
		columns.addAll(getBatchIdAttributes().keySet());
		return columns;
	}


	public Map<String, String> getBatchIdAttributes() {
		return batchIdAttributes;
	}

	@Required
	public void setBatchIdAttributes(final Map<String, String> batchIdAttributes) {
		this.batchIdAttributes = batchIdAttributes;
	}


	@Override
	public List<Map<String, Object>> createRows(final ConsignmentModel consignment)
	{
		final Map<String, Object> row = new HashMap<>();

		row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
		row.put(OrderCsvColumns.DATE, consignment.getOrder().getDate());
		row.put(OrderCsvColumns.ORDER_CURRENCY_ISO_CODE, consignment.getOrder().getCurrency().getIsocode());
		final DeliveryModeModel deliveryMode = consignment.getOrder().getDeliveryMode();
		row.put(OrderCsvColumns.DELIVERY_MODE, deliveryMode != null ? deliveryMode.getCode() : "");
		row.put(OrderCsvColumns.BASE_STORE, consignment.getOrder().getStore().getUid());
		row.put(OrderCsvColumns.PURCHASE_ORDER_NUMBER, consignment.getOrder().getCode());
		final String delModeCode = consignment.getConsignmentEntries().iterator().next().getDeliveryModeCode();
		row.put(GPOrderCsvColumns.SHIP_TYPE, delModeCode.contains(ONE_DAY_DELIVERY) || delModeCode.contains(NEXT_DAY_DELIVERY) ? "D1" :delModeCode.contains(TWO_DAY_DELIVERY) ? "D2" : "D3");
		final Set<ConsignmentEntryModel> entries = consignment.getConsignmentEntries();
		final Set<String> prodList = new HashSet<String>();
		for (final ConsignmentEntryModel entry : entries)
		{

			final String product = entry.getOrderEntry().getProduct().getGpdivision();
			prodList.add(product);
		}
		final String defaultDivision = consignment.getOrder().getStore().getSAPConfiguration().getSapcommon_division();
		final int size = prodList.size();
		final boolean flag = prodList.isEmpty();
		if (size == 1 && !flag)
		{
			row.put(GPOrderCsvColumns.GPDIVISION, prodList.iterator().next() != null ? prodList.iterator().next() : defaultDivision);
		}
		else
		{
			row.put(GPOrderCsvColumns.GPDIVISION, defaultDivision);
		}
		if (consignment.getOrder().getUser() instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) consignment.getOrder().getUser();
			row.put(GPOrderCsvColumns.EMAIL_ID, customer.getOriginalUid());
		}
		if (consignment.getOrder().getUser() instanceof B2BCustomerModel)
		{
			final B2BCustomerModel customer = (B2BCustomerModel) consignment.getOrder().getUser();
			row.put(GPOrderCsvColumns.EMAIL_ID, customer.getOriginalUid());
		}
		row.put(GPOrderCsvColumns.SUBSCRIPTION_FLAG, consignment.getOrder().getIsSubscription());
		getBatchIdAttributes().forEach(row::putIfAbsent);
		row.put("dh_batchId", consignment.getCode());

		return Arrays.asList(row);
	}
}
