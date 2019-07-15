/**
 *
 */
package com.hybris.multicountry.solrsearch;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearchsolr.strategies.impl.SolrAsSearchProvider;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
/**
 * @author i304602
 *
 */
public class MultiCountrySolrAsSearchProvider extends SolrAsSearchProvider
{
	private static final String AVAILABILITY = "availability";

	private MulticountryRestrictionService multicountryRestrictionService;




	@Override
	protected SearchQuery convertSearchQuery(final AsSearchProfileContext context, final AsSearchQueryData searchQuery)
			throws FacetConfigServiceException, AsException
	{
		final SearchQuery query = super.convertSearchQuery(context, searchQuery);
		addAvailibityField(query);

		return query;
	}

	private void addAvailibityField(final SearchQuery query)
	{
		final Collection<ProductAvailabilityGroupModel> availabilities = multicountryRestrictionService
				.getCurrentProductAvailabilityGroup();
		if (CollectionUtils.isEmpty(availabilities))
		{
			return;
		}

		final Set<String> values = new HashSet<String>();
		for (final ProductAvailabilityGroupModel availability : availabilities)
		{
			values.add(availability.getId());
		}


		final FacetValueField facetValueField = new FacetValueField(AVAILABILITY, values);
		query.getFacetValues().add(facetValueField);
	}




	public void setMulticountryRestrictionService(final MulticountryRestrictionService multicountryRestrictionService)
	{
		this.multicountryRestrictionService = multicountryRestrictionService;
	}






}
