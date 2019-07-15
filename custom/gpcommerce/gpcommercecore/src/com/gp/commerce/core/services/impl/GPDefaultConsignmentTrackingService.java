/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.impl;

import de.hybris.platform.consignmenttrackingservices.service.impl.DefaultConsignmentTrackingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.GPConsignmentTrackingService;

/**
 * This class is used for processing GP consignment tracking services
 */
public class GPDefaultConsignmentTrackingService extends DefaultConsignmentTrackingService implements GPConsignmentTrackingService{

	@Autowired
	FlexibleSearchService flexibleSearchService;

	public static final String query = "select {pk} from   {consignment}  where {code}=?code";

	@Override
	public ConsignmentModel getConsignmentByCode(final String code) {
		return fetchConsignmentByCode(code);
	}

	protected ConsignmentModel fetchConsignmentByCode(final String code){
        final Map<String, Object> params = new HashMap();
		params.put(GpcommerceCoreConstants.CODE, code);
        final SearchResult<ConsignmentModel> searchResult = flexibleSearchService.search(query, params);
        if(searchResult != null && searchResult.getResult().iterator().hasNext())
        {
			return (ConsignmentModel) searchResult.getResult().iterator().next();
        }
        return null;
    }


}
