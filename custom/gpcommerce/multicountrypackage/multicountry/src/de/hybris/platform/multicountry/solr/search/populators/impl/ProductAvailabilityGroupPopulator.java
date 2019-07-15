/**
 * 
 */
package de.hybris.platform.multicountry.solr.search.populators.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author pawan.shrivastava
 * 
 */
public class ProductAvailabilityGroupPopulator implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest>
{
	/**
	 * 
	 */
	private static final String AVAILABILITY = "availability";
	private MulticountryRestrictionService multicountryRestrictionService;

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source, final SolrSearchRequest target)
			throws ConversionException
	{
		final Collection<ProductAvailabilityGroupModel> availabilityGroups = multicountryRestrictionService
				.getCurrentProductAvailabilityGroup();
		final SearchQuery targetSolrSearchQuery = (SearchQuery) target.getSearchQuery();

		if (!availabilityGroups.isEmpty())
		{
			for (final ProductAvailabilityGroupModel group : availabilityGroups)
			{
				targetSolrSearchQuery.addFacetValue(AVAILABILITY, group.getId());
			}
		}

	}

	/**
	 * @param multicountryRestrictionService
	 *           the multicountryRestrictionService to set
	 */
	@Required
	public void setMulticountryRestrictionService(final MulticountryRestrictionService multicountryRestrictionService)
	{
		this.multicountryRestrictionService = multicountryRestrictionService;
	}
}
