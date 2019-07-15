package de.hybris.commercesearch.searchandizing.boost.impl;

import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.data.BoostRuleData;
import de.hybris.platform.commercesearch.model.AbstractSolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.CategorySolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.SolrBoostRuleModel;
import de.hybris.platform.commercesearch.searchandizing.boost.impl.DefaultBoostService;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.CategorySearchProfileService;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.GlobalSearchProfileService;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.strategy.CategorySolrSearchProfileMatchStrategy;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.daos.impl.DefaultSolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;


public class MulticountryBoostService extends DefaultBoostService
{
	private CommerceCategoryService commerceCategoryService;
	private CategorySearchProfileService categorySearchProfileService;
	private GlobalSearchProfileService globalSearchProfileService;
	private FacetSearchConfigService facetSearchConfigService;
	private DefaultSolrFacetSearchConfigDao facetSearchConfigDao;
	private MulticountrySearchProfileDao searchProfileDao;
	private CategorySolrSearchProfileMatchStrategy categorySearchProfileMatchStrategy;
	private ModelService modelService;
	private BaseStoreService baseStoreService;


	@Override
	public Collection<SolrBoostRuleModel> getBoostRulesForGlobalSearchProfile(final IndexedType indexedType,
			final FacetSearchConfig facetSearchConfig)
	{
		final GlobalSolrSearchProfileModel globalProfile = this.globalSearchProfileService
				.getGlobalSolrSearchProfileForIndexedType(indexedType, facetSearchConfig);
		return globalProfile != null ? globalProfile.getBoostRules() : Collections.EMPTY_LIST;
	}

	@Override
	public AbstractSolrSearchProfileModel getBoostProfileForCategoryAndIndexedType(final IndexedType indexedType,
			final CategoryModel categoryModel, final FacetSearchConfig facetSearchConfig)
	{
		final Collection<List<CategoryModel>> categoryPaths = categoryModel == null ? Collections.EMPTY_LIST
				: this.commerceCategoryService.getPathsForCategory(categoryModel);
		final Set results = new LinkedHashSet();
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		for (final List<CategoryModel> categories : categoryPaths)
		{
			final List profiles = this.searchProfileDao.getSolrBoostSearchProfiles(convertIntoCategoryCodes(categories),
					getIndexedTypeModel(indexedType, facetSearchConfig), baseStore);

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

		return this.globalSearchProfileService.getGlobalSolrSearchProfileForIndexedType(indexedType, facetSearchConfig);
	}

	@Override
	protected SolrIndexedTypeModel getIndexedTypeModel(final IndexedType indexedType, final FacetSearchConfig facetSearchConfig)
	{
		final SolrFacetSearchConfigModel config = this.facetSearchConfigDao
				.findFacetSearchConfigByName(facetSearchConfig.getName());
		for (final SolrIndexedTypeModel sitm : config.getSolrIndexedTypes())
		{
			if (sitm.getType().equals(indexedType.getComposedType()))
			{
				return sitm;
			}
		}
		return null;
	}

	@Override
	public AbstractSolrSearchProfileModel getProfileForCategoryAndIndexedType(final IndexedType indexedType,
			final CategoryModel categoryModel, final FacetSearchConfig facetSearchConfig)
	{
		if (categoryModel == null)
		{
			return this.globalSearchProfileService.getGlobalSolrSearchProfileForIndexedType(indexedType, facetSearchConfig);
		}

		return this.categorySearchProfileService.getCategorySolrSearchProfile(categoryModel.getCode(),
				getIndexedTypeModel(indexedType, facetSearchConfig));
	}


	@Override
	protected AbstractSolrSearchProfileModel getSearchProfile(final IndexedType indexedType,
			final SolrFacetSearchConfigModel facetSearchConfig, final String categoryCode)
	{
		AbstractSolrSearchProfileModel searchProfile = null;
		final FacetSearchConfig configuration = getConfiguration(facetSearchConfig);

		if (StringUtils.isEmpty(categoryCode))
		{
			searchProfile = this.globalSearchProfileService.getGlobalSolrSearchProfileForIndexedType(indexedType, configuration);
			if (searchProfile == null)
			{
				searchProfile = this.globalSearchProfileService.createGlobalSolrSearchProfile(getIndexedTypeModel(indexedType,
						configuration));
			}
		}
		else
		{
			searchProfile = this.categorySearchProfileService.getCategorySolrSearchProfile(categoryCode,
					getIndexedTypeModel(indexedType, configuration));
			if (searchProfile == null)
			{
				searchProfile = this.categorySearchProfileService.createSolrSearchProfile(categoryCode,
						getIndexedTypeModel(indexedType, configuration));
			}
		}
		return searchProfile;
	}



	@Override
	protected FacetSearchConfig getConfiguration(final SolrFacetSearchConfigModel facetSearchConfig)
	{
		try
		{
			return this.facetSearchConfigService.getConfiguration(facetSearchConfig.getName());
		}
		catch (final FacetConfigServiceException localFacetConfigServiceException)
		{
			throw new IllegalStateException("No facet configuration found for the solr facet search config name ["
					+ facetSearchConfig.getName() + "]", localFacetConfigServiceException);
		}

	}


	@Override
	public SolrBoostRuleModel createBoostRule(final IndexedType indexedType, final SolrFacetSearchConfigModel facetSearchConfig,
			final String categoryCode, final BoostRuleData boostRuleData)
	{
		final AbstractSolrSearchProfileModel searchProfile = getSearchProfile(indexedType, facetSearchConfig, categoryCode);
		final SolrBoostRuleModel newBoostRule = createBoostRule(searchProfile, facetSearchConfig, boostRuleData);
		final ArrayList currentBoostRules = Lists.newArrayList(searchProfile.getBoostRules());
		currentBoostRules.add(newBoostRule);
		searchProfile.setBoostRules(currentBoostRules);
		this.modelService.saveAll();
		return newBoostRule;
	}

	@Override
	protected SolrBoostRuleModel createBoostRule(final AbstractSolrSearchProfileModel searchProfile,
			final SolrFacetSearchConfigModel facetSearchConfig, final BoostRuleData boostRuleData)
	{
		final SolrBoostRuleModel newBoostRule = (SolrBoostRuleModel) this.modelService.create(SolrBoostRuleModel.class);
		newBoostRule.setOperator(boostRuleData.getOperatorType());
		newBoostRule.setPropertyValue(boostRuleData.getPropertyValue());
		newBoostRule.setBoostFactor(boostRuleData.getBoostFactor().intValue());
		newBoostRule.setSolrIndexedProperty(extractSolrIndexedProperty(facetSearchConfig, boostRuleData.getPropertyName()));
		final ArrayList searchProfiles = Lists.newArrayList();
		searchProfiles.add(searchProfile);
		newBoostRule.setSolrSearchProfiles(searchProfiles);
		return newBoostRule;
	}

	@Override
	public void removeSolrBoostRule(final String pk)
	{
		this.modelService.remove(PK.parse(pk));
	}

	@Override
	protected SolrIndexedPropertyModel extractSolrIndexedProperty(final SolrFacetSearchConfigModel facetSearchConfig,
			final String propertyName)
	{
		final List indexedTypes = facetSearchConfig.getSolrIndexedTypes();

		if (CollectionUtils.isEmpty(indexedTypes))
		{
			throw new IllegalStateException("No indexed type found for the solr facet search config [" + facetSearchConfig.getName()
					+ "]");
		}
		final SolrIndexedTypeModel currentIndexedType = (SolrIndexedTypeModel) indexedTypes.iterator().next();

		final SolrIndexedPropertyModel indexProperty = findIndexedProperty(propertyName,
				currentIndexedType.getSolrIndexedProperties());
		if (indexProperty == null)
		{
			throw new IllegalStateException("No indexed property found for the property name [" + propertyName + "]");
		}
		return indexProperty;
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
	 * @return the categorySearchProfileService
	 */
	public CategorySearchProfileService getCategorySearchProfileService()
	{
		return categorySearchProfileService;
	}

	/**
	 * @param categorySearchProfileService
	 *           the categorySearchProfileService to set
	 */
	@Override
	public void setCategorySearchProfileService(final CategorySearchProfileService categorySearchProfileService)
	{
		this.categorySearchProfileService = categorySearchProfileService;
	}

	/**
	 * @return the globalSearchProfileService
	 */
	public GlobalSearchProfileService getGlobalSearchProfileService()
	{
		return globalSearchProfileService;
	}

	/**
	 * @param globalSearchProfileService
	 *           the globalSearchProfileService to set
	 */
	@Override
	public void setGlobalSearchProfileService(final GlobalSearchProfileService globalSearchProfileService)
	{
		this.globalSearchProfileService = globalSearchProfileService;
	}

	/**
	 * @return the facetSearchConfigService
	 */
	public FacetSearchConfigService getFacetSearchConfigService()
	{
		return facetSearchConfigService;
	}

	/**
	 * @param facetSearchConfigService
	 *           the facetSearchConfigService to set
	 */
	@Override
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
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
	 * @return the facetSearchConfigDao
	 */
	public DefaultSolrFacetSearchConfigDao getFacetSearchConfigDao()
	{
		return facetSearchConfigDao;
	}

	/**
	 * @param facetSearchConfigDao
	 *           the facetSearchConfigDao to set
	 */
	public void setFacetSearchConfigDao(final DefaultSolrFacetSearchConfigDao facetSearchConfigDao)
	{
		this.facetSearchConfigDao = facetSearchConfigDao;
	}






}