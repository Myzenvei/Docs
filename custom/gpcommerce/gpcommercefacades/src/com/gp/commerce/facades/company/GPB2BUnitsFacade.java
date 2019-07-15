/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.company;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.List;


/**
 * Interface for GP Unit Facade.
 */
public interface GPB2BUnitsFacade
{
	
	/**
	 * Disabling the addresses for the Unit - Inactive.
	 *
	 * @param unitUid           the unit uid
	 * @param enableUnit           the enable/disable flag
	 */
	void disableUnit(final String unitUid, final Boolean enableUnit);

	/**
	 * Get all the associated Units for the current user.
	 *
	 * @param userId the user id
	 * @param isSubChildNodesNotReq           - whether to get sub child units
	 * @return List<B2BUnitNodeData>
	 */
	List<B2BUnitNodeData> getUnitNodes(final String userId, final boolean isSubChildNodesNotReq);

	/**
	 * Add unit to the current user.
	 *
	 * @param unitUid the unit uid
	 * @param userUid the user uid
	 * @return B2BUnitNodeData
	 */
	B2BUnitNodeData addUnitToUser(final String unitUid, final String userUid);

	/**
	 * Remove unit from User.
	 *
	 * @param unitUid the unit uid
	 * @param userUid the user uid
	 */
	void removeUnitFromUser(final String unitUid, final String userUid);

	/**
	 * Add user to unit.
	 *
	 * @param unitUid the unit uid
	 * @param customer the customer
	 * @return CustomerData
	 */
	CustomerData addUserToUnit(String unitUid, CustomerData customer);

	/**
	 * Update or Unit for the current user.
	 *
	 * @param originalUid the original uid
	 * @param unit the unit
	 * @return B2BUnitNodeData
	 */
	B2BUnitNodeData updateOrCreateBusinessUnit(String originalUid, B2BUnitData unit);

	/**
	 * Get all the associated users for the current user.
	 *
	 * @return CustomerData
	 */
	List<CustomerData> getUsersOfUserGroup();

	/**
	 * Remove b2b user group from a customer.
	 *
	 * @param user the user
	 * @param usergroup the usergroup
	 */
	void removeB2BUserGroupFromCustomerGroups(final String user, final String usergroup);

	/**
	 * Get Unit Details with units for admins and customers.
	 *
	 * @param unitUid the unit uid
	 * @return B2BUnitData
	 */
	B2BUnitData getUnitForUid(final String unitUid);

	/**
	 * Get all admins and customers for a Unit.
	 *
	 * @param unitUid the unit uid
	 * @param role the role
	 * @return List<CustomerData>
	 */
	List<CustomerData> getUsersOfUserGroupForUnit(String unitUid, String role);

	/**
	 * Get associated units for user.
	 *
	 * @param userId the user id
	 * @return List<B2BUnitNodeData>
	 */
	List<B2BUnitNodeData> getUnitsForUser(final String userId);
}
