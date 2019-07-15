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

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.orderprocessing.events.SendDeliveryMessageEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 * Listener for SendDeliveryMessageEvent events.
 */
public class SendDeliveryMessageEventListener extends GPAbstractAcceleratorSiteEventListener<SendDeliveryMessageEvent>
{

	

	@Override
	protected void onSiteEvent(final SendDeliveryMessageEvent sendDeliveryMessageEvent)
	{
		ConsignmentProcessModel process = sendDeliveryMessageEvent.getProcess();
		final ConsignmentProcessModel consignmentProcessModel = getBusinessProcessService().createProcess(
				"sendDeliveryEmailProcess-" + process.getConsignment().getCode() + "-" + System.currentTimeMillis(),
				"sendDeliveryEmailProcess");
		consignmentProcessModel.setConsignment(process.getConsignment());
		consignmentProcessModel.setTrackings(process.getTrackings());
		getModelService().save(consignmentProcessModel);
		getBusinessProcessService().startProcess(consignmentProcessModel);
	}
	
	@Override
	protected SiteChannel getSiteChannelForEvent(SendDeliveryMessageEvent event)
	{
		final AbstractOrderModel order = event.getProcess().getConsignment().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return site.getChannel();
	}
}
