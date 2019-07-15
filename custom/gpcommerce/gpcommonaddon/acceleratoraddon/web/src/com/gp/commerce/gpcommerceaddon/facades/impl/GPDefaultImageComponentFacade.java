/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPImageTextComponentModel;
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPImageComponentFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPImageComponentPopulator;

public class GPDefaultImageComponentFacade implements GPImageComponentFacade{

	@Resource(name = "gpImageComponentPopulator")
	private GPImageComponentPopulator gpImageComponentPopulator;
	
	@Override
	public void populateImageComponent(GPImageTextComponentModel component, GPImageComponentdata gpImageComponentdata) {
		gpImageComponentPopulator.populate(component, gpImageComponentdata);
		
	}

}
