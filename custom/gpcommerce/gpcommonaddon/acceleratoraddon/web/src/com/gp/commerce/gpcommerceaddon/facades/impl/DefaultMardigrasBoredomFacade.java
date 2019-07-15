/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBoredomFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasBoredomComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.MardigrasBoredomPopulator;
import com.gp.commercegpcommerceaddon.facades.MardigrasBoredomComponentData;

public class DefaultMardigrasBoredomFacade implements MardigrasBoredomFacade{

	@Resource(name = "mardigrasBoredomPopulator")
	private MardigrasBoredomPopulator mardigrasBoredomPopulator;

	@Override
	public MardigrasBoredomComponentData populateMardigrasBoredom(MardigrasBoredomComponentModel component,
			MardigrasBoredomComponentData boredomData) {
		
		return mardigrasBoredomPopulator.populateMardigrasBoredom(component, boredomData);
	}

	@Override
	public List<MardigrasBannerData> populateMardigrasBannerData(Collection<GPBannerComponentModel> gpImages,
			MardigrasBoredomComponentData boredomData, List<MardigrasBannerData> banners) {
		
		return mardigrasBoredomPopulator.populateMardigrasBannerData(gpImages, boredomData, banners);
	}
	
	
}
