/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.job;

import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.distributorfeed.services.GPDistributorFeedService;
import com.gp.commerce.core.model.GPDistributorFeedCronJobModel;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class GPDistributorFeedCronJob extends AbstractJobPerformable<GPDistributorFeedCronJobModel>{

	private static final Logger LOG = Logger.getLogger(GPDistributorFeedCronJob.class);
	private ConfigurationService configurationService;
	private GPDistributorFeedService gpDistributorFeedService;
	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel )
	 */
	@Override
	public PerformResult perform(GPDistributorFeedCronJobModel arg0) {
		LOG.info("GP Distributor Feed CronJob started sucessfully");
		if(configurationService.getConfiguration().getBoolean(GpcommerceCoreConstants.ENABLE_DISTRIBUTOR_FEED)) {
			try {
				gpDistributorFeedService.fetchDistributorFeed();
			}
			catch(Exception e) {
				LOG.error("Error in running GP Distributor Feed CronJob"+e.getMessage(),e);
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}
		}
		else {
			LOG.info("GP Distributor Feed CronJob is not enabled");
		}
		LOG.info("GP Distributor Feed CronJob finised sucessfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService the configurationService to set
	 */
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	public GPDistributorFeedService getGpDistributorFeedService() {
		return gpDistributorFeedService;
	}
	public void setGpDistributorFeedService(GPDistributorFeedService gpDistributorFeedService) {
		this.gpDistributorFeedService = gpDistributorFeedService;
	}
	
}
