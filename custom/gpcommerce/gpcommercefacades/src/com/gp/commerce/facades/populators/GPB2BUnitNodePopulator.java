/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUnitNodePopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

public class GPB2BUnitNodePopulator extends B2BUnitNodePopulator {

	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<B2BCustomerModel, CustomerData> b2BCustomerConverter;

	public Converter<B2BCustomerModel, CustomerData> getB2BCustomerConverter() {
		return b2BCustomerConverter;
	}

	public void setB2BCustomerConverter(
			final Converter<B2BCustomerModel, CustomerData> b2bCustomerConverter) {
		b2BCustomerConverter = b2bCustomerConverter;
	}

	public Converter<AddressModel, AddressData> getAddressConverter() {
		return addressConverter;
	}

	public void setAddressConverter(
			final Converter<AddressModel, AddressData> addressConverter) {
		this.addressConverter = addressConverter;
	}


	@Override
	public void populate(final B2BUnitModel source, final B2BUnitNodeData target)
	{
		super.populate(source, target);
		populateAddresses(source, target);
		populateAdministrators(source, target);
		populateCustomers(source, target);
		target.setRole(source.getRole());
	}


	protected void populateAddresses(final B2BUnitModel source, final B2BUnitNodeData target)
	{
		if (CollectionUtils.isNotEmpty(source.getAddresses()))
		{
			target.setAddresses(Converters.convertAll(source.getAddresses(), getAddressConverter()));
		}
	}

	protected void populateAdministrators(final B2BUnitModel source, final B2BUnitNodeData target)
	{

		final Collection<B2BCustomerModel> administrators = getB2BUnitService().getUsersOfUserGroup(source,
				B2BConstants.B2BADMINGROUP, false);
		if (CollectionUtils.isNotEmpty(administrators))
		{
			target.setNoOfAdministrators(administrators.size());
		}
	}

	protected void populateCustomers(final B2BUnitModel source, final B2BUnitNodeData target)
	{
		final Collection<B2BCustomerModel> b2BCustomers = getB2BUnitService().getUsersOfUserGroup(source,
				B2BConstants.B2BCUSTOMERGROUP, false);
		if (CollectionUtils.isNotEmpty(b2BCustomers))
		{
			target.setNoOfCustomers(b2BCustomers.size());
		}
	}

	@Override
	protected void populateParent(final B2BUnitModel source, final B2BUnitNodeData target)
	{
		final B2BUnitModel parent = getB2BUnitService().getParent(source);
		if (parent != null)
		{
			target.setParent(parent.getUid());
			target.setParentName(parent.getName());
		}
	}
}
