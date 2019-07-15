package de.hybris.commercesearch.searchandizing.searchprofile.interceptors;

import de.hybris.platform.commercesearch.model.CategorySolrSearchProfileModel;
import de.hybris.commercesearch.searchandizing.searchprofile.dao.MulticountrySearchProfileDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


public class MulticountryCategorySolrSearchProfileValidator implements ValidateInterceptor
{
	private MulticountrySearchProfileDao searchProfileDao;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if ((model instanceof CategorySolrSearchProfileModel))
		{
			final CategorySolrSearchProfileModel categoryProfile = (CategorySolrSearchProfileModel) model;
			final SolrIndexedTypeModel indexedType = categoryProfile.getIndexedType();
			final String categoryCode = categoryProfile.getCategoryCode();
			final BaseStoreModel baseStore = categoryProfile.getBaseStore();
			final List profiles = this.searchProfileDao.findCategorySolrSearchProfiles(indexedType, categoryCode, baseStore);

			if (!profiles.isEmpty())
			{
				if (ctx.isNew(categoryProfile))
				{
					throw new InterceptorException(
							"cannot create the CategorySolrSearchProfile because there is already another category with the same solr indexed type ["
									+ indexedType.getIdentifier() + "] , the same category [" + categoryCode
									+ "] and the same basestore [" + baseStore.getName() + "] existing in the system");
				}

				final CategorySolrSearchProfileModel existingCategoryProfile = (CategorySolrSearchProfileModel) profiles.iterator()
						.next();
				if (!categoryProfile.equals(existingCategoryProfile))
				{
					throw new InterceptorException(
							"cannot update the CategorySolrSearchProfile because there is already another category with the same solr indexed type ["
									+ indexedType.getIdentifier() + "], the same category [" + categoryCode + "] and the same basestore ["
									+ baseStore.getName() + "] existing in the system");
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