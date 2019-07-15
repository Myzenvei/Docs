/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.facades.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.facades.MardigrasBannerData;
import com.gp.commerce.gpcommerceaddon.facades.MaridgrasNapkinsFacade;
import com.gp.commerce.gpcommerceaddon.model.MaridgrasNapkinsComponentModel;
import com.gp.commerce.gpcommerceaddon.populators.MaridgrasNapkinsPopulator;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

public class DefaultMaridgrasNapkinsFacade implements MaridgrasNapkinsFacade{

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	@Resource(name = "maridgrasNapkinsPopulator")
	private MaridgrasNapkinsPopulator maridgrasNapkinsPopulator;

	@Override
	public GPImageAndTextData populateMaridgrasNapkins(MaridgrasNapkinsComponentModel component, GPImageAndTextData napkinsData) {
		maridgrasNapkinsPopulator.populate(component, napkinsData);
		final List<ImageData> medias = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if (CollectionUtils.isNotEmpty(medias))
		{
			for (final ImageData imageData : medias)
			{
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					napkinsData.setImgSrcD(imageData.getUrl());
					napkinsData.setBannerDesc(imageData.getAltText());
				}
			}
		}
		return napkinsData;
	}
	
	public List<MardigrasBannerData> getImageCarouselData(final MaridgrasNapkinsComponentModel component)
	{
		final List<MardigrasBannerData> imageCarouselData = new ArrayList<MardigrasBannerData>();
		final Collection<GPBannerComponentModel> banners = component.getBanners();
		for (final GPBannerComponentModel gpBannerComponent : banners)
		{

			final MardigrasBannerData banner = new MardigrasBannerData();
			final List<ImageData> mediaData = responsiveMediaFacade
					.getImagesFromMediaContainer(gpBannerComponent.getMedia(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(mediaData))
			{
				for (final ImageData imageData : mediaData)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						banner.setBackgroundImage(imageData.getUrl());
						banner.setBannerDesc(imageData.getAltText());
					}
				}
			}
			imageCarouselData.add(banner);
		}
		return imageCarouselData;
	}

}
