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
import de.hybris.platform.commerceservices.event.OrderCancelledEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;


public class OrderCancelledEventListener extends GPAbstractAcceleratorSiteEventListener<OrderCancelledEvent>
{

	private static final String SEND_ORDER_CANCELLED_EMAIL_PROCESS = "sendOrderCancelledEmailProcess";
	private static final String SEND_ORDER_CANCELLED_EMAIL_PROCESS_LABEL = "sendOrderCancelledEmailProcess-";
	
	@Override
	protected void onSiteEvent(final OrderCancelledEvent event)
	{
		final OrderModel orderModel = event.getProcess().getOrder();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				SEND_ORDER_CANCELLED_EMAIL_PROCESS_LABEL + orderModel.getCode() + "-" + System.currentTimeMillis(),
				SEND_ORDER_CANCELLED_EMAIL_PROCESS);

		orderProcessModel.setOrder(orderModel);
		//setting consignment entry and declined qty for partially cancelled scenario
		orderProcessModel.setConsignmentEntry(event.getProcess().getConsignmentEntry());
		orderProcessModel.setDeclinedQty( event.getProcess().getDeclinedQty());
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);

	}


	@Override
	protected SiteChannel getSiteChannelForEvent(final OrderCancelledEvent event)
	{
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER, order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER_SITE, site);
		return site.getChannel();
	}

}
