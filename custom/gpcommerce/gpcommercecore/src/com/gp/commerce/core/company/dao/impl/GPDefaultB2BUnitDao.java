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
package com.gp.commerce.core.company.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.commerce.core.company.dao.GPB2BUnitDao;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPUserApprovalStatusEnum;



/**
 * Default implementation of the {@link GPB2BUnitDao}
 *
 * @spring.bean gpb2bUnitDao
 */
public class GPDefaultB2BUnitDao extends DefaultGenericDao<B2BUnitModel> implements GPB2BUnitDao
{

	private static final String IS_PRIMARY_ADMIN = "isPrimaryAdmin";
	private static final String PENDING_STATUS = "pendingStatus";
	private static final String APPROVAL_STATUS = "approvalStatus";

	/**
	 * GPDefaultB2BUnitDao Constructor
	 *
	 */
	public GPDefaultB2BUnitDao()
	{
		super(B2BUnitModel._TYPECODE);
	}

	/**
	 * Finds member of b2bunit who are also in the group specified by userGroupId
	 */
	@Override
	public List<B2BCustomerModel> findB2BUnitMembersByGroup(final List<B2BUnitModel> units, final List<String> userGroupIds)
	{
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT {c:pk}	");
		sql.append("FROM	");
		sql.append("{	");
		sql.append(B2BUnitModel._TYPECODE).append(" as unit	");
		sql.append("	JOIN PrincipalGroupRelation as unit_rel	");
		sql.append("	ON   {unit_rel:target} = {unit:pk} 	");
		sql.append("	JOIN B2BCustomer as c	");
		sql.append("	ON   {c:pk} = {unit_rel:source}	");
		sql.append("	JOIN PrincipalGroupRelation as group_rel	");
		sql.append("	ON   {group_rel:source} = {c:pk} 	");
		sql.append("	JOIN UserGroup as user_group	");
		sql.append("	ON   {user_group:pk} = {group_rel:target}	");
		sql.append("}	");
		sql.append("WHERE {unit:pk} in (?units)	");
		sql.append("AND   {user_group:uid} in (?uids)	");
		sql.append("AND   ({c:userApprovalStatus} = ?approvalStatus  ");
		sql.append("OR   ({c:userApprovalStatus} = ?pendingStatus  AND  {c:active} = 1)) ");

		final Map<String, Object> attr = new HashMap<String, Object>(4);
		attr.put(GpcommerceCoreConstants.UNITS, units);
		attr.put(GpcommerceCoreConstants.UIDS, userGroupIds);
		attr.put(APPROVAL_STATUS, GPUserApprovalStatusEnum.APPROVED);
		attr.put(PENDING_STATUS, GPUserApprovalStatusEnum.PENDING);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
		query.getQueryParameters().putAll(attr);

		final SearchResult<B2BCustomerModel> result = this.getFlexibleSearchService().search(query);

		return result.getResult();

	}




	@Override
	public List<B2BCustomerModel> getUsersforB2BUnit(final B2BUnitModel units, final String userGroupIds, final Boolean isPrimaryAdmin) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT {c:pk}	");
		sql.append("FROM	");
		sql.append("{	");
		sql.append(B2BUnitModel._TYPECODE).append(" as unit	");
		sql.append("	JOIN PrincipalGroupRelation as unit_rel	");
		sql.append("	ON   {unit_rel:target} = {unit:pk} 	");
		sql.append("	JOIN B2BCustomer as c	");
		sql.append("	ON   {c:pk} = {unit_rel:source}	");
		sql.append("	JOIN PrincipalGroupRelation as group_rel	");
		sql.append("	ON   {group_rel:source} = {c:pk} 	");
		sql.append("	JOIN UserGroup as user_group	");
		sql.append("	ON   {user_group:pk} = {group_rel:target}	");
		sql.append("}	");
		sql.append("WHERE {unit:pk} in (?units)	");
		sql.append("AND   {user_group:uid} in (?uids)	");

		if(isPrimaryAdmin){
			sql.append("AND   {c.primaryAdmin}  = ?isPrimaryAdmin	");
		}

		final Map<String, Object> attr = new HashMap<String, Object>(3);
		attr.put(GpcommerceCoreConstants.UNITS, units);
		attr.put(GpcommerceCoreConstants.UIDS, userGroupIds);
		if(isPrimaryAdmin){
			attr.put(IS_PRIMARY_ADMIN, Boolean.TRUE);
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
		query.getQueryParameters().putAll(attr);

		final SearchResult<B2BCustomerModel> result = this.getFlexibleSearchService().search(query);

		return result.getResult();

	}
}
