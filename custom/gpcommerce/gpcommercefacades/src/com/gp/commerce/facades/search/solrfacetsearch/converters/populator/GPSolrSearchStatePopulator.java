/**
 *
 */
package com.gp.commerce.facades.search.solrfacetsearch.converters.populator;

import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.search.solrfacetsearch.converters.populator.SolrSearchStatePopulator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author deepbhat
 *
 */
public class GPSolrSearchStatePopulator extends SolrSearchStatePopulator {
	private static final Logger LOG = Logger.getLogger(GPSolrSearchStatePopulator.class);

	@Override
	protected String buildUrlQueryString(final SolrSearchQueryData source, final SearchStateData target)
	{
		final String searchQueryParam = target.getQuery().getValue();
		if (StringUtils.isNotBlank(searchQueryParam))
		{
			try
			{
				//DB - Changed to query from q
				return "?query=" + URLEncoder.encode(searchQueryParam, "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error("Unsupported encoding (UTF-8). Fallback to html escaping.", e);
				return "?query=" + StringEscapeUtils.escapeHtml(searchQueryParam);
			}
		}
		return StringUtils.EMPTY;
	}

}
