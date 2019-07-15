/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPProductCarouselComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GPProductCarouseldata;
import com.gp.commerce.gpcommerceaddon.facades.GPProductCarouselFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPProductCarouselPopulator;

import de.hybris.platform.commercefacades.product.data.ProductData;

public class GPDefaultProductCarouselFacade implements GPProductCarouselFacade{

	@Resource(name = "gpProductCarouselPopulator")
	GPProductCarouselPopulator gpProductCarouselPopulator;

	@Override
	public void populateProductCarousel(GPProductCarouselComponentModel component,
			GPImagetileComponentdata gpImagetileComponentdata) {
		
		 gpProductCarouselPopulator.populate(component, gpImagetileComponentdata);
		
	}

	@Override
	public void populateProductCarouselData(GPProductCarouseldata product, ProductData productData) {
		
		 gpProductCarouselPopulator.populate(product, productData);
	}

	
}
