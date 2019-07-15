/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.ymkt.gpsapymktexpressway.jobs;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.util.Config;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.gpsapymktexpressway.constants.GpsapymktexpresswayConstants;
import com.hybris.ymkt.gpsapymktexpressway.model.SyncCronJobModel;
import com.hybris.ymkt.gpsapymktexpressway.services.AbandonedCartSyncService;


public class GPAbandonedCartSyncJob
		extends AbstractChildSyncJob<SyncCronJobModel, CartModel, CartEntryModel, AbandonedCartSyncService>
{
	private static final Logger LOG = LoggerFactory.getLogger(AbandonedCartSyncJob.class);

	@Override
	protected Class<CartModel> getModelClass()
	{
		return CartModel.class;
	}

	@Override
	protected Optional<String> getStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional.of("?abandonedCartTime > {item.modifiedtime} AND {item.saveTime} IS NULL AND " //
				+ "{item.user} IN ({{ SELECT {pk} FROM {Customer} }}) AND " //
				+ "{item.site} in ({{ select {s.pk} from {BaseSite AS s} where {s.uid} = ?siteId}})");
	}

	@Override
	protected Class<CartEntryModel> getChildClass()
	{
		return CartEntryModel.class;
	}

	@Override
	protected Optional<String> getChildStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional.of("?abandonedCartTime > {item.modifiedtime} AND " //
				+ "{item.order} IN ({{ SELECT {c.pk} FROM {Cart AS c} WHERE {c.saveTime} IS NULL AND " //
				+ "{c.user} IN ({{ SELECT {pk} FROM {Customer} }}) AND" //
				+ "{c.site} in ({{ select {s.pk} from {BaseSite AS s} where {s.uid} = ?siteId}}) }})");
	}

	protected int getInterval()
	{ // Minutes
		return Config.getInt("gpsapymktexpressway.syncjob.abandonedcart.interval", 120);
	}

	@Override
	protected Function<CartEntryModel, CartModel> getParentFunction()
	{
		return CartEntryModel::getOrder;
	}

	@Override
	protected Map<String, Object> getStreamConfigurationParameters(SyncCronJobModel cronJob)
	{
		final Map<String, Object> streamConfigurationParameters = super.getStreamConfigurationParameters(cronJob);
		final long cronJobStartTime = cronJob.getStartTime().getTime();
		final long abandonedCartInterval = this.getInterval() * 60 * 1000L;
		final long abandonedCartTime = cronJobStartTime - abandonedCartInterval;
		final Date abandonedCartTimeDate = new Date(abandonedCartTime);
		streamConfigurationParameters.put("abandonedCartTime", abandonedCartTimeDate);
		LOG.debug("abandonedCartTimeDate={}", abandonedCartTimeDate);

		streamConfigurationParameters.put(GpsapymktexpresswayConstants.SITE_ID, cronJob.getSiteId());
		LOG.debug("siteId={}", cronJob.getSiteId());

		return streamConfigurationParameters;
	}


}
