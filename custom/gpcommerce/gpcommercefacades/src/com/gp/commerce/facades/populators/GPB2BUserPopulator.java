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
package com.gp.commerce.facades.populators;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUserPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link CustomerData} from a {@link B2BCustomerModel}
 */
public class GPB2BUserPopulator extends B2BUserPopulator implements Populator<B2BCustomerModel, CustomerData>
{
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	@Override
	public void populate(final B2BCustomerModel source, final CustomerData target)
	{
		super.populate(source, target);
		target.setUserApprovalStatus(source.getUserApprovalStatus().getCode());
		if (!"L1".equalsIgnoreCase(source.getDefaultB2BUnit().getB2bUnitLevel()))
		{
		target.setEdited(null != source.getNewEmail() ? Boolean.TRUE : Boolean.FALSE);
		}
		else
		{
			target.setEdited(false);
		}
		if(null != source.getEmail()) {
			target.setEmail(source.getEmail());
		}
		target.setName(gpDefaultCustomerNameStrategy.getName(source.getName()));
		target.setFirstName(StringUtils.substringBefore(source.getName(),"|"));
		target.setLastName(StringUtils.substringAfter(source.getName(),"|"));
		
	}

	@Required
	public void setGpDefaultCustomerNameStrategy(GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy) {
		this.gpDefaultCustomerNameStrategy = gpDefaultCustomerNameStrategy;
	}
}
