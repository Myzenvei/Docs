/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.email.actions;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorservices.process.email.actions.GenerateEmailAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;


/**
 * A process action to generate email.
 */
public class GPGenerateEmailAction extends GenerateEmailAction
{
	private UserService userService;
	
	@Override
	public Transition executeAction(final BusinessProcessModel businessProcessModel) throws RetryLaterException
	{
		return super.executeAction(businessProcessModel);
		
	}

	protected UserService getUserService() {
		return userService;
	}
	
	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}