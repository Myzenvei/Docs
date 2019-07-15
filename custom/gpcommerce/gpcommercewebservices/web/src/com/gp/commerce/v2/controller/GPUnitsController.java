/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.controller;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUserFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUserGroupFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.util.Config;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.constants.GPB2BExceptionEnum;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.dto.company.data.B2BPermissionDataList;
import com.gp.commerce.dto.company.data.B2BPermissionListWsDTO;
import com.gp.commerce.dto.company.data.B2BPermissionRequestDTO;
import com.gp.commerce.dto.company.data.B2BPermissionWsDTO;
import com.gp.commerce.dto.company.data.B2BUserGroupDataList;
import com.gp.commerce.dto.company.data.B2BUserGroupListWsDTO;
import com.gp.commerce.dto.company.data.GPRequestWsDTO;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;
import com.gp.commerce.dto.company.data.UserDataList;
import com.gp.commerce.dto.company.data.UserListWsDTO;
import com.gp.commerce.facades.company.GPB2BUnitsFacade;
import com.gp.commerce.facades.customer.GpCustomerFacade;
import com.gp.commerce.facades.permissions.impl.GPDefaultB2BPermissionFacade;
import com.gp.commerce.facades.usergroups.impl.GPDefaultB2BUserGroupFacade;
import com.gp.commerce.swagger.ApiBaseSiteIdAndUserIdParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Class for managing B2B Permissions, Users and User Groups
 */
@Secured(
{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_ASAGENTSALESMANAGERGROUP" })
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/units")
@Api(tags = "GP B2B Org")
public class GPUnitsController extends BaseController
{

	private static final Logger LOG = Logger.getLogger(GPUnitsController.class);

	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;

	@Resource(name = "b2bPermissionFacade")
	protected B2BPermissionFacade b2bPermissionFacade;

	@Resource(name = "b2bUserGroupFacade")
	protected B2BUserGroupFacade b2bUserGroupFacade;

	@Resource(name = "b2bUserFacade")
	protected B2BUserFacade b2bUserFacade;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "gpB2BPermissionFacade")
	private GPDefaultB2BPermissionFacade gpB2BPermissionFacade;

	@Resource(name = "gpB2BUserGroupFacade")
	private GPDefaultB2BUserGroupFacade gpB2BUserGroupFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "gpB2BUnitsFacade")
	protected GPB2BUnitsFacade gpUnitFacade;

	@Resource(name = "gpDefaultCustomerFacade")
	private GpCustomerFacade gpDefaultCustomerFacade;

	

	/**
	 * End point to get the permissions
	 *
	 * @param fields
	 * @param page
	 * @param sortCode
	 * @return B2BPermissionListWsDTO
	 */
	@RequestMapping(value = "/permissions", method = RequestMethod.GET)
	@ApiOperation(value = "Get Permission for an admin.", notes = "Returns permissions for an admin.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BPermissionListWsDTO getPermissions(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "sort", defaultValue = B2BPermissionModel.CODE) final String sortCode)
	{

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setSort(sortCode);

		pageableData.setPageSize(Config.getInt(YcommercewebservicesConstants.GPCOMMERCEWEBSERVICES_SEARCH_PAGE_SIZE, YcommercewebservicesConstants.DEFAULT_SEARCH_PAGE_SIZE));
		final List<B2BPermissionData> permissions = b2bPermissionFacade.getPagedPermissions(pageableData).getResults();

		final B2BPermissionDataList dataList = new B2BPermissionDataList();

		dataList.setPermissions(permissions);
		return getDataMapper().map(dataList, B2BPermissionListWsDTO.class, fields);
	}

	/**
	 * End Point to disable the permissions
	 *
	 * @param permissionCode
	 *
	 */
	@RequestMapping(value = "/permissions/disablePermissions/{permissionCode}", method = RequestMethod.POST)
	@ApiOperation(value = "Disable Permissions", notes = "Disables permissions for a user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void disablePermissions(@PathVariable final String permissionCode)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bPermissionFacade.enableDisablePermission(permissionCode, false);
		searchRestrictionService.enableSearchRestrictions();
	}

	/**
	 * End Point to enable the permissions
	 *
	 * @param permissionCode
	 */
	@RequestMapping(value = "/permissions/enablePermissions/{permissionCode}", method = RequestMethod.POST)
	@ApiOperation(value = "Enable Permissions", notes = "Enable permissions for a user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void enablePermissions(@PathVariable final String permissionCode)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bPermissionFacade.enableDisablePermission(permissionCode, true);
		searchRestrictionService.enableSearchRestrictions();
	}

	/**
	 * End Point to get the permission details
	 *
	 * @param fields
	 * @param permissionCode
	 * @return B2BPermissionWsDTO
	 */
	@RequestMapping(value = "/permissions/getPermissions/{permissionCode}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Permissions", notes = "Gets permission for user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BPermissionWsDTO getPermissions(@PathVariable final String permissionCode,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BPermissionData permission = b2bPermissionFacade.getPermissionDetails(permissionCode);
		searchRestrictionService.enableSearchRestrictions();
		return getDataMapper().map(permission, B2BPermissionWsDTO.class, fields);
	}

	/**
	 * End point to add Permissions
	 *
	 * @param permissionRequestDto
	 * @param fields
	 * @throws GPCommerceBusinessException
	 */
	@RequestMapping(value = "/permissions/addPermissions", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permissions", notes = "Add permission for user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addPermissions(@RequestBody final B2BPermissionRequestDTO permissionRequestDto,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws GPCommerceBusinessException
	{
		try
		{
			b2bPermissionFacade.addPermission(getDataMapper().map(permissionRequestDto, B2BPermissionData.class));
		}
		catch (final ModelSavingException | AmbiguousIdentifierException | JaloItemNotFoundException e)
		{
			LOG.error("Add Permission " + permissionRequestDto.getCode(), e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.PERMISSION.getCode(),
					GPB2BExceptionEnum.PERMISSION.getErrMsg());
		}
	}

	/**
	 * End point to edit permissions
	 *
	 * @param fields
	 * @param permissionRequestDto
	 * @throws GPCommerceBusinessException
	 */
	@RequestMapping(value = "/permissions/editPermissions", method = RequestMethod.POST)
	@ApiOperation(value = "Update Permissions", notes = "Update permission for user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void editPermissions(@RequestBody final B2BPermissionRequestDTO permissionRequestDto,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws GPCommerceBusinessException
	{
		try
		{
			b2bPermissionFacade.updatePermissionDetails(getDataMapper().map(permissionRequestDto, B2BPermissionData.class));
		}
		catch (final ModelSavingException | AmbiguousIdentifierException | JaloItemNotFoundException e)
		{
			LOG.error("Edit Permission " + permissionRequestDto.getCode(), e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.PERMISSION.getCode(),
					GPB2BExceptionEnum.PERMISSION.getErrMsg());
		}
	}

	/**
	 * End point to manage user details
	 *
	 * @param userDetails
	 * @param fields
	 * @param isUserEdit
	 * @return UserWsDTO
	 * @throws GPCommerceBusinessException
	 */
	@RequestMapping(value = "/userdetails/manageuser", method = RequestMethod.POST)
	@ApiOperation(value = "Update user details", notes = "Update user details")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserWsDTO manageUserDetails(@RequestBody final UserWsDTO userDetails,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)

	{
		try
		{
			final Boolean isEmailChanged = userDetails.getNewEmail().equalsIgnoreCase(userDetails.getUid().split("\\|")[0])
					? Boolean.FALSE
					: Boolean.TRUE;

			final String role = userDetails.isEdited() && customerFacade.getUserForUID(userDetails.getUid()).getRoles() != null
					? customerFacade.getUserForUID(userDetails.getUid()).getRoles().iterator().next()
					: null;

			final Boolean isRoleChanged = null != role && !userDetails.getSelectedRole().equalsIgnoreCase(role) ? Boolean.TRUE
					: Boolean.FALSE;

			final CustomerData customerData = gpDefaultCustomerFacade
					.updateCustomer(getDataMapper().map(userDetails, CustomerData.class), isEmailChanged);

			customerData.setUserIdUpdateFlag(
					(isEmailChanged || isRoleChanged) &&
							customerFacade.getCurrentCustomerUid().equalsIgnoreCase(userDetails.getUid()) ? Boolean.TRUE
									: Boolean.FALSE);

			return getDataMapper().map(customerData, UserWsDTO.class, fields);
		}
		catch (final ModelSavingException | AmbiguousIdentifierException | JaloItemNotFoundException e)
		{
			LOG.error("Manage User " + userDetails.getUid(), e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.USER.getCode(), GPB2BExceptionEnum.USER.getErrMsg());
		}
	}

	/**
	 * End point to invite Existing Customer
	 *
	 * @param fields
	 * @param invitedUser
	 * @throws GPCommerceBusinessException
	 */
	@RequestMapping(value = "/userdetails/inviteuser/{invitedUser:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Invite Exisiting User", notes = "Invite Exisitng user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void inviteExistingUser(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@PathVariable final String invitedUser) throws GPCommerceBusinessException
	{
		final CustomerData currentUser = customerFacade.getCurrentCustomer();
		try {
			gpB2BUserGroupFacade.inviteUser(invitedUser, currentUser);
		}
		catch (final ModelSavingException | AmbiguousIdentifierException | JaloItemNotFoundException e)
		{
			LOG.error("Invite User " + invitedUser, e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.INVITE_USER.getCode(),
					GPB2BExceptionEnum.INVITE_USER.getErrMsg());
		}
	}

	/**
	 * End point to get users with units
	 *
	 * @param fields
	 * @param page
	 * @param sortCode
	 * @return UserListWsDTO
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ApiOperation(value = "Get users.", notes = "Returns users associated with the admin")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserListWsDTO getUsers(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "sort", defaultValue = B2BCustomerModel.UID) final String sortCode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setSort(sortCode);

		pageableData.setPageSize(Config.getInt(YcommercewebservicesConstants.GPCOMMERCEWEBSERVICES_SEARCH_PAGE_SIZE, YcommercewebservicesConstants.DEFAULT_SEARCH_PAGE_SIZE));

		final List<CustomerData> customers = b2bUserGroupFacade.getPagedUserData(pageableData).getResults();

		final UserDataList dataList = new UserDataList();

		for (final CustomerData customerData : customers)
		{
			//Fetching multiple Units associated to the user
			final List<B2BUnitNodeData> unitNodes = gpUnitFacade.getUnitsForUser(customerData.getUid());
			customerData.setUnits(unitNodes);
			customerData.setName(customerData.getName().replace("|", " "));
		}

		dataList.setUsers(customers);

		return getDataMapper().map(dataList, UserListWsDTO.class, fields);
	}

	/**
	 * End point to retrieve user groups
	 *
	 * @param fields
	 * @param page
	 * @param sortCode
	 * @return B2BUserGroupListWsDTO
	 */
	@RequestMapping(value = "/usergroups", method = RequestMethod.GET)
	@ApiOperation(value = "Get user groups.", notes = "Returns user groups associated for an admin.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUserGroupListWsDTO getUserGroups(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "sort", defaultValue = B2BCustomerModel.UID) final String sortCode)
	{

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setSort(sortCode);

		pageableData.setPageSize(Config.getInt(YcommercewebservicesConstants.GPCOMMERCEWEBSERVICES_SEARCH_PAGE_SIZE, YcommercewebservicesConstants.DEFAULT_SEARCH_PAGE_SIZE));
		searchRestrictionService.disableSearchRestrictions();
		final List<B2BUserGroupData> customers = b2bUserGroupFacade.getPagedB2BUserGroups(pageableData).getResults();
		searchRestrictionService.enableSearchRestrictions();
		final B2BUserGroupDataList dataList = new B2BUserGroupDataList();

		dataList.setUsergroups(customers);
		return getDataMapper().map(dataList, B2BUserGroupListWsDTO.class, fields);
	}

	/**
	 * End point to enable b2b user
	 *
	 * @param id
	 *
	 */
	@RequestMapping(value = "/enableUser/{id:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Enable User", notes = "Enables a user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void enableUser(@PathVariable final String id)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bUserFacade.enableCustomer(id);
		searchRestrictionService.enableSearchRestrictions();
	}

	/**
	 * End point to disable b2b user
	 *
	 * @param id
	 */
	@RequestMapping(value = "/disableUser/{id:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Disable User", notes = "Disables  a user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void disableUser(@PathVariable final String id)
	{

		searchRestrictionService.disableSearchRestrictions();
		b2bUserFacade.disableCustomer(id);
		searchRestrictionService.enableSearchRestrictions();

	}

	/**
	 * End point to Add Permission to the b2b user
	 *
	 * @param id
	 * @param permissionCode
	 * @return B2BSelectionData
	 *
	 */
	@RequestMapping(value = "/permissions/addPermissionToUser/{permissionCode}/{id:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permission to User", notes = "Adds  permission to  user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionData addPermissionToUser(@PathVariable final String id, @PathVariable final String permissionCode)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BSelectionData addPermissionToCustomer = b2bPermissionFacade.addPermissionToCustomer(id, permissionCode);
		searchRestrictionService.enableSearchRestrictions();
		return addPermissionToCustomer;
	}

	/**
	 * End point to Remove Permission from the b2b user
	 *
	 * @param id
	 * @param permissionCode
	 * @return B2BSelectionData
	 *
	 */
	@RequestMapping(value = "/permissions/removePermissionToUser/{permissionCode}/{id:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Removes's Permission from user", notes = "Removes permission from user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionData removePermissionFromUser(@PathVariable final String id, @PathVariable final String permissionCode)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BSelectionData removePermissionFromCustomer = b2bPermissionFacade.removePermissionFromCustomer(id, permissionCode);
		searchRestrictionService.enableSearchRestrictions();
		return removePermissionFromCustomer;
	}

	/**
	 * End point to get permissions
	 *
	 * @param userB2BUnitList
	 * @param fields
	 * @return B2BPermissionListWsDTO
	 */
	@RequestMapping(value = "/permissions/permissionsforunit", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BPermissionListWsDTO getPermissionsForUnit(@RequestBody final UserB2BUnitWsDTO userB2BUnitList,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		B2BPermissionDataList dataList = null;
		if (CollectionUtils.isNotEmpty(userB2BUnitList.getB2BUnitList()))
		{
			dataList = gpB2BPermissionFacade.findPermissionsByUnits(userB2BUnitList);
		}

		return getDataMapper().map(dataList, B2BPermissionListWsDTO.class, fields);
	}

	/**
	 * End point to Get User Groups for unit
	 *
	 * @param userB2BUnitList
	 * @param fields
	 * @return B2BUserGroupListWsDTO
	 */
	@RequestMapping(value = "/usergroupsforunit", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUserGroupListWsDTO getUserGroupsForUnit(@RequestBody final UserB2BUnitWsDTO userB2BUnitList,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		B2BUserGroupDataList dataList = null;
		if (CollectionUtils.isNotEmpty(userB2BUnitList.getB2BUnitList()))
		{
			dataList = gpB2BUserGroupFacade.findUserGroupsByUnits(userB2BUnitList);
		}
		return getDataMapper().map(dataList, B2BUserGroupListWsDTO.class, fields);
	}

	/**
	 * End point to Add Permission to the b2b user group
	 *
	 * @param userGroupId
	 * @param permissionCode
	 * @return B2BSelectionData
	 *
	 */
	@RequestMapping(value = "/permissions/addPermissionToUserGroup/{userGroupId}/{permissionCode}", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permission to User Group", notes = "Adds  permission to  User Group")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionData addPermissionToUserGroup(@PathVariable final String userGroupId,
			@PathVariable final String permissionCode)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BSelectionData selectionData = b2bPermissionFacade.addPermissionToUserGroup(userGroupId, permissionCode);
		searchRestrictionService.enableSearchRestrictions();
		return selectionData;
	}

	/**
	 * End point to Remove Permission to the b2b usergroup
	 *
	 * @param userGroupId
	 * @param permissionCode
	 * @return B2BSelectionData
	 *
	 */
	@RequestMapping(value = "/permissions/removePermissionFromUserGroup/{userGroupId}/{permissionCode}", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permission to User", notes = "Adds  permission to  user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionData removePermissionFromUserGroup(@PathVariable final String userGroupId,
			@PathVariable final String permissionCode)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BSelectionData selectionData = b2bPermissionFacade.removePermissionFromUserGroup(userGroupId, permissionCode);
		searchRestrictionService.enableSearchRestrictions();
		return selectionData;
	}

	/**
	 * End point to Add B2Buser group TO User
	 *
	 * @param id
	 * @param usergroup
	 * @return B2BSelectionData
	 *
	 */
	@RequestMapping(value = "/addUserGroupToUser/{usergroup}/{id}", method = RequestMethod.POST)
	@ApiOperation(value = "Adds B2BUserGroup to User ", notes = "Adds B2BUserGroup to User")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionData addUserGroupToUser(@PathVariable final String id, @PathVariable final String usergroup)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BSelectionData addUserGroupToUser = b2bUserFacade.addB2BUserGroupToCustomer(id, usergroup);
		searchRestrictionService.enableSearchRestrictions();
		return addUserGroupToUser;
	}

	/**
	 * End point to Remove UserGroup from the user group
	 *
	 * @param id
	 * @param usergroup
	 * @return B2BSelectionData
	 *
	 */
	@RequestMapping(value = "/removeUserGroupToUser/{usergroup}/{id:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Removes B2BUserGroup to User ", notes = "Removes B2BUserGroup to User")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionData removeUserGroupFromUser(@PathVariable final String id, @PathVariable final String usergroup)
	{
		searchRestrictionService.disableSearchRestrictions();
		final B2BSelectionData removeUserGroupFromUser = b2bUserFacade.deselectB2BUserGroupFromCustomer(id, usergroup);
		searchRestrictionService.enableSearchRestrictions();
		return removeUserGroupFromUser;
	}

	/**
	 * Add Member to User Group
	 *
	 * @param userGroupUid
	 * @param id
	 */
	@RequestMapping(value = "/addUserGroup/{userGroupUid}/{id:.+}", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addMemberToUserGroup(@PathVariable final String userGroupUid, @PathVariable final String id)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bUserGroupFacade.addMemberToUserGroup(userGroupUid, id);
		searchRestrictionService.enableSearchRestrictions();
	}

	/**
	 * End point to remove the user from the user group
	 *
	 * @param userGroupUid
	 * @param id
	 */
	@RequestMapping(value = "/removeusergroup/{userGroupUid}/{id:.+}", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void removeMemberFromUserGroup(@PathVariable final String userGroupUid, @PathVariable final String id)
	{
		searchRestrictionService.disableSearchRestrictions();
		b2bUserGroupFacade.removeMemberFromUserGroup(userGroupUid, id);
		searchRestrictionService.enableSearchRestrictions();
	}

	/**
	 * Add permissions to user group
	 *
	 * @param userGroupId
	 * @param gpRequest
	 */
	@RequestMapping(value = "/addPermissionsToUserGroup/{userGroupId}", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permission to User Group", notes = "Adds  permission to  User Group")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addPermissionsToUserGroup(@PathVariable final String userGroupId, @RequestBody final GPRequestWsDTO gpRequest)
	{
		gpB2BPermissionFacade.addPermissionsToUserGroup(userGroupId, gpRequest.getCodes());
	}

	/**
	 * End point to Add B2Buser group TO User group
	 *
	 * @param ugId
	 * @param gpRequest
	 *
	 */
	@RequestMapping(value = "/addUsersToUserGroup/{ugId}", method = RequestMethod.POST)
	@ApiOperation(value = "Adds B2BUserGroup to User ", notes = "Adds B2BUserGroup to User")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addUsersToUserGroup(@PathVariable final String ugId, @RequestBody final GPRequestWsDTO gpRequest)
	{
		gpB2BUserGroupFacade.addUsersToUserGroup(ugId, gpRequest.getCodes());
	}

	/**
	 * Add Permissions to Users
	 *
	 * @param userUid
	 * @param gpRequest
	 */
	@RequestMapping(value = "/addPermissionsToUsers/{userUid:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permission to User Group", notes = "Adds  permission to  User Group")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addPermissionsToUsers(@PathVariable final String userUid, @RequestBody final GPRequestWsDTO gpRequest)
	{
		gpB2BPermissionFacade.addPermissionsToUser(userUid, gpRequest.getCodes());
	}

	/**
	 * Add User Groups to Users
	 *
	 * @param userUid
	 * @param gpRequest
	 */
	@RequestMapping(value = "/addUserGroupsToUser/{userUid:.+}", method = RequestMethod.POST)
	@ApiOperation(value = "Add Permission to User Group", notes = "Adds  permission to  User Group")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void addUserGroupsToUser(@PathVariable final String userUid, @RequestBody final GPRequestWsDTO gpRequest)
	{
		gpB2BUserGroupFacade.addUserGroupsToUser(userUid, gpRequest.getCodes());
	}

	/**
	 * Get Users associated to admin
	 *
	 * @param userUid
	 * @param fields
	 * @return UserWsDTO
	 */
	@RequestMapping(value = "/userdetails/{userUid:.+}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get customer profile", notes = "Returns customer profile.")
	@ApiBaseSiteIdAndUserIdParam
	public UserWsDTO getUser(@PathVariable final String userUid,
			@ApiParam(value = "Response configuration (list of fields, which should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		searchRestrictionService.disableSearchRestrictions();
		final CustomerData customerData = gpB2BUserGroupFacade.getB2BCustomerForUid(userUid);
		final List<B2BUnitNodeData> unitNodes = gpUnitFacade.getUnitsForUser(userUid);
		customerData.setUnits(unitNodes);
		searchRestrictionService.enableSearchRestrictions();
		return getDataMapper().map(customerData, UserWsDTO.class, fields);
	}

}
