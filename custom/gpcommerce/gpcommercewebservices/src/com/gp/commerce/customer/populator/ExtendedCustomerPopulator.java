/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.customer.populator;


import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;


/**
 * Extended populator implementation for {@link de.hybris.platform.core.model.user.CustomerModel} as source and
 * {@link de.hybris.platform.commercefacades.user.data.CustomerData} as target type.
 */
public class ExtendedCustomerPopulator implements Populator<CustomerModel, CustomerData>
{
    private static final String ANONYMOUS = "anonymous";
	private Converter<AddressModel, AddressData> addressConverter;
	private static final int DEFAULT_ANONYMOUS_CART_MAX_AGE = Config.getInt("default.anonymous.cart.max.age", 7200);
	
    protected Converter<AddressModel, AddressData> getAddressConverter()
    {
        return addressConverter;
    }

    @Required
    public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
    {
        this.addressConverter = addressConverter;
    }

	@Override
	public void populate(final CustomerModel source, final CustomerData target) throws ConversionException //NOSONAR
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getTitle() != null)
		{
			target.setTitle(source.getTitle().getName());
		}
		
		if (source.getCellPhone()  != null)
		{
			target.setContactNumber(source.getCellPhone());
		}

        if (source.getDefaultPaymentAddress() != null)
        {
            target.setDefaultBillingAddress(getAddressConverter().convert(source.getDefaultPaymentAddress()));
        }
        if (source.getDefaultShipmentAddress() != null)
        {
            target.setDefaultShippingAddress(getAddressConverter().convert(source.getDefaultShipmentAddress()));
        }
        if (ANONYMOUS.equalsIgnoreCase(target.getUid()))
        {
        	target.setCartTimeOut(DEFAULT_ANONYMOUS_CART_MAX_AGE);
        }
        if (source instanceof B2BCustomerModel)
        {	
        	if (((B2BCustomerModel)source).isLeaseSigner()) {
        		target.setIsB2bCustomerLeaseable(((B2BCustomerModel)source).isLeaseSigner());
        	}
        	
        	if (null != ((B2BCustomerModel)source).getDefaultB2BUnit() && null != ((B2BCustomerModel)source).getDefaultB2BUnit().getB2bUnitLevel())
        	{
	        	String userlevel=((B2BCustomerModel) source).getDefaultB2BUnit().getB2bUnitLevel();
	        	target.setB2bUnitLevel(userlevel);
        	}
        	
        	if(GPUserApprovalStatusEnum.PENDING.getCode().equalsIgnoreCase(((B2BCustomerModel) source).getUserApprovalStatus().getCode()))
        	{
        		target.setInReview(true);
        	}
        }
	}

}
