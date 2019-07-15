/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.validator;

import javax.annotation.Resource;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.gp.commerce.exceptions.InvalidUnitLevelException;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;


/**
 * @author snnagarajan
 *
 */
public class SiteValidator implements Validator
{

	@Resource(name="baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name="userService")
	private UserService userService;
	private static final String B2BUNITL1="L1";
	private static final String PENDINGBYGP="PENDINGBYGP";
	private static final String PENDINGBYADMIN="PENDINGBYADMIN";
	
	@Override
	public void validate(final Object target, final Errors errors)
	{			
		if(baseSiteService.getCurrentBaseSite()!=null && baseSiteService.getCurrentBaseSite().getChannel()!=null
				&& SiteChannel.B2B.equals(baseSiteService.getCurrentBaseSite().getChannel()))	
		{
			checkIfL1User(target);
		}

	}

	private void checkIfL1User(final Object target)
	{
		UserModel currentUser=userService.getCurrentUser();
		AddressWsDTO address=(AddressWsDTO)target;
		Boolean isL2L3=(PENDINGBYADMIN.equalsIgnoreCase(address.getApprovalStatus()) || PENDINGBYGP.equalsIgnoreCase(address.getApprovalStatus())) ? true : false;
		
		if(currentUser instanceof B2BCustomerModel && ((B2BCustomerModel) currentUser).getDefaultB2BUnit()!=null)
		{
			B2BUnitModel unitModel = ((B2BCustomerModel) currentUser).getDefaultB2BUnit();
			if ((!B2BUNITL1.equals(unitModel.getB2bUnitLevel())) && !(isL2L3))
			{
				throw new InvalidUnitLevelException("User is not allowed to add new address,as B2B Unit Level associated is not L1");
			}

		}
		
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return AddressData.class.isAssignableFrom(clazz);
	}
}
