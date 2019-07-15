/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.model.ProductTaxCodeCustomModel;
import com.gp.commerce.core.services.GPProductTaxCodeService;

/**
 * This class is used to process GP Product tax code services
 */
public class DefaultGPProductTaxCodeService implements GPProductTaxCodeService{


	FlexibleSearchService flexibleSearchService;

	public static final String PRODUCT_KEY = "productKey";
	public static final String TAX_AREA = "taxArea";
	public static final String query = "select {pk} from   {ProductTaxCodeCustom}  where {productKey}=?productKey and {taxArea}=?taxArea";

	@Override
	public String fetchTaxCode(final String productKey, final String taxArea) {

	        final Map<String, Object> params = new HashMap();
		params.put(PRODUCT_KEY, productKey);
		params.put(TAX_AREA, taxArea);
	        final SearchResult<ProductTaxCodeCustomModel> searchResult = flexibleSearchService.search(query, params);
	        if(searchResult != null && searchResult.getResult().iterator().hasNext())
	        {
	        		return searchResult.getResult().iterator().next().getTaxCode();
	        }
	        return null;

	}
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

}
