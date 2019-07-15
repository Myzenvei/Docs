/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.company.services.impl;

import static com.gp.commerce.core.enums.GPApprovalEnum.ACTIVE;
import static com.gp.commerce.core.enums.GPApprovalEnum.DISABLED;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.organization.services.OrgUnitHierarchyService;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanPropertyValueChangeClosure;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.company.dao.GPB2BUnitDao;
import com.gp.commerce.core.company.services.GPB2BUnitsService;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPApprovalEnum;
import com.gp.commerce.core.enums.SelectRoleEnum;
import com.gp.commerce.core.user.dao.GPUserDao;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * GP Unit Service
 */
public class GPDefaultB2BUnitsService extends DefaultB2BUnitService implements GPB2BUnitsService<B2BUnitModel, B2BCustomerModel>
{

	@Resource(name = "gpb2bUnitDao")
	protected GPB2BUnitDao gpb2bUnitDao;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userDao")
	protected GPUserDao gPUserDao;

	@Resource(name = "userService")
	private UserService userService;

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

	private static final Logger LOG = Logger.getLogger(GPDefaultB2BUnitsService.class);

	@Override
	public void disableBranch(final B2BUnitModel unit, final Boolean enableUnit)
	{
		validateParameterNotNullStandardMessage("unit", unit);
		this.toggleBranch(unit, enableUnit);
	}


	/**
	 * Enables or disables a branch and it sibling types {@link B2BBudgetModel} {@link B2BCustomerModel}
	 * {@link B2BPermissionModel} {@link AddressModel} assigned to all units in the branch.
	 *
	 * @param unit
	 *           A unit to look up a branch for via {@link #getBranch(de.hybris.platform.b2b.model.B2BUnitModel)}
	 * @param enable
	 *           If {@link Boolean#TRUE} all units in the branch will be activated by setting active attribute to true.
	 *           If false the reverse will happen.
	 */
	@Override
	protected void toggleBranch(final B2BUnitModel unit, final Boolean enable)
	{
		LOG.debug("Inside toggle branch");
		 // disable all units first
      final Set<B2BUnitModel> branch = getBranch(unit);
      final List<B2BUnitModel> units = new ArrayList<B2BUnitModel>();
      units.add(unit);

      final List<String> userGroupIds = new ArrayList<String>();
      userGroupIds.add(B2BConstants.B2BCUSTOMERGROUP);
      userGroupIds.add(B2BConstants.B2BADMINGROUP);


      final Collection<B2BCustomerModel> employeesOfGroup = gpb2bUnitDao.findB2BUnitMembersByGroup(units, userGroupIds);

      for (final B2BCustomerModel b2bCustomerModel : employeesOfGroup)
		{
			final List<B2BUnitModel> unitsForUser = getUnitNodes(b2bCustomerModel);
			if (unitsForUser.size() == 1)
			{
				LOG.debug("Setting Customer Active to False");
				b2bCustomerModel.setActive(false);
			}
			else
			{
				for (final B2BUnitModel b2bUnitModel : unitsForUser)
				{
					if (!b2bCustomerModel.getDefaultB2BUnit().getUid().equalsIgnoreCase(b2bUnitModel.getUid()))
					{
						LOG.debug("Assigning another unit");
						b2bCustomerModel.setDefaultB2BUnit(b2bUnitModel);
					}
				}
			}

		}


      CollectionUtils.forAllDo(branch, new Closure()
      {
            @Override
            public void execute(final Object object)
            {
                  final B2BUnitModel b2bUnitModel = (B2BUnitModel) object;
                  toggleUnit(b2bUnitModel, enable);
            }
      });
      this.getModelService().saveAll(branch);
      this.getModelService().saveAll(employeesOfGroup);

	}

	@Override
	protected void toggleUnit(final B2BUnitModel b2bUnitModel, final Boolean enable)
	{
		b2bUnitModel.setActive(enable);
      //disable budgets
      final List<B2BBudgetModel> budgets = b2bUnitModel.getBudgets();
      CollectionUtils.forAllDo(budgets, new BeanPropertyValueChangeClosure(B2BBudgetModel.ACTIVE, enable));
      getModelService().saveAll(budgets);

      //disable permissions
      final Set<B2BPermissionModel> permissions = b2bUnitModel.getPermissions();
      CollectionUtils.forAllDo(permissions, new BeanPropertyValueChangeClosure(B2BPermissionModel.ACTIVE, enable));
      getModelService().saveAll(permissions);

      //disable address
      final List<B2BUnitModel> units = new ArrayList<>();
      units.add(b2bUnitModel);
      final Collection<AddressModel> addresses = gPUserDao.getAddressesForB2BUser(units,false);

      final List<AddressModel> reviewAddresses = new ArrayList<>();
      addresses.stream()
      .filter(item -> (null != item.getApprovalStatus() &&  ( GPApprovalEnum.PENDING.equals(item.getApprovalStatus())
      || GPApprovalEnum.PENDINGBYADMIN.equals(item.getApprovalStatus())
      || GPApprovalEnum.PENDINGBYGP.equals(item.getApprovalStatus()) )

      ))
      .forEach(item -> reviewAddresses.add(item));

      final List<AddressModel> addressList = new ArrayList<>(addresses);

		if (CollectionUtils.isNotEmpty(reviewAddresses))
		{
			addressList.removeAll(reviewAddresses);
			getModelService().removeAll(reviewAddresses);
		}

      CollectionUtils.forAllDo(addresses,
                  new BeanPropertyValueChangeClosure(AddressModel.APPROVALSTATUS, enable ? ACTIVE : DISABLED));
      getModelService().saveAll(addressList);
	}

	@Override
	public List<B2BUnitModel> getUnitNodes(final B2BCustomerModel employee)
	{
		List<B2BUnitModel> unitsList = new ArrayList<>();
		if (employee != null) {
			unitsList = (List<B2BUnitModel>) CollectionUtils.select(employee.getGroups(),
					PredicateUtils.instanceofPredicate(B2BUnitModel.class));
		}
		return unitsList;
	}

	@Override
	public B2BUnitModel getUnitForUid(final String uid)
	{
		B2BUnitModel unit;
		try
		{
			unit = getUserService().getUserGroupForUID(uid, B2BUnitModel.class);
		}
		catch (final UnknownIdentifierException | ClassMismatchException e)
		{
			LOG.error(e.getMessage(), e);
			unit = null;
		}
		return unit;
	}

	@Override
	public Collection<B2BCustomerModel> getUsersOfUserGroup(final List<B2BUnitModel> units, final List<String> userGroupIds)
	{
		final Collection<B2BCustomerModel> employeesOfGroup = gpb2bUnitDao.findB2BUnitMembersByGroup(units, userGroupIds);

		if (CollectionUtils.isNotEmpty(employeesOfGroup))
		{
			return employeesOfGroup;
		}
		else
		{
			return Collections.<B2BCustomerModel> emptyList();
		}
	}

	@Override
	public boolean isB2BAdmin(final B2BCustomerModel customerModel)
	{
		final Set<PrincipalGroupModel> userGroups = customerModel.getGroups();
		for (final PrincipalGroupModel principalGroupModel : userGroups)
		{	
			final String groupId = principalGroupModel.getUid();
			if (groupId != null && groupId.equals(B2BConstants.B2BADMINGROUP))
			{
				return true;

			}
		}
		return false;
	}
	
	/**
	 * Check if the logged-in user is a buyer
	 * @param customerModel
	 */
	public boolean isBuyer(final B2BCustomerModel customerModel)
	{
		if (customerModel != null && customerModel.getSelectedRole() != null 
				&& customerModel.getSelectedRole().getCode().equals(SelectRoleEnum.BUYER.getCode()))
		{
			return true;	
		}
		return false;
	}

	@Override
	public void removeB2BUserGroupFromCustomerGroups(final String user, final String usergroup)
	{
		final B2BCustomerModel customer = getUserService().getUserForUID(user, B2BCustomerModel.class);
		final Set<PrincipalGroupModel> groupsWithoutUsergroup = removeUsergroupFromGroups(usergroup, customer.getGroups());
		final B2BUnitModel defaultB2BUnit = customer.getDefaultB2BUnit();
		final List<B2BUnitModel> units = getUnitNodes(customer);
		if (usergroup.equalsIgnoreCase(defaultB2BUnit.getUid()) && units.size() > 1)
		{
			units.remove(defaultB2BUnit);
			// Assigning the first unit to default from the list
			customer.setDefaultB2BUnit(units.get(0));
		}
		customer.setGroups(groupsWithoutUsergroup);
		getModelService().saveAll(customer);
	}

	protected Set<PrincipalGroupModel> removeUsergroupFromGroups(final String usergroup, final Set<PrincipalGroupModel> groups)
	{
		final Set<PrincipalGroupModel> groupsWithoutUsergroup = new HashSet<PrincipalGroupModel>(groups);
		CollectionUtils.filter(groupsWithoutUsergroup, new Predicate()
		{
			@Override
			public boolean evaluate(final Object object)
			{
				final PrincipalGroupModel group = (PrincipalGroupModel) object;
				return !StringUtils.equals(usergroup, group.getUid());
			}
		});
		return groupsWithoutUsergroup;
	}

	@Override
	public List<B2BUnitModel> getUnitsWithChildNodes(final B2BCustomerModel employee, final boolean isSubChildNodesReq)
	{
		final List<B2BUnitModel> units = new ArrayList<>();
		List<B2BUnitModel> unitsList = new ArrayList<>();
		if (employee != null)
		{
			unitsList = (List<B2BUnitModel>) CollectionUtils.select(employee.getGroups(),
					PredicateUtils.instanceofPredicate(B2BUnitModel.class));
		}
		List<B2BUnitModel> childUnitModels;
		for (final B2BUnitModel unit : unitsList)
		{
			childUnitModels = (List<B2BUnitModel>) CollectionUtils.select(unit.getMembers(),
					PredicateUtils.instanceofPredicate(B2BUnitModel.class));
			if (isSubChildNodesReq)
			{
				List<B2BUnitModel> subChildUnitModels;
				for (final B2BUnitModel childUnit : childUnitModels)
				{
					subChildUnitModels = (List<B2BUnitModel>) CollectionUtils.select(childUnit.getMembers(),
							PredicateUtils.instanceofPredicate(B2BUnitModel.class));
					units.addAll(subChildUnitModels);
				}
			}
			units.addAll(childUnitModels);
		}
		units.addAll(unitsList);
		return units;
	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.company.services.GPB2BUnitsService#getUsersforB2BUnit(de.hybris.platform.b2b.model.B2BUnitModel, java.lang.String)
	 * Fetching all the Users having defaultB2Bunit as units and are members of the group with groupId as userGroupIds
	 */
	@Override
	public Collection<B2BCustomerModel> getUsersforB2BUnit(final B2BUnitModel units, final String userGroupIds ,final Boolean isPrimaryAdmin)
	{
		final Collection<B2BCustomerModel> employeesOfGroup = gpb2bUnitDao.getUsersforB2BUnit(units, userGroupIds ,isPrimaryAdmin);

		if (CollectionUtils.isNotEmpty(employeesOfGroup))
		{
			return employeesOfGroup;
		}
		else
		{
			return Collections.<B2BCustomerModel> emptyList();
		}
	}

	/**
	 * Update usergroup for list of users based on unit level
	 *
	 * @param b2bUnitLevel
	 * 			the b2bunit level (L1/L2)
	 * @param units
	 * 			the list of unit models
	 */
	@Override
	public void updateUserGroupByUnitLevel(final String b2bUnitLevel,final List<UserModel> users){
		validateParameterNotNullStandardMessage("b2bUnitLevel", b2bUnitLevel);
		validateParameterNotNullStandardMessage("users", users);
		final List<String> b2bUnitLevels = Arrays.asList(Config.getParameter(GpcommerceCoreConstants.B2B_UNITLEVEL_CODES).split(","));
		if (StringUtils.isNotEmpty(b2bUnitLevel) &&  CollectionUtils.isNotEmpty(users) && b2bUnitLevels.contains(b2bUnitLevel)) {
			final List<UserModel> updatedUsers = new ArrayList<>();
			//ugIds hold user group Ids
			final List<String> ugIds = new ArrayList<>();
			//Pull key value for unit level:user group id
			final List<String> ugKeyVals = Arrays.asList(Config.getParameter(GpcommerceCoreConstants.B2B_CUSTOMER_GROUP_CODES).split(","));
			//ugMap holds key as unit level and usergroup as value
			final Map<String, String> ugMap = new HashMap<>();
			for (final String ugKeyValue : ugKeyVals) {
				final String[] split = ugKeyValue.split(":");
				ugMap.put(split[0], split[1]);
				ugIds.add(split[1]);
			}
			// set user group at user level based on b2bunit level

			for (final UserModel b2bCustomer : users) {


				final List<PrincipalGroupModel> b2bUgList = new ArrayList<>(b2bCustomer.getGroups());
				final List<PrincipalGroupModel> updatedUgList = new ArrayList<>();

				//Filter and hold user groups other than ugIds
				b2bUgList.stream().filter(item -> !ugIds.contains(item.getUid()))
						.forEach(item -> updatedUgList.add(item));

				final UserGroupModel userGroupModel = userService.getUserGroupForUID(ugMap.get(b2bUnitLevel));
				updatedUgList.add(userGroupModel);

				//set updated user group list
				b2bCustomer.setGroups(new HashSet<>(updatedUgList));
				updatedUsers.add(b2bCustomer);
			}
			//save all updated users
			if(CollectionUtils.isNotEmpty(updatedUsers)){
				getModelService().saveAll(updatedUsers);
			}
		}
	}

	/**
	 * Overrides the default implementation of this method in DefaultB2BUnitsService. This implementation just has one change.
	 * It doesn't set the B2B_DEFAULT_PRICE_GROUP in session so that it cannot be used as default pricing group.
	 */
	@Override
	public void updateBranchInSession(final Session session, final UserModel currentUser)
	{
		if (currentUser instanceof B2BCustomerModel)
		{
			final Object[] branchInfo = (Object[]) getSessionService().executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public Object[] execute()
				{
					getSearchRestrictionService().disableSearchRestrictions();
					final B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
					final B2BUnitModel unitOfCustomer = getParent(currentCustomer);


					/**
					 * Europe1PriceFactory does not allow a user to belong to multiple price groups with themselves have
					 * different UPGs assigned see https://jira.hybris.com/browse/BTOB-488 get the upg assigned to the parent
					 * unit and set it in the context if none is assigned default to 'B2B_DEFAULT_PRICE_GROUP'
					 */
					final EnumerationValueModel userPriceGroup = (unitOfCustomer.getUserPriceGroup() != null ? getTypeService()
							.getEnumerationValue(unitOfCustomer.getUserPriceGroup()) : lookupPriceGroupFromClosestParent(unitOfCustomer));
					return new Object[]
					{ getRootUnit(unitOfCustomer), getBranch(unitOfCustomer), unitOfCustomer, userPriceGroup };
				}
			});

			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_ROOTUNIT, branchInfo[0]);
			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_BRANCH, branchInfo[1]);
			getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_UNIT, branchInfo[2]);
			if (branchInfo[3] instanceof EnumerationValueModel && !((EnumerationValueModel)branchInfo[3]).getCode().equalsIgnoreCase(B2BConstants.B2BDEFAULTPRICEGROUP))
			{
				getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, branchInfo[3]);
			}
		}
	}

	@Override
	public void addUnitToUser(final B2BUnitModel group, final PrincipalModel member)
	{
		final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>(
				(member.getGroups() != null ? member.getGroups() : Collections.emptySet()));
		// for units only one parent is allowed however customers can belong to multiple units
		if (member instanceof B2BUnitModel)
		{
			CollectionUtils.filter(groups, PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUnitModel.class)));
		}
		groups.add(group);
		member.setGroups(groups);
		modelService.saveAll(member);
	}

	@Override
	public void addUserToUnit(final PrincipalModel member, final PrincipalGroupModel group)
	{
		final HashSet<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>(
				(member.getGroups() != null ? member.getGroups() : Collections.emptySet()));
		groups.add(group);
		member.setGroups(groups);
		modelService.saveAll(member);
	}


	@Override
	public B2BUnitModel updateOrCreateBusinessUnit(final String originalUid, final B2BUnitData unit)
	{
		B2BUnitModel unitModel = originalUid != null ? getUnitForUid(originalUid) : null;
		B2BUnitModel parentUnitBefore = null;
		B2BUnitModel parentUnitAfter = null;
		boolean newModel = false;
		if (unitModel == null)
		{
			newModel = true;
			unitModel = modelService.create(B2BUnitModel.class);
			unit.setUid(unit.getName() + System.currentTimeMillis());
		}
		else
		{
			parentUnitBefore = b2BCommerceUnitService.getParentUnit(unitModel);
		}

		b2BUnitReverseConverter.convert(unit, unitModel);
		parentUnitAfter = b2BCommerceUnitService.getParentUnit(unitModel);
		newModel = newModel || !Objects.equals(parentUnitBefore, parentUnitAfter);

		unitModel.setB2bUnitLevel(parentUnitAfter.getB2bUnitLevel());

		final boolean isPathGenerationEnabled = configurationService.getConfiguration()
				.getBoolean(CommerceServicesConstants.ORG_UNIT_PATH_GENERATION_ENABLED, true);
		if (newModel && isPathGenerationEnabled)
		{
			orgUnitHierarchyService.saveChangesAndUpdateUnitPath(unitModel);
		}
		else
		{
			modelService.save(unitModel);
		}

		// if a new unit is being created update branch in the session
		if (newModel)
		{
			final B2BCustomerModel currentUser = (B2BCustomerModel) userService.getCurrentUser();
			updateBranchInSession(sessionService.getCurrentSession(), currentUser);
		}
		return unitModel;
	}
	
	
	/**
	 * Gets the b 2 b unit for customer.
	 *
	 * @param b2bCustomer the b 2 b customer
	 * @return the b 2 b unit for customer
	 */
	public B2BUnitModel getB2bUnitForCustomer(B2BCustomerModel b2bCustomer)
	{
		return (null != b2bCustomer.getDefaultB2BUnit()) ? b2bCustomer.getDefaultB2BUnit()
				: getModelService().create(B2BUnitModel.class);
	}
}
