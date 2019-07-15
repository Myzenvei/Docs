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
package com.gp.commerce.core.company.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * A data access to {@link B2BUnitModel}
 *
 *
 * @spring.bean gpb2bUnitDao
 */
public interface GPB2BUnitDao extends GenericDao<B2BUnitModel>
{

	/**
	 * Returns all member of the B2BUnit who are also member of the group with the userGroupId passed in
	 *
	 * @param unit
	 *           - The B2BUnitModel whose member we are going to select
	 * @param userGroupId
	 *           - The uid of the UserGroup
	 * @return List<B2BCustomerModel> B2BCustomers in B2BUnit who are members of the group with uid userGroupId
	 */
	List<B2BCustomerModel> findB2BUnitMembersByGroup(final List<B2BUnitModel> units, final List<String> userGroupId);


	/**
	 * Return all the users who has defaultB2BUnits as units and also are members of the group with the userGroupId and has B2BUnits and isPrimaryAdmin is true
	 * @param units
	 *          - The defaultB2BUnit whole member we are going to select
	 * @param userGroupIds
	 *          - The uid of the UserGroup
	 * @return  List<B2BCustomerModel> B2BCustomers in B2BUnit who are members of the group with uid userGroupId
	 */
	List<B2BCustomerModel> getUsersforB2BUnit(B2BUnitModel units, String userGroupIds , Boolean isPrimaryAdmin);

}
