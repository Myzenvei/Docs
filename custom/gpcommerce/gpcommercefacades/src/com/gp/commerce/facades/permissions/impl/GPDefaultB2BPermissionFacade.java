/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.permissions.impl;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.services.permission.impl.GPDefaultB2BPermissionService;
import com.gp.commerce.dto.company.data.B2BPermissionDataList;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;
import com.gp.commerce.facades.permissions.GPB2BPermissionFacade;


/**
 * The Class GPDefaultB2BPermissionFacade.
 */
public class GPDefaultB2BPermissionFacade implements GPB2BPermissionFacade
{

	@Resource
	private GPDefaultB2BPermissionService gpb2bPermissionService;
	private Converter<B2BPermissionModel, B2BPermissionData> b2BPermissionConverter;

	@Resource(name = "b2bCommerceB2BUserGroupService")
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Override
	public B2BPermissionDataList findPermissionsByUnits(final UserB2BUnitWsDTO userB2BUnitList)
	{

		final B2BPermissionDataList datalist = new B2BPermissionDataList();
		final List<B2BPermissionModel> permissions = getGpb2bPermissionService().findPermissionsByUnits(userB2BUnitList);
		final List<B2BPermissionData> permissionDatalist = new ArrayList<>();
		for (final B2BPermissionModel permission : permissions)
		{
			final B2BPermissionData permissionData = new B2BPermissionData();
			permissionDatalist.add(getB2BPermissionConverter().convert(permission, permissionData));
		}

		datalist.setPermissions(permissionDatalist);

		return datalist;

	}

	public Converter<B2BPermissionModel, B2BPermissionData> getB2BPermissionConverter()
	{
		return b2BPermissionConverter;
	}

	public void setB2BPermissionConverter(final Converter<B2BPermissionModel, B2BPermissionData> b2bPermissionConverter)
	{
		b2BPermissionConverter = b2bPermissionConverter;
	}

	public GPDefaultB2BPermissionService getGpb2bPermissionService()
	{
		return gpb2bPermissionService;
	}

	public void setGpb2bPermissionService(final GPDefaultB2BPermissionService gpb2bPermissionService)
	{
		this.gpb2bPermissionService = gpb2bPermissionService;
	}

	@Override
	public void addPermissionsToUser(final String uid, final List<String> codes)
	{
		getGpb2bPermissionService().addPermissionsToUser(uid, codes);
	}

	@Override
	public void addPermissionsToUserGroup(final String uid, final List<String> codes)
	{
		getGpb2bPermissionService().addPermissionsToUserGroup(uid, codes);
	}

}
