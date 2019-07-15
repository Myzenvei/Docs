/**
 *
 */
package de.hybris.commercesearch.searchandizing.heroproduct.impl;

import de.hybris.commercesearch.searchandizing.heroproduct.dao.MulticountryHeroProductDefinitionDao;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.impl.DefaultHeroProductDefinitionService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author AL
 *
 */
public class MulticountryHeroProductDefinitionService extends DefaultHeroProductDefinitionService
{
	private ModelService modelService;
	private CategoryService categoryService;
	private CatalogVersionService catalogVersionService;
	private MulticountryHeroProductDefinitionDao heroProductDefinitionDao;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;
	private BaseStoreService baseStoreService;


	@Override
	public SolrHeroProductDefinitionModel getSolrHeroProductDefinitionForCategoryCode(final String categoryCode)
	{
		return getSolrHeroProductDefinitionForCategory(getCategoryService().getCategoryForCode(categoryCode));
	}

	@Override
	public SolrHeroProductDefinitionModel getSolrHeroProductDefinitionForCategory(final CategoryModel category)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		return getSolrHeroProductDefinitionForCategory(category, baseStore);
	}

	@Override
	public SolrIndexedTypeModel getIndexedType()
	{
		final SolrFacetSearchConfigModel solrFacetSearchConfig = getSolrFacetSearchConfig();
		final List<SolrIndexedTypeModel> indexedTypes = solrFacetSearchConfig.getSolrIndexedTypes();
		if (CollectionUtils.isEmpty(indexedTypes))
		{
			throw new IllegalStateException(
					"no indexed type found for the solr facet search config [" + solrFacetSearchConfig.getName() + "]");
		}
		return indexedTypes.iterator().next();
	}

	@Override
	public SolrHeroProductDefinitionModel createSolrHeroProductDefinition(final String categoryCode)
	{
		return createSolrHeroProductDefinition(getCategoryService().getCategoryForCode(categoryCode));
	}

	@Override
	public SolrHeroProductDefinitionModel createSolrHeroProductDefinition(final CategoryModel category)
	{
		final SolrIndexedTypeModel indexedType = getIndexedType();
		final CatalogVersionModel catVersion = getCatalogVersion();
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		final SolrHeroProductDefinitionModel heroProductDef = getModelService().create(SolrHeroProductDefinitionModel.class);
		heroProductDef.setCategory(category);
		heroProductDef.setIndexedType(indexedType);
		heroProductDef.setCatalogVersion(catVersion);
		heroProductDef.setBaseStore(baseStore);
		getModelService().save(heroProductDef);
		return heroProductDef;
	}

	@Override
	public SolrFacetSearchConfigModel getSolrFacetSearchConfig()
	{
		try
		{
			return getSolrFacetSearchConfigSelectionStrategy().getCurrentSolrFacetSearchConfig();
		}
		catch (final NoValidSolrConfigException e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/*
	 * find the hero product definition model using category and baseStore as filter.
	 */
	public SolrHeroProductDefinitionModel getSolrHeroProductDefinitionForCategory(final CategoryModel category,
			final BaseStoreModel baseStore)
	{
		final List<SolrHeroProductDefinitionModel> result = getHeroProductDefinitionDao()
				.findSolrHeroProductDefinitionsByCategory(category, baseStore);
		if (result.isEmpty())
		{
			return null;
		}
		if (result.size() == 1)
		{
			return result.iterator().next();
		}
		else
		{
			throw new IllegalStateException("more than one SolrHeroProductDefinition found");
		}
	}

	public MulticountryHeroProductDefinitionDao getHeroProductDefinitionDao()
	{
		return heroProductDefinitionDao;
	}

	@Required
	public void setHeroProductDefinitionDao(final MulticountryHeroProductDefinitionDao heroProductDefinitionDao)
	{
		this.heroProductDefinitionDao = heroProductDefinitionDao;
	}

	@Override
	public CatalogVersionModel getCatalogVersion()
	{
		final SolrFacetSearchConfigModel solrFacetSearchConfig = getSolrFacetSearchConfig();
		final List<CatalogVersionModel> solrCatalogVersions = solrFacetSearchConfig.getCatalogVersions();
		final Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();
		boolean found = false;
		CatalogVersionModel result = null;
		for (final CatalogVersionModel catalogVersion : solrCatalogVersions)
		{
			if (sessionCatalogVersions.contains(catalogVersion))
			{
				if (found)
				{
					throw new SystemException("more than one catalog version found");
				}
				else
				{
					found = true;
					result = catalogVersion;
				}
			}
		}
		if (found)
		{
			return result;
		}
		else
		{
			throw new SystemException("no catalog version found");
		}
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	//@Override
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Override
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy()
	{
		return solrFacetSearchConfigSelectionStrategy;
	}

	@Override
	public void setSolrFacetSearchConfigSelectionStrategy(
			final SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy)
	{
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
	}
}
