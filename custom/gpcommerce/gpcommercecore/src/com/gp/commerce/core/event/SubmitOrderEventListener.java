/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.event;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;


/**
 * Listener for order submits.
 */
public class SubmitOrderEventListener extends GPAbstractAcceleratorSiteEventListener<SubmitOrderEvent>
{
	private static final Logger LOG = Logger.getLogger(SubmitOrderEventListener.class);

	private BaseStoreService baseStoreService;
	@Resource
	private EventService eventService;

	public EventService getEventService()
	{
		return eventService;
	}

	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	/**
	 * @return the baseStoreService
	 */
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	protected void onSiteEvent(final SubmitOrderEvent event)
	{
		final OrderModel order = event.getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER, order);

		// Try the store set on the Order first, then fallback to the session
		BaseStoreModel store = order.getStore();
		if (store == null)
		{
			store = getBaseStoreService().getCurrentBaseStore();
		}

		if (store == null)
		{
			LOG.warn("Unable to start fulfilment process for order [" + order.getCode()
					+ "]. Store not set on Order and no current base store defined in session.");
		}
		else
		{
			final String fulfilmentProcessDefinitionName = store.getSubmitOrderProcessCode();
			if (fulfilmentProcessDefinitionName == null || fulfilmentProcessDefinitionName.isEmpty())
			{
				LOG.warn("Unable to start fulfilment process for order [" + order.getCode() + "]. Store [" + store.getUid()
						+ "] has missing SubmitOrderProcessCode");
			}
			else
			{
				final String processCode = fulfilmentProcessDefinitionName + "-" + order.getCode() + "-" + System.currentTimeMillis();
				final OrderProcessModel businessProcessModel = getBusinessProcessService().createProcess(processCode,
						fulfilmentProcessDefinitionName);
				businessProcessModel.setOrder(order);

				if (OrderStatus.PENDING_APPROVAL.equals(order.getStatus()))
				{
					getEventService().publishEvent(new OrderPlacedEvent(businessProcessModel));
					LOG.info("Process: " + businessProcessModel.getCode() + " in step " + getClass());
				}

				getModelService().save(businessProcessModel);
				getBusinessProcessService().startProcess(businessProcessModel);
				if (LOG.isInfoEnabled())
				{
					LOG.info(String.format("Started the process %s", processCode));
				}
			}
		}
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final SubmitOrderEvent event)
	{
		final OrderModel order = event.getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER, order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER_SITE, site);
		return site.getChannel();
	}
}
