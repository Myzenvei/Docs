/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.b2bunit.interceptor;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;


/**
 * This interceptor validates that only a member of groups b2badmingroup can create a new root.
 */
public class GPB2BUnitModelValidateInterceptor implements ValidateInterceptor
{

	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private UserService userService;
	private ModelService modelService;
	private L10NService l10NService;
	
	@Resource(name = "gpB2BUnitsService")
	private GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	private static final Logger LOG = Logger.getLogger(GPB2BUnitModelValidateInterceptor.class);

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof B2BUnitModel)
		{
			final B2BUnitModel unit = (B2BUnitModel) model;


			// ensure all approvers of the unit are members of the b2bapprovergroup
			if (unit.getApprovers() != null)
			{
				final HashSet<B2BCustomerModel> unitApprovers = new HashSet<B2BCustomerModel>(unit.getApprovers());
				if (CollectionUtils.isNotEmpty(unitApprovers))
				{
					final UserGroupModel b2bApproverGroup = getUserService().getUserGroupForUID(B2BConstants.B2BAPPROVERGROUP);

					final Iterator<B2BCustomerModel> iterator = unitApprovers.iterator();
					while (iterator.hasNext())
					{
						final B2BCustomerModel approver = iterator.next();
						if (!getUserService().isMemberOfGroup(approver, b2bApproverGroup))
						{
							// remove approvers who are not in the b2bapprovergroup.
							iterator.remove();
							LOG.warn(String.format("Removed approver %s from unit %s due to lack of membership of group %s",
									approver.getUid(), unit.getUid(), B2BConstants.B2BAPPROVERGROUP));

						}
					}
					unit.setApprovers(unitApprovers);
				}
			}

			//ensures that all of a deactivated unit's subunit's are also deactivated (except in case of new unit).
			if (!unit.getActive().booleanValue() && !ctx.getModelService().isNew(model))
			{
				final Set<B2BUnitModel> childUnits = getB2bUnitService().getB2BUnits(unit);

				for (final B2BUnitModel child : childUnits)
				{
					if (child.getActive().booleanValue())
					{
						child.setActive(Boolean.FALSE);
						getModelService().save(child);
					}

				}
			}

			List<B2BUnitModel> units = new ArrayList<>();
			//updating the Unit level for child (except in case of new unit).
			if (!ctx.getModelService().isNew(model) && ctx.isModified(model, B2BUnitModel.B2BUNITLEVEL) && CollectionUtils.isEmpty(unit.getGroups()) && StringUtils.isNotEmpty(unit.getB2bUnitLevel()))
			{
				//Adding parent unit to list
				units.add(unit);
				final Set<B2BUnitModel> childUnits = getB2bUnitService().getB2BUnits(unit);

				for (final B2BUnitModel child : childUnits)
				{
					child.setB2bUnitLevel(unit.getB2bUnitLevel());
					//Adding child units to list
					units.add(child);
					getModelService().save(child);
				}
				
				//update user group for all customers of units list only for L1/L2
				List<String> b2bUnitLevels = Arrays.asList(Config.getParameter(GpcommerceCoreConstants.B2B_UNITLEVEL_CODES).split(","));
				
				if(b2bUnitLevels.contains(unit.getB2bUnitLevel()) && CollectionUtils.isNotEmpty(units)){
					final List<String> groups = new ArrayList<>();
					groups.add(B2BConstants.B2BCUSTOMERGROUP);
					groups.add(B2BConstants.B2BADMINGROUP);
					
					Collection<B2BCustomerModel> users = gpB2BUnitsService.getUsersOfUserGroup(units, groups);
					List<UserModel> usersList = new ArrayList<>(users);
					gpB2BUnitsService.updateUserGroupByUnitLevel(unit.getB2bUnitLevel(), usersList);
				}
			}

		}
	}

	@Required
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

	protected L10NService getL10NService()
	{
		return l10NService;
	}
}

