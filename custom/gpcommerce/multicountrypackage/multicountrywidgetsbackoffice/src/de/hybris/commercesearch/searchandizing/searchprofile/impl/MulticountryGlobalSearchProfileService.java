package de.hybris.commercesearch.searchandizing.searchprofile.impl;

import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.platform.commercesearch.searchandizing.searchprofile.impl.DefaultGlobalSearchProfileService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.daos.impl.DefaultSolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import org.apache.log4j.Logger;


public class MulticountryGlobalSearchProfileService extends DefaultGlobalSearchProfileService
{
	private static final Logger LOG = Logger.getLogger(MulticountryGlobalSearchProfileService.class);
	private static final String GLOBALCATALOG_SRCH_PROFILE_PREFIX = "globalcatalog-srch-profile";
	private ModelService modelService;
	private MulticountrySearchProfileDao searchProfileDao;
	private DefaultSolrFacetSearchConfigDao facetSearchConfigDao;
	private BaseStoreService baseStoreService;

	@Override
	public GlobalSolrSearchProfileModel createGlobalSolrSearchProfile(final SolrIndexedTypeModel solrIndexedTypeModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Creating a new global solr search profile");
		}
		final GlobalSolrSearchProfileModel globalSolrSearchProfileModel = (GlobalSolrSearchProfileModel) this.modelService
				.create(GlobalSolrSearchProfileModel.class);
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		globalSolrSearchProfileModel.setCode(GLOBALCATALOG_SRCH_PROFILE_PREFIX + solrIndexedTypeModel.getPk());
		globalSolrSearchProfileModel.setIndexedType(solrIndexedTypeModel);
		globalSolrSearchProfileModel.setBaseStore(baseStore);
		this.modelService.save(globalSolrSearchProfileModel);
		return globalSolrSearchProfileModel;
	}

	@Override
	public GlobalSolrSearchProfileModel getGlobalSolrSearchProfileForIndexedType(final IndexedType indexedType,
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