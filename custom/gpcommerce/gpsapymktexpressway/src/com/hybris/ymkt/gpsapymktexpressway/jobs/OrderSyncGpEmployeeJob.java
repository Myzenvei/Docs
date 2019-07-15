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

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.gpsapymktexpressway.constants.GpsapymktexpresswayConstants;
import com.hybris.ymkt.gpsapymktexpressway.model.SyncCronJobModel;
import com.hybris.ymkt.gpsapymktexpressway.services.OrderSyncService;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;


public class OrderSyncGpEmployeeJob extends AbstractChildSyncJob<SyncCronJobModel, OrderModel, OrderEntryModel, OrderSyncService>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(OrderSyncJob.class);

	@Override
	protected Class<OrderModel> getModelClass()
	{
		return OrderModel.class;
	}

	@Override
	protected Optional<String> getStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional.of("{item.user} IN ({{ SELECT {pk} FROM {Customer} }}) " //
				+ "and {item.site} in ({{ select {s.pk} from {BaseSite AS s} where {s.uid} = ?siteId}})");
	}

	@Override
	protected Class<OrderEntryModel> getChildClass()
	{
		return OrderEntryModel.class;
	}

	@Override
	protected Optional<String> getChildStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional
				.of("{item.order} IN ({{ SELECT {o.pk} FROM {Order AS o} WHERE {o.user} IN ({{ SELECT {pk} FROM {Customer}}}) " //
						+ " and {o.site} in ({{ select {s.pk} from {BaseSite AS s} where {s.uid} = ?siteId}}) }})" );//
	}

	@Override
	protected Function<OrderEntryModel, OrderModel> getParentFunction()
	{
		return OrderEntryModel::getOrder;
	}
	
	@Override
	protected Map<String, Object> getStreamConfigurationParameters(SyncCronJobModel cronJob)
	{
		final Map<String, Object> streamConfigurationParameters = super.getStreamConfigurationParameters(cronJob);
		streamConfigurationParameters.put(GpsapymktexpresswayConstants.SITE_ID, cronJob.getSiteId());

		LOG.debug("siteId={}", cronJob.getSiteId());
		return streamConfigurationParameters;
	}

}
