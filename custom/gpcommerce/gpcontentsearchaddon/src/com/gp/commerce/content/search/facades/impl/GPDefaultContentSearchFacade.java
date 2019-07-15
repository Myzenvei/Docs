/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.content.search.facades.impl;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.gp.commerce.content.search.facades.GPContentSearchFacade;
import com.gp.commerce.content.search.facades.data.GPContentPageData;
import com.gp.commerce.content.search.facetdata.GPContentSearchPageData;
import com.gp.commerce.content.search.service.GPContentSearchService;

/**
 * This class is used for content search based on the search text, {@link SearchQueryContext} and {@link PageableData}.
 *
 * @param <ITEM> the generic type
 */
public class GPDefaultContentSearchFacade<ITEM extends GPContentPageData> implements GPContentSearchFacade<ITEM> {


		private GPContentSearchService<SolrSearchQueryData, SearchResultValueData, GPContentSearchPageData<SolrSearchQueryData, SearchResultValueData>> contentSearchService;
		private Converter<GPContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, GPContentSearchPageData<SearchStateData, ITEM>> gpContentSearchPageConverter;
		private Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder;
		private ThreadContextService threadContextService;

		protected GPContentSearchService<SolrSearchQueryData, SearchResultValueData, GPContentSearchPageData<SolrSearchQueryData, SearchResultValueData>> getContentSearchService()
		{
			return contentSearchService;
		}

		@Required
		public void setContentSearchService(
			final GPContentSearchService<SolrSearchQueryData, SearchResultValueData, GPContentSearchPageData<SolrSearchQueryData, SearchResultValueData>> contentSearchService)
		{
			this.contentSearchService = contentSearchService;
		}

		protected Converter<GPContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, GPContentSearchPageData<SearchStateData, ITEM>> getGpContentSearchPageConverter()
		{
			return gpContentSearchPageConverter;
		}

		@Required
		public void setGpContentSearchPageConverter(
				final Converter<GPContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, GPContentSearchPageData<SearchStateData, ITEM>> gpContentSearchPageConverter)
		{
			this.gpContentSearchPageConverter = gpContentSearchPageConverter;
		}

		protected Converter<SearchQueryData, SolrSearchQueryData> getSearchQueryDecoder()
		{
			return searchQueryDecoder;
		}

		@Required
		public void setSearchQueryDecoder(final Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder)
		{
			this.searchQueryDecoder = searchQueryDecoder;
		}

		protected ThreadContextService getThreadContextService()
		{
			return threadContextService;
		}

		@Required
		public void setThreadContextService(final ThreadContextService threadContextService)
		{
			this.threadContextService = threadContextService;
		}

		/**
		 * Returns the Content search data based on the text provided
		 * 
		 * @param text               the text used
		 * @return {@link GPContentSearchPageData}
		 */
		@Override
		public GPContentSearchPageData<SearchStateData, ITEM> contentSearch(final String text)
		{
			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<GPContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
					{
						@Override
						public GPContentSearchPageData<SearchStateData, ITEM> execute()
						{
							return getGpContentSearchPageConverter().convert(getContentSearchService().textSearch(text, null,
									null));
						}
					});
		}
		
		/**
		 * Returns the Content search data based on the text and searchQueryContext data provided
		 * 
		 * @param text               the text used
		 * @param searchQueryContext the {@link SearchQueryContext} used
		 * @return {@link GPContentSearchPageData}
		 */
		@Override
		public GPContentSearchPageData<SearchStateData, ITEM> contentSearch(final String text, final SearchQueryContext searchQueryContext)
		{
			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<GPContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
					{
						@Override
						public GPContentSearchPageData<SearchStateData, ITEM> execute()
						{
							return getGpContentSearchPageConverter()
									.convert(getContentSearchService().textSearch(text, searchQueryContext, null));
						}
					});
		}


		/**
		 * Returns the Content search data based on the searchState and pageabale data provided
		 * 
		 * @param searchState  the {@link SearchStateData} used
		 * @param pageableData the {@link PageableData} used
		 * @return {@link GPContentSearchPageData}
		 */
		@Override
		public GPContentSearchPageData<SearchStateData, ITEM> contentSearch(final SearchStateData searchState,
				final PageableData pageableData)
		{
			Assert.notNull(searchState, "SearchStateData must not be null.");

			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<GPContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
					{
						@Override
						public GPContentSearchPageData<SearchStateData, ITEM> execute()
						{
							return getGpContentSearchPageConverter()
									.convert(getContentSearchService().searchAgain(decodeState(searchState, null), pageableData));
						}
					});
		}


	/**
	 * Decodes the state and sets the category code based on the searchState and
	 * categorycode provided
	 * 
	 * @param searchState  the {@link SearchStateData}
	 * @param categoryCode the categorycode used
	 * @return {@link SolrSearchQueryData}
	 */
		protected SolrSearchQueryData decodeState(final SearchStateData searchState, final String categoryCode)
		{
			final SolrSearchQueryData searchQueryData = getSearchQueryDecoder().convert(searchState.getQuery());
			if (categoryCode != null)
			{
				searchQueryData.setCategoryCode(categoryCode);
			}

			return searchQueryData;
		}
	}



