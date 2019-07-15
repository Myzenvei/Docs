/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.usergroups;

import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;

import java.util.List;

import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


/**
 * This interface is used for processing GP Usergroup services
 */
public interface GPB2BUserGroupService
{

	/**
	 * This method finds user groups by unit
	 * @param userB2BUnitList
	 * 			the user b2b unit list
	 * @return list of user groups by unit
	 */
	List<B2BUserGroupModel> findUserGroupsByUnits(UserB2BUnitWsDTO userB2BUnitList);

	/**
	 * This method finds user groups by code
	 * @param codes
	 * 			the code
	 * @returnlist of user groups by code
	 */
	List<B2BUserGroupModel> findB2BUserGroupsByCodes(List<String> codes);

	/**
	 * Adds User groups to user
	 * 
	 * @param uid   the uid
	 * @param codes list of codes
	 */
	void addUserGroupsToUser(final String uid, final List<String> codes);

	/**
	 * Adds users to usergroup
	 * 
	 * @param uid   the uid
	 * @param codes list of codes
	 */
	void addUsersToUserGroup(final String uid, final List<String> codes);

	/**
	 * Updates User group based on usergroup data
	 * 
	 * @param userGroupUid  the usergroup ID
	 * @param userGroupData the B2b user group data
	 * @param isCreate      boolean value to indicate whether user group is created
	 *                      or not
	 * @throws GPCommerceBusinessException on error
	 */
	void updateUserGroup(final String userGroupUid, final B2BUserGroupData userGroupData, final Boolean isCreate)
			throws GPCommerceBusinessException;

}
