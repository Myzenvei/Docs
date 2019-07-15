/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.permission.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdTimespanPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2b.strategies.impl.AbstractPermissionEvaluationStrategy;
import de.hybris.platform.b2b.strategies.impl.DefaultB2BOrderThresholdTimespanEvaluationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.util.StandardDateRange;


public class GPDefaultB2BOrderThresholdTimespanEvaluationStrategy extends DefaultB2BOrderThresholdTimespanEvaluationStrategy
{

	private static final Logger LOG = Logger.getLogger(GPDefaultB2BOrderThresholdEvaluationStrategy.class);


	@Override
	public B2BPermissionResultModel evaluate(final AbstractOrderModel order, final B2BCustomerModel employee)
	{
		
		Set<B2BOrderThresholdTimespanPermissionModel> permissions = new HashSet<B2BOrderThresholdTimespanPermissionModel>();
		permissions = getTypesToEvaluate(employee, order);
		final List<B2BOrderThresholdTimespanPermissionModel> thresholdPermissions = null != permissions
				? (List<B2BOrderThresholdTimespanPermissionModel>) CollectionUtils.select(permissions,
						PredicateUtils.instanceofPredicate(B2BOrderThresholdTimespanPermissionModel.class))
				: null;


		B2BPermissionResultModel result = null;
		PermissionStatus status = PermissionStatus.OPEN;

		if (CollectionUtils.isNotEmpty(thresholdPermissions))
		{
			for (final B2BOrderThresholdTimespanPermissionModel permissionToEvaluate : thresholdPermissions)
			{

				try
				{

					if (permissionToEvaluate != null && permissionToEvaluate.getThreshold() != null)
					{
						final BigDecimal threshold = toMoney(permissionToEvaluate.getThreshold());
						final StandardDateRange dateRange = getB2bDateUtils().createDateRange(permissionToEvaluate.getRange());
						final List<OrderModel> approvedOrdersForDateRange = getB2bOrderDao()
								.findOrdersApprovedByDateRange(order.getUser(), dateRange.getStart(), dateRange.getEnd());
						final BigDecimal totalForOrders = toMoney(order.getTotalPrice())
								.add(getOrderTotals(approvedOrdersForDateRange));
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Order total is: " + totalForOrders + " for date range: [" + dateRange.getStart() + " - "
									+ dateRange.getEnd() + "] and permissing threshold is: " + threshold);
						}

						if (threshold.compareTo(toMoney(AbstractPermissionEvaluationStrategy.NOLIMIT)) != 0
								&& threshold.compareTo(totalForOrders) < 0)
						{
							status = PermissionStatus.PENDING_APPROVAL;
						}
					}

					result = this.getModelService().create(B2BPermissionResultModel.class);
					result.setPermission(permissionToEvaluate);
					result.setApprover(employee);
					result.setPermissionTypeCode(B2BOrderThresholdTimespanPermissionModel._TYPECODE);
					result.setStatus(status);
					if (LOG.isDebugEnabled())
					{
						LOG.debug(String.format("PermissionResult %s|%s|%s ", result.getPermissionTypeCode(), result.getStatus(),
								result.getApprover().getUid()));
					}

				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(), e);
				}



			}
		}
		return result;
	}



}
