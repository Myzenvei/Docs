/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.model.MardigrasBoredomComponentModel;
import com.gp.commercegpcommerceaddon.facades.MardigrasBoredomComponentData;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

public class MardigrasBoredomPopulator {

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	public MardigrasBoredomComponentData populateMardigrasBoredom(MardigrasBoredomComponentModel component, MardigrasBoredomComponentData boredomData )
	{
		if (null != component.getComponentHeader())
		{
			boredomData.setBannerTitle(component.getComponentHeader());
		}
		return boredomData;
		
	}
	
	public List<MardigrasBannerData> populateMardigrasBannerData(Collection<GPBannerComponentModel> gpImages , MardigrasBoredomComponentData boredomData , List<MardigrasBannerData> banners) 
	{
		if (null != gpImages)
		{
			for (final GPBannerComponentModel gpBanner : gpImages)
			{
				final MardigrasBannerData banner = new MardigrasBannerData();
				final List<ImageData> medias = responsiveMediaFacade
						.getImagesFromMediaContainer(gpBanner.getMedia(commerceCommonI18NService.getCurrentLocale()));
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
				if (null != gpBanner.getHeadline())
				{
					banner.setBannerHeading(gpBanner.getHeadline());
				}
				if (null != gpBanner.getContent())
				{
					banner.setBannerDesc(gpBanner.getContent());
				}
				banners.add(banner);
			}
		}
		return banners;
	}
}
