/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.strategies.GPDefaultCustomerNameStrategy;

public class GPB2BCustomerPopulator extends B2BCustomerPopulator implements Populator<CustomerModel, CustomerData>
{
	@Resource(name="b2bUnitService")
	private B2BUnitService<B2BUnitModel, UserModel> b2bUnitService;

	@Resource(name = "addressConverter")
	private Converter<AddressModel, AddressData> addressConverter;
	
	@Resource(name = "gpDefaultCustomerNameStrategy")
	private GPDefaultCustomerNameStrategy gpDefaultCustomerNameStrategy;

	@Override
	public void populate(final CustomerModel source, final CustomerData target) throws ConversionException
	{

		super.populate(source, target);
		target.setCellPhone(source.getCellPhone());
		target.setLeaseSigner(source.isLeaseSigner());
		target.setName(gpDefaultCustomerNameStrategy.getName(source.getName()));
		if (null != target.getUnit())
		{
			final B2BUnitModel parent = getB2bUnitService().getParent((B2BCustomerModel) source);
			final B2BUnitData unit = target.getUnit();
			unit.setB2bUnitLevel(parent.getB2bUnitLevel());
			populateAddresses(parent, unit);
			unit.setRole(parent.getRole());
			target.setUnit(unit);
		}
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
	}

	protected void populateAddresses(final B2BUnitModel source, final B2BUnitData target)
	{
		if (CollectionUtils.isNotEmpty(source.getAddresses()))
		{
			target.setAddresses(Converters.convertAll(source.getAddresses(), addressConverter));
		}
	}
}
