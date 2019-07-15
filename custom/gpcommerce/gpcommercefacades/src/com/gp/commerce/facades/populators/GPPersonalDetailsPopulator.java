/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import org.springframework.util.Assert;

import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;

public class GPPersonalDetailsPopulator implements Populator<UserModel, CustomerData>
{
	@Override
	public void populate(final UserModel source, final CustomerData target)
	{
		Assert.notNull(source, GpcommerceFacadesConstants.SOURCE_VALIDATION_ERROR);
		Assert.notNull(target, GpcommerceFacadesConstants.TARGET_VALIDATION_ERROR);

		target.setCellPhone(source.getCellPhone());
		final CustomerModel customer = (CustomerModel) source;
		target.setLoginType(customer.getLoginType());
		target.setAddToMarketComm(customer.getAddToMarketComm());
		target.setAddToAffilatedMarketComm(customer.getAddToAffilatedMarketComm());
	}
}



