/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.cronjob.leaseagreement;

import org.apache.log4j.Logger;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gpintegration.service.impl.GPDefaultCommerceLegalTermsService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class GPCustomerLeaseAgreementCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(GPCustomerLeaseAgreementCronJob.class);
	private ConfigurationService configurationService;
	private GPDefaultCommerceLegalTermsService gpCommerceLegalTermsService;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		LOG.info("Legal Terms CronJob started sucessfully");
		
			if(configurationService.getConfiguration().getBoolean(GpcommerceCoreConstants.ENABLE_LEASE_AGREEMENT)) {
	
				try {
					gpCommerceLegalTermsService.fetchLegalTerms();
					if (clearAbortRequestedIfNeeded(arg0))
					{
						LOG.debug("The job is aborted.");
						return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
					}
				}
				catch(Exception e) {
					LOG.error("Error in running legal terms job"+e.getMessage(),e);
					return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
				}
			}
			else {
				LOG.info("Legal terms job is not enabled");
			}
	
	
			LOG.info("Lease agreement CronJob finised sucessfully");
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

	/**
	 * @return the gpCommerceLegalTermsService
	 */
	public GPDefaultCommerceLegalTermsService getGpCommerceLegalTermsService() {
		return gpCommerceLegalTermsService;
	}

	/**
	 * @param gpCommerceLegalTermsService the gpCommerceLegalTermsService to set
	 */
	public void setGpCommerceLegalTermsService(GPDefaultCommerceLegalTermsService gpCommerceLegalTermsService) {
		this.gpCommerceLegalTermsService = gpCommerceLegalTermsService;
	}

	@Override
	public boolean isAbortable() {
		return true;
	} 


	
	
}