/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.dao.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPFAQDao;
import com.gp.commerce.core.model.FAQModel;


/**
 * FAQ DAO implementation
 */
public class GPDefaultFAQDao implements GPFAQDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Override
	public List<FAQModel> getFAQs()
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
				"SELECT {f.pk} FROM {FAQ AS f JOIN CMSSITE AS c on {f:site} = {c:PK}} WHERE {c.uid}=?site");
		searchQuery.addQueryParameter(GpcommerceCoreConstants.SITE, cmsSiteService.getCurrentSite().getUid());
		final SearchResult<FAQModel> result = flexibleSearchService.search(searchQuery);
		if (CollectionUtils.isNotEmpty(result.getResult()))
		{
			return result.getResult();
		}
		return new ArrayList<>();
	}
}

