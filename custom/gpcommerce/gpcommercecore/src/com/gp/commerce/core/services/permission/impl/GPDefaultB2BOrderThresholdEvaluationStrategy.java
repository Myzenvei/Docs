/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.services.permission.impl;

import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BOrderThresholdPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2b.strategies.impl.AbstractPermissionEvaluationStrategy;
import de.hybris.platform.b2b.strategies.impl.DefaultB2BOrderThresholdEvaluationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.log4j.Logger;


/**
 * A strategy for evaluating {@link B2BOrderThresholdPermissionModel}
 */
public class GPDefaultB2BOrderThresholdEvaluationStrategy extends DefaultB2BOrderThresholdEvaluationStrategy
{
	private static final Logger LOG = Logger.getLogger(GPDefaultB2BOrderThresholdEvaluationStrategy.class);


	@Override
	public B2BPermissionResultModel evaluate(final AbstractOrderModel order, final B2BCustomerModel employee)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Evaluating B2BOrderThresholdPermissionModel for employee: " + employee.getUid());
		}
		
		Set<B2BOrderThresholdPermissionModel> permissions = new HashSet<B2BOrderThresholdPermissionModel>();
		permissions = getTypesToEvaluate(employee, order);

		final List<B2BOrderThresholdPermissionModel> thresholdPermissions = null != permissions
				? (List<B2BOrderThresholdPermissionModel>) CollectionUtils.select(permissions,
						PredicateUtils.instanceofPredicate(B2BOrderThresholdPermissionModel.class))
				: null;

		B2BPermissionResultModel result = null;
		if (CollectionUtils.isNotEmpty(thresholdPermissions))
		{
			for (final B2BOrderThresholdPermissionModel permissionToEvaluate : thresholdPermissions)
			{
				try
				{

					if (permissionToEvaluate.getThreshold() != null)
					{
						final BigDecimal threshold = toMoney(permissionToEvaluate.getThreshold());
						if (threshold.compareTo(toMoney(AbstractPermissionEvaluationStrategy.NOLIMIT)) != 0
								&& threshold.compareTo(toMoney(order.getTotalPrice())) < 0)
						{
							result = this.getModelService().create(B2BPermissionResultModel.class);
							result.setApprover(employee);
							result.setPermission(permissionToEvaluate);
							result.setPermissionTypeCode(B2BOrderThresholdPermissionModel._TYPECODE);
							result.setStatus(PermissionStatus.PENDING_APPROVAL);
							break;
						}
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Order total is: " + order.getTotalPrice().doubleValue() + " and permissing threshold is: "
									+ permissionToEvaluate.getThreshold());
						}
					}
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(), e);
				}


			}
		}


		if (LOG.isDebugEnabled() && null != result)
		{
			LOG.debug(String.format("PermissionResult %s|%s|%s ", result.getPermissionTypeCode(), result.getStatus(),
					result.getApprover().getUid()));
		}

		return result;
	}


}
