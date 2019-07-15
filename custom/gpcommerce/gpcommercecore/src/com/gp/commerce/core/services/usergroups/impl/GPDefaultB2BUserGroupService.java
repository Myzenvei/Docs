/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.usergroups.impl;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;

import com.gp.commerce.core.constants.GPB2BExceptionEnum;
import com.gp.commerce.core.dao.impl.DefaultGPB2BUserGroupDao;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.services.usergroups.GPB2BUserGroupService;
import com.gp.commerce.core.user.dao.GPUserDao;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;


/**
 * This class is used for processing GP B2b user group services
 */
public class GPDefaultB2BUserGroupService implements GPB2BUserGroupService
{
	@Resource
	private DefaultGPB2BUserGroupDao defaultGPPagedB2BUserGroupDao;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "gpUserDao")
	private GPUserDao gpUserDao;

	@Resource(name = "b2bCommerceB2BUserGroupService")
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Resource(name = "b2bCommerceUnitService")
	private B2BCommerceUnitService b2BCommerceUnitService;

	@Override
	public List<B2BUserGroupModel> findUserGroupsByUnits(final UserB2BUnitWsDTO userB2BUnitList)
	{
		return getDefaultGPPagedB2BUserGroupDao().findUserGroupsByUnits(userB2BUnitList);
	}

	public DefaultGPB2BUserGroupDao getDefaultGPPagedB2BUserGroupDao()
	{
		return defaultGPPagedB2BUserGroupDao;
	}

	public void setDefaultGPPagedB2BUserGroupDao(final DefaultGPB2BUserGroupDao defaultGPPagedB2BUserGroupDao)
	{
		this.defaultGPPagedB2BUserGroupDao = defaultGPPagedB2BUserGroupDao;
	}

	@Override
	public List<B2BUserGroupModel> findB2BUserGroupsByCodes(final List<String> codes)
	{
		return getDefaultGPPagedB2BUserGroupDao().findB2BUserGroupsByCodes(codes);
	}

	@Override
	public void addUserGroupsToUser(final String uid, final List<String> codes)
	{
		final List<B2BUserGroupModel> userGroupModels = CollectionUtils.isNotEmpty(codes)
				? findB2BUserGroupsByCodes(codes)
				: Collections.emptyList();

		searchRestrictionService.disableSearchRestrictions();
		final B2BCustomerModel customer = userService.getUserForUID(uid, B2BCustomerModel.class);
		final List<PrincipalGroupModel> b2bUgList = new ArrayList<>(customer.getGroups());
		CollectionUtils.filter(b2bUgList, PredicateUtils.instanceofPredicate(B2BUserGroupModel.class));

		final List<PrincipalGroupModel> membersList = new ArrayList<>(customer.getGroups());
		membersList.removeAll(b2bUgList);
		membersList.addAll(userGroupModels);
		customer.setGroups(new HashSet<>(membersList));
		modelService.saveAll(customer);
		searchRestrictionService.enableSearchRestrictions();

	}

	@Override
	public void addUsersToUserGroup(final String uid, final List<String> codes)
	{
		final List<B2BCustomerModel> users = CollectionUtils.isNotEmpty(codes) ? gpUserDao.findUsersByCodes(codes)
				: Collections.emptyList();
		searchRestrictionService.disableSearchRestrictions();
		final B2BUserGroupModel userGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(uid, B2BUserGroupModel.class);
		userGroupModel.setMembers(new HashSet<PrincipalModel>(users));
		modelService.saveAll(userGroupModel);
		searchRestrictionService.enableSearchRestrictions();

	}

	@Override
	public void updateUserGroup(final String userGroupUid, final B2BUserGroupData userGroupData, final Boolean isCreate)
			throws GPCommerceBusinessException
	{

		searchRestrictionService.disableSearchRestrictions();
		B2BUserGroupModel userGroupModel = b2BCommerceB2BUserGroupService.getUserGroupForUID(userGroupUid, B2BUserGroupModel.class);

		if (isCreate)
		{
			if (userGroupModel == null)
			{
				userGroupModel = modelService.create(B2BUserGroupModel.class);
			}
			else
			{
				throw new GPCommerceBusinessException(GPB2BExceptionEnum.USERGROUP.getCode(),
						GPB2BExceptionEnum.USERGROUP.getErrMsg());
			}
		}
		userGroupModel.setName(userGroupData.getName());
		userGroupModel.setLocName(userGroupData.getName());
		userGroupModel.setUid(userGroupData.getUid());
		if (userGroupData.getUnit() != null)
		{
			final B2BUnitModel unitModel = b2BCommerceUnitService.getUnitForUid(userGroupData.getUnit().getUid());
			userGroupModel.setUnit(unitModel);
		}
		modelService.save(userGroupModel);
		searchRestrictionService.enableSearchRestrictions();
	}
}
