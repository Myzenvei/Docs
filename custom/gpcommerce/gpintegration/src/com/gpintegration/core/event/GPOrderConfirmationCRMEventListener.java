/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.event;

import org.apache.log4j.Logger;

import com.gpintegration.service.impl.GPDefaultCRMShellOrderReplicationService;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;

public class GPOrderConfirmationCRMEventListener extends AbstractAcceleratorSiteEventListener<OrderPlacedEvent>{

	private static final Logger LOG = Logger.getLogger(GPOrderConfirmationCRMEventListener.class);

	private GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService;


	@Override
	protected SiteChannel getSiteChannelForEvent(OrderPlacedEvent event) {
		return null;
	}

	@Override
	protected void onSiteEvent(final OrderPlacedEvent orderPlacedEvent) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void onEvent(final OrderPlacedEvent orderPlacedEvent) {
		final OrderModel orderModel = orderPlacedEvent.getProcess().getOrder();
		LOG.info("CRM Shell order replication triggered from OrderConfirmationCRMEventListner");
		gpCRMShellOrderReplicationService.replicateCRMShellOrder(orderModel);
		

	}

	/**
	 * @return the gpCRMShellOrderReplicationService
	 */
	public GPDefaultCRMShellOrderReplicationService getGpCRMShellOrderReplicationService() {
		return gpCRMShellOrderReplicationService;
	}

	/**
	 * @param gpCRMShellOrderReplicationService the gpCRMShellOrderReplicationService to set
	 */
	public void setGpCRMShellOrderReplicationService(
			GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService) {
		this.gpCRMShellOrderReplicationService = gpCRMShellOrderReplicationService;
	}
	
	

}
