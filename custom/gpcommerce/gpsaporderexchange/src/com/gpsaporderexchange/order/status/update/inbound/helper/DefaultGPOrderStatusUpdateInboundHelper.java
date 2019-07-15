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
package com.gpsaporderexchange.order.status.update.inbound.helper;

import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;

import com.gpintegration.core.services.event.GPOrderStatusUpdateEvent;



/**
 * Helper class to trigger order status update event
 */
public class DefaultGPOrderStatusUpdateInboundHelper
{
	private static final Logger LOG = Logger.getLogger(DefaultGPOrderStatusUpdateInboundHelper.class);

	private EventService eventService;


	public EventService getEventService()
	{
		return eventService;
	}

	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}


	public void triggerOrderStatusUpdateEvent(final String code)
	{

		if (null != code)
		{
			getEventService().publishEvent(new GPOrderStatusUpdateEvent(code));
			LOG.info(
					"GPOrderStatusUpdateEvent Successfully published with code  ---------------------------------------------------------------"
							+ code);
		}

	}


}
