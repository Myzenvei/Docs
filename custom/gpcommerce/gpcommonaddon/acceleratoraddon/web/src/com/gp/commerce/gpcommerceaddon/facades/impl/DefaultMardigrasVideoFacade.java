/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasVideoFacade;
import com.gp.commerce.gpcommerceaddon.model.MardigrasVideosComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.MardigrasVideoPopulator;
import com.gp.commercegpcommerceaddon.facades.MardigrasVideoComponentData;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

public class DefaultMardigrasVideoFacade implements MardigrasVideoFacade{

	@Resource(name = "mardigrasVideoPopulator")
	private MardigrasVideoPopulator mardigrasVideoPopulator;
	
	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Override
	public MardigrasVideoComponentData populateMardigrasVideos(MardigrasVideosComponentModel component, MardigrasVideoComponentData videoData) {
		mardigrasVideoPopulator.populate(component, videoData);
		
		return videoData;
	}
	@Override
	public List<MardigrasBannerData> populateBanners(List<MardigrasBannerData> bannerData,
			Collection<GPBannerComponentModel> banners) 
	{
		
		for (final GPBannerComponentModel gpBannerComponent : banners)
		{
			final MardigrasBannerData banner = new MardigrasBannerData();
			if (null != gpBannerComponent.getBackgroundVideo())
			{
				banner.setUrl(gpBannerComponent.getBackgroundVideo());
			}
			final List<ImageData> medias = responsiveMediaFacade
					.getImagesFromMediaContainer(gpBannerComponent.getMedia(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(medias))
			{
				for (final ImageData imageData : medias)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						banner.setBackgroundImage(imageData.getUrl());
						banner.setBannerDesc(imageData.getAltText());
					}
				}
			}
			bannerData.add(banner);
		}
		return bannerData;
	}

}
