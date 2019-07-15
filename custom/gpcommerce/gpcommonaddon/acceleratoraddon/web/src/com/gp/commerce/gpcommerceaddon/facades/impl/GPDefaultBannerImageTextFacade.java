/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.facades.component.data.GPImagetextComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPBannerImageTextFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPBannerImageTextPopulator;

public class GPDefaultBannerImageTextFacade implements GPBannerImageTextFacade{

	@Resource(name = "gpBannerImageTextPopulator")
	private GPBannerImageTextPopulator gpBannerImageTextPopulator;
	
	@Override
	public void populateBannerImageText(GPBannerComponentModel component,
			GPImagetextComponentdata gpImagetextComponentdata) {
			
		gpBannerImageTextPopulator.populate(component, gpImagetextComponentdata);
		
	}

}
