/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.permission.impl;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.dao.impl.DefaultGPB2BPermissionDao;
import com.gp.commerce.core.services.permission.GPB2BPermissionService;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;

/**
 * This class is used for processing GP B2B permission services
 */
public class GPDefaultB2BPermissionService implements GPB2BPermissionService
{
	@Resource
	private DefaultGPB2BPermissionDao defaultGPB2BPermissionDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "b2bCommerceB2BUserGroupService")
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;
	
	@Override
	public List<B2BPermissionModel> findPermissionsByUnits(final UserB2BUnitWsDTO userB2BUnitList)
	{
		return getDefaultGPB2BPermissionDao().findPermissionsByUnits(userB2BUnitList);
	}

	public DefaultGPB2BPermissionDao getDefaultGPB2BPermissionDao()
	{
		return defaultGPB2BPermissionDao;
	}

	public void setDefaultGPB2BPermissionDao(final DefaultGPB2BPermissionDao defaultGPB2BPermissionDao)
	{
		this.defaultGPB2BPermissionDao = defaultGPB2BPermissionDao;
	}


	@Override
	public List<B2BPermissionModel> findPermissionsByCodes(final List<String> codes)
	{
		return CollectionUtils.isNotEmpty(codes)? getDefaultGPB2BPermissionDao().findPermissionsByCodes(codes):Collections.emptyList();
	}
	
	public void addPermissionsToUser(final String uid, final List<String> codes) {
		final List<B2BPermissionModel> permissions = findPermissionsByCodes(codes);
		searchRestrictionService.disableSearchRestrictions();
		final B2BCustomerModel b2bCustomer = userService.getUserForUID(uid, B2BCustomerModel.class);
		b2bCustomer.setPermissions(new HashSet<B2BPermissionModel>(permissions));
		modelService.save(b2bCustomer);
		searchRestrictionService.enableSearchRestrictions();
	}
	
	public void addPermissionsToUserGroup(final String uid, final List<String> codes){
		final List<B2BPermissionModel> permissions = findPermissionsByCodes(codes);
		searchRestrictionService.disableSearchRestrictions();
		final B2BUserGroupModel userGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(uid, B2BUserGroupModel.class);
		userGroupModel.setPermissions(permissions);
		modelService.save(userGroupModel);
		searchRestrictionService.enableSearchRestrictions();
	}

}
