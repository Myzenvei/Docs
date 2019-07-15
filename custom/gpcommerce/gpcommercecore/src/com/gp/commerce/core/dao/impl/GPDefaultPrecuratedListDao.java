/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPPrecuratedListDao;
import com.gp.commerce.core.enums.WishlistTypeEnum;


/**
 * A dao class for getting pre curated list.
 */
public class GPDefaultPrecuratedListDao implements GPPrecuratedListDao
{

	private static final String LIST_TYPE = "type";

	/** The flexible search service. */
	@Resource(name="flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	private static final String PRECURATED_LIST = "PRE_CURATED_LIST";

	@Override
	public List<Wishlist2Model> getPrecuratedWishList()
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
				"SELECT {w.pk} FROM {WishList2 AS w JOIN CMSSITE AS c on {w:site} = {c:PK}} WHERE  {w.wishlistType}=?type and {c.uid}=?site");
		searchQuery.addQueryParameter(LIST_TYPE, WishlistTypeEnum.valueOf(PRECURATED_LIST));
		searchQuery.addQueryParameter(GpcommerceCoreConstants.SITE, cmsSiteService.getCurrentSite().getUid());
		final SearchResult<Wishlist2Model> result = flexibleSearchService.search(searchQuery);
		if (CollectionUtils.isNotEmpty(result.getResult()))
		{
			return result.getResult();
		}
		return Collections.emptyList();
	}

}
