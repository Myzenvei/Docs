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
package com.gpsaporderexchangeb2b.outbound.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gpsaporderexchange.outbound.impl.GPOrderContributor;



/**
 * @author Siddharth Jain
 *
 */

/**
 * Order header contributor for B2B orders to be replicated to SAP ERP system. Enhances B2C contributor by channel and
 * external PO number
 *
 */
public class GPB2BOrderContributor extends GPOrderContributor
{

	@Override
	public Set<String> getColumns()
	{
		final Set<String> columns = super.getColumns();
		columns.addAll(Arrays.asList(OrderCsvColumns.CHANNEL, OrderCsvColumns.PURCHASE_ORDER_NUMBER));
		return columns;
	}

	@Override
	public List<Map<String, Object>> createRows(final ConsignmentModel model)
	{
		final List<Map<String, Object>> rows = super.createRows(model);
		return enhanceRowsByB2BFields(model, rows);
	}

	protected List<Map<String, Object>> enhanceRowsByB2BFields(final ConsignmentModel model, final List<Map<String, Object>> rows)
	{
		// There is only one row on order level
		final Map<String, Object> row = rows.get(0);
		row.put(OrderCsvColumns.CHANNEL, model.getOrder().getSite().getChannel());

		return rows;
	}


}