/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.gpcommerceaddon.facades.GPBannerImageData;
import com.gp.commerce.gpcommerceaddon.facades.GPBannerImageDataFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPBannerImageDataPopulator;

import de.hybris.platform.commercefacades.product.data.ImageData;

public class GPDefaultBannerImageDataFacade implements GPBannerImageDataFacade{
	
	@Resource(name = "imageDataPopulator")
	private GPBannerImageDataPopulator imageDataPopulator;

	@Override
	public void populateBannerImageData(List<ImageData> source, GPBannerImageData target) {
		
		imageDataPopulator.populate(source, target);
		
	}

}
