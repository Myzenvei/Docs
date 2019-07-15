/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.company.impl;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.company.B2BCommerceUserService;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.organization.services.OrgUnitHierarchyService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;

import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.facades.company.GPB2BUnitsFacade;




/**
 * GP B2B Units Facade
 */
public class GPDefaultB2BUnitsFacade implements GPB2BUnitsFacade
{

	private static final String ANONYMOUS_USER = "anonymous";
	private static final String B2B_GUEST = "B2BGuest";
	private static final String GP_GUEST_B2BUNIT = "gp.guest.b2b.unit";
	
	@Resource(name = "gpB2BUnitsService")
	protected GPB2BUnitsService<B2BUnitModel, UserModel> gpB2BUnitsService;

	@Resource(name = "b2bUnitService")
	protected B2BUnitService<B2BUnitModel, UserModel> b2bUnitService;

	@Resource(name = "userService")
	protected UserService userService;

	@Resource(name = "b2bUnitNodeConverter")
	protected Converter<B2BUnitModel, B2BUnitNodeData> unitTreeConverter;

	@Resource(name = "b2bCustomerService")
	protected B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "b2bCommerceUserService")
	protected B2BCommerceUserService b2BCommerceUserService;

	@Resource(name = "sessionService")
	protected SessionService sessionService;

	@Resource(name = "b2bUnitReverseConverter")
	protected Converter<B2BUnitData, B2BUnitModel> b2BUnitReverseConverter;

	@Resource(name = "orgUnitHierarchyService")
	protected OrgUnitHierarchyService orgUnitHierarchyService;

	@Resource(name = "b2bCommerceUnitService")
	protected B2BCommerceUnitService b2BCommerceUnitService;

	@Resource(name = "configurationService")
	protected ConfigurationService configurationService;

	@Resource(name = "b2BCustomerConverter")
	protected Converter<B2BCustomerModel, CustomerData> b2BCustomerConverter;

	@Resource(name = "b2bUnitConverter")
	private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;

	@Override
	public void disableUnit(final String unitUid, final Boolean enableUnit)
	{
		final B2BUnitModel unit = b2bUnitService.getUnitForUid(unitUid);
		gpB2BUnitsService.disableBranch(unit, enableUnit);

	}

	@Override
	public List<B2BUnitNodeData> getUnitNodes(final String userId, final boolean isSubChildNodesNotReq)
	{
		List<B2BUnitModel> units = new ArrayList<>();
		if (userId.equalsIgnoreCase(ANONYMOUS_USER)) {
			B2BUnitModel unit = new B2BUnitModel();
			if (null != configurationService.getConfiguration().getString(GP_GUEST_B2BUNIT)) {
				unit = gpB2BUnitsService
						.getUnitForUid(configurationService.getConfiguration().getString(GP_GUEST_B2BUNIT));
			} else {
				unit = gpB2BUnitsService.getUnitForUid(B2B_GUEST);
			}
			units.add(unit);
		} else {
			final B2BCustomerModel currentUser = (B2BCustomerModel) userService.getUserForUID(userId);
			if (isSubChildNodesNotReq) {
				units = gpB2BUnitsService.getUnitsWithChildNodes(currentUser, false);
			} else {
				units = gpB2BUnitsService.getUnitsWithChildNodes(currentUser, true);
			}
		}
		final List<B2BUnitNodeData> unitNodes = Converters.convertAll(units, unitTreeConverter);
		final List<B2BUnitNodeData> activeNodes = new ArrayList<>();
		for (final B2BUnitNodeData node : unitNodes) {
			if (node.isActive()) {
				activeNodes.add(node);
			}
		}
		return activeNodes.stream().filter(distinctByKey(p -> p.getId())).collect(Collectors.toList());
	}

	@Override
	public B2BUnitNodeData addUnitToUser(final String unitUid, final String userUid)
	{
		final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(unitUid);
		final B2BCustomerModel currentUser = (B2BCustomerModel) userService.getUserForUID(userUid);
		gpB2BUnitsService.addUnitToUser(unitModel, currentUser);
		return unitTreeConverter.convert(unitModel);
	}

	@Override
	public void removeUnitFromUser(final String unitUid, final String userUid)
	{
		final B2BCustomerModel currentUser = (B2BCustomerModel) userService.getUserForUID(userUid);
		gpB2BUnitsService.removeB2BUserGroupFromCustomerGroups(currentUser.getUid(), unitUid);
	}

	@Override
	public CustomerData addUserToUnit(final String unitUid, final CustomerData customer)
	{

		final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(unitUid);
		final B2BCustomerModel customerModel = (B2BCustomerModel) userService.getUserForUID(customer.getUid());
		final Set<PrincipalGroupModel> userRoles = new HashSet<>(customerModel.getGroups());
		final Set<PrincipalGroupModel> roleModels = new HashSet<>(customerModel.getGroups());
		CollectionUtils.filter(roleModels, PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUnitModel.class)));
		CollectionUtils.filter(roleModels,
				PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUserGroupModel.class)));
		for (final PrincipalGroupModel role : roleModels)
		{
			final String requestedRole = customer.getRoles().iterator().next();
			if (!requestedRole.equalsIgnoreCase(role.getUid()))
			{
				userRoles.remove(role);
				userRoles.add(userService.getUserGroupForUID(requestedRole));
			}
		}
		customerModel.setGroups(userRoles);
		gpB2BUnitsService.addUserToUnit(customerModel, unitModel);
		final CustomerData customerData = b2BCustomerConverter.convert(customerModel);
		customerData.setUnits(getUnitsForUser(customer.getUid()));
		return customerData;
	}

	@Override
	public B2BUnitNodeData updateOrCreateBusinessUnit(final String originalUid, final B2BUnitData unit)
	{
		final B2BUnitModel unitModel = gpB2BUnitsService.updateOrCreateBusinessUnit(originalUid, unit);
		return unitTreeConverter.convert(unitModel);
	}

	@Override
	public List<CustomerData> getUsersOfUserGroup()
	{
		final List<CustomerData> customers = new ArrayList<>();
		final List<String> groups = new ArrayList<>();
		groups.add(B2BConstants.B2BCUSTOMERGROUP);
		groups.add(B2BConstants.B2BADMINGROUP);
		final B2BCustomerModel currentUser = (B2BCustomerModel) userService.getCurrentUser();
		final List<B2BUnitModel> units = gpB2BUnitsService.getUnitsWithChildNodes(currentUser, true);
		final Collection<B2BCustomerModel> b2BCustomers = gpB2BUnitsService.getUsersOfUserGroup(units, groups);
		if (CollectionUtils.isNotEmpty(b2BCustomers))
		{
			customers.addAll(Converters.convertAll(b2BCustomers, b2BCustomerConverter));
		}
		return customers;
	}

	@Override
	public void removeB2BUserGroupFromCustomerGroups(final String customerUid, final String userGroupUid)
	{
		gpB2BUnitsService.removeB2BUserGroupFromCustomerGroups(customerUid, userGroupUid);
	}

	@Override
	public B2BUnitData getUnitForUid(final String unitUid)
	{
		final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(unitUid);
		final B2BUnitData unit = unitModel == null ? new B2BUnitData() : b2bUnitConverter.convert(unitModel);
		final Collection<CustomerData> admins = new ArrayList<>();
		final Collection<CustomerData> customers = new ArrayList<>();
		for (final CustomerData admin : CollectionUtils.emptyIfNull(unit.getAdministrators()))
		{
			admin.setUnits(getUnitsForUser(admin.getUid()));
			admins.add(admin);
		}
		for (final CustomerData customer : CollectionUtils.emptyIfNull(unit.getCustomers()))
		{
			customer.setUnits(getUnitsForUser(customer.getUid()));
			customers.add(customer);
		}
		unit.setAdministrators(admins);
		unit.setCustomers(customers);
		unit.setParent(unit.getUnit() != null ? unit.getUnit().getName() : null);
		return unit;
	}

	@Override
	public List<CustomerData> getUsersOfUserGroupForUnit(final String unitUid, final String role)
	{
		final List<CustomerData> customers = new ArrayList<>();
		final List<String> groups = new ArrayList<>();
		if (null == role)
		{
			groups.add(B2BConstants.B2BCUSTOMERGROUP);
			groups.add(B2BConstants.B2BADMINGROUP);
		}
		else if (role.equalsIgnoreCase(B2BConstants.B2BCUSTOMERGROUP))
		{
			groups.add(B2BConstants.B2BCUSTOMERGROUP);
		}
		else if (role.equalsIgnoreCase(B2BConstants.B2BADMINGROUP))
		{
			groups.add(B2BConstants.B2BADMINGROUP);
		}
		final List<B2BUnitModel> units = new ArrayList<>();
		units.add(b2bUnitService.getUnitForUid(unitUid));
		final Collection<B2BCustomerModel> b2BCustomers = gpB2BUnitsService.getUsersOfUserGroup(units, groups);
		if (CollectionUtils.isNotEmpty(b2BCustomers))
		{
			customers.addAll(Converters.convertAll(b2BCustomers, b2BCustomerConverter));
		}
		return customers;
	}

	@Override
	public List<B2BUnitNodeData> getUnitsForUser(final String userId)
	{
		final B2BCustomerModel currentUser = (B2BCustomerModel) userService.getUserForUID(userId);
		final List<B2BUnitModel> units = gpB2BUnitsService.getUnitNodes(currentUser);
		final List<B2BUnitNodeData> nodes = Converters.convertAll(units, unitTreeConverter);
		return nodes.stream().filter(distinctByKey(p -> p.getId())).collect(Collectors.toList());
	}

	private static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor)
	{
		final Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
