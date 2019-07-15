/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerReversePopulator;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.HashSet;
import java.util.Set;

import com.gp.commerce.core.enums.SelectRoleEnum;


/**
 * Reverse Populator to populate values for customer
 */
public class GPB2BCustomerReversePopulator extends B2BCustomerReversePopulator
{

	private static final String B2BADMINGROUP = "b2badmingroup";
	private static final String B2BCUSTOMERGROUP = "b2bcustomergroup";
	private ModelService modelService;
	private UserService userService;
	private BaseSiteService baseSiteService;


	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
	@Override
	public UserService getUserService()
	{
		return userService;
	}

	@Override
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	@Override
	public void populate(final CustomerData source, final B2BCustomerModel target)
	{
		super.populate(source, target);
		target.setCellPhone(source.getCellPhone());
		target.setLeaseSigner(source.isLeaseSigner());
		
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		target.setSite(baseSite);
		final Set<PrincipalGroupModel> groups = new HashSet<>(target.getGroups());
		if (source.getSelectedRole().equalsIgnoreCase(B2BADMINGROUP))
		{
			final UserGroupModel userGroupModel = getUserService().getUserGroupForUID(B2BADMINGROUP);
			final UserGroupModel existingUserGroupModel = getUserService().getUserGroupForUID(B2BCUSTOMERGROUP);
			if (groups.contains(existingUserGroupModel))
			{
				groups.remove(existingUserGroupModel);
			}
			groups.add(userGroupModel);
			target.setGroups(groups);
			target.setSelectedRole(SelectRoleEnum.ADMIN);
		}
		else if (source.getSelectedRole().equalsIgnoreCase(B2BCUSTOMERGROUP))
		{
			final UserGroupModel userGroupModel = getUserService().getUserGroupForUID(B2BCUSTOMERGROUP);
			final UserGroupModel existingUserGroupModel = getUserService().getUserGroupForUID(B2BADMINGROUP);
			if (groups.contains(existingUserGroupModel))
			{
				groups.remove(existingUserGroupModel);
			}
			groups.add(userGroupModel);
			target.setGroups(groups);
			target.setSelectedRole(SelectRoleEnum.BUYER);
			
		}

	}

}

