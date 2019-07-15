/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import de.hybris.platform.b2b.dao.impl.DefaultPagedB2BUserGroupDao;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPB2BUserGroupDao;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


public class DefaultGPB2BUserGroupDao extends DefaultPagedB2BUserGroupDao implements GPB2BUserGroupDao
{

	private static final String FIND_USERGROUP_BY_UNIT = "SELECT {ug:pk} FROM { B2BUserGroup as ug JOIN B2BUnit as u"
			+ " ON {ug:unit} = {u:pk} } where {u:uid} in (?uids)";

	private static final String FIND_USERGROUPS_BY_CODES = "SELECT {ug:pk} FROM { B2BUserGroup as ug } where {ug:uid} in (?codes)";

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	public DefaultGPB2BUserGroupDao(final String typeCode)
	{
		super(typeCode);
	}

	@Override
	public List<B2BUserGroupModel> findUserGroupsByUnits(final UserB2BUnitWsDTO userB2BUnitList)
	{

		final Map<String, Object> attr = new HashMap<>(1);
		final List<String> uids = userB2BUnitList.getB2BUnitList();
		attr.put(GpcommerceCoreConstants.UIDS, uids);


		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_USERGROUP_BY_UNIT);
		query.getQueryParameters().putAll(attr);

		searchRestrictionService.disableSearchRestrictions();

		final List<B2BUserGroupModel> userGroups = flexibleSearchService.<B2BUserGroupModel> search(query).getResult();
		searchRestrictionService.enableSearchRestrictions();

		return CollectionUtils.isEmpty(userGroups) ? Collections.emptyList() : userGroups;

	}

	@Override

	public List<B2BUserGroupModel> findB2BUserGroupsByCodes(final List<String> codes)

	{
		final Map<String, Object> attr = new HashMap<>(1);
		attr.put(GpcommerceCoreConstants.CODES, codes);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_USERGROUPS_BY_CODES);
		query.getQueryParameters().putAll(attr);
		searchRestrictionService.disableSearchRestrictions();
		final List<B2BUserGroupModel> usergroups = flexibleSearchService.<B2BUserGroupModel> search(query).getResult();
		searchRestrictionService.enableSearchRestrictions();
		return CollectionUtils.isEmpty(usergroups) ? Collections.emptyList() : usergroups;
	}

}
