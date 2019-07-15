/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPMarketingSidebySideComponentModel;
import com.gp.commerce.facades.component.data.GPMarketingImagedata;
import com.gp.commerce.facades.component.data.GPMarketingSidebySideComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.impl.GPDefaultMarketingFacade;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

/**
 * @author minal
 *
 */

public class GPMarketingSidebySidePopulator implements Populator<GPBannerComponentModel, List<GPMarketingImagedata>> {
	
	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	
	public void populate(final GPBannerComponentModel marketingSide,
			final List<GPMarketingImagedata> marketingImages) {
		final GPMarketingImagedata gpMarketingImagedata = new GPMarketingImagedata();
		if(null != marketingSide.getBackgroundColor())
		{
			gpMarketingImagedata.setSbsBodyBg(marketingSide.getBackgroundColor());
		}
		if(null != marketingSide.getCtaColor())
		{
			gpMarketingImagedata.setCtaColor(marketingSide.getCtaColor());
		}
		if(null != marketingSide.getCtaText())
		{
			gpMarketingImagedata.setCtaText(marketingSide.getCtaText());
		}
		if(null != marketingSide.getUrlLink())
		{
			gpMarketingImagedata.setCtaLink(marketingSide.getUrlLink());
		}
		if(null != marketingSide.getHeadline())
		{
			gpMarketingImagedata.setSbsTitle(marketingSide.getHeadline());
		}
		if(null != marketingSide.getHeaderColor())
		{
			gpMarketingImagedata.setSbsTitleColor(marketingSide.getHeaderColor());
		}
		if(null != marketingSide.getContent())
		{
			gpMarketingImagedata.setSbsBody(marketingSide.getContent());
		}
		if(null != marketingSide.getContentColor())
		{
			gpMarketingImagedata.setSbsBodyTxtColor(marketingSide.getContentColor());
		}
		if (null != marketingSide.getIsExternalLink())
		{
			gpMarketingImagedata.setIsExternalLink(marketingSide.getIsExternalLink());
		}
		if (null != marketingSide.getImageLink())
		{
			gpMarketingImagedata.setImageLink(marketingSide.getImageLink());
		}
		if (null != marketingSide.getIsExternalImageLink())
		{
			gpMarketingImagedata.setIsExternalImage(marketingSide.getIsExternalImageLink());
		}
		final List<ImageData> imageList = responsiveMediaFacade
				.getImagesFromMediaContainer(marketingSide.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if(CollectionUtils.isNotEmpty(imageList))
		{
			for (final ImageData imageData : imageList)
			{
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					gpMarketingImagedata.setSbsImage(imageData.getUrl());
					gpMarketingImagedata
							.setSbsImageAltText(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
			}
		}
		marketingImages.add(gpMarketingImagedata);
	}

	public void populate(GPMarketingSidebySideComponentModel component,
			GPMarketingSidebySideComponentdata gpMarketingSidebySideComponentdata) {
		if(null != component.getBackgroundColor())
		{
			gpMarketingSidebySideComponentdata.setSbsGlobalBG(component.getBackgroundColor());
		}
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if(CollectionUtils.isNotEmpty(mediaList))
		{
			for (final ImageData imageData : mediaList)
			{
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					gpMarketingSidebySideComponentdata.setSbsGlobalBgImgD(imageData.getUrl());
					gpMarketingSidebySideComponentdata
							.setSbsGlobalBgImgAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
				{
					gpMarketingSidebySideComponentdata.setSbsGlobalBgImgM(imageData.getUrl());
					gpMarketingSidebySideComponentdata
							.setSbsGlobalBgImgAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
				{
					gpMarketingSidebySideComponentdata.setSbsGlobalBgImgT(imageData.getUrl());
					gpMarketingSidebySideComponentdata
							.setSbsGlobalBgImgAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
			}
		}
		
	}

	

}
