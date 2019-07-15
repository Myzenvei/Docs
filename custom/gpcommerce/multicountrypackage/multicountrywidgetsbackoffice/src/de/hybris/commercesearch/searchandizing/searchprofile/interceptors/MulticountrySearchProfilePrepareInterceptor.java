package de.hybris.commercesearch.searchandizing.searchprofile.interceptors;

import de.hybris.platform.commercesearch.model.CategorySolrSearchProfileModel;
import de.hybris.platform.commercesearch.model.GlobalSolrSearchProfileModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * @author Miguel Uhagon
 *
 */
public class MulticountrySearchProfilePrepareInterceptor implements PrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CategorySolrSearchProfileModel)
		{

			final CategorySolrSearchProfileModel solrSearchProfileDef = (CategorySolrSearchProfileModel) model;
			final StringBuilder code = new StringBuilder(solrSearchProfileDef.getCategoryCode()).append("_")
					.append(solrSearchProfileDef.getIndexedType().getIdentifier()).append("_")
					.append(solrSearchProfileDef.getBaseStore().getUid());
			solrSearchProfileDef.setCode(code.toString());
		}

		if (model instanceof GlobalSolrSearchProfileModel)
		{

			final GlobalSolrSearchProfileModel solrSearchProfileDef = (GlobalSolrSearchProfileModel) model;
			final StringBuilder code = new StringBuilder(solrSearchProfileDef.getIndexedType().getIdentifier()).append("_").append(
					solrSearchProfileDef.getBaseStore().getUid());
			solrSearchProfileDef.setCode(code.toString());
		}

	}

}
