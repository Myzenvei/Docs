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
package com.gpreturnorder.hybris.inboud.helper;

import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;

import com.gpreturnorder.hybris.inbound.events.CreditMemoEvent;


/**
 *
 */
public class DefaultGPDataHubInboundOrderHelper
{
	private static final Logger LOG = Logger.getLogger(DefaultGPDataHubInboundOrderHelper.class);

	private EventService eventService;




	public EventService getEventService()
	{
		return eventService;
	}

	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}



	public void triggerCreditMemoEvent(final String returnRequestCode)
	{

		if (null != returnRequestCode)
		{
			getEventService().publishEvent(new CreditMemoEvent(returnRequestCode));
			LOG.info("CreditMemoEvent Successfully published ---------------------------------------------------------------");
		}

	}


}
