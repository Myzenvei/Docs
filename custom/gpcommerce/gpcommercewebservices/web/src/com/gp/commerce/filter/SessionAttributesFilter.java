/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.filter;

import de.hybris.platform.contextualattributevalues.model.ContextualAttributesContextModel;
import de.hybris.platform.contextualattributevalues.services.ContextualAttributeValuesSessionService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import com.gp.commerce.context.ContextInformationLoader;

import de.hybris.platform.multicountry.enums.TimezoneEnum;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;
import de.hybris.platform.multicountry.util.TimeZoneHelper;

import java.io.IOException;
import java.util.Collection;
import java.util.TimeZone;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter sets session context basing on request parameters:<br>
 * <ul>
 * <li><b>lang</b> - set current {@link LanguageModel}</li>
 * <li><b>curr</b> - set current {@link CurrencyModel}</li>
 * </ul>
 * 
 *
 * 
 */
public class SessionAttributesFilter extends OncePerRequestFilter
{
	private ContextInformationLoader contextInformationLoader;
	private BaseStoreService baseStoreService;
	private SessionService sessionService;
	private MulticountryRestrictionService multicountryRestrictionService;
	private TimeService timeService;
	private ContextualAttributeValuesSessionService contextualAttributeValuesSessionService;	

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		

		
		setUserTaxGroupAttribute();
		getContextInformationLoader().setLanguageFromRequest(request);
		getContextInformationLoader().setCurrencyFromRequest(request);
		
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		if (currentBaseStore != null) {
			initializeSessionProductAssignment(currentBaseStore);
			initializeSessionPriceGroup(currentBaseStore);
			
			// set the timezone and timezone offset for this session
			initializeTimezone(currentBaseStore);
			initializeContextualContext(currentBaseStore);			
		}

		filterChain.doFilter(request, response);
	}
	
	protected void initializeSessionProductAssignment(final BaseStoreModel currentBaseStore) 
	{
		// Get and save the availability groups for the current base store
		final Collection<ProductAvailabilityGroupModel> availabilityGroups = currentBaseStore.getAvailabilityGroups();
		getMulticountryRestrictionService().setCurrentProductAvailabilityGroups(availabilityGroups);
	}
	
	protected void initializeSessionPriceGroup(final BaseStoreModel currentBaseStore)
	{
		//Get the price group assigned to the current base store...
		final UserPriceGroup priceGroup = currentBaseStore.getUserPriceGroup();
		if (priceGroup == null)
		{
			return; //no price group?? error?
		}

		//... set price group into the session.
		getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, priceGroup);
	}
	
	protected void initializeTimezone(final BaseStoreModel currentBaseStore)
	{
		if (currentBaseStore.getTimezone() != null)
		{
			final TimezoneEnum storeTimeZone = currentBaseStore.getTimezone();
			final TimeZone timeZone = TimeZone.getTimeZone(storeTimeZone.getCode());

			getSessionService().getCurrentSession().setAttribute("timezone", timeZone);

			final int timeZoneOffsetDifferential = TimeZoneHelper.getTimeZoneOffsetDifferential(storeTimeZone.getCode());
			getTimeService().setTimeOffset(timeZoneOffsetDifferential);
		}
	}
	
	protected void initializeContextualContext(final BaseStoreModel currentBaseStore)
	{
		// Get and save the contextual context for the current base store
		final ContextualAttributesContextModel currentContext = currentBaseStore.getContextualAttributesContext();
		getContextualAttributeValuesSessionService().setCurrentContext(currentContext);
	}

	protected void setUserTaxGroupAttribute()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		if (currentBaseStore != null)
		{
			final UserTaxGroup taxGroup = currentBaseStore.getTaxGroup();
			if (taxGroup != null)
			{
				getSessionService().setAttribute(Europe1Constants.PARAMS.UTG, taxGroup);
			}
		}
	}

	protected ContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

	@Required
	public void setContextInformationLoader(final ContextInformationLoader contextInformationLoader)
	{
		this.contextInformationLoader = contextInformationLoader;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public MulticountryRestrictionService getMulticountryRestrictionService() {
		return multicountryRestrictionService;
	}

	@Required
	public void setMulticountryRestrictionService(MulticountryRestrictionService multicountryRestrictionService) {
		this.multicountryRestrictionService = multicountryRestrictionService;
	}
	
	public TimeService getTimeService() {
		return timeService;
	}

	@Required
	public void setTimeService(TimeService timeService) {
		this.timeService = timeService;
	}

	public ContextualAttributeValuesSessionService getContextualAttributeValuesSessionService() {
		return contextualAttributeValuesSessionService;
	}

	@Required
	public void setContextualAttributeValuesSessionService(
			ContextualAttributeValuesSessionService contextualAttributeValuesSessionService) {
		this.contextualAttributeValuesSessionService = contextualAttributeValuesSessionService;
	}
}
