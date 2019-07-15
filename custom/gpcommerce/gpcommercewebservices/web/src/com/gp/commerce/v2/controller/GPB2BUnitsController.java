/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.controller;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUserFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.organization.services.impl.OrgUnitHierarchyException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gp.commerce.core.constants.GPB2BExceptionEnum;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.dto.company.B2BUnitNodeListWsDTO;
import com.gp.commerce.dto.company.B2BUnitNodeWsDTO;
import com.gp.commerce.dto.company.B2BUnitWsDTO;
import com.gp.commerce.dto.company.data.UserListWsDTO;
import com.gp.commerce.facades.company.GPB2BUnitsFacade;
import com.gp.commerce.swagger.ApiBaseSiteIdAndUserIdParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Class for managing B2B Units
 */
@Secured(
{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_ASAGENTSALESMANAGERGROUP" })
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/units")
@Api(tags = "GP B2B Units")
public class GPB2BUnitsController extends BaseController
{

	private static final Logger LOG = Logger.getLogger(GPB2BUnitsController.class);

	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;

	@Resource(name = "gpB2BUnitsFacade")
	protected GPB2BUnitsFacade gpUnitFacade;

	@Resource(name = "b2bUserFacade")
	protected B2BUserFacade b2bUserFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	/**
	 * Unit details along with child units, buyers and administrators
	 *
	 * @param unitUid
	 * @param fields
	 * @return B2BUnitWsDTO
	 */
	@RequestMapping(value = "/{unitUid:.+}", method = RequestMethod.GET)
	@ApiOperation(value = "Get unit details of a customer.", notes = "Returns unit details along with child units, buyers and administrators.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUnitWsDTO getUnitDetails(
			@ApiParam(value = "Unit details identifier.", required = true) @PathVariable final String unitUid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BUnitData unit = gpUnitFacade.getUnitForUid(unitUid);
		return getDataMapper().map(unit, B2BUnitWsDTO.class, fields);
	}

	/**
	 * Node unit details along with child units of a customer
	 *
	 * @param fields
	 * @return B2BUnitNodeWsDTO
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Get Node unit of a customer.", notes = "Returns Node unit details along with child units of a customer.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUnitNodeWsDTO getNodeUnit(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BUnitNodeData unitNode = b2bUnitFacade.getParentUnitNode();
		return getDataMapper().map(unitNode, B2BUnitNodeWsDTO.class, fields);
	}

	/**
	 * Disable Child Unit
	 *
	 * @param unitUid
	 */
	@RequestMapping(value = "/{unitUid:.+}/{enableUnit}", method = RequestMethod.POST)
	@ApiOperation(value = "Disables Unit.", notes = "Disable the unit")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void disableChildUnit(@ApiParam(value = "Unit details identifier.", required = true) @PathVariable final String unitUid,
			@ApiParam(value = "Enable unit identifier.", required = true) @PathVariable final Boolean enableUnit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Disable Unit: id=" + unitUid);
		}
		gpUnitFacade.disableUnit(unitUid,enableUnit);
	}

	/**
	 * Associated unit nodes with id and name for current user
	 *
	 * @param fields
	 * @param unit
	 * @return B2BUnitNodeListWsDTO
	 */
	@RequestMapping(value = "/nodes", method = RequestMethod.GET)
	@ApiOperation(value = "Get associated unit nodes.", notes = "Returns unit nodes of the current user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUnitNodeListWsDTO getUnitNodes(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false) final boolean unit)
	{
		final B2BUnitNodeListWsDTO response = new B2BUnitNodeListWsDTO();
		final List<B2BUnitNodeWsDTO> nodes = new ArrayList<>();
		final CustomerData customer = customerFacade.getCurrentCustomer();
		final List<B2BUnitNodeData> unitNodes = gpUnitFacade.getUnitNodes(customer.getUid(), unit);
		for (final B2BUnitNodeData unitNode : unitNodes)
		{
			nodes.add(getDataMapper().map(unitNode, B2BUnitNodeWsDTO.class, "active,id,name"));
		}
		response.setUnits(nodes);
		return response;
	}


	/**
	 * Add Existing Child Units to the User
	 *
	 * @param units
	 * @param userUid
	 * @param fields
	 * @return B2BUnitNodeListWsDTO
	 */
	@RequestMapping(value = "/addUnits/{userUid:.+}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Add Existing Units.", notes = "Add the units")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUnitNodeListWsDTO addChildUnits(
			@ApiParam(value = "Unit list", required = true) @RequestBody final B2BUnitNodeListWsDTO units,
			@ApiParam(value = "User details identifier.", required = true) @PathVariable final String userUid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BUnitNodeListWsDTO response = new B2BUnitNodeListWsDTO();
		final List<B2BUnitNodeWsDTO> nodes = new ArrayList<>();
		final List<B2BUnitNodeData> unitNodes = gpUnitFacade.getUnitsForUser(userUid);
		for (final B2BUnitNodeWsDTO unit : units.getUnits())
		{
			unitNodes.removeIf(p -> p.getId().equalsIgnoreCase(unit.getId()));
			searchRestrictionService.disableSearchRestrictions();
			final B2BUnitNodeData unitNode = gpUnitFacade.addUnitToUser(unit.getId(), userUid);
			nodes.add(getDataMapper().map(unitNode, B2BUnitNodeWsDTO.class, fields));
			searchRestrictionService.enableSearchRestrictions();
		}
		if (!unitNodes.isEmpty())
		{
			unitNodes.forEach(n -> gpUnitFacade.removeUnitFromUser(n.getId(), userUid));
		}
		response.setUnits(nodes);
		return response;
	}

	/**
	 * Remove Existing Child Units from the User
	 *
	 * @param units
	 * @param userUid
	 */
	@RequestMapping(value = "/removeUnits/{userUid:.+}", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Remove Existing Units.", notes = "Remove the units")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void removeChildUnits(@ApiParam(value = "Unit list", required = true) @RequestBody final B2BUnitNodeListWsDTO units,
			@ApiParam(value = "User details identifier.", required = true) @PathVariable final String userUid)
	{
		for (final B2BUnitNodeWsDTO unit : units.getUnits())
		{
			gpUnitFacade.removeUnitFromUser(unit.getId(), userUid);
		}
	}

	/**
	 * Add users to unit
	 *
	 * @param users
	 * @param unitUid
	 * @param fields
	 * @return UserListWsDTO
	 */
	@RequestMapping(value = "/addUsers/{unitUid:.+}/{userGroup}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Add Existing Users.", notes = "Add users to unit")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserListWsDTO addUsers(@ApiParam(value = "User list", required = true) @RequestBody final UserListWsDTO users,
			@ApiParam(value = "Unit details identifier.", required = true) @PathVariable final String unitUid,
			@ApiParam(value = "UserGroup identifier.", required = true) @PathVariable final String userGroup,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserListWsDTO response = new UserListWsDTO();
		final List<UserWsDTO> addedUsers = new ArrayList<>();
		List<CustomerData> unitUsers = gpUnitFacade.getUsersOfUserGroupForUnit(unitUid, userGroup);
		

		for (final UserWsDTO user : users.getUsers())
		{
			unitUsers.removeIf(p -> p.getUid().equalsIgnoreCase(user.getUid()));
			final CustomerData customer = new CustomerData();
			getDataMapper().map(user, customer, true);
			final CustomerData addedUser = gpUnitFacade.addUserToUnit(unitUid, customer);
			addedUsers.add(getDataMapper().map(addedUser, UserWsDTO.class, fields));
		}
		if (!unitUsers.isEmpty())
		{
		unitUsers.forEach(n -> gpUnitFacade.removeB2BUserGroupFromCustomerGroups(n.getUid(), unitUid));
		}
		response.setUsers(addedUsers);
		return response;
	}

	/**
	 * Create or update unit
	 *
	 * @param unitName
	 * @param parentUid
	 * @param uid
	 * @param fields
	 * @return B2BUnitNodeWsDTO
	 * @throws GPCommerceBusinessException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = "Create or update Child unit", notes = "Create or update Child unit")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUnitNodeWsDTO addOrUpdateUnit(@ApiParam(value = "Unit Name", required = true) @RequestParam final String unitName,
			@ApiParam(value = "Parent unit identifier", required = true) @RequestParam final String parentUid,
			@ApiParam(value = "Parent unit identifier", required = false) @RequestParam(required = false) final String uid,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws GPCommerceBusinessException
	{
		final B2BUnitData unitData = new B2BUnitData();
		final B2BUnitData parentUnit = new B2BUnitData();
		unitData.setName(unitName);
		unitData.setUid(uid);
		parentUnit.setUid(parentUid);
		unitData.setUnit(parentUnit);
		B2BUnitNodeData unit = new B2BUnitNodeData();
		try {
			unit = gpUnitFacade.updateOrCreateBusinessUnit(uid, unitData);
		}
		catch (final ModelSavingException | AmbiguousIdentifierException | JaloItemNotFoundException | OrgUnitHierarchyException e)
		{
			LOG.error("Manage Unit " + unitName, e);
			throw new GPCommerceBusinessException(GPB2BExceptionEnum.UNIT.getCode(), GPB2BExceptionEnum.UNIT.getErrMsg());
		}
		return getDataMapper().map(unit, B2BUnitNodeWsDTO.class, fields);
	}

	/**
	 * Get all user of the Org associated with current User
	 *
	 * @param fields
	 * @return UserListWsDTO
	 */
	@RequestMapping(value = "/b2bUsers", method = RequestMethod.GET)
	@ApiOperation(value = "Get associated org users.", notes = "Returns associated users of the current user of a organisation")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserListWsDTO getB2BUsers(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserListWsDTO response = new UserListWsDTO();
		final List<UserWsDTO> users = new ArrayList<>();
		final List<CustomerData> orgUsers = gpUnitFacade.getUsersOfUserGroup();
		for (final CustomerData customer : orgUsers)
		{
			users.add(getDataMapper().map(customer, UserWsDTO.class));
		}
		response.setUsers(users);
		return response;
	}

	/**
	 * Remove users from the organization
	 *
	 * @param users
	 * @param unitUid
	 */
	@RequestMapping(value = "/removeUsers/{unitUid:.+}", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Remove associated org users.", notes = "Remove associated users of the current user from a organisation")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void removeB2BUsers(@ApiParam(value = "Users list", required = true) @RequestBody final UserListWsDTO users,
			@ApiParam(value = "Unit details identifier.", required = true) @PathVariable final String unitUid)
	{
		for (final UserWsDTO user : users.getUsers())
		{
			final CustomerData customer = new CustomerData();
			getDataMapper().map(user, customer, true);
			gpUnitFacade.removeB2BUserGroupFromCustomerGroups(customer.getUid(), unitUid);
		}
	}

	/**
	 * Get all users of associated with unit
	 *
	 * @param fields
	 * @param page
	 * @param sortCode
	 * @param unitUid
	 * @return UserListWsDTO
	 */
	@RequestMapping(value = "/b2bUsers/{unitUid:.+}", method = RequestMethod.GET)
	@ApiOperation(value = "Get associated org users.", notes = "Returns associated users of the current user of a organisation")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public UserListWsDTO getB2BUsersForUnit(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "sort", defaultValue = B2BCustomerModel.UID) final String sortCode,
			@PathVariable final String unitUid)
	{
		final UserListWsDTO response = new UserListWsDTO();
		final List<UserWsDTO> users = new ArrayList<>();

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setSort(sortCode);

		pageableData.setPageSize(Config.getInt("gpcommercewebservices.search.pageSize", 100));

		final List<CustomerData> orgUsers = b2bUnitFacade.getPagedUserDataForUnit(pageableData, unitUid).getResults();
		for (final CustomerData customer : orgUsers)
		{
			users.add(getDataMapper().map(customer, UserWsDTO.class));
		}
		response.setUsers(users);
		return response;
	}

	/**
	 * Associated unit nodes with id and name for user
	 *
	 * @param fields
	 * @param userUid
	 * @param unit
	 * @return B2BUnitNodeListWsDTO
	 */
	@RequestMapping(value = "/nodes/{userUid:.+}", method = RequestMethod.GET)
	@ApiOperation(value = "Get associated unit nodes.", notes = "Returns unit nodes of the current user")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BUnitNodeListWsDTO getUnitNodesForUser(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@ApiParam(value = "User details identifier.", required = true) @PathVariable final String userUid,
			@RequestParam(required = false) final boolean unit)
	{
		final B2BUnitNodeListWsDTO response = new B2BUnitNodeListWsDTO();
		final List<B2BUnitNodeWsDTO> nodes = new ArrayList<>();
		final List<B2BUnitNodeData> unitNodes = gpUnitFacade.getUnitNodes(userUid, unit);
		for (final B2BUnitNodeData unitNode : unitNodes)
		{
			nodes.add(getDataMapper().map(unitNode, B2BUnitNodeWsDTO.class, "active,id,name"));
		}
		response.setUnits(nodes);
		return response;
	}
}
