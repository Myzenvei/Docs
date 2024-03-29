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
package com.gp.commerce.core.strategies.impl;

import java.util.Optional;

import de.hybris.platform.acceleratorservices.process.strategies.impl.AbstractProcessContextStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import com.gp.commerce.core.model.CartProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.servicelayer.user.UserService; 


/**
 * Strategy to impersonate site and initialize session context from an instance of CartProcessModel.
 */
public class ShareCartProcessContextStrategy extends AbstractProcessContextStrategy
{
	private UserService userService; 
	@Override
	public BaseSiteModel getCmsSite(final BusinessProcessModel businessProcessModel)
	{
		ServicesUtil.validateParameterNotNull(businessProcessModel, BUSINESS_PROCESS_MUST_NOT_BE_NULL_MSG);

		return Optional.of(businessProcessModel)
				.filter(businessProcess -> businessProcess instanceof CartProcessModel)
				.map(businessProcess -> ((CartProcessModel) businessProcess).getSite())
				.orElse(null);
	}

	@Override
	protected CustomerModel getCustomer(final BusinessProcessModel businessProcess)
	{
		CustomerModel customer = Optional.of(businessProcess)
				.filter(bp -> bp instanceof CartProcessModel)
				.map(bp -> ((CustomerModel) ((CartProcessModel) businessProcess).getCart().getUser()))
				.orElse(null);

		userService.setCurrentUser(userService.getAdminUser());

		return customer; 
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
