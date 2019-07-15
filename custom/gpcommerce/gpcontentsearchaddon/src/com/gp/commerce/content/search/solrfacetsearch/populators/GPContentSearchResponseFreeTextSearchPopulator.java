/**
 *
 */
package com.gp.commerce.content.search.solrfacetsearch.populators;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;

import com.gp.commerce.content.search.facetdata.GPContentSearchPageData;

/**
 */
public class GPContentSearchResponseFreeTextSearchPopulator<STATE, ITEM> implements Populator<SolrSearchResponse, GPContentSearchPageData<STATE, ITEM>>
{
	@Override
	public void populate(final SolrSearchResponse source, final GPContentSearchPageData<STATE, ITEM> target)
	{
		target.setTextSearch(source.getRequest().getSearchText());
	}
}
