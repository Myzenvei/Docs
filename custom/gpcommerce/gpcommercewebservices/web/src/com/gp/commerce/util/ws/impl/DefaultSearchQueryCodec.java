/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.util.ws.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import com.gp.commerce.util.ws.SearchQueryCodec;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 */
public class DefaultSearchQueryCodec implements SearchQueryCodec<SolrSearchQueryData>
{

	private static final Logger LOG = Logger.getLogger(DefaultSearchQueryCodec.class);
	@Override
	public SolrSearchQueryData decodeQuery(final String queryString)
	{
		final SolrSearchQueryData searchQuery = new SolrSearchQueryData();
		final List<SolrSearchQueryTermData> filters = new ArrayList<SolrSearchQueryTermData>();

		if (queryString == null)
		{
			return searchQuery;
		}

		final String[] parts = queryString.split(":");

		if (parts.length > 0)
		{
			String searchTerm = parts[0];

			if (StringUtils.isNotEmpty(searchTerm))
			{
				try
				{
					searchTerm = URLDecoder.decode(searchTerm, "UTF-8");
				}
				catch (final UnsupportedEncodingException e)
				{
					// UTF-8 is supported encoding, so it shouldn't come here
					LOG.error("Solr search query URLdecoding failed.", e);
				}
			}

			searchQuery.setFreeTextSearch(searchTerm);

			if (parts.length > 1)
			{
				searchQuery.setSort(parts[1]);
			}
		}

		for (int i = 2; i < parts.length; i = i + 2)
		{
			final SolrSearchQueryTermData term = new SolrSearchQueryTermData();
			term.setKey(parts[i]);
			term.setValue(parts[i + 1]);
			try
			{
				term.setValue(URLDecoder.decode(parts[i + 1], "UTF-8"));
			}
			catch (final UnsupportedEncodingException e)
			{
				// UTF-8 is supported encoding, so it shouldn't come here
				LOG.error("Solr search query URLdecoding failed.", e);
			}

			filters.add(term);
		}
		searchQuery.setFilterTerms(filters);

		return searchQuery;
	}

	@Override
	public String encodeQuery(final SolrSearchQueryData searchQueryData)
	{
		if (searchQueryData == null)
		{
			return null;
		}

		final StringBuilder builder = new StringBuilder();
		builder.append((searchQueryData.getFreeTextSearch() == null) ? "" : searchQueryData.getFreeTextSearch());


		if (searchQueryData.getSort() != null
				|| (searchQueryData.getFilterTerms() != null && !searchQueryData.getFilterTerms().isEmpty()))
		{
			builder.append(":");
			builder.append((searchQueryData.getSort() == null) ? "" : searchQueryData.getSort());
		}

		final List<SolrSearchQueryTermData> terms = searchQueryData.getFilterTerms();
		if (terms != null && !terms.isEmpty())
		{
			for (final SolrSearchQueryTermData term : searchQueryData.getFilterTerms())
			{
				builder.append(":");
				builder.append(term.getKey());
				builder.append(":");
				try
				{
					builder.append(URLEncoder.encode(term.getValue(), "UTF-8"));
				}
				catch (final UnsupportedEncodingException e)
				{
					// UTF-8 is supported encoding, so it shouldn't come here
					LOG.error("Solr search query URLencoding failed.", e);
				}
			}
		}

		//URLEncode?
		return builder.toString();
	}
}
