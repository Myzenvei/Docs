/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpsaporderexchange.cancellation.strategy;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.cancellation.DefaultSAPOrderCancelStateMappingStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class GPSAPOrderCancelStateMappingStrategy extends DefaultSAPOrderCancelStateMappingStrategy{
	
	private final String  OMS_ORDER_CANCEL="oms.order.cancel";
	private ConfigurationService configurationService;

	@Override
	public OrderCancelState getOrderCancelState(final OrderModel order)
	{
		final OrderStatus orderStatus = order.getStatus();
		if ((OrderStatus.CANCELLED.equals(orderStatus)) || (OrderStatus.COMPLETED.equals(orderStatus)))
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}
		if((getProperty().contains(orderStatus.toString())) ) {
			return OrderCancelState.PENDINGORHOLDINGAREA;
		}

		final Collection<ConsignmentModel> consignments = order.getConsignments();
		if ((consignments != null) && (!consignments.isEmpty()))
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}

		return OrderCancelState.SENTTOWAREHOUSE;
	}
	
	String getProperty(){
		return getConfigurationService().getConfiguration().getString(OMS_ORDER_CANCEL);
	}
	
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}