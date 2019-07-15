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

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.GPServiceProductModel;
import com.gpsaporderexchange.constants.GPOrderCsvColumns;


/**
 * @author Siddharth Jain
 *
 */

/**
 * Builds the Row map for the CSV files for the Order Entry
 */
public class GPOrderEntryContributor implements RawItemContributor<ConsignmentModel> {
	private final static Logger LOG = Logger.getLogger(GPOrderEntryContributor.class);

	private Map<String, String> batchIdAttributes;

	private CustomerAccountService customerAccountService;

	public Map<String, String> getBatchIdAttributes() {
		return batchIdAttributes;
	}

	@Required
	public void setBatchIdAttributes(final Map<String, String> batchIdAttributes) {
		this.batchIdAttributes = batchIdAttributes;
	}

	@Override
	public Set<String> getColumns() {

		final Set<String> columns = new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID,
				OrderEntryCsvColumns.ENTRY_NUMBER, OrderEntryCsvColumns.QUANTITY, OrderEntryCsvColumns.REJECTION_REASON,
				OrderEntryCsvColumns.NAMED_DELIVERY_DATE, OrderEntryCsvColumns.ENTRY_UNIT_CODE,
				OrderEntryCsvColumns.PRODUCT_CODE, OrderEntryCsvColumns.PRODUCT_NAME,
				OrderEntryCsvColumns.EXTERNAL_PRODUCT_CONFIGURATION, GPOrderCsvColumns.ITEM_NUMBER_FOR_S4));
		columns.addAll(getBatchIdAttributes().keySet());

		return columns;
	}

	@Override
	public List<Map<String, Object>> createRows(final ConsignmentModel consignment) {
		final Set<ConsignmentEntryModel> entries = consignment.getConsignmentEntries();
		final List<Map<String, Object>> result = new ArrayList<>();

		for (final ConsignmentEntryModel entry : entries) {
			if(!(entry.getOrderEntry().getProduct() instanceof GPServiceProductModel))
			{
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, consignment.getCode());
			row.put(OrderEntryCsvColumns.ENTRY_NUMBER, entry.getConsignmentEntryNumber());
			row.put(GPOrderCsvColumns.ITEM_NUMBER_FOR_S4, ((entry.getConsignmentEntryNumber().intValue() + 1) * 10));
			row.put(OrderEntryCsvColumns.QUANTITY, entry.getQuantity());
			row.put(OrderEntryCsvColumns.PRODUCT_CODE, entry.getOrderEntry().getProduct().getCode());
			final UnitModel unit = entry.getOrderEntry().getUnit();
			if (unit != null) {
				row.put(OrderEntryCsvColumns.ENTRY_UNIT_CODE, unit.getCode());
			} else {
				LOG.warn("Could not determine unit code for product " + entry.getOrderEntry().getProduct().getCode()
						+ "as entry " + entry.getOrderEntry().getEntryNumber() + "of order " + consignment.getCode());
			}
			row.put(OrderEntryCsvColumns.EXTERNAL_PRODUCT_CONFIGURATION,
					getProductConfigurationData(entry.getOrderEntry()));
			String language = "EN";
			String shortText = determineItemShortText(entry, language);
			final OrderModel order = getOrderForCode(consignment.getOrder());
			if (shortText.isEmpty()) {
				final List<LanguageModel> fallbackLanguages = order.getLanguage().getFallbackLanguages();
				if (!fallbackLanguages.isEmpty()) {
					language = fallbackLanguages.get(0).getIsocode();
					shortText = determineItemShortText(entry, language);
				}
			}

			row.put(OrderEntryCsvColumns.PRODUCT_NAME, shortText);

			getBatchIdAttributes().forEach(row::putIfAbsent);
			row.put("dh_batchId", consignment.getCode());

			result.add(row);
			}
		}

		return result;
	}

	protected String getProductConfigurationData(final AbstractOrderEntryModel entry) {
		return entry.getExternalConfiguration();
	}

	private OrderModel getOrderForCode(final AbstractOrderModel order) {

		final OrderModel orderModel = getCustomerAccountService().getOrderForCode(order.getCode(), order.getStore());
		return orderModel;
	}

	protected String determineItemShortText(final ConsignmentEntryModel item, final String language) {
		final String shortText = item.getOrderEntry().getProduct().getName(new java.util.Locale(language));
		return shortText == null ? "" : shortText;
	}

	@SuppressWarnings("javadoc")
	public CustomerAccountService getCustomerAccountService() {
		return customerAccountService;
	}

	@SuppressWarnings("javadoc")
	public void setCustomerAccountService(final CustomerAccountService customerAccountService) {
		this.customerAccountService = customerAccountService;
	}

}
