/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.cronjob.crm.shellorder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.impl.GPDefaultCRMShellOrderReplicationService;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class GPCRMShellOrderReplicationJob extends AbstractJobPerformable<CronJobModel>
{
	private static final String SFDC_REPLICATION_STATUS = "sfdcReplicationStatus";
	private static final Logger LOG = Logger.getLogger(GPCRMShellOrderReplicationJob.class);
	private ConfigurationService configurationService;
	private GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService;
	
    private FlexibleSearchService flexibleSearchService;
	
	public static final String CRM_ORDER_REPLICATION_QUERY = "SELECT {o.pk} FROM {Order as o} WHERE {o.sfdcReplicationStatus} in (?sfdcReplicationStatus) ";

	public static final String CRM_SHELL_ORDER_MAX_COUNT = "crm.shell.order.max.order.count";

	public static final Integer DEFAULT_MAX_COUNT = 50;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.
	 * CronJobModel )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		LOG.info("CRM Shell Order Cron job started");
		if(configurationService.getConfiguration().getBoolean(GpintegrationConstants.CRM_SHELL_ORDER_REPLICATION_FLAG)) {
			Integer successCount = 0;
			Integer errorCount = 0;
			try {
				List<OrderModel> crmShellOrders = getOrdersForCRMReplication();
				if(CollectionUtils.isNotEmpty(crmShellOrders)) {
					LOG.info("CRM Shell Order :Total number of records for replication:"+crmShellOrders.size());
					for(OrderModel orderModel: crmShellOrders) {
						LOG.debug("Replicating CRM Shell Order from cron job:"+orderModel.getCode());
						if (clearAbortRequestedIfNeeded(arg0))
						{
							LOG.debug("The job is aborted.");
							return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
						}
						if(gpCRMShellOrderReplicationService.replicateCRMShellOrder(orderModel) ) {
							successCount++;
						} else {
							LOG.error(" Error Replicating CRM Shell Order from cron job:"+orderModel.getCode());
							errorCount++;
						}
					}
					LOG.info("CRM Shell Order Cron Job finished ; Total number of success records " + successCount + " No of error records "+  errorCount  );

				} else {
					LOG.info("CRM Shell order replication : No orders found for  replication.");
				}
				
			}
			catch(Exception e) {
				LOG.error("CRM shell Order Replication error :"+e.getMessage(),e);
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			}
		}
		else {
			LOG.info("CRM Shell Order is not enabled");
		}

		LOG.info("CRM Shell Order CronJob finised sucessfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	/**
	 * Method to fetch the order details from order table.
	 * 
	 * @return List<OrderModel>
	 */
	private List<OrderModel> getOrdersForCRMReplication(){
		final Integer maxOrderCount = configurationService.getConfiguration().getInteger(CRM_SHELL_ORDER_MAX_COUNT,DEFAULT_MAX_COUNT);
		Set<GPNetsuiteOrderExportStatus> statusList = new HashSet<>();
		statusList.add(GPNetsuiteOrderExportStatus.NOTEXPORTED);
		statusList.add(GPNetsuiteOrderExportStatus.FAILURE);
		FlexibleSearchQuery crmOrderReplicationQuery = new FlexibleSearchQuery(CRM_ORDER_REPLICATION_QUERY);
		crmOrderReplicationQuery.addQueryParameter(SFDC_REPLICATION_STATUS, statusList);
		final SearchResult<OrderModel> queryResults = getFlexibleSearchService().search(crmOrderReplicationQuery);
		final List<OrderModel> results = queryResults.getResult();
		//Limit the max number of order 
		if(CollectionUtils.isNotEmpty(results) && results.size() > 	maxOrderCount) {
			LOG.warn(" Limiting order to " +maxOrderCount + "from current size  " + results.size() );
			return results.stream().limit(maxOrderCount).collect(Collectors.toList());
		}	
		return results;
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
	 * @return the gpCRMShellOrderReplicationService
	 */
	public GPDefaultCRMShellOrderReplicationService getGpCRMShellOrderReplicationService() {
		return gpCRMShellOrderReplicationService;
	}

	/**
	 * @param gpCRMShellOrderReplicationService the gpCRMShellOrderReplicationService to set
	 */
	public void setGpCRMShellOrderReplicationService(
			GPDefaultCRMShellOrderReplicationService gpCRMShellOrderReplicationService) {
		this.gpCRMShellOrderReplicationService = gpCRMShellOrderReplicationService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public boolean isAbortable() {
		return true;
	} 


	
	
}
