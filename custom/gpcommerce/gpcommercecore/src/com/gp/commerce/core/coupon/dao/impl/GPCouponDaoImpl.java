/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.coupon.dao.impl;

import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.coupon.dao.GPCouponDAO;

public class GPCouponDaoImpl implements GPCouponDAO{

	final String GET_COUPON ="select {pk} from {AbstractCoupon} where {couponId}=?code";
	private FlexibleSearchService flexibleSearchService;

	@Override
	public AbstractCouponModel getCouponForCode(final String code) {
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_COUPON);
		searchQuery.addQueryParameter(GpcommerceCoreConstants.CODE,code);
		final SearchResult<AbstractCouponModel> result = getFlexibleSearchService().search(searchQuery);
		if (CollectionUtils.isNotEmpty(result.getResult()))
		{
			return result.getResult().get(0);
		}
		return null;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}
