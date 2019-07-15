/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.core.cronjob.xpress.alert;

import org.apache.log4j.Logger;

import com.gp.commerce.core.catalog.GPProductAuditService;
import com.gp.commerce.core.model.SaveAuditRecordsCronJobModel;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class GPSaveAuditRecordsCronJob extends AbstractJobPerformable<SaveAuditRecordsCronJobModel> {
	private static final Logger LOG = Logger.getLogger(GPSaveAuditRecordsCronJob.class);
	
	private GPProductAuditService gpProductAuditService;

	@Override
	public PerformResult perform(SaveAuditRecordsCronJobModel arg0) {
		LOG.debug("SaveAuditRecordsCronJob started with last start time: "+arg0.getLastStartTime());
		
		try 
		{
				gpProductAuditService.saveAuditProducts(arg0.getLastStartTime());
				arg0.setLastStartTime(arg0.getStartTime());
				modelService.save(arg0);
				if (clearAbortRequestedIfNeeded(arg0))
				{
					LOG.debug("The job is aborted.");
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				LOG.debug("SaveAuditRecordsCronJob finised sucessfully");
		}
		catch(Exception e)
		{
			LOG.error("Error in running SaveAuditRecordsCronJob: " + e.getMessage(), e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED); 
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		
	}

	public GPProductAuditService getGpProductAuditService() {
		return gpProductAuditService;
	}

	public void setGpProductAuditService(GPProductAuditService gpProductAuditService) {
		this.gpProductAuditService = gpProductAuditService;
	}
	
	@Override
	public boolean isAbortable() {
		return true;
	} 

}
