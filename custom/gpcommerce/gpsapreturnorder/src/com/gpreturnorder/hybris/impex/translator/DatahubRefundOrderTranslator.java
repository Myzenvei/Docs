/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.gpreturnorder.hybris.impex.translator;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;

import org.apache.log4j.Logger;

/**
 *
 */
public class DatahubRefundOrderTranslator extends GPSpecialValueTranslator
{
	private static final Logger LOG = Logger.getLogger(DatahubRefundOrderTranslator.class);

	@Override
	public void performImport(final String orderInfo, final Item processedItem) throws ImpExException
	{
		LOG.info("Inside DatahubRefundOrderTranslator -----------------------------------------------------");
		final String returnRequestCode = getOrderCode(processedItem);
		getInboundHelper().triggerCreditMemoEvent(returnRequestCode);

	}


}

