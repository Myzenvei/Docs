/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextDataFacade;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasImageAndTextComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.GPImageAndTextDataPopulator;

public class GPDefaultImageAndTextDataFacade implements GPImageAndTextDataFacade {
	@Resource(name = "imageAndTextDataPopulator")
	private GPImageAndTextDataPopulator imageAndTextDataPopulator;

	@Override
	public void populateImageAndTextData(MardiGrasImageAndTextComponentModel source, GPImageAndTextData target) {
		imageAndTextDataPopulator.populate(source, target);
		
	}
}
