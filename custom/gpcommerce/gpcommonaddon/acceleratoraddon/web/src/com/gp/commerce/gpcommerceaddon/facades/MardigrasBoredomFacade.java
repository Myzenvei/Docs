/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import java.util.Collection;
import java.util.List;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.model.MardigrasBoredomComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasBoredomComponentData;

public interface MardigrasBoredomFacade {
	
	public MardigrasBoredomComponentData populateMardigrasBoredom(MardigrasBoredomComponentModel component, MardigrasBoredomComponentData boredomData );
	
	public List<MardigrasBannerData> populateMardigrasBannerData(Collection<GPBannerComponentModel> gpImages , MardigrasBoredomComponentData boredomData , List<MardigrasBannerData> banners);
}
