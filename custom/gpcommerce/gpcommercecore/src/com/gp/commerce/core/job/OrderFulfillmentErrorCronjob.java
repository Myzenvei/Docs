/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.FailedOrderCronjobModel;
import com.gp.commerce.core.model.FailedOrderEmailProcessModel;
import com.gp.commerce.core.order.service.GpOrderService;

public class OrderFulfillmentErrorCronjob extends AbstractJobPerformable<FailedOrderCronjobModel>
{

	private static final String SEND_FAILED_ORDER_EMAIL = "sendFailedOrderEmail";
	private static final String SEND_FAILED_ORDER_EMAIL_LABEL = "sendFailedOrderEmail - ";
	private static final Logger LOG = Logger.getLogger(OrderFulfillmentErrorCronjob.class);
	private GpOrderService gpOrderService;
	private BusinessProcessService businessProcessService;
	List<OrderModel> failedOrders = new ArrayList<>();
	List<ConsignmentModel> failedConsignments = new ArrayList<>();

	@Override
	public PerformResult perform(final FailedOrderCronjobModel cronjob)
	{
		LOG.info("Inside OrderFulfillmentErrorCronjob");
		
		final PerformResult cronJobResult;
		final CMSSiteModel site = cronjob.getSite();
		final OrderStatus orderProcessingStatus = cronjob.getOrderProcessingStatus();
		final ConsignmentStatus consignProcessingStatus = cronjob.getConsignProcessingStatus();
		final FailedOrderEmailProcessModel failedOrderProcessModel = getBusinessProcessService()
				.createProcess(SEND_FAILED_ORDER_EMAIL_LABEL + System.currentTimeMillis(), SEND_FAILED_ORDER_EMAIL);
		if(site != null && orderProcessingStatus!=null && consignProcessingStatus!=null)
		{
			failedOrders = getGpOrderService().getFailedOrders(site,orderProcessingStatus);
			failedConsignments = getGpOrderService().getFailedConsignments(site,consignProcessingStatus);
			if (clearAbortRequestedIfNeeded(cronjob))
			{
				LOG.debug("The job is aborted.");
				return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
			}
			triggerEmailForFailedOrders(failedOrderProcessModel,failedOrders,failedConsignments,site);
		}
		else
		{
			LOG.info("OrderFulfillmentErrorCronjob : Mandatory configurations cannot be null");
		}

		if (null!=failedOrderProcessModel && failedOrderProcessModel.getProcessState().equals(ProcessState.SUCCEEDED))
		{
			cronJobResult = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else if (null!=failedOrderProcessModel && (failedOrderProcessModel.getProcessState().equals(ProcessState.ERROR)
				|| failedOrderProcessModel.getProcessState().equals(ProcessState.FAILED)))
		{
			cronJobResult = new PerformResult(CronJobResult.ERROR, CronJobStatus.UNKNOWN);
		}
		else
		{
			cronJobResult = new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.FINISHED);
		}

		return cronJobResult;
		
	}

	private void triggerEmailForFailedOrders(final FailedOrderEmailProcessModel failedOrderProcessModel, final List<OrderModel> failedOrders,final List<ConsignmentModel> failedConsignments, final CMSSiteModel site) {

		if(failedOrderProcessModel !=null)
		{
			failedOrderProcessModel.setOrder(failedOrders);
			failedOrderProcessModel.setSite(site);
			failedOrderProcessModel.setConsignments(failedConsignments);
			modelService.save(failedOrderProcessModel);
			if(LOG.isDebugEnabled()){
				LOG.debug("OrderFulfillmentErrorCronjob |No of orders in error state are :"+failedOrders.size()+" and consignments in error state are :"+ failedConsignments.size() +",triggering email for site:" +site.getUid());
			}
			getBusinessProcessService().startProcess(failedOrderProcessModel);
		}
		else
		{
			LOG.info("Could not send email as there are no orders retrieved in error state");
		}
	}

	public GpOrderService getGpOrderService() {
		return gpOrderService;
	}

	@Required
	public void setGpOrderService(final GpOrderService gpOrderService) {
		this.gpOrderService = gpOrderService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
}
