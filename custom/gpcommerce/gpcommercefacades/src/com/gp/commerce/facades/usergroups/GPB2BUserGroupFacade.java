/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.usergroups;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.List;

import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.dto.company.data.B2BUserGroupDataList;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


/**
 * The Interface GPB2BUserGroupFacade.
 */
public interface GPB2BUserGroupFacade
{

	/**
	 * Finds user groups by units
	 * @param userB2BUnitList
	 * 			the user b2b unit list
	 * @return the user groups by units
	 */
	B2BUserGroupDataList findUserGroupsByUnits(UserB2BUnitWsDTO userB2BUnitList);

	/**
	 * Adds user groups to user
	 * @param uid
	 * 			the id of user
	 * @param codes
	 * 			the code list
	 */
	void addUserGroupsToUser(String uid, List<String> codes);

	/**
	 * Adds users to user groups by their uids
	 * @param uid
	 * 			the id of user
	 * @param codes
	 * 			the code list
	 */
	void addUsersToUserGroup(String uid, List<String> codes);

	/**
	 * Invites user to user group by a current user
	 * @param invitedUser
	 * 			the invited user
	 * @param currentUser
	 * 			the current user
	 */
	void inviteUser(final String invitedUser, final CustomerData currentUser);

	/**
	 * Updates User groups based on user group data.
	 *
	 * @param userGroupUid  user group id
	 * @param userGroupData user group data
	 * @param isCreate      boolean status to create
	 * @throws GPCommerceBusinessException the GP commerce business exception
	 */
	void updateUserGroup(String userGroupUid, B2BUserGroupData userGroupData, Boolean isCreate) throws GPCommerceBusinessException;

	/**
	 * Gets the b2B customer for uid.
	 *
	 * @param uid the uid
	 * @return the b2B customer for uid
	 */
	CustomerData getB2BCustomerForUid(String uid);
}
