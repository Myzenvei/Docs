/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.service;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

/**
 * This is an interface for content search.
 *
 * @param <STATE> the generic type
 * @param <ITEM> the generic type
 * @param <RESULT> the generic type
 */
public interface GPContentSearchService<STATE, ITEM, RESULT extends FacetSearchPageData<STATE, ITEM>>
{

	/**
	 * Initiate a new search using simple free text query.
	 *
	 * @param text
	 *           the search text
	 * @param pageableData
	 *           the page to return, can be null to use defaults
	 * @return the search results
	 */
	RESULT textSearch(String text, PageableData pageableData);

	/**
	 * Initiate a new search using simple free text query in a search query context.
	 *
	 * @param text
	 *           the search text
	 * @param searchQueryContext
	 *           search query context
	 * @param pageableData
	 *           the page to return, can be null to use defaults
	 * @return the search results
	 */
	RESULT textSearch(String text, SearchQueryContext searchQueryContext, PageableData pageableData);

	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchQueryData
	 * must have been obtained from the results of a call to either {@link #textSearch(String,PageableData)} or
	 * {@link #categorySearch(String,PageableData)}.
	 *
	 * @param searchQueryData
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	RESULT searchAgain(STATE searchQueryData, PageableData pageableData);


}
