/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.permission;

import de.hybris.platform.b2b.model.B2BPermissionModel;

import java.util.List;

import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


/**
 * The Interface GPB2BPermissionService.
 */
public interface GPB2BPermissionService
{
	/**
	 * Gets the b2b permission for the unit provided.
	 * @param userB2BUnitList
	 * 			the permission unit
	 * @return the permission
	 */
	List<B2BPermissionModel> findPermissionsByUnits(UserB2BUnitWsDTO userB2BUnitList);

	/**
	 * Gets the b2b permission for the code provided.
	 * @param codes
	 * 			the permission code
	 * @return the permission
	 */
	List<B2BPermissionModel> findPermissionsByCodes(List<String> codes);
	
	/**
	 * Adds permissions to user
	 * @param uid
	 * 			the uid
	 * @param codes
	 * 			the codes
	 */
	public void addPermissionsToUser(final String uid, final List<String> codes);
	
	/**
	 * Adds permissions to user group
	 * @param uid
	 * 			the uid
	 * @param codes
	 * 			the codes
	 */
	public void addPermissionsToUserGroup(final String uid, final List<String> codes);
}
