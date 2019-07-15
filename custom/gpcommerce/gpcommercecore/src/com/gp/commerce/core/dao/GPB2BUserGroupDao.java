/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import de.hybris.platform.b2b.model.B2BUserGroupModel;

import java.util.List;

import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


public interface GPB2BUserGroupDao
{

	List<B2BUserGroupModel> findUserGroupsByUnits(UserB2BUnitWsDTO userB2BUnitList);

	List<B2BUserGroupModel> findB2BUserGroupsByCodes(List<String> codes);

}
