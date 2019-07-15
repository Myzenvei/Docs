/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import javax.annotation.Resource;

import com.gp.commerce.core.model.GPBundleImageComponentModel;
import com.gp.commerce.core.model.GPMediaContainerlinkModel;
import com.gp.commerce.facades.component.data.GPBundleImageData;
import com.gp.commerce.facades.component.data.GPImageComponentdata;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

public class GPBundleImagePopulator implements Populator<ImageData, GPImageComponentdata>{

	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	public void populate(ImageData imageData, GPImageComponentdata gpImageComponentData) {
		if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
		{
			gpImageComponentData.setImageSrcD(imageData.getUrl());
			gpImageComponentData
					.setImageSrcAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
		}
		if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
		{
			gpImageComponentData.setImageSrcM(imageData.getUrl());
			gpImageComponentData
					.setImageSrcAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
		}
		if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
		{
			gpImageComponentData.setImageSrcT(imageData.getUrl());
			gpImageComponentData
					.setImageSrcAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

		}
		
	}

	public void populate(GPMediaContainerlinkModel mediaContainer, GPImageComponentdata gpImageComponentData) {
		if(null != mediaContainer.getMediaUrl())
		{
			gpImageComponentData.setImageLink(mediaContainer.getMediaUrl());
		}
		
	}

	public void populate(GPBundleImageComponentModel component, GPBundleImageData gpBundleData) {
		gpBundleData.setTitle(component.getTitle());
		
	}
}
