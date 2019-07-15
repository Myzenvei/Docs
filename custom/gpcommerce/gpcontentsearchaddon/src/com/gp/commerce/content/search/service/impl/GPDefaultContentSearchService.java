/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;

import com.gp.commerce.content.search.facetdata.GPContentSearchPageData;
import com.gp.commerce.content.search.service.GPContentSearchService;

/**
 * This class provides services to process content search.
 *
 * @param <ITEM> the generic type
 */
public class GPDefaultContentSearchService<ITEM> implements
		GPContentSearchService<SolrSearchQueryData, ITEM, GPContentSearchPageData<SolrSearchQueryData, ITEM>>
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GPDefaultContentSearchService.class);

	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter;
	private Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter;
	private Converter<SolrSearchResponse, GPContentSearchPageData<SolrSearchQueryData, ITEM>> searchResponseConverter;

	protected Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getSearchQueryPageableConverter()
	{
		return searchQueryPageableConverter;
	}

	@Required
	public void setSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter)
	{
		this.searchQueryPageableConverter = searchQueryPageableConverter;
	}

	protected Converter<SolrSearchRequest, SolrSearchResponse> getSearchRequestConverter()
	{
		return searchRequestConverter;
	}

	@Required
	public void setSearchRequestConverter(final Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter)
	{
		this.searchRequestConverter = searchRequestConverter;
	}

	protected Converter<SolrSearchResponse, GPContentSearchPageData<SolrSearchQueryData, ITEM>> getSearchResponseConverter()
	{
		return searchResponseConverter;
	}

	@Required
	public void setSearchResponseConverter(
			final Converter<SolrSearchResponse, GPContentSearchPageData<SolrSearchQueryData, ITEM>> searchResponseConverter)
	{
		this.searchResponseConverter = searchResponseConverter;
	}

	// End spring inject methods

	@Override
	public GPContentSearchPageData<SolrSearchQueryData, ITEM> textSearch(final String text,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public GPContentSearchPageData<SolrSearchQueryData, ITEM> textSearch(final String text,
			final SearchQueryContext searchQueryContext, final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());
		searchQueryData.setSearchQueryContext(searchQueryContext);

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public GPContentSearchPageData<SolrSearchQueryData, ITEM> searchAgain(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return doSearch(searchQueryData, pageableData);
	}

	protected GPContentSearchPageData<SolrSearchQueryData, ITEM> doSearch(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");

		// Create the SearchQueryPageableData that contains our parameters
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
				pageableData);

		// Build up the search request
		final SolrSearchRequest solrSearchRequest = getSearchQueryPageableConverter().convert(searchQueryPageableData);

		// Execute the search
		final SolrSearchResponse solrSearchResponse = getSearchRequestConverter().convert(solrSearchRequest);

		// Convert the response
		return getSearchResponseConverter().convert(solrSearchResponse);
	}

	protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}

	// Create methods for data object - can be overridden in spring config

	protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData<SolrSearchQueryData>();
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}


}
