/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades;

import java.util.List;

import de.hybris.platform.commercefacades.product.data.ImageData;

public interface GPBannerImageDataFacade {

	public void populateBannerImageData(final List<ImageData> source, final GPBannerImageData target);
}
