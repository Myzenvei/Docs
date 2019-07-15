/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gp.commerce.core.dao.impl;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.dao.GPBundleDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import javax.annotation.Resource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPBundleDaoImpl implements GPBundleDao {

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    private static final String GET_ENTRIES_BY_BUDNLENUM= " SELECT {CE."+OrderEntryModel.PK+"} FROM {"+ CartModel._TYPECODE +" AS C JOIN "+ CartEntryModel._TYPECODE+" AS CE ON " +
            "{C."+ CartModel.PK+"}={CE."+CartEntryModel.ORDER+"}} WHERE {C."+CartModel.CODE+"}=?"+GpcommerceCoreConstants.CART_LABEL+" AND " +
            "{CE."+CartEntryModel.BUNDLENO+"}=?"+GpcommerceCoreConstants.BUNDLE_NO;

    @Override
    public List<CartEntryModel> getOrderEntriesByBundleNo(int bundleNo, String cartId) {

        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_ENTRIES_BY_BUDNLENUM);

        final Map<String, Object> params = new HashMap<>();

        params.put(GpcommerceCoreConstants.BUNDLE_NO, bundleNo);

        params.put(GpcommerceCoreConstants.CART_LABEL, cartId);

        searchQuery.addQueryParameters(params);

        final SearchResult<CartEntryModel> searchResult = flexibleSearchService.search(searchQuery);

        if (searchResult.getResult().isEmpty()) {

            return Collections.emptyList();
        }

        return searchResult.getResult();
    }
}
