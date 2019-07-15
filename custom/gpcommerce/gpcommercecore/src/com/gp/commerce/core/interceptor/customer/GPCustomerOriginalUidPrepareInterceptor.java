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
package com.gp.commerce.core.interceptor.customer;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;
import com.gp.commerce.core.services.GPUserService;

/**
 * Changes uid of {@link CustomerModel} to lowercase
 */
public class GPCustomerOriginalUidPrepareInterceptor implements PrepareInterceptor
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "gpB2BUnitsService")
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;
	
	@Resource(name = "userService")
	private GPUserService userService;

	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";

	/* (non-Javadoc)
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object, de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) model;

			if (ctx.isNew(customer))
			{
				adjustUid(customer);
			}
			else if (ctx.isModified(model, CustomerModel.ORIGINALUID) || ctx.isModified(model, CustomerModel.UID))
			{
				adjustUid(customer);
			}
		}

		if(model instanceof B2BCustomerModel)
		{

			final B2BCustomerModel b2bcustomer = (B2BCustomerModel) model;
			final B2BUnitModel defaultUnit = b2bcustomer.getDefaultB2BUnit();

			if (ctx.isNew(b2bcustomer) && null != defaultUnit
					&& GpcommerceCoreConstants.B2B_UNIT_L1.equalsIgnoreCase(defaultUnit.getB2bUnitLevel()))
			{
				b2bcustomer.setUserApprovalStatus(GPUserApprovalStatusEnum.APPROVED);
			}


			if (null != defaultUnit && CollectionUtils.isNotEmpty(b2bcustomer.getGroups())
					&& CollectionUtils.isEmpty(defaultUnit.getGroups()) && null != b2bcustomer.getPk())
			{
				final Collection<B2BCustomerModel> administrators = gpB2BUnitsService.getUsersforB2BUnit(
						 b2bcustomer.getDefaultB2BUnit(), B2BConstants.B2BADMINGROUP ,true);

				final B2BCustomerModel b2bCustomerModel = CollectionUtils.isNotEmpty(administrators)
						? administrators.iterator().next()
						: null;
				if (null != b2bCustomerModel && b2bcustomer.getPrimaryAdmin()
						&& !b2bCustomerModel.getUid().equalsIgnoreCase(b2bcustomer.getUid()))
				{
					throw new InterceptorException(b2bCustomerModel.getUid() + " is already a primary admin, kindly remove the " +b2bCustomerModel.getUid() + " as primary admin first, before assigning "+ b2bcustomer.getUid() + " as primary admin");
				}

			}
		}

	}


	/**
	 * @param customer
	 * This method will intercept when we are saving the customer details
	 * and OrginalUid remains as it is , uid is appended with | symbol
	 */
	protected void adjustUid(final CustomerModel customer)
	{
		final String original = customer.getOriginalUid();
		final String uid = customer.getUid();
		final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
		if (StringUtils.isNotEmpty(uid))
		{
			if (!uid.equals(uid.toLowerCase(userService.getCurrentLocale())))
			{
				customer.setUid(uid.toLowerCase(userService.getCurrentLocale()));
				if(!uid.contains(uidDelimiter))
				{
					customer.setOriginalUid(uid);
				}
				else
				{
					customer.setOriginalUid(uid.split("\\|")[0]);
				}
			}
			else if (!uid.equalsIgnoreCase(original))
			{
				if(!uid.contains(uidDelimiter))
				{
					customer.setOriginalUid(uid);
				}
				else
				{
					customer.setOriginalUid(uid.split("\\|")[0]);
				}
			}
		}
		else if (StringUtils.isNotEmpty(original))
		{
			customer.setUid(original.toLowerCase(userService.getCurrentLocale()));
		}
	}
}
