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
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

public class GPFacetSearchQueryCollapseFilterPopulator implements Populator<SearchQueryConverterData, SolrQuery>{

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	private static final String COMMA =",";
	
	@Override
	public void populate(SearchQueryConverterData source, SolrQuery target) {
		String indexedTypes = configurationService.getConfiguration().getString("gp.indexed.type");
		final List<String> indexedTypesList = Arrays.asList(indexedTypes.split(COMMA));
			
			if(CollectionUtils.isNotEmpty(indexedTypesList) &&  indexedTypesList.contains(source.getFacetSearchContext().getIndexedType().getCode())){
				StringBuilder rawQuery = new StringBuilder();
				rawQuery.append("{!collapse field").append("=code_string sort='priceValue_usd_double asc'}");
				target.addFilterQuery(new String[]{rawQuery.toString()});
			}
	}
}
