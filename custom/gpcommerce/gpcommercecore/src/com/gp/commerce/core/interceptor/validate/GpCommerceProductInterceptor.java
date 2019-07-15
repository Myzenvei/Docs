/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.interceptor.validate;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPCommerceProductModel;


/**
 * Interceptor for category retention in products. If property "gp.category.retention.flag.disable" is set to "true"
 * then skip this interceptor logic
 */
public class GpCommerceProductInterceptor implements PrepareInterceptor<GPCommerceProductModel>
{

	private static final String ISINTERCEPTORENABLE = "gp.category.retention.flag.disable";

	@Override
	public void onPrepare(final GPCommerceProductModel prod, final InterceptorContext ctx) throws InterceptorException
	{

		if (isInterceptorEnable())
		{
			if (ctx.isModified(prod, GPCommerceProductModel.SUPERCATEGORIES) && !ctx.isNew(prod)
					&& prod.getSupercategories() != null)
			{
				final Collection<CategoryModel> newCategories = prod.getSupercategories();
				ModelValueHistory modelValueHistory=getModelValueHistory(prod);
				 Object oldValue=null;
				if(null !=modelValueHistory)
				{
					 oldValue = modelValueHistory.getOriginalValue(GPCommerceProductModel.SUPERCATEGORIES);
				}
				if (oldValue != null && !CollectionUtils.isEmpty(newCategories))
				{
					final Collection<CategoryModel> oldCategories = (Collection<CategoryModel>) oldValue;
					final Set<CategoryModel> gpCategories = new HashSet<CategoryModel>();
					gpCategories.addAll(oldCategories);
					gpCategories.addAll(newCategories);
					final Collection<CategoryModel> immutablelist = Collections.unmodifiableCollection(gpCategories);
					prod.setSupercategories(immutablelist);
				}
			}
		}
	}

	private boolean isInterceptorEnable()
	{
		return Config.getParameter(ISINTERCEPTORENABLE) != null && "true".equalsIgnoreCase(Config.getParameter(ISINTERCEPTORENABLE)) ? false : true;
	}

	private ModelValueHistory getModelValueHistory(final GPCommerceProductModel itemModel)
	{
		final ItemModelContext itemModelContext = ModelContextUtils.getItemModelContext(itemModel);
		if (itemModelContext != null)
		{
			return ((ItemModelContextImpl) itemModelContext).getValueHistory();
		}
		return null;
	}

}
