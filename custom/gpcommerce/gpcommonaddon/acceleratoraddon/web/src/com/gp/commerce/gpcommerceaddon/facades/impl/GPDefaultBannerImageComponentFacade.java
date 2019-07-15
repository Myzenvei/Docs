
/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPBannerImageComponentFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPBannerImageComponentPopulator;

public class GPDefaultBannerImageComponentFacade implements GPBannerImageComponentFacade{
	
	@Resource(name = "gpBannerImageComponentPopulator")
	private GPBannerImageComponentPopulator gpBannerImageComponentPopulator;
	
	@Override
	public void populateBannerImage(GPBannerComponentModel component, GPImageComponentdata gpImageComponentdata) {
		
		gpBannerImageComponentPopulator.populate(component, gpImageComponentdata);
	}

}
