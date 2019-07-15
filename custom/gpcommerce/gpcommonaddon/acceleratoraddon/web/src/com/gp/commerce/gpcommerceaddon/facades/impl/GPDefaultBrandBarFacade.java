/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBrandBarComponentModel;
import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.facades.component.data.GPBrandBarComponentdata;
import com.gp.commerce.facades.component.data.GPBranddata;
/**
 * @author minal
 *
 */
import com.gp.commerce.gpcommerceaddon.facades.GPBrandBarFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPBrandBarPopulator;

public class GPDefaultBrandBarFacade implements GPBrandBarFacade {

	@Resource(name = "gpBrandBarPopulator")
	private GPBrandBarPopulator gpBrandBarPopulator;

	public void populateBrandBarData(GPBrandBarComponentModel component,
			GPBrandBarComponentdata gpBrandBarComponentdata) {
		
		gpBrandBarPopulator.populate(component,gpBrandBarComponentdata);
	}

	public void populateBrandData(GPImageLinkComponentModel banner, GPBranddata gpBrandData) {
		
		gpBrandBarPopulator.populate(banner,gpBrandData);
		
	}
}
