package de.hybris.commercesearch.searchandizing.searchprofile.interceptors;

import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


public class MulticountryGlobalSolrSearchProfileValidator implements ValidateInterceptor
{
	private MulticountrySearchProfileDao searchProfileDao;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if ((model instanceof GlobalSolrSearchProfileModel))
		{
			final GlobalSolrSearchProfileModel globalProfile = (GlobalSolrSearchProfileModel) model;
			final SolrIndexedTypeModel indexedType = globalProfile.getIndexedType();
			final BaseStoreModel baseStore = globalProfile.getBaseStore();
			final List profiles = this.searchProfileDao.findGlobalSolrSearchProfiles(indexedType, baseStore);

			if (!profiles.isEmpty())
			{
				if (ctx.isNew(globalProfile))
				{
					throw new InterceptorException(
							"cannot create the GlobalSolrSearchProfile because there is already another global profile with the same solr indexed type ["
									+ indexedType.getIdentifier() + "] and the same base store [" + baseStore.getName()
									+ "] existing in the system");
				}

				final GlobalSolrSearchProfileModel existingGlobalProfile = (GlobalSolrSearchProfileModel) profiles.iterator().next();
				if (!globalProfile.equals(existingGlobalProfile))
				{
					throw new InterceptorException(
							"cannot update the GlobalSolrSearchProfile because there is already another global profile with the same solr indexed type ["
									+ indexedType.getIdentifier() + "] and the same base store [" + baseStore.getName()
									+ "] existing in the system");
				}
			}
		}
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

}