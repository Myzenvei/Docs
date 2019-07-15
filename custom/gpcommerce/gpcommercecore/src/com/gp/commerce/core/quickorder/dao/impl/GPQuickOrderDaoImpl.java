/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.quickorder.dao.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gp.commerce.core.model.GPCustomerMaterialInfoModel;
import com.gp.commerce.core.quickorder.dao.GPQuickOrderDao;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

public class GPQuickOrderDaoImpl extends AbstractItemDao implements GPQuickOrderDao{

	private static final String B2B_UNIT = "b2bUnit";
	private static final String PRODUCT_CODES = "productCodes";
	private static final String CMIR_CODES = "cmirCodes";
	private static final Logger LOG = Logger.getLogger(GPQuickOrderDaoImpl.class);
	@Override
	public List<GPCustomerMaterialInfoModel> getMaterialInfoForB2BUnit(String b2bUnit, String productCodes) {

		StringBuilder materialInfoQuery = new StringBuilder();
		materialInfoQuery.append(" SELECT {PK} FROM {"+GPCustomerMaterialInfoModel._TYPECODE+"} ");
		materialInfoQuery.append(" WHERE ({"+GPCustomerMaterialInfoModel.CMIRCODE+"} in (?cmirCodes)");
		materialInfoQuery.append(" OR {"+GPCustomerMaterialInfoModel.PRODUCTCODE +" } in (?productCodes))");
		materialInfoQuery.append("  AND {"+GPCustomerMaterialInfoModel.SOLDTOID + "} = ?b2bUnit ");
		
		final Map<String, Object> params = new HashMap<>();
		List<String> productList = Arrays.asList(productCodes.split(","));
		params.put(CMIR_CODES, productList);
		params.put(PRODUCT_CODES, productList);
		params.put(B2B_UNIT, b2bUnit);
		
		LOG.info(" Customer material info query "+materialInfoQuery);
		
		return doSearch(materialInfoQuery.toString(), params, GPCustomerMaterialInfoModel.class);
	}
	
	
	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}
		fQuery.setResultClassList(Collections.singletonList(resultClass));

		final SearchResult<T> searchResult = search(fQuery);
		
		LOG.info(" actual query "+fQuery);
		
		return searchResult.getResult();
	}

	
}
