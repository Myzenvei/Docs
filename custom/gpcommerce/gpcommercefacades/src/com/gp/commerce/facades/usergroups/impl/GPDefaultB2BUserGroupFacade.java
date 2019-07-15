/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.usergroups.impl;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.event.EmailSubjectType;
import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.services.event.GPEmailItemEvent;
import com.gp.commerce.core.services.usergroups.impl.GPDefaultB2BUserGroupService;
import com.gp.commerce.dto.company.data.B2BUserGroupDataList;
import com.gp.commerce.dto.company.data.UserB2BUnitWsDTO;
import com.gp.commerce.facades.usergroups.GPB2BUserGroupFacade;


/**
 * The Class GPDefaultB2BUserGroupFacade.
 */
public class GPDefaultB2BUserGroupFacade implements GPB2BUserGroupFacade
{

	private GPDefaultB2BUserGroupService gpb2buserGroupService;
	private Converter<B2BUserGroupModel, B2BUserGroupData> b2BUserGroupConverter;

	@Resource(name = "b2bCommerceB2BUserGroupService")
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "eventService")
	private EventService eventService;

	@Resource(name = "customerConverter")
	private Converter<UserModel, CustomerData> customerConverter;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "b2bCommerceUnitService")
	private B2BCommerceUnitService b2BCommerceUnitService;
	
	private Converter<B2BCustomerModel, CustomerData> b2BUserConverter;

	@Override
	public B2BUserGroupDataList findUserGroupsByUnits(final UserB2BUnitWsDTO userB2BUnitList)
	{

		final B2BUserGroupDataList datalist = new B2BUserGroupDataList();
		final List<B2BUserGroupModel> usergroups = getGpb2buserGroupService().findUserGroupsByUnits(userB2BUnitList);
		final List<B2BUserGroupData> usergroupDatalist = new ArrayList<>();
		for (final B2BUserGroupModel usergroup : usergroups)
		{
			final B2BUserGroupData usergroupData = new B2BUserGroupData();
			getB2BUserGroupConverter().convert(usergroup, usergroupData);
			usergroupDatalist.add(usergroupData);
		}

		datalist.setUsergroups(usergroupDatalist);

		return datalist;

	}


	public GPDefaultB2BUserGroupService getGpb2buserGroupService()
	{
		return gpb2buserGroupService;
	}


	public void setGpb2buserGroupService(final GPDefaultB2BUserGroupService gpb2buserGroupService)
	{
		this.gpb2buserGroupService = gpb2buserGroupService;
	}


	public Converter<B2BUserGroupModel, B2BUserGroupData> getB2BUserGroupConverter()
	{
		return b2BUserGroupConverter;
	}


	public void setB2BUserGroupConverter(final Converter<B2BUserGroupModel, B2BUserGroupData> b2bUserGroupConverter)
	{
		b2BUserGroupConverter = b2bUserGroupConverter;
	}

	@Override
	public void addUserGroupsToUser(final String uid, final List<String> codes)
	{
		getGpb2buserGroupService().addUserGroupsToUser(uid, codes);
	}

	@Override
	public void addUsersToUserGroup(final String uid, final List<String> codes)
	{
		getGpb2buserGroupService().addUsersToUserGroup(uid, codes);
	}


	/**
	 * Update customer model {@link de.hybris.platform.b2b.model.B2BCustomerModel}.
	 *
	 * @param customer
	 *           The Customer Data {@link CustomerData}
	 */
	@Override
	public void inviteUser(final String invitedUser, final CustomerData currentUser)
	{
		final GPEmailItemEvent item = new GPEmailItemEvent();
		searchRestrictionService.disableSearchRestrictions();
		final UserModel invited = userService.getUserForUID(invitedUser);
		searchRestrictionService.enableSearchRestrictions();
		if(invited instanceof B2BCustomerModel){
			final B2BCustomerModel invCustomer = (B2BCustomerModel)invited;
			item.setInvitedCustomer(invCustomer);
			final List<String> toEmails = new ArrayList<>();
			toEmails.add(invCustomer.getEmail());
			item.setToEmails(toEmails);
		}
		item.setEmailSubject(EmailSubjectType.INVITE_USER.getSubject());
		item.setCustomer((CustomerModel) userService.getCurrentUser());
		eventService.publishEvent(initializeEvent(item, (CustomerModel) userService.getCurrentUser()));
	}

	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
	{
		event.setBaseStore(customerModel.getSite().getStores().get(0));
		event.setSite(customerModel.getSite());
		event.setCustomer(customerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}

	@Override
	public void updateUserGroup(final String userGroupUid, final B2BUserGroupData userGroupData, final Boolean isCreate) throws GPCommerceBusinessException
	{
		getGpb2buserGroupService().updateUserGroup(userGroupUid, userGroupData, isCreate);
	}


	@Override
	public CustomerData getB2BCustomerForUid(String uid) {
		B2BCustomerModel customer= userService.getUserForUID(uid, B2BCustomerModel.class);
		CustomerData customerdata= customerConverter.convert(customer);
		if(null!= customer.getUserApprovalStatus()) {
		customerdata.setUserApprovalStatus(customer.getUserApprovalStatus().getCode()); 
		}
		return customerdata;
	}


	public Converter<B2BCustomerModel, CustomerData> getB2BUserConverter() {
		return b2BUserConverter;
	}


	public void setB2BUserConverter(Converter<B2BCustomerModel, CustomerData> b2bUserConverter) {
		b2BUserConverter = b2bUserConverter;
	}
	
	

}
