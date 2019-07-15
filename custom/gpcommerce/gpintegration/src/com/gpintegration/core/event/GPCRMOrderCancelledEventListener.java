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
package com.gpintegration.core.event;

import org.apache.log4j.Logger;

import com.gpintegration.service.impl.GPDefaultCRMShellOrderReplicationService;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.OrderCancelledEvent;
import de.hybris.platform.core.model.order.OrderModel;


public class GPCRMOrderCancelledEventListener extends AbstractAcceleratorSiteEventListener<OrderCancelledEvent>
{

	private static final Logger LOG = Logger.getLogger(GPCRMOrderCancelledEventListener.class);

	private GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService;
	
	@Override
	protected void onEvent(final OrderCancelledEvent orderCancelledEvent) {
		final OrderModel orderModel = orderCancelledEvent.getProcess().getOrder();
		LOG.info("CRM Shell order replication triggered for cancellation");
		gpCRMShellOrderReplicationService.updateCRMShellOrder(orderModel);
	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final OrderCancelledEvent event)
	{
		return null;
	}

	@Override
	protected void onSiteEvent(OrderCancelledEvent event) {
		// TODO Auto-generated method stub
		
	}

	public GPDefaultCRMShellOrderReplicationService getGpCRMShellOrderReplicationService() {
		return gpCRMShellOrderReplicationService;
	}

	public void setGpCRMShellOrderReplicationService(
			GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService) {
		this.gpCRMShellOrderReplicationService = gpCRMShellOrderReplicationService;
	}
	
	

}
