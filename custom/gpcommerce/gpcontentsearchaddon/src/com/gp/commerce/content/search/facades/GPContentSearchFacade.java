/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.facades;

import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import com.gp.commerce.content.search.facades.data.GPContentPageData;
import com.gp.commerce.content.search.facetdata.GPContentSearchPageData;


/**
 * Content search facade interface.
 * Used to retrieve content of type {@link GPContentPageData} (or subclasses of).
 *
 * @param <ITEM> The type of the Content result items
 */

public interface GPContentSearchFacade<ITEM extends GPContentPageData> {


	/**
	 * Initiate a new search using simple free text query.
	 *
	 * @param text the search text
	 * @return the search results
	 */
	public GPContentSearchPageData<SearchStateData, ITEM> contentSearch(String text);

	/**
	 * Initiate a new search using simple free text query in a search query context.
	 *
	 * @param text
	 *           the search text
	 * @param searchQueryContext
	 *           search query context
	 * @return the search results
	 */
	public GPContentSearchPageData<SearchStateData, ITEM> contentSearch(final String text,
			final SearchQueryContext searchQueryContext);

	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchStateData
	 * must have been obtained from the results of a call to {@link #textSearch(String)}.
	 *
	 * @param searchState  the search query object
	 * @param pageableData the page to return
	 * @return the search results
	 */
	public GPContentSearchPageData<SearchStateData, ITEM> contentSearch(final SearchStateData searchState,
			final PageableData pageableData);

}
