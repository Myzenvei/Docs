/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPImageComponentModel;
import com.gp.commerce.facades.component.data.Rotatingbannerdata;
import com.gp.commerce.gpcommerceaddon.facades.GPRotatingImagesFacade;
import com.gp.commerce.gpcommerceaddon.populators.GPRotatingImagesPopulator;

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.commercefacades.product.data.ImageData;

public class GPDefaultRotatingImagesFacade implements GPRotatingImagesFacade{
	
	@Resource(name = "gpRotatingImagesPopulator")
	private GPRotatingImagesPopulator gpRotatingImagesPopulator;

	@Override
	public void populateWrapperData(GPImageComponentModel gpImageComponentModel,
			List<Rotatingbannerdata> rotatingbannerListdata) {
		gpRotatingImagesPopulator.populate(gpImageComponentModel, rotatingbannerListdata);
		
	}

	@Override
	public void getImageData(List<ImageData> mediaList, GPImageComponentModel gpImageComponentModel,
			List<Rotatingbannerdata> rotatingbannerListdata) {
		
		gpRotatingImagesPopulator.getImageData(mediaList, gpImageComponentModel, rotatingbannerListdata);
	}

	@Override
	public void getGpVideoData(List<Rotatingbannerdata> rotatingbannerListdata,
			SimpleResponsiveBannerComponentModel banner) {
		
		gpRotatingImagesPopulator.getGpVideoData(rotatingbannerListdata, banner);
	}

	@Override
	public void getGpImageOrWrapperData(List<Rotatingbannerdata> rotatingbannerListdata,
			SimpleResponsiveBannerComponentModel banner) {
		gpRotatingImagesPopulator.getGpImageOrWrapperData(rotatingbannerListdata, banner);
		
	}
	
	
}
