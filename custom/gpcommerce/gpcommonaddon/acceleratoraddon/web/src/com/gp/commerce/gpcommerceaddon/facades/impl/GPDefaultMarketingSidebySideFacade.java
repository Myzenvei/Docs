/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPMarketingSidebySideComponentModel;
import com.gp.commerce.facades.component.data.GPMarketingImagedata;
import com.gp.commerce.facades.component.data.GPMarketingSidebySideComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPMarketingSidebySideFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPMarketingSidebySidePopulator;

/**
 * @author minal
 *
 */
public class GPDefaultMarketingSidebySideFacade implements GPMarketingSidebySideFacade{

	@Resource(name ="gpMarketingSidebySidePopulator")
	private GPMarketingSidebySidePopulator gpMarketingSidebySidePopulator;

	@Override
	public void setMarketingBanner(GPBannerComponentModel marketingSide, List<GPMarketingImagedata> marketingImages) {
		gpMarketingSidebySidePopulator.populate(marketingSide, marketingImages);
		
	}
	@Override
	public void populateMarketingSidebySideComponentdata(GPMarketingSidebySideComponentModel component,
			GPMarketingSidebySideComponentdata gpMarketingSidebySideComponentdata) {
		
		gpMarketingSidebySidePopulator.populate(component, gpMarketingSidebySideComponentdata);
	}
}
