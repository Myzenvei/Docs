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
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.RetryLaterException;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderManualCheckedAction extends AbstractOrderAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(OrderManualCheckedAction.class);
	private static final String ORDER_REJECT_RETRY ="order.reject.retry";
	
	@Autowired
	private EventService eventService;
	private ConfigurationService configurationService;
	
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public enum Transition
	{
		OK, NOK, UNDEFINED;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	@Override
	public final String execute(final OrderProcessModel process) throws RetryLaterException, Exception
	{
		return executeAction(process).toString();
	}

	protected Transition executeAction(final OrderProcessModel process)
	{
		ServicesUtil.validateParameterNotNull(process, "Process cannot be null");

		final OrderModel order = process.getOrder();
		ServicesUtil.validateParameterNotNull(order, "Order in process cannot be null");
		if (order.getFraudulent() != null)
		{
			final OrderHistoryEntryModel historyLog = createHistoryLog(
					"Order Manually checked by CSA - Fraud = " + order.getFraudulent(), order);
			modelService.save(historyLog);
			if (order.getFraudulent().booleanValue())
			{
				setOrderStatus(order, OrderStatus.FRAUD_REJECTED);
				return Transition.NOK;
			}
			setOrderStatus(order, OrderStatus.APPROVED);
			return Transition.OK;
		}
		else
		{
			LOG.error("FULFILLMENT_ERROR | Fraudulent order :" +order.getCode());
			order.setProcessingStatus(OrderStatus.FULFILLMENT_ERROR);
			
			int csrRetryCount = order.getCsrRetryCount() != null ? order.getCsrRetryCount().intValue() : 0;
			order.setCsrRetryCount(csrRetryCount + 1);
			getModelService().save(order);
			
			int retryCount = configurationService.getConfiguration().getInt(ORDER_REJECT_RETRY, 3);
			if(retryCount > csrRetryCount)
			{
				return Transition.UNDEFINED;
			}
			else
			{
				LOG.error("PROCESSING_ERROR | exceeds retry count  :" +order.getCode());
				setOrderStatus(order, OrderStatus.PROCESSING_ERROR);
				getModelService().save(order);
			}
		}
		return Transition.NOK;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
}
