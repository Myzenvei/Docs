/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPJspIncludePropertiesFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPJspIncludePropertiesDataPopulator;

public class GPDefaultJspIncludePropertiesFacade implements GPJspIncludePropertiesFacade {
	
	@Resource(name = "jspIncludeDataPopulator")
	private GPJspIncludePropertiesDataPopulator jspIncludeDataPopulator;

	@Override
	public String populateJuicerFeed() {
		return jspIncludeDataPopulator.populateJuicerFeed();
	}

	@Override
	public String populate(String source) {
		
		return jspIncludeDataPopulator.populate(source);
	}

}
