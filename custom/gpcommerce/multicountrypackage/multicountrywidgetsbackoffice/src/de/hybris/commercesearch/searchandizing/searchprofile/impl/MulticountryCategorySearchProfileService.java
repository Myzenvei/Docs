/**
 * Changes by MU
 */
package de.hybris.commercesearch.searchandizing.searchprofile.impl;



import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.AbstractSolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.CategorySolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.impl.DefaultCategorySearchProfileService;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.strategy.CategorySolrSearchProfileMatchStrategy;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


public class MulticountryCategorySearchProfileService extends DefaultCategorySearchProfileService
{
	private MulticountrySearchProfileDao searchProfileDao;
	private ModelService modelService;
	private BaseStoreService baseStoreService;
	private CommerceCategoryService commerceCategoryService;
	private CategorySolrSearchProfileMatchStrategy categorySearchProfileMatchStrategy;


	@Override
	public AbstractSolrSearchProfileModel getEffectiveSolrFacetReconfigSearchProfile(final CategoryModel categoryModel,
			final SolrIndexedTypeModel solrIndexedTypeModel)
	{
		final Collection<List<CategoryModel>> categoryPaths = this.commerceCategoryService.getPathsForCategory(categoryModel);
		final Set results = new LinkedHashSet();
		for (final List<CategoryModel> category : categoryPaths)
		{
			final List configs = this.searchProfileDao.getSolrFacetReconfigSearchProfiles(convertIntoCategoryCodes(category),
					solrIndexedTypeModel);
			final CategorySolrSearchProfileModel exactMatch = this.categorySearchProfileMatchStrategy.findExactMatch(configs,
					categoryModel);

			if (exactMatch != null)
			{
				return exactMatch;
			}
			results.addAll(configs);
		}

		if (!results.isEmpty())
		{
			final CategorySolrSearchProfileModel nearestMatch = this.categorySearchProfileMatchStrategy.findNearestMatch(results,
					categoryPaths, categoryModel);
			if (nearestMatch != null)
			{
				return nearestMatch;
			}

		}

		final GlobalSolrSearchProfileModel globalProfile = getGlobalSolrSearchProfileModel(solrIndexedTypeModel);
		if (globalProfile != null)
		{
			return globalProfile;
		}

		return null;
	}



	@Override
	public AbstractSolrSearchProfileModel getFacetSearchProfileForCategoryAndIndexedType(final IndexedType indexedType,
			final CategoryModel categoryModel, final FacetSearchConfig facetSearchConfig)
	{
		final Collection<List<CategoryModel>> categoryPaths = this.commerceCategoryService.getPathsForCategory(categoryModel);
		final Set results = new LinkedHashSet();
		for (final List<CategoryModel> categories : categoryPaths)
		{
			final List profiles = this.searchProfileDao.getSolrFacetReconfigSearchProfiles(convertIntoCategoryCodes(categories),
					getIndexedTypeModel(indexedType, facetSearchConfig));

			final CategorySolrSearchProfileModel exactMatch = this.categorySearchProfileMatchStrategy.findExactMatch(profiles,
					categoryModel);

			if (exactMatch != null)
			{
				return exactMatch;
			}
			results.addAll(profiles);
		}

		if (!results.isEmpty())
		{
			final CategorySolrSearchProfileModel nearestMatch = this.categorySearchProfileMatchStrategy.findNearestMatch(results,
					categoryPaths, categoryModel);
			if (nearestMatch != null)
			{
				return nearestMatch;
			}

		}

		final GlobalSolrSearchProfileModel globalProfile = getGlobalSolrSearchProfileForIndexedType(indexedType, facetSearchConfig);
		if (globalProfile != null)
		{
			return globalProfile;
		}

		return null;
	}


	@Override
	public CategorySolrSearchProfileModel getCategorySolrSearchProfile(final String categoryCode,
			final SolrIndexedTypeModel solrIndexedTypeModel)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		final List profiles = this.searchProfileDao.findCategorySolrSearchProfiles(solrIndexedTypeModel, categoryCode, baseStore);
		if (CollectionUtils.isNotEmpty(profiles))
		{
			return (CategorySolrSearchProfileModel) profiles.get(0);
		}
		return null;
	}


	@Override
	protected GlobalSolrSearchProfileModel getGlobalSolrSearchProfileModel(final SolrIndexedTypeModel solrIndexedTypeModel)
	{

		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();

		final List globalProfiles = this.searchProfileDao.findGlobalSolrSearchProfiles(solrIndexedTypeModel, baseStore);
		if (CollectionUtils.isNotEmpty(globalProfiles))
		{
			return (GlobalSolrSearchProfileModel) globalProfiles.get(0);
		}
		return null;
	}

	@Override
	public CategorySolrSearchProfileModel createSolrSearchProfile(final String categoryCode,
			final SolrIndexedTypeModel solrIndexedTypeModel)
	{
		final CategorySolrSearchProfileModel categorySolrSearchProfileModel = (CategorySolrSearchProfileModel) this.modelService
				.create(CategorySolrSearchProfileModel.class);
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		categorySolrSearchProfileModel.setCode(categoryCode + "-" + solrIndexedTypeModel.getPk());
		categorySolrSearchProfileModel.setCategoryCode(categoryCode);
		categorySolrSearchProfileModel.setIndexedType(solrIndexedTypeModel);
		categorySolrSearchProfileModel.setBaseStore(baseStore);
		this.modelService.save(categorySolrSearchProfileModel);
		return categorySolrSearchProfileModel;
	}



	@Override
	protected GlobalSolrSearchProfileModel getGlobalSolrSearchProfileForIndexedType(final IndexedType indexedType,
			final FacetSearchConfig facetSearchConfig)
	{

		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		final List globalProfiles = this.searchProfileDao.findGlobalSolrSearchProfiles(
				getIndexedTypeModel(indexedType, facetSearchConfig), baseStore);

		if (!globalProfiles.isEmpty())
		{
			return (GlobalSolrSearchProfileModel) globalProfiles.get(0);
		}
		return null;
	}




	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}


	/**
	 * @return the searchProfileDao
	 */
	public MulticountrySearchProfileDao getSearchProfileDao()
	{
		return searchProfileDao;
	}


	/**
	 * @param searchProfileDao
	 *           the searchProfileDao to set
	 */
	public void setSearchProfileDao(final MulticountrySearchProfileDao searchProfileDao)
	{
		this.searchProfileDao = searchProfileDao;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the commerceCategoryService
	 */
	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}


	/**
	 * @param commerceCategoryService
	 *           the commerceCategoryService to set
	 */
	@Override
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}


	/**
	 * @return the categorySearchProfileMatchStrategy
	 */
	public CategorySolrSearchProfileMatchStrategy getCategorySearchProfileMatchStrategy()
	{
		return categorySearchProfileMatchStrategy;
	}


	/**
	 * @param categorySearchProfileMatchStrategy
	 *           the categorySearchProfileMatchStrategy to set
	 */
	@Override
	public void setCategorySearchProfileMatchStrategy(
			final CategorySolrSearchProfileMatchStrategy categorySearchProfileMatchStrategy)
	{
		this.categorySearchProfileMatchStrategy = categorySearchProfileMatchStrategy;
	}
}
