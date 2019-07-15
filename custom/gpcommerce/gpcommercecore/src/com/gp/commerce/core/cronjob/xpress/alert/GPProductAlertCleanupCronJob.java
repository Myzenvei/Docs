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

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;
import com.gp.commerce.core.xpress.alert.service.GPProductAlertService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class GPProductAlertCleanupCronJob extends AbstractJobPerformable<CronJobModel> {

	private final static Logger LOG = Logger.getLogger(GPProductAlertCleanupCronJob.class.getName());
	@Resource
	GPProductAlertService gpProductAlertService;

	@Override
	public PerformResult perform(CronJobModel arg0) {
		try 
		{
				List<GPXpressAlertProdHistoryModel> alertHistryModelList = gpProductAlertService.getExpiredGPProductALertHistoryModels();
				if (CollectionUtils.isNotEmpty(alertHistryModelList))
				{
					for(GPXpressAlertProdHistoryModel alertHistoryModel:alertHistryModelList)
					{
						if (clearAbortRequestedIfNeeded(arg0))
						{
							LOG.debug("The job is aborted.");
							return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
						}
						modelService.remove(alertHistoryModel);
					}
				}
				
		
		} 
		catch (final Exception e) 
		{
			LOG.error("Exception occurred during product alert history cleanup", e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	@Override
	public boolean isAbortable() {
		return true;
	} 

}
