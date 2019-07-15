/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;

import de.hybris.platform.commercefacades.user.converters.populator.PrincipalPopulator;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.core.model.security.PrincipalModel;

public class GPPrincipalPopulator extends PrincipalPopulator {
	
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	@Override
	public void populate(final PrincipalModel source, final PrincipalData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setUid(source.getUid());
		target.setName(gpDefaultCustomerNameStrategy.getName(source.getDisplayName()));
	}

}
