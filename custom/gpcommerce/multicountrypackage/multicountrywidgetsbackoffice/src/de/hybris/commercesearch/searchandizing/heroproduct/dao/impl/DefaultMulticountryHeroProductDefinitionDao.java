/**
 *
 */
package de.hybris.commercesearch.searchandizing.heroproduct.dao.impl;

import de.hybris.commercesearch.searchandizing.heroproduct.dao.MulticountryHeroProductDefinitionDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.dao.impl.DefaultHeroProductDefinitionDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author AL
 *
 */
public class DefaultMulticountryHeroProductDefinitionDao extends DefaultHeroProductDefinitionDao implements
		MulticountryHeroProductDefinitionDao
{
	public DefaultMulticountryHeroProductDefinitionDao(final String typecode)
	{
		super(typecode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SolrHeroProductDefinitionModel> findSolrHeroProductDefinitionsByCategory(final CategoryModel category,
			final BaseStoreModel baseStore)
	{
		final String query = "SELECT {hero." + SolrHeroProductDefinitionModel.PK + "} " + "FROM {"
				+ SolrHeroProductDefinitionModel._TYPECODE + " AS hero} " + "WHERE {hero." + SolrHeroProductDefinitionModel.CATEGORY
				+ "} = ?" + SolrHeroProductDefinitionModel.CATEGORY + " AND {hero." + SolrHeroProductDefinitionModel.BASESTORE
				+ "} = ?" + SolrHeroProductDefinitionModel.BASESTORE;

		final Map<String, Object> params = new HashMap<>();
		params.put(SolrHeroProductDefinitionModel.CATEGORY, category);
		params.put(SolrHeroProductDefinitionModel.BASESTORE, baseStore);

		return getFlexibleSearchService().<SolrHeroProductDefinitionModel> search(query, params).getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SolrHeroProductDefinitionModel> findSolrHeroProductDefinitionsByCategory(final CategoryModel category,
			final BaseStoreModel baseStore, final SolrIndexedTypeModel indexedType)
	{
		final String query = "SELECT {hero." + SolrHeroProductDefinitionModel.PK + "} " + "FROM {"
				+ SolrHeroProductDefinitionModel._TYPECODE + " AS hero} " + "WHERE {hero." + SolrHeroProductDefinitionModel.CATEGORY
				+ "} = ?" + SolrHeroProductDefinitionModel.CATEGORY + " AND {hero." + SolrHeroProductDefinitionModel.BASESTORE
				+ "} = ?" + SolrHeroProductDefinitionModel.BASESTORE + " AND {hero." + SolrHeroProductDefinitionModel.INDEXEDTYPE
				+ "} = ?" + SolrHeroProductDefinitionModel.INDEXEDTYPE;

		final Map<String, Object> params = new HashMap<>();
		params.put(SolrHeroProductDefinitionModel.CATEGORY, category);
		params.put(SolrHeroProductDefinitionModel.BASESTORE, baseStore);
		params.put(SolrHeroProductDefinitionModel.INDEXEDTYPE, indexedType);

		return getFlexibleSearchService().<SolrHeroProductDefinitionModel> search(query, params).getResult();
	}

}
