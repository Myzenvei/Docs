/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.core.services.GPConsignmentService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;

public class GPOrderShipmentCronjob extends AbstractJobPerformable<CronJobModel> 
{

	private static final Logger LOG = Logger.getLogger(GPOrderShipmentCronjob.class);
	private GPConsignmentService gpConsignmentService;
	private EventService eventService;
	private BusinessProcessService businessProcessService;
	
	@Override
	public PerformResult perform(CronJobModel cronjob) 
	{
		LOG.info("**** GPOrderShipmentCronjob Starts ****");
		PerformResult cronJobResult;
		Map<String, List<TrackingModel>> trackingMap = new HashMap<>();
		try {
			//Fetch tracking details which have not been processed yet
			List<TrackingModel> trackingDetails = getGpConsignmentService().getTrackingDetailsForEmail();
			if(CollectionUtils.isNotEmpty(trackingDetails)) {
				for(TrackingModel tracking : trackingDetails) {
					//Group by tracking id
					trackingMap.computeIfAbsent(tracking.getTrackingID(), k -> new ArrayList<>()).add(tracking);
				}
				
				for(String trackingKey : trackingMap.keySet()) {
					if (clearAbortRequestedIfNeeded(cronjob))
					{
						LOG.debug("The job is aborted.");
						return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
					}
					List<TrackingModel> trackingList = trackingMap.get(trackingKey);
					
					//Trigger email for each consignment
					if(triggerShipmentEmail(trackingList)) {
						//Set timestamp for each processed tracking
						trackingList.forEach(t -> {
							t.setEmailProcessedTime(new Date());
							modelService.save(t);
						});
					}
				}
				cronJobResult = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}			
			else {
				LOG.info("GPOrderShipmentCronjob : No tracking found to be processed for shipment emails");
				cronJobResult = new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.FINISHED);
			}			
		}
		catch(Exception e) {
			LOG.error("GPOrderShipmentCronjob failed due to : " + e.getMessage(),e);
			cronJobResult = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return cronJobResult;
	}

	private boolean triggerShipmentEmail(List<TrackingModel> trackingList) {
		try {
			if(CollectionUtils.isNotEmpty(trackingList)) {
				ConsignmentModel consignment = trackingList.get(0).getConsignmentEntries().get(0).getConsignment();
				final ConsignmentProcessModel consignmentProcessModel = getBusinessProcessService().createProcess(
						"sendDeliveryEmailProcess-" + consignment.getCode() + "-" + System.currentTimeMillis(),
						"sendDeliveryEmailProcess");
				consignmentProcessModel.setConsignment(consignment);
				consignmentProcessModel.setTrackings(trackingList);
				modelService.save(consignmentProcessModel);
				getBusinessProcessService().startProcess(consignmentProcessModel);
				
				if (null != consignmentProcessModel && consignmentProcessModel.getProcessState().equals(ProcessState.SUCCEEDED))
				{
					return true;
				}
				else if (null != consignmentProcessModel && (consignmentProcessModel.getProcessState().equals(ProcessState.ERROR)
						|| consignmentProcessModel.getProcessState().equals(ProcessState.FAILED)))
				{
					return false;
				}
				return true;
			}
		}
		catch(Exception e) {
			LOG.error("Shipment Email trigger failed due to : " + e.getMessage(),e);
		}
		return false;
	}

	protected GPConsignmentService getGpConsignmentService() {
		return gpConsignmentService;
	}
	
	@Required
	public void setGpConsignmentService(GPConsignmentService gpConsignmentService) {
		this.gpConsignmentService = gpConsignmentService;
	}

	protected EventService getEventService() {
		return eventService;
	}

	@Required
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	protected BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}
	
	@Required
	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 
	
}
