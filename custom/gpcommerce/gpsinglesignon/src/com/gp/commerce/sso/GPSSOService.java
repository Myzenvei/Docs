/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.sso;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;


/**
 * SSO service interface for getting/creating user
 */
public interface GPSSOService
{
	
	/**
	 * Return UserModel for existing user or for the newly created.
	 *
	 * @param id                   the user id
	 * @param name                 the user name
	 * @param roles                user roles
	 * @param soldTo               the sold to
	 * @param email                the email
	 * @param approvalSampleStatus the approval sample status
	 * @return existing or newly created user model
	 * @throws IllegalArgumentException in case the user cannot be mapped due to
	 *                                  roles being unknown or disallowed
	 */
	UserModel getOrCreateSSOUser(String id, String name, Collection<String> roles, String soldTo, String email, Boolean approvalSampleStatus);
}
