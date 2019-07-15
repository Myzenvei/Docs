/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.core.xpress.alert.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.commerce.core.model.GPXpressAlertProdHistoryModel;
import com.gp.commerce.core.xpress.alert.dao.GPProductAlertDao;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

public class GPProductAlertDaoImpl extends AbstractItemDao implements GPProductAlertDao{

	protected final static String SELECTCLAUSE = "SELECT {" + GPXpressAlertProdHistoryModel.PK + "} FROM {" + GPXpressAlertProdHistoryModel._TYPECODE + "} ";
	protected final static String FIND_EXPIRED_PRODUCT_ALERT_HISTORY = SELECTCLAUSE + "WHERE {" + GPXpressAlertProdHistoryModel.MODIFIEDTIME
			+ "} <= ?modifiedBefore ";

	
	@Override
	public List<GPXpressAlertProdHistoryModel> getGPProductAlertHistoryForDate(Date modifiedBefore) {
		
		final Map<String, Object> params = new HashMap<>();
		params.put("modifiedBefore", modifiedBefore);
		return doSearch(FIND_EXPIRED_PRODUCT_ALERT_HISTORY, params, GPXpressAlertProdHistoryModel.class);
	
		
	}
	
	/**
	 * Do search with count. For example, if set count to 1, only the first one of the result will be returned.
	 *
	 * @param <T>
	 *           the generic type
	 * @param query
	 *           the query
	 * @param params
	 *           the params
	 * @param resultClass
	 *           the result class
	 * @param count
	 *           the count
	 * @return the list
	 */
	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = createSearchQuery(query, params, resultClass);
		final SearchResult<T> searchResult = search(fQuery);
		return searchResult.getResult();
	}

	protected <T> FlexibleSearchQuery createSearchQuery(final String query, final Map<String, Object> params,
			final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}
		fQuery.setResultClassList(Collections.singletonList(resultClass));
		return fQuery;
	}

}
