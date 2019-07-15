package de.hybris.commercesearch.searchandizing.searchprofile.dao.impl;

import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.commercesearch.model.CategorySolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.dao.impl.DefaultSearchProfileDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultMulticountrySearchProfileDao extends DefaultSearchProfileDao implements MulticountrySearchProfileDao
{
	@Override
	public List<CategorySolrSearchProfileModel> getSolrFacetReconfigSearchProfiles(final Collection<String> categoryCodes,
			final SolrIndexedTypeModel indexedType, final BaseStoreModel baseStore)
	{
		final String query = "SELECT {srchProfile.pk} "
				+ "FROM {CategorySolrSearchProfile AS srchProfile} "
				+ "WHERE {srchProfile.pk} in ( {{ SELECT {target} from {SolrFacetReconfiguration2SolrSearchProfile} }} ) AND {srchProfile.categoryCode} in (?categoryPath)"
				+ " AND {srchProfile.indexedType} = ?indexedType"
				+ " AND {srchProfile." + CategorySolrSearchProfileModel.BASESTORE
				+ "} = ?" + CategorySolrSearchProfileModel.BASESTORE;

		final Map<String, Object> params = createParamsMap(categoryCodes, indexedType, baseStore);
		return  getFlexibleSearchService().<CategorySolrSearchProfileModel> search(query, params).getResult();
	}

	@Override
	public List<CategorySolrSearchProfileModel> getSolrBoostSearchProfiles(final Collection<String> categoryCodes,
			final SolrIndexedTypeModel indexedType, final BaseStoreModel baseStore)
	{
		final String query = "SELECT {srchProfile.pk} "
				+ "FROM {CategorySolrSearchProfile AS srchProfile} "
				+ "WHERE {srchProfile.pk} in ( {{ SELECT {target} from {SolrBoostRule2SolrSearchProfile} }} ) AND {srchProfile.categoryCode} in (?categoryPath)"
				+ " AND {srchProfile.indexedType} = ?indexedType"
				+ " AND {srchProfile." + CategorySolrSearchProfileModel.BASESTORE
				+ "} = ?" + CategorySolrSearchProfileModel.BASESTORE;

		final Map<String, Object> params = createParamsMap(categoryCodes, indexedType, baseStore);
		return  getFlexibleSearchService().<CategorySolrSearchProfileModel> search(query, params).getResult();
	}

	@Override
	public List<GlobalSolrSearchProfileModel> findGlobalSolrSearchProfiles(final SolrIndexedTypeModel indexedType,
			final BaseStoreModel baseStore)
	{
		final String query = "SELECT {profile.pk} "
				+ " FROM {GlobalSolrSearchProfile AS profile} "
				+ " WHERE {profile.indexedType} = ?indexedType"
				+ " AND {profile." + GlobalSolrSearchProfileModel.BASESTORE
				+ "} = ?" + GlobalSolrSearchProfileModel.BASESTORE;

		final Map<String, Object> params = new HashMap<>();
		params.put("indexedType", indexedType);
		params.put(GlobalSolrSearchProfileModel.BASESTORE, baseStore);
		return getFlexibleSearchService().<GlobalSolrSearchProfileModel> search(query, params).getResult();
	}

	@Override
	public List<CategorySolrSearchProfileModel> findCategorySolrSearchProfiles(final SolrIndexedTypeModel indexedType,
			final String categoryCode, final BaseStoreModel baseStore)
	{
		final String query = "SELECT {profile.pk} "
				+ " FROM {CategorySolrSearchProfile AS profile} "
				+ " WHERE {profile.indexedType} = ?indexedType"
				+ " AND {profile.categoryCode} = ?categoryCode"
				+ " AND {profile." + CategorySolrSearchProfileModel.BASESTORE
				+ "} = ?" + CategorySolrSearchProfileModel.BASESTORE;

		final Map<String, Object> params = new HashMap<>();
		params.put("indexedType", indexedType);
		params.put("categoryCode", categoryCode);
		params.put(CategorySolrSearchProfileModel.BASESTORE, baseStore);
		return getFlexibleSearchService().<CategorySolrSearchProfileModel> search(query, params).getResult();
	}

	private Map<String, Object> createParamsMap(Collection<String> categoryCodes, SolrIndexedTypeModel indexedType,
			BaseStoreModel baseStore)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("categoryPath", categoryCodes);
		params.put("indexedType", indexedType);
		params.put(CategorySolrSearchProfileModel.BASESTORE, baseStore);
		return params;
	}
}