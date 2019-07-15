/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.facades;

import java.util.List;

import com.gp.commerce.core.model.GPImageComponentModel;
import com.gp.commerce.facades.component.data.Rotatingbannerdata;

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.commercefacades.product.data.ImageData;

public interface GPRotatingImagesFacade {

	public void populateWrapperData(GPImageComponentModel gpImageComponentModel, List<Rotatingbannerdata> rotatingbannerListdata);
	
	public void getImageData(final List<ImageData> mediaList, final GPImageComponentModel gpImageComponentModel,
			final List<Rotatingbannerdata> rotatingbannerListdata);
	
	public void getGpVideoData(final List<Rotatingbannerdata> rotatingbannerListdata,
			final SimpleResponsiveBannerComponentModel banner);
	
	public void getGpImageOrWrapperData(final List<Rotatingbannerdata> rotatingbannerListdata,
			final SimpleResponsiveBannerComponentModel banner);
	
	
}
