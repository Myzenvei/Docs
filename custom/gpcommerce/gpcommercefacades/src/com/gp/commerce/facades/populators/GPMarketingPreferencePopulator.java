/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import com.gp.commerce.core.model.MarketingPreferenceModel;
import com.gp.commerce.facades.data.user.data.MarketingPreferenceData;

public class GPMarketingPreferencePopulator implements Populator<MarketingPreferenceModel, MarketingPreferenceData>{

	@Resource
	private UserService userService;

	@Override
	public void populate(final MarketingPreferenceModel source, final MarketingPreferenceData target) throws ConversionException {
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setPreferenceTypeId(source.getPreferenceTypeId());
		final CustomerModel currentCustomer = (CustomerModel)userService.getCurrentUser();
		target.setSelected(false);
		Boolean value = Boolean.parseBoolean(source.getValue());

		if (currentCustomer != null && currentCustomer.getMarketingPreferences().isEmpty()
				&& (currentCustomer.getAddToMarketComm().booleanValue() == value))
		{
			target.setSelected(true);
		}
		else if (currentCustomer != null && !currentCustomer.getMarketingPreferences().isEmpty())
		{
			target.setSelected(currentCustomer.getMarketingPreferences().contains(source));
		}
	}
}
