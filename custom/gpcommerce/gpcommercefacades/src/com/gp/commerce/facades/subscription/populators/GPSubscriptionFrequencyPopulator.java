/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.facades.subscription.populators;

import com.gp.commerce.core.model.GPSubscriptionFrequencyModel;
 import com.gp.commerce.facades.subscription.data.GPSubscriptionFrequencyData;
import de.hybris.platform.converters.Populator;
 import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPSubscriptionFrequencyPopulator
		implements Populator<GPSubscriptionFrequencyModel, GPSubscriptionFrequencyData> {
	@Override
	public void populate(GPSubscriptionFrequencyModel source, GPSubscriptionFrequencyData target)
			throws ConversionException {

		target.setFrequency(source.getFrequency());
		target.setCode(source.getCode());

	}
}
