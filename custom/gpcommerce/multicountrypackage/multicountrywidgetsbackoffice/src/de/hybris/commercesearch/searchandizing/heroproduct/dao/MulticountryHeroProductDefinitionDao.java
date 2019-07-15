/**
 *
 */
package de.hybris.commercesearch.searchandizing.heroproduct.dao;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.dao.HeroProductDefinitionDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


/**
 * The multi-country hero product definition DAO.
 */
public interface MulticountryHeroProductDefinitionDao extends HeroProductDefinitionDao
{
	/**
	 * Find solr hero product definitions by category belonging to a particular base store.
	 *
	 * @param category
	 *           the category the hero definitions belong to
	 * @param baseStore
	 *           the base store the hero definitions belong to
	 * @return hero product definitions
	 */
	List<SolrHeroProductDefinitionModel> findSolrHeroProductDefinitionsByCategory(final CategoryModel category,
			final BaseStoreModel baseStore);

	/**
	 * Find solr hero product definitions by category belonging to a particular base store and index type.
	 *
	 * @param category
	 *           the category the hero definitions belong to
	 * @param baseStore
	 *           the base store the hero definitions belong to
	 * @param indexedType
	 *           the index type the hero definitions belong to
	 * @return hero product definitions
	 */
	List<SolrHeroProductDefinitionModel> findSolrHeroProductDefinitionsByCategory(CategoryModel category,
			BaseStoreModel baseStore, SolrIndexedTypeModel indexedType);
}
