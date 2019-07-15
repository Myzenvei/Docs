/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.subscription.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPSubscriptionCartModel;
import com.gp.commerce.subscription.dao.GPSubscriptionDao;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class GPSubscriptionDaoImpl implements GPSubscriptionDao {

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	
	private static final String GET_SUBSCRIPTION_CART="SELECT {pk}  from {GPSubscriptionCart} where {code} = ?code";
	private static final String RETRIEVE_ACTIVE_SUBSCRIPTIONS = "select {pk} from {GPSubscriptionCart} where {user} = ?currentUser AND {isActive} = ?activeFlag ORDER BY {" + GPSubscriptionCartModel.SUBSCRIPTIONCARTSTATUS + "} ASC, {" + GPSubscriptionCartModel.CREATIONTIME + "} DESC ";
	private static final String SUBSCRIPTION_CART_QUERY = "select {pk} from {GPSubscriptionCart as c join SubscriptionCartStatusEnum as e on {c.subscriptionCartStatus}={e.pk} } where {c.isActive} = ?isActive and {e.code}=?subscriptionCartStatus" ;
	private static final String RETRIEVE_INACTIVE_SUBSCRIPTIONS="select {pk} from {GPSubscriptionCart as c LEFT JOIN SubscriptionCartStatusEnum as e on {c.subscriptionCartStatus}={e.pk} } where {c.isActive} = ?isActive and ({e.code}!=?subscriptionCartStatus or {e.code} is NULL) AND {user} = ?currentUser " ;
	@Override
	public List<GPSubscriptionCartModel> cancelSubscription(String code) { 
		final Map<String, Object> params = new HashMap<>();
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_SUBSCRIPTION_CART);
		params.put("code", code);
		fQuery.addQueryParameters(params);
		final SearchResult<GPSubscriptionCartModel> searchResult  = flexibleSearchService.search(fQuery) ;
		return searchResult.getResult();
		
	}
	@Override
	public List<GPSubscriptionCartModel> getActiveSubscriptions(UserModel currentUser)
	{			
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_ACTIVE_SUBSCRIPTIONS);
		final Map<String, Object> params = new HashMap<>();
		params.put("currentUser", currentUser);
		params.put("activeFlag", Boolean.TRUE);
		searchQuery.addQueryParameters(params);
		final SearchResult<GPSubscriptionCartModel> searchResult = getFlexibleSearchService().search(searchQuery);
		if (searchResult.getResult().isEmpty()) {
			return Collections.emptyList();
		}
		return searchResult.getResult();
		
	}
	
	
	@Override
	public List<GPSubscriptionCartModel> getInActiveSubscriptions(UserModel currentUser)
	{			
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RETRIEVE_INACTIVE_SUBSCRIPTIONS);
		final Map<String, Object> params = new HashMap<>();
		params.put("currentUser", currentUser);
		params.put("isActive", Boolean.FALSE);
		params.put("subscriptionCartStatus", "CANCELLED");
		searchQuery.addQueryParameters(params);
		final SearchResult<GPSubscriptionCartModel> searchResult = getFlexibleSearchService().search(searchQuery);
		if (searchResult.getResult().isEmpty()) {
			return Collections.emptyList();
		}
		return searchResult.getResult();
		
	}

	@Override
	public List<GPSubscriptionCartModel> getSubscriptionCartModels(String subscriptionCartStatus, boolean isActive) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(SUBSCRIPTION_CART_QUERY);
		query.addQueryParameter("isActive",isActive);
		query.addQueryParameter("subscriptionCartStatus", subscriptionCartStatus);
		final SearchResult<GPSubscriptionCartModel> searchResult = flexibleSearchService.search(query);
		return searchResult.getResult();
	}
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}
