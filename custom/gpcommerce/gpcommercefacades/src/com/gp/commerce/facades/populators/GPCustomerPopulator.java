/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;


public class GPCustomerPopulator extends CustomerPopulator implements Populator<CustomerModel, CustomerData>
{
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;
	
	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		super.populate(source, target);
		target.setName(gpDefaultCustomerNameStrategy.getName(source.getName()));
		if(null != source.getDateOfBirth())
		{
			Date dateOfBirth = source.getDateOfBirth();
			SimpleDateFormat sdf= new SimpleDateFormat("dd-MM");
			String format = sdf.format(dateOfBirth);
			target.setDateOfBirth(format);
		}
		if(null != source.getGender() && StringUtils.isNotEmpty(source.getGender().getCode()))
		{
			target.setGender(source.getGender().getCode());
		}
		if(source.getCountry() != null) {
			target.setCountry(source.getCountry());	
		}	
		if(source instanceof B2BCustomerModel) {
			B2BCustomerModel customer = (B2BCustomerModel) source;
			if(null != customer.getEmail()) {
			target.setEmail(customer.getEmail());
			}
		}
	}
}
