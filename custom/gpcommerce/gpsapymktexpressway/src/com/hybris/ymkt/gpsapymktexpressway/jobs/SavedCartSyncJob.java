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

import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.gpsapymktexpressway.model.SyncCronJobModel;
import com.hybris.ymkt.gpsapymktexpressway.services.SavedCartSyncService;


public class SavedCartSyncJob extends AbstractChildSyncJob<SyncCronJobModel, CartModel, CartEntryModel, SavedCartSyncService>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SavedCartSyncJob.class);

	@Override
	protected Class<CartModel> getModelClass()
	{
		return CartModel.class;
	}

	@Override
	protected Optional<String> getStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional.of("{item.saveTime} IS NOT NULL AND {item.user} IN ({{ SELECT {pk} FROM {Customer} }})");
	}

	@Override
	protected Class<CartEntryModel> getChildClass()
	{
		return CartEntryModel.class;
	}

	@Override
	protected Optional<String> getChildStreamConfigurationItemSelector(SyncCronJobModel cronJob)
	{
		return Optional.of("{item.order} IN ({{ SELECT {c.pk} FROM {Cart AS c} WHERE {c.saveTime} IS NOT NULL AND " //
				+ "{c.user} IN ({{ SELECT {pk} FROM {Customer} }}) }})");
	}

	@Override
	protected Function<CartEntryModel, CartModel> getParentFunction()
	{
		return CartEntryModel::getOrder;
	}

	@Override
	protected Optional<Function<CartModel, String>> getItemModelSerializer()
	{
		return Optional.of(CartModel::getCode);
	}

	@Override
	protected Optional<Function<String, CartModel>> getItemModelDeserializer()
	{
		return Optional.of(s -> {
			final CartModel cart = modelService.create(getModelClass());
			cart.setCode(s);
			return cart;
		});
	}
}
