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

import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import com.gp.commerce.fulfilmentprocess.CheckOrderService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This example action checks the order for required data in the business process. Skipping this action may result in
 * failure in one of the subsequent steps of the process. The relation between the order and the business process is
 * defined in basecommerce extension through item OrderProcess. Therefore if your business process has to access the
 * order (a typical case), it is recommended to use the OrderProcess as a parentClass instead of the plain
 * BusinessProcess.
 */
public class GPFraudCheckOrderInternalAction extends AbstractFraudCheckAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(GPFraudCheckOrderInternalAction.class);

	private CheckOrderService checkOrderService;
	
	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (order == null)
		{
			LOG.error("FULFILLMENT_ERROR | Missing the order, exiting the process)");
			return Transition.FRAUD;
		}

		if (getCheckOrderService().check(order))
		{
			setOrderStatus(order, OrderStatus.FRAUD_CHECKED_PASSED);
			return Transition.OK;
		}
		else
		{
			FraudServiceResponse response= new FraudServiceResponse("Hybris- Fraud Check");
            FraudSymptom fraudSymptom = new FraudSymptom("Potential Fraud", 100);
            response.addSymptom(fraudSymptom);
            final FraudReportModel fraudReport = createFraudReport("Hybris- Fraud Check",response, order, FraudStatus.CHECK);
            final OrderHistoryEntryModel historyEntry = createHistoryLog("Hybris- Fraud Check", order, FraudStatus.CHECK,
                                            fraudReport.getCode());
            order.setFraudulent(Boolean.FALSE);
            order.setProcessingStatus(OrderStatus.FULFILLMENT_ERROR);
            order.setPotentiallyFraudulent(Boolean.TRUE);
            order.setStatus(OrderStatus.FRAUD_CHECKED);
            order.setCsrRetryCount(0);
            modelService.save(fraudReport);
            modelService.save(historyEntry);
            modelService.save(order);
            return Transition.POTENTIAL;
		}
	}

	protected CheckOrderService getCheckOrderService()
	{
		return checkOrderService;
	}

	@Required
	public void setCheckOrderService(final CheckOrderService checkOrderService)
	{
		this.checkOrderService = checkOrderService;
	}
}
