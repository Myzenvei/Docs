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
package com.gp.commerce.core.company.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;
import java.util.List;


/**
 * A service around {@link B2BUnitModel}.
 *
 * @param <T>
 *           extension of {@link CompanyModel}
 * @param <U>
 *           extension of {@link UserModel}
 * @spring.bean gpb2bUnitService
 */
public interface GPB2BUnitsService<T extends CompanyModel, U extends UserModel> extends B2BUnitService<B2BUnitModel, B2BCustomerModel>
{


	/**
	 * Disable branch. Mark all units in the branch as active = false
	 *
	 * @param unit
	 *           the unit
	 * @param enableUnit
	 *           the enable/disable flag
	 */
	void disableBranch(T unit, Boolean enableUnit);

	/**
	 * Get all the associated Units for the current user
	 *
	 * @param employee the B2bCustomer
	 * @return List<B2BUnitModel>
	 */
	List<B2BUnitModel> getUnitNodes(B2BCustomerModel employee);

	/**
	 * Get Unit for unit identifier
	 *
	 * @param originalUid the uid
	 * @return {@link B2BUnitModel} 
	 */
	B2BUnitModel getUnitForUid(String originalUid);

	/**
	 * Gets all members from the unit who belong to specified User Group for multiple units
	 *
	 * @param units        list of B2b Units
	 * @param userGroupIds list of user groupd ID's
	 * @return Collection<B2BCustomerModel>
	 */
	Collection<B2BCustomerModel> getUsersOfUserGroup(final List<B2BUnitModel> units, final List<String> userGroupIds);

	/**
	 * Check if customer is B2B admin
	 *
	 * @param customerModel the customer
	 * @return <b>true</b> if the customer is B2BAdmin <br>else <b>false</b>
	 */
	boolean isB2BAdmin(B2BCustomerModel customerModel);

	/**
	 * Remove b2b user group from a customer
	 *
	 * @param customerUid  the customer id
	 * @param userGroupUid the user group id
	 */
	void removeB2BUserGroupFromCustomerGroups(String customerUid, String userGroupUid);

	/**
	 * Get all the associated units and child units, if sub child nodes true then return 2nd level child nodes
	 *
	 * @param employee the B2bCustomer
	 * @param isSubChildNodesReq a boolean value to indicate whether the request is for Sub child nodes
	 * @return List<B2BUnitModel>
	 */
	List<B2BUnitModel> getUnitsWithChildNodes(B2BCustomerModel employee, boolean isSubChildNodesReq);


	/**
	 * Get all the members who are having defaultB2BUnit as defaultB2BUnit and are members of the group with groupId b2badmingroup
	 * 
	 * @param defaultB2BUnit the {@link B2BUnitModel}
	 * @param b2badmingroup  the admingroup
	 * @param isPrimaryAdmin a boolean value to indicate whether the user is a primary Admin
	 * @return list of {@link B2BCustomerModel}s
	 */
	Collection<B2BCustomerModel> getUsersforB2BUnit(B2BUnitModel defaultB2BUnit, String b2badmingroup ,Boolean isPrimaryAdmin);

	/**
	 * Update usergroup for list of users based on unit level
	 *
	 * @param b2bUnitLevel
	 * 			the b2bunit level (L1/L2)
	 * @param users
	 * 			the list of users models
	 */
	void updateUserGroupByUnitLevel(final String b2bUnitLevel,final List<UserModel> users);

	/**
	 * Add Unit to user
	 *
	 * @param group  the group used
	 * @param member the principal
	 */
	void addUnitToUser(final T group, final PrincipalModel member);

	/**
	 * Add User to unit
	 *
	 * @param member the principal model
	 * @param group  the principal group model
	 */
	void addUserToUnit(PrincipalModel member, PrincipalGroupModel group);

	/**
	 * Update or create Unit for the current user
	 * 
	 * @param originalUid the original id
	 * @param unit        the {@link B2BUnitData}
	 * @return B2BUnitModel
	 */
	B2BUnitModel updateOrCreateBusinessUnit(final String originalUid, final B2BUnitData unit);
	
	/**
	 * Check if the logged-in user is a buyer
	 * 
	 * @param customerModel the logged-in user
	 * @return <b>true</b> if the logged-in user is a BUYER<br> else <b>false</b>
	 */
	boolean isBuyer(final B2BCustomerModel customerModel);
}
