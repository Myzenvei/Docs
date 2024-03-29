/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.core.sitemap.generator.impl;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.converters.Converters;

import java.util.List;


/**
 * Site Map Generator for CUSTOM Type
 */
public class GPCustomPageSiteMapGenerator extends GPAbstractSiteMapGenerator<String>
{
	@Override
	public List<SiteMapUrlData> getSiteMapUrlData(final List<String> models)
	{
		return Converters.convertAll(models, getSiteMapUrlDataConverter());
	}

	@Override
	protected List<String> getDataInternal(final CMSSiteModel siteModel)
	{
		return (List<String>) siteModel.getSiteMapConfig().getCustomUrls();
	}
}
