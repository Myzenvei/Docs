/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.permissions;

import java.util.List;

import com.gp.commerce.dto.company.data.B2BPermissionDataList;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;

/**
 * interface for b2b permission facade.
 */
public interface GPB2BPermissionFacade
{
	
	/**
	 * This method finds permissions by units.
	 *
	 * @param userB2BUnitList the user B 2 B unit list
	 * @return permision data list
	 */
	B2BPermissionDataList findPermissionsByUnits(UserB2BUnitWsDTO userB2BUnitList);

	/**
	 * This method adds permissions to user.
	 *
	 * @param uid the uid
	 * @param codes the codes
	 */
	void addPermissionsToUser(String uid, List<String> codes);

	/**
	 * This method adds permissions to user groups.
	 *
	 * @param uid the uid
	 * @param codes the codes
	 */
	void addPermissionsToUserGroup(String uid, List<String> codes);

}
