/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.sso.service.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.sso.DefaultGPSSOService;

/**
 * The Class CustomSSOService.
 */
public class CustomSSOService extends DefaultGPSSOService {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(CustomSSOService.class);
	
	/** The Constant B2BUNITL1. */
	private static final String B2BUNITL1 = "L1";

	/**
	 * Gets the or create SSO user.
	 *
	 * @param id the id
	 * @param name the name
	 * @param roles the roles
	 * @param soldTo the sold to
	 * @param email the email
	 * @param approvalSampleStatus the approval sample status
	 * @return the or create SSO user
	 */
	@Override
	public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles, final String soldTo,
			final String email, final Boolean approvalSampleStatus)
	{
		LOG.info("In getOrCreateSSOUser ----------------" + id + " Name" + name);

		if(CollectionUtils.isNotEmpty(roles) && roles.contains("backofficeuser"))
		{
			return super.getOrCreateSSOUser(id.toLowerCase(), name, roles, soldTo, email, approvalSampleStatus);
		}

		final UserModel user = super.getOrCreateSSOUser(id.toLowerCase(), name, roles, soldTo, email, approvalSampleStatus);
		final B2BUnitModel unit = getUnitForUid(soldTo);
		if (null != unit)
		{
			((B2BCustomerModel) user).setDefaultB2BUnit(unit);
		}
		else		
			
		{
			((B2BCustomerModel) user).setDefaultB2BUnit(createB2bUnit(soldTo));
		}

		((B2BCustomerModel) user).setEmail(StringUtils.isNotBlank(email) ? email.toLowerCase() : email);
		((B2BCustomerModel) user).setApprovedSampleStatus(approvalSampleStatus);
		getModelService().save(user);
		return user;
	}

	/**
	 * Creates the B 2 b unit.
	 *
	 * @param soldTo the sold to
	 * @return the b 2 B unit model
	 */
	private B2BUnitModel createB2bUnit(final String soldTo)
	{
		LOG.info("Create unit | gpxpress :" + soldTo);
		final B2BUnitModel b2bUnit = getModelService().create(B2BUnitModel.class);
		b2bUnit.setUid(soldTo);
		b2bUnit.setName(soldTo);
		b2bUnit.setLocName(soldTo);
		b2bUnit.setB2bUnitLevel(B2BUNITL1);
		getModelService().save(b2bUnit);
		return b2bUnit;
	}

	/**
	 * Gets the unit for uid.
	 *
	 * @param uid the uid
	 * @return the unit for uid
	 */
	private B2BUnitModel getUnitForUid(final String uid)
	{
		B2BUnitModel unit;
		try
		{
			unit = getUserService().getUserGroupForUID(uid, B2BUnitModel.class);
		}
		catch (final UnknownIdentifierException | ClassMismatchException e)
		{
			LOG.error("Unit not present | gpxpress :" + uid,e);
			unit = null;
		}
		return unit;
	}





}
