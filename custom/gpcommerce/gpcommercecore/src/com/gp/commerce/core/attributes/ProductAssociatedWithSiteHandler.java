/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.attributes;

import static java.lang.String.format;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.site.BaseSiteService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gp.commerce.core.product.dao.GPProductDao;


/**
 * Dynamic attribute handler to determine if the product is associated with a site
 */
public class ProductAssociatedWithSiteHandler extends AbstractDynamicAttributeHandler<Boolean, ProductModel>
{
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "gpProductDao")
	private GPProductDao gpProductDao;

	private static final Logger LOG = Logger.getLogger(ProductAssociatedWithSiteHandler.class);

	@Override
	public Boolean get(final ProductModel model)
	{
		boolean productExists = false;
		try
		{
			final CMSSiteModel site = (CMSSiteModel) baseSiteService.getCurrentBaseSite();
			if (null != site)
			{
				productExists = gpProductDao.isProductAssociatedWithSite(model, site);
			}
		}
		catch (final Exception e)
		{
			LOG.error(format("Error in fetching isAvailableForSite value for '%s'", model.getCode()), e);
		}
		return Boolean.valueOf(productExists);
	}
}
