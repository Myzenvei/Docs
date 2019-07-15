package de.hybris.commercesearch.searchandizing.facet.reconfiguration.impl;

import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.platform.commercesearch.searchandizing.facet.reconfiguration.impl.GlobalFacetAdminService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;


/**
 * Added BaseStore context to GlobalSolrSearchProfileModel
 *
 */
public class MulticountryGlobalFacetAdminService extends GlobalFacetAdminService
{
	private static final Logger LOG = Logger.getLogger(MulticountryGlobalFacetAdminService.class);
	private MulticountrySearchProfileDao multicountrySearchProfileDao;

	//Workaround for private attribute without getter in GlobalFacetAdminService
	private ModelService multicountryModelService;
	private BaseStoreService baseStoreService;


	protected GlobalSolrSearchProfileModel findGlobalSolrSearchProfileModel(SolrIndexedTypeModel solrIndexedType)
	{
		List globalSolrSearchProfiles = this.multicountrySearchProfileDao.findGlobalSolrSearchProfiles(solrIndexedType, baseStoreService.getCurrentBaseStore());
		return CollectionUtils.isNotEmpty(globalSolrSearchProfiles) ? (GlobalSolrSearchProfileModel) globalSolrSearchProfiles.get(0)
				: null;
	}

	protected GlobalSolrSearchProfileModel createGlobalSolrSearchProfile(SolrIndexedTypeModel solrIndexedTypeModel)
	{
		GlobalSolrSearchProfileModel globalSolrSearchProfileModel = this.multicountryModelService
				.create(GlobalSolrSearchProfileModel.class);
		globalSolrSearchProfileModel.setCode("globalcatalog-srch-profile" + solrIndexedTypeModel.getPk());
		globalSolrSearchProfileModel.setIndexedType(solrIndexedTypeModel);
        globalSolrSearchProfileModel.setBaseStore(baseStoreService.getCurrentBaseStore());
		this.multicountryModelService.save(globalSolrSearchProfileModel);
		return globalSolrSearchProfileModel;
	}

    public BaseStoreService getBaseStoreService() {
        return baseStoreService;
    }

    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService) {
        this.baseStoreService = baseStoreService;
    }

    public MulticountrySearchProfileDao getMulticountrySearchProfileDao() {
        return multicountrySearchProfileDao;
    }

    @Required
    public void setMulticountrySearchProfileDao(MulticountrySearchProfileDao multicountrySearchProfileDao) {
        this.multicountrySearchProfileDao = multicountrySearchProfileDao;
    }

    public ModelService getMulticountryModelService() {
        return multicountryModelService;
    }

    public void setMulticountryModelService(ModelService multicountryModelService) {
        this.multicountryModelService = multicountryModelService;
    }
}
