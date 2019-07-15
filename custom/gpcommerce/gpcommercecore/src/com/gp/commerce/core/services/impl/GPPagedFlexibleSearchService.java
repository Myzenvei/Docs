/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.search.flexiblesearch.impl.DefaultPagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * This class is used for processing GP paged flexible search services
 */
@SuppressWarnings("deprecation")
public class GPPagedFlexibleSearchService extends DefaultPagedFlexibleSearchService{

	@Override
	public <T> SearchPageData<T> search(final FlexibleSearchQuery searchQuery, final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("searchQuery", searchQuery);
		validatePagableParameters(pageableData);

		final SearchResult<T> searchResult = getFlexibleSearchService().search(searchQuery);
		final SearchPageData<T> result = createPagedSearchResult(searchResult, pageableData);
		return result;
	}
}