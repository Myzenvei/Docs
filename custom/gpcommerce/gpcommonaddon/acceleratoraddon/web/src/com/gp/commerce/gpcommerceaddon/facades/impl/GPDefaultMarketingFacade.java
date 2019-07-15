/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPMarketingComponentModel;
import com.gp.commerce.facades.component.data.GpPromotionData;
import com.gp.commerce.gpcommerceaddon.facades.GPMarketingFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPMarketingPopulator;

public class GPDefaultMarketingFacade implements GPMarketingFacade{
	
	@Resource(name = "gpMarketingPopulator")
	private GPMarketingPopulator gpMarketingPopulator;

	public void populatePromotionData(GPMarketingComponentModel component, GpPromotionData gpPromotionData) {
		gpMarketingPopulator.populate(component,gpPromotionData);
		
	}
}
