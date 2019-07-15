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

import de.hybris.platform.core.GenericSearchConstants.LOG;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.util.Config;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.gp.commerce.core.util.GPSiteConfigUtils;


public class GPB2BManualOrderApprovalAction extends AbstractOrderAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(GPB2BManualOrderApprovalAction.class);

	

	public enum Transition
	{
		OK, NOK, WAIT;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();
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
		
		if (GPSiteConfigUtils.isB2BSite(order.getSite()))
		{

			if (OrderStatus.PENDING_APPROVAL.equals(order.getStatus()))
			{
				return Transition.WAIT;
			}
			else if (OrderStatus.APPROVED.equals(order.getStatus()) || OrderStatus.CREATED.equals(order.getStatus()))
			{
				return Transition.OK;
			}
			return Transition.NOK;
		}
		else
		{
			if(LOG.isDebugEnabled()){
				LOG.info("b2c Order:" + order.getCode());
			}
			return Transition.OK;
		}
	}

}
