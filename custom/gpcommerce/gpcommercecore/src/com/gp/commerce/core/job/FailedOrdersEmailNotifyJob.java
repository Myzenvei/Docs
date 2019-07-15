/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.FailedOrderEmailNotifyCronjobModel;
import com.gp.commerce.core.model.OrderIssuesNotificationProcessModel;
import com.gp.commerce.core.order.service.GpOrderService;

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

public class FailedOrdersEmailNotifyJob extends AbstractJobPerformable<FailedOrderEmailNotifyCronjobModel>
{

	private static final String SEND_FAILED_ORDER_EMAIL = "orderIssuesNotificationProcess";
	private static final String SEND_FAILED_ORDER_EMAIL_LABEL = "orderIssuesNotificationProcess-";
	private static final Logger LOG = Logger.getLogger(FailedOrdersEmailNotifyJob.class);
	private GpOrderService gpOrderService;
	private BusinessProcessService businessProcessService;
	List<OrderModel> failedOrders = new ArrayList<>();
	List<ConsignmentModel> failedConsignments = new ArrayList<>();

	@Override
	public PerformResult perform(final FailedOrderEmailNotifyCronjobModel cronjob)
	{
		LOG.info("Inside FailedOrdersEmailNotifyJob");
		
			final PerformResult cronJobResult;
			final CMSSiteModel site = cronjob.getSite();
			Map<String,List<String>> failedOrdersMap = new HashMap<>();
			OrderIssuesNotificationProcessModel orderIssuesNotificationProcessModel = getBusinessProcessService()
					.createProcess(SEND_FAILED_ORDER_EMAIL_LABEL + System.currentTimeMillis(), SEND_FAILED_ORDER_EMAIL);
			if(site != null)
			{
				failedOrders = getGpOrderService().getFailedOrdersOfNetSuiteAndPayment(site);
				populateFailedOrdersMap(failedOrdersMap);
				if (clearAbortRequestedIfNeeded(cronjob))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				triggerEmailForFailedOrders(orderIssuesNotificationProcessModel,failedOrdersMap,site);
			}
			else
			{
				LOG.info("FailedOrdersEmailNotifyJob : Mandatory configurations cannot be null");
			}
			
	
			if (null!=orderIssuesNotificationProcessModel && orderIssuesNotificationProcessModel.getProcessState().equals(ProcessState.SUCCEEDED))
			{
				cronJobResult = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
			else if (null!=orderIssuesNotificationProcessModel && (orderIssuesNotificationProcessModel.getProcessState().equals(ProcessState.ERROR)
					|| orderIssuesNotificationProcessModel.getProcessState().equals(ProcessState.FAILED)))
			{
				cronJobResult = new PerformResult(CronJobResult.ERROR, CronJobStatus.UNKNOWN);
			}
			else
			{
				cronJobResult = new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.FINISHED);
			}
	
			return cronJobResult;
		
	}

	private void populateFailedOrdersMap(Map<String,List<String>> failedOrdersMap){
		GPNetsuiteOrderExportStatus netsuiteStatusEnum = null;
		String netSuiteStatusCode = null;
		String paymentErrorCode = null;
		
		for (OrderModel orderModel : failedOrders) {
			netsuiteStatusEnum = orderModel.getNetsuiteReplicationStatus();
			if((null!=orderModel.getStatus() && orderModel.getStatus().equals(OrderStatus.PAYMENT_ERROR)))
			{
				paymentErrorCode=orderModel.getStatus().getCode();
				if(!failedOrdersMap.containsKey(paymentErrorCode)){
					failedOrdersMap.put(paymentErrorCode, new ArrayList<String>());
				}
				failedOrdersMap.get(paymentErrorCode).add(orderModel.getCode());
			
			}
			else 
			{
				if(netsuiteStatusEnum.equals(GPNetsuiteOrderExportStatus.NOTEXPORTED) || netsuiteStatusEnum.equals(GPNetsuiteOrderExportStatus.FAILURE)
						|| netsuiteStatusEnum.equals(GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS) || netsuiteStatusEnum.equals(GPNetsuiteOrderExportStatus.FAILED_CUSTOMER_REPLICATION)
						){
					
					netSuiteStatusCode = orderModel.getNetsuiteReplicationStatus().getCode();
					if(!failedOrdersMap.containsKey(netSuiteStatusCode)){
						failedOrdersMap.put(netSuiteStatusCode, new ArrayList<String>());
					}
					failedOrdersMap.get(netSuiteStatusCode).add(orderModel.getCode());
				}
			}
		}
	}
	
	private void triggerEmailForFailedOrders(final OrderIssuesNotificationProcessModel orderIssuesNotificationProcessModel, final Map<String,List<String>> failedOrdersMap, final CMSSiteModel site) {

		if(orderIssuesNotificationProcessModel !=null)
		{
			orderIssuesNotificationProcessModel.setIssueMap(failedOrdersMap);
			orderIssuesNotificationProcessModel.setSite(site);
			modelService.save(orderIssuesNotificationProcessModel);
			if(LOG.isDebugEnabled()){
				LOG.debug("OrderFulfillmentErrorCronjob |No of orders in error state are :"+failedOrders.size()+" and consignments in error state are :"+ failedConsignments.size() +",triggering email for site:" +site.getUid());
			}
			getBusinessProcessService().startProcess(orderIssuesNotificationProcessModel);
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
