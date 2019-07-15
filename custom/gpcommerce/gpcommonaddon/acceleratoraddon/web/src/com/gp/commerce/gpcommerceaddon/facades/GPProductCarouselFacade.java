/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import com.gp.commerce.core.model.GPProductCarouselComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GPProductCarouseldata;

import de.hybris.platform.commercefacades.product.data.ProductData;

public interface GPProductCarouselFacade {
	
	public void populateProductCarousel(final GPProductCarouselComponentModel component, final GPImagetileComponentdata gpImagetileComponentdata);
	
	public void populateProductCarouselData(GPProductCarouseldata product, ProductData productData);
}
