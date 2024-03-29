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
package com.gp.commerce.queues.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import com.gp.commerce.model.expressupdate.cron.ProductExpressUpdateCleanerCronJobModel;
import com.gp.commerce.queues.impl.ProductExpressUpdateQueue;

import java.util.Date;


/**
 * A Cron Job for cleaning up {@link ProductExpressUpdateQueue}.
 */
public class ProductExpressUpdateCleanerJob extends AbstractJobPerformable<ProductExpressUpdateCleanerCronJobModel>
{
	private ProductExpressUpdateQueue productExpressUpdateQueue;

	@Override
	public PerformResult perform(final ProductExpressUpdateCleanerCronJobModel cronJob)
	{
		
			final Date timestamp = new Date(System.currentTimeMillis() - (cronJob.getQueueTimeLimit().intValue() * 60 * 1000));
			productExpressUpdateQueue.removeItems(timestamp);
			if (clearAbortRequestedIfNeeded(cronJob))
			{
				return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
			}
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		
	}

	public void setProductExpressUpdateQueue(final ProductExpressUpdateQueue productExpressUpdateQueue)
	{
		this.productExpressUpdateQueue = productExpressUpdateQueue;
	}
	
	@Override
	public boolean isAbortable() {
		return true;
	} 
}
