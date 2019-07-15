/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPMarketingComponentModel;
import com.gp.commerce.facades.component.data.GpPromotionData;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

public class GPMarketingPopulator implements Populator<GPMarketingComponentModel, GpPromotionData>{

	private static final String DESKTOP = "desktop";
	private static final String MOBILE = "mobile";
	private static final String TABLET = "tablet";
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	
	public void populate(GPMarketingComponentModel component, GpPromotionData gpPromotionData) {
		if (null != component.getHeaderText())
		{
			gpPromotionData.setHeader(component.getHeaderText());
		}

		if (null != component.getHeaderTextColor())
		{
			gpPromotionData.setFontcolor(component.getHeaderTextColor());
		}

		if (null != component.getBackgroundColor())
		{
			gpPromotionData.setBackgroundColor(component.getBackgroundColor());
		}
		if (null != component.getUrlLink())
		{
			gpPromotionData.setUrl(component.getUrlLink());
		}
		if (null != component.getIsExternalLink())
		{
			gpPromotionData.setIsExternalLink(component.getIsExternalLink());
		}
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));

		if (CollectionUtils.isNotEmpty(mediaList))
		{
			for (final ImageData imageData : mediaList)
			{
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					gpPromotionData.setBackgroundImageD(imageData.getUrl());
					gpPromotionData
							.setBackgroundImageAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
				{
					gpPromotionData.setBackgroundImageM(imageData.getUrl());
					gpPromotionData
							.setBackgroundImageAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
				{
					gpPromotionData.setBackgroundImageT(imageData.getUrl());
					gpPromotionData
							.setBackgroundImageAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
			}
		}
		
	}

}
