/**
 *
 */
package de.hybris.commercesearch.searchandizing.searchprofile.dao;

import de.hybris.platform.commercesearch.model.CategorySolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.dao.SearchProfileDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.List;


/**
 * @author Miguel Uhagon
 *
 */
public interface MulticountrySearchProfileDao extends SearchProfileDao
{
	List<CategorySolrSearchProfileModel> getSolrFacetReconfigSearchProfiles(
			Collection<String> categoryCodes, SolrIndexedTypeModel indexedType, final BaseStoreModel baseStore);

	List<CategorySolrSearchProfileModel> getSolrBoostSearchProfiles(
			Collection<String> categoryCodes, SolrIndexedTypeModel indexedType, final BaseStoreModel baseStore);

	List<GlobalSolrSearchProfileModel> findGlobalSolrSearchProfiles(
			final SolrIndexedTypeModel indexedType, final BaseStoreModel baseStore);

	List<CategorySolrSearchProfileModel> findCategorySolrSearchProfiles(
			final SolrIndexedTypeModel indexedType, final String categoryCode, final BaseStoreModel baseStore);
}
