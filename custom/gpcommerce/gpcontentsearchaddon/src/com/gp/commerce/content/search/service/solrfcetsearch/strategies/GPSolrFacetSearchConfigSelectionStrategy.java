/**
 *
 */
package com.gp.commerce.content.search.service.solrfcetsearch.strategies;

import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

/**
* Resolves suitable {@link SolrFacetSearchConfigModel} that should be used for searching in the current session
* context.<br>
* 
*/
public interface GPSolrFacetSearchConfigSelectionStrategy
{

	SolrFacetSearchConfigModel getContentSolrFacetSearchConfig() throws NoValidSolrConfigException;
}
