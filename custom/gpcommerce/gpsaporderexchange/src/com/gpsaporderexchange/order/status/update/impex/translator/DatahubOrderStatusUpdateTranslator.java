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
package com.gpsaporderexchange.order.status.update.impex.translator;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;


/**
 * Translator for order status update event call
 */
public class DatahubOrderStatusUpdateTranslator extends GPOrderStatusUpdateSpecialValueTranslator
{

	@Override
	public void performImport(final String code, final Item processedItem) throws ImpExException
	{
		if (null != code)
		{
		final String shippingNotificationcode = getCode(processedItem);
		getInboundHelper().triggerOrderStatusUpdateEvent(shippingNotificationcode);
		}

	}


}

