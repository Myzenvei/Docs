/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import de.hybris.platform.b2b.dao.impl.DefaultB2BPermissionDao;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPB2BPermissionDao;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


public class DefaultGPB2BPermissionDao extends DefaultB2BPermissionDao implements GPB2BPermissionDao
{

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	private static final String FIND_PERMISSION_BY_UNIT = "SELECT {B2BPermission:pk} FROM { B2BPermission as B2BPermission"
			+ " JOIN B2BUnit as B2BUnit ON  {B2BPermission:unit} = {B2BUnit:pk}} where {B2BUnit:uid} in (?uids)";
	private static final String FIND_PERMISSIONS_BY_CODES = "SELECT DISTINCT {B2BPermission:pk} FROM {B2BPermission as B2BPermission} "
			+ "where {B2BPermission:code} in (?codes)";


	@Override
	public List<B2BPermissionModel> findPermissionsByUnits(final UserB2BUnitWsDTO userB2BUnitList)
	{
		final Map<String, Object> attr = new HashMap<>(1);
		final List<String> uids = userB2BUnitList.getB2BUnitList();

		attr.put(GpcommerceCoreConstants.UIDS, uids);


		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PERMISSION_BY_UNIT);
		query.getQueryParameters().putAll(attr);

		searchRestrictionService.disableSearchRestrictions();

		final List<B2BPermissionModel> permissions = getFlexibleSearchService().<B2BPermissionModel> search(query).getResult();
		searchRestrictionService.enableSearchRestrictions();

		return CollectionUtils.isEmpty(permissions) ? Collections.emptyList() : permissions;
	}

	@Override

	public List<B2BPermissionModel> findPermissionsByCodes(final List<String> codes)

	{

		final Map<String, Object> attr = new HashMap<>(1);
		attr.put(GpcommerceCoreConstants.CODES, codes);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PERMISSIONS_BY_CODES);
		query.getQueryParameters().putAll(attr);
		searchRestrictionService.disableSearchRestrictions();
		final List<B2BPermissionModel> permissions = getFlexibleSearchService().<B2BPermissionModel> search(query).getResult();
		searchRestrictionService.enableSearchRestrictions();
		return CollectionUtils.isEmpty(permissions) ? Collections.emptyList() : permissions;
	}

}
