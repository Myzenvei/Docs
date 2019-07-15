/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AvalaraModelDAO {
	
	@Autowired
	FlexibleSearchService flexibleSearchService;
	
	public static final String query = "select {pk} from   {ProductTaxCode}  where {productCode}=?code and {taxArea}=?area";
	
	public static final String query_settings = "select {pk} from   {AvalaraSettings}  where {productCode}=?code and {taxArea}=?area";
	
	
	protected String fetchTaxCode(String code,String area){
        Map<String, Object> params = new HashMap();
        params.put("code", code);
        params.put("area", area);
        SearchResult<ProductTaxCodeModel> searchResult = flexibleSearchService.search(query, params);
        if(searchResult != null && searchResult.getResult().iterator().hasNext()){
        		return searchResult.getResult().iterator().next().getTaxCode();
        }
        return null;
    }
	
	

}
