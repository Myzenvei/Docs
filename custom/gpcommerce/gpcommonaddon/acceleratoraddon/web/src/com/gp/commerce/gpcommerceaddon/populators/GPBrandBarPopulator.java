/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPBrandBarComponentModel;
import com.gp.commerce.core.model.GPImageLinkComponentModel;
import com.gp.commerce.facades.component.data.GPBrandBarComponentdata;
import com.gp.commerce.facades.component.data.GPBranddata;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;

public class GPBrandBarPopulator implements Populator<GPBrandBarComponentModel, GPBrandBarComponentdata>{
	
	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	
	public void populate(GPBrandBarComponentModel component, GPBrandBarComponentdata gpBrandBarComponentdata) {
		if(null != component.getHeadline())
		{
			gpBrandBarComponentdata.setHeader(component.getHeadline());
		}
		if(null != component.getHeaderColor())
		{
			gpBrandBarComponentdata.setHeaderColor(component.getHeaderColor());
		}
		if(null != component.getContent())
		{
			gpBrandBarComponentdata.setSubHeader(component.getContent());
		}
		if(null != component.getContentColor())
		{
			gpBrandBarComponentdata.setSubHeaderColor(component.getContentColor());
		}
		if(null != component.getBackgroundColor())
		{
			gpBrandBarComponentdata.setBackgroundColor(component.getBackgroundColor());
		}
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if(CollectionUtils.isNotEmpty(mediaList))
		{
			for (final ImageData imageData : mediaList)
			{
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					gpBrandBarComponentdata.setBackgroundImageD(imageData.getUrl());
					gpBrandBarComponentdata
							.setBackgroundImageAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
				{
					gpBrandBarComponentdata.setBackgroundImageM(imageData.getUrl());
					gpBrandBarComponentdata
							.setBackgroundImageAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
				{
					gpBrandBarComponentdata.setBackgroundImageT(imageData.getUrl());
					gpBrandBarComponentdata
							.setBackgroundImageAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
			}
		}
		if(null != component.getCtaColor())
		{
			gpBrandBarComponentdata.setCtaColor(component.getCtaColor());
		}
		if(null != component.getCtaText())
		{
			gpBrandBarComponentdata.setCtaText(component.getCtaText());
		}
		if(null != component.getUrlLink())
		{
			gpBrandBarComponentdata.setCtaLink(component.getUrlLink());
		}
		if (null != component.getIsExternalLink())
		{
			gpBrandBarComponentdata.setIsExternalLink(component.getIsExternalLink());
		}
		if (null != component.getCtaStyle())
		{
			gpBrandBarComponentdata.setCtaStyle(component.getCtaStyle());
		}
		if (null != component.getComponentTheme())
		{
			gpBrandBarComponentdata.setComponentTheme(component.getComponentTheme());
		}
		
	}

	public void populate(GPImageLinkComponentModel banner, GPBranddata gpBrandData) {
		if (null != banner.getMedia())
		{
			gpBrandData.setBrandImage(banner.getMedia().getURL());
			gpBrandData.setBrandImageAltText(
					banner.getMedia().getAltText() != null ? banner.getMedia().getAltText() : banner.getMedia().getRealFileName());
		}
		if(null != banner.getUrl())
		{
			gpBrandData.setBrandLink(banner.getUrl());
			if (banner.getUrl().trim().startsWith("#"))
			{
				gpBrandData.setJumpToHtml(Boolean.TRUE);
			}
			else
			{
				gpBrandData.setJumpToHtml(Boolean.FALSE);
			}
		}
		if (null != banner.getIsExternalLink())
		{
			gpBrandData.setIsExternalImage(banner.getIsExternalLink());
		}
		if (null != banner.getTitle())
		{
			gpBrandData.setBrandName(banner.getTitle());
		}
		if (null != banner.getTitleColor())
		{
			gpBrandData.setBrandNameColor(banner.getTitleColor());
		}
		
	}

}
