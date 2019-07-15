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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.event.GPOrderRejectedEvent;


public class GPOrderRejectedEventListener extends GPAbstractAcceleratorSiteEventListener<GPOrderRejectedEvent>
{
	private static final String ORDER_REJECT_EMAIL_PROCESS = "orderRejectEmailProcess";
	private static final String ORDER_REJECT_EMAIL_PROCESS_LABEL = "orderRejectEmailProcess-";
	

	@Override
	protected SiteChannel getSiteChannelForEvent(final GPOrderRejectedEvent event) {
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER, order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage(GpcommerceCoreConstants.EVENT_ORDER_SITE, site);
		return site.getChannel();
	}

	@Override
	protected void onSiteEvent(final GPOrderRejectedEvent orderRejectEvent) {
		final OrderModel order = orderRejectEvent.getProcess().getOrder();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				ORDER_REJECT_EMAIL_PROCESS_LABEL + order.getCode() + "-" + System.currentTimeMillis(), ORDER_REJECT_EMAIL_PROCESS);
		orderProcessModel.setOrder(order);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);

	}
}
