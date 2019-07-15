/**
 *
 */
package de.hybris.commercesearch.searchandizing.heroproduct.interceptors;

import de.hybris.commercesearch.searchandizing.heroproduct.dao.MulticountryHeroProductDefinitionDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.interceptors.SolrHeroProductValidator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import javax.annotation.Resource;


/**
 * Extending the Solr Hero Product Validator to account for multi-country changes to the hero product, a hero product
 * definition is unique if the base store and the product are both unique tuple.
 */
public class MulticountrySolrHeroProductValidator extends SolrHeroProductValidator
{
	@Resource
	private BaseStoreService baseStoreService;

	@Resource
	private MulticountryHeroProductDefinitionDao heroProductDefinitionDao;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof SolrHeroProductDefinitionModel)
		{
			final SolrHeroProductDefinitionModel heroProductDef = (SolrHeroProductDefinitionModel) model;

			final CategoryModel category = heroProductDef.getCategory();
			final SolrIndexedTypeModel indexedType = heroProductDef.getIndexedType();
			final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();

			final List<SolrHeroProductDefinitionModel> solrHeroProductDefinitions = heroProductDefinitionDao
					.findSolrHeroProductDefinitionsByCategory(category, baseStore, indexedType);

			if (!solrHeroProductDefinitions.isEmpty())
			{
				if (ctx.isNew(heroProductDef))
				{
					throw new InterceptorException(
							"cannot create the SolrHeroProductDefinition because it exists already with the same category ["
									+ category.getName() + "] and the same solr config [" + indexedType.getIdentifier() + "]");
				}
				if (ctx.isModified(heroProductDef) && !heroProductAlreadyOnList(heroProductDef, solrHeroProductDefinitions))
				{
					throw new InterceptorException(
							"cannot update the SolrHeroProductDefinition because it exists already with the given category ["
									+ category.getName() + "] and / or the given solr config [" + indexedType.getIdentifier() + "]");
				}
			}
		}
	}
}
