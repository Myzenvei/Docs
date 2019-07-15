/**
 * 
 */
package de.hybris.commercesearch.searchandizing.heroproduct.interceptors;

import de.hybris.platform.commercesearch.model.SolrHeroProductDefinitionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * @author AL
 * 
 */
public class MulticountryHeroProductPrepareInterceptor implements PrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof SolrHeroProductDefinitionModel)
		{

			final SolrHeroProductDefinitionModel solrHeroProductDef = (SolrHeroProductDefinitionModel) model;
			final StringBuilder code = new StringBuilder(solrHeroProductDef.getCategory().getCode()).append("_")
					.append(solrHeroProductDef.getIndexedType().getIdentifier()).append("_")
					.append(solrHeroProductDef.getBaseStore().getUid());
			solrHeroProductDef.setCode(code.toString());
		}
	}

}
