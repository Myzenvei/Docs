/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import java.util.List;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPMarketingSidebySideComponentModel;
import com.gp.commerce.facades.component.data.GPMarketingImagedata;
import com.gp.commerce.facades.component.data.GPMarketingSidebySideComponentdata;

/**
 * @author minal
 *
 */
public interface GPMarketingSidebySideFacade {
	public void setMarketingBanner(final GPBannerComponentModel marketingSide,
			final List<GPMarketingImagedata> marketingImages);
	
	public void populateMarketingSidebySideComponentdata(GPMarketingSidebySideComponentModel component,
			GPMarketingSidebySideComponentdata gpMarketingSidebySideComponentdata);
}
