/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import java.util.Collection;
import java.util.List;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.model.MardigrasVideosComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasVideoComponentData;

public interface MardigrasVideoFacade {

	public MardigrasVideoComponentData populateMardigrasVideos(final MardigrasVideosComponentModel component, MardigrasVideoComponentData videoData);
	
	public List<MardigrasBannerData> populateBanners(List<MardigrasBannerData> bannerData,
			Collection<GPBannerComponentModel> banners);
}
