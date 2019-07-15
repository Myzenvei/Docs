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
package com.gp.commerce.fulfilmentprocess.actions.order;


import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.events.OrderFraudEmployeeNotificationEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.RetryLaterException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.event.CSROrderApprovalRejectionEvent;
import com.gp.commerce.fulfilmentprocess.CheckOrderService;


/**
 * Action in order business process, which sets the order status to {@link OrderStatus#WAIT_FRAUD_MANUAL_CHECK} and
 * sends event {@link OrderFraudEmployeeNotificationEvent}. <br>
 * To notify back-end user about potential order frauds, a proper business logic needs to be implemented in appropriate
 * event listener.
 * 
 * 
 */
public class PrepareOrderForManualCheckAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(PrepareOrderForManualCheckAction.class);

	private EventService eventService;
	
	@Resource(name="checkOrderService")
	private CheckOrderService checkOrderService;

	@Override
	public void executeAction(final OrderProcessModel process) throws RetryLaterException, Exception
	{
		ServicesUtil.validateParameterNotNull(process, "Process cannot be null");
		final OrderModel order = process.getOrder();
		ServicesUtil.validateParameterNotNull(order, "Process order cannot be null");
		order.setStatus(OrderStatus.WAIT_FRAUD_MANUAL_CHECK);
		process.setFraudType(checkOrderService.checkFraudType(order));
		
		modelService.save(order);
		modelService.save(process);
		eventService.publishEvent(new OrderFraudEmployeeNotificationEvent(order));
		LOG.debug("Process: " + process.getCode() + " in step " + getClass());
		
		String fraudType=checkOrderService.checkFraudType(order);
		if(null!=fraudType)
		{
			LOG.info("Triggering event to notify CSR about Fraud Order waiting for verification");
			eventService.publishEvent(new CSROrderApprovalRejectionEvent(process,fraudType));
			LOG.debug("Process: " + process.getCode() + " in step " + getClass());
		}
		

	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
