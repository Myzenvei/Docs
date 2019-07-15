/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPImageTextComponentModel;
import com.gp.commerce.facades.component.data.GPImagetextComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPImageTextFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPImageTextPopulator;

public class GPDefaultImageTextFacade implements GPImageTextFacade{

	@Resource(name = "gpImageTextPopulator")
	private GPImageTextPopulator gpImageTextPopulator;
	
	@Override
	public void populateImageText(GPImageTextComponentModel component,
			GPImagetextComponentdata gpImagetextComponentdata) {
		gpImageTextPopulator.populate(component, gpImagetextComponentdata);
		
	}

}
