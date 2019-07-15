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

import de.hybris.platform.customerreview.model.CustomerReviewModel;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.gpsapymktexpressway.model.SyncCronJobModel;
import com.hybris.ymkt.gpsapymktexpressway.services.ReviewSyncService;


public class ReviewSyncJob extends AbstractSyncJob<SyncCronJobModel, CustomerReviewModel, ReviewSyncService>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ReviewSyncJob.class);

	@Override
	protected Class<CustomerReviewModel> getModelClass()
	{
		return CustomerReviewModel.class;
	}

	@Override
	protected Optional<String> getStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional.of("{item.blocked}=FALSE AND " //
				+ "{item.approvalstatus} IN ({{ SELECT {c.pk} FROM {CustomerReviewApprovalType as c} WHERE {c.code}='approved' }}) AND " //
				+ "{item.product} IN ({{ SELECT {p.pk} FROM {Product as p} WHERE {p.catalogVersion}=?cronJob.catalogVersion }}) AND " //
				+ "{item.user} IN ({{ SELECT {pk} FROM {Customer} }})");
	}

}
