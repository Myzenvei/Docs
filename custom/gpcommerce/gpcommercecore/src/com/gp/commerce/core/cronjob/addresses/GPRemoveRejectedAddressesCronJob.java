/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.cronjob.addresses;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.user.dao.GPUserDao;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class GPRemoveRejectedAddressesCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(GPRemoveRejectedAddressesCronJob.class);
	private ConfigurationService configurationService;
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	
	@Resource(name = "userDao")
	private GPUserDao userDao;

	/*
	 * job to remove all rejected addresses
	 *
	 *	@param arg0
	 *          CronJobModel
	 *  returns 
	 *   PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel cronjob)
	{
		LOG.debug("Remove Addresses CronJob started sucessfully");
		try {
				
				List<GPApprovalEnum> approvalStatuses = new ArrayList<>();
				approvalStatuses.add(GPApprovalEnum.REJECTED);
				List<AddressModel> addresses = userDao.getAllAddressesOnStatus(approvalStatuses);
	
				if (CollectionUtils.isNotEmpty(approvalStatuses) && CollectionUtils.isNotEmpty(addresses)) 
				{
					for(AddressModel address : addresses) 
					{
						if (clearAbortRequestedIfNeeded(cronjob))
						{
							LOG.debug("The job is aborted.");
							return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
						}
						LOG.debug("status" + address.getApprovalStatus().getCode());
						modelService.remove(address);
					}
				} 
				else
				{
					LOG.debug("no addresses in rejected state");
	
				}
			}
		
		catch(Exception e) 
			{
				LOG.error("Error in running Remove Addresses job" + e.getMessage(),e);
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			}
		LOG.debug("Remove Addresses CronJob finised sucessfully");
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

	@Override
	public boolean isAbortable() {
		return true;
	} 

	
}
