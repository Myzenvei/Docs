/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.impl.populators;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;

import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

public class GPFacetSearchQueryMaterialStatusFilterPopulator implements Populator<SearchQueryConverterData, SolrQuery> {

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	private static final String COMMA =",";
	
	private static final String PRODUCT_AUTOSUGGEST_ENABLE = "autoSuggestEnable_string";
	
	@Override
	public void populate(SearchQueryConverterData source, SolrQuery target) throws ConversionException {
		
		String applicableSites = configurationService.getConfiguration().getString("gp.exclude.obselete.sites");
		List<QueryField> queryFields = source.getSearchQuery().getFilterQueries();
		boolean autoSuggestFlow = false;
		
		if(queryFields !=null && CollectionUtils.isNotEmpty(queryFields))
		{
			for(QueryField fq : queryFields)
			{
				if(fq.getField().equalsIgnoreCase(PRODUCT_AUTOSUGGEST_ENABLE))
				{
					autoSuggestFlow = true;
					break;
				}
			}
		}
		
		final List<String> applicableSitesList = Arrays.asList(applicableSites.split(COMMA));
		if(autoSuggestFlow || (applicableSitesList !=null && CollectionUtils.isNotEmpty(applicableSitesList) 
				&&  cmsSiteService.getCurrentSite() != null && applicableSitesList.contains(cmsSiteService.getCurrentSite().getUid()))){
			StringBuilder rawQuery = new StringBuilder();
			rawQuery.append('(');
			rawQuery.append("!");
			rawQuery.append('(');
			rawQuery.append("materialStatus_string").append(":\"").append("OBSOLETE").append('"');
			rawQuery.append(" AND ");
			rawQuery.append("stockLevelStatus_string").append(":\"")
					.append("outOfStock").append('"');
			rawQuery.append(')');
			rawQuery.append(')');
	
			target.addFilterQuery(new String[] { rawQuery.toString() });
		}
		
		
	}

}
