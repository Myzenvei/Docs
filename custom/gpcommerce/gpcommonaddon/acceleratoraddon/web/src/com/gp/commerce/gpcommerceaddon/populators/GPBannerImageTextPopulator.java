/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPImageTextComponentModel;
import com.gp.commerce.core.model.GPMediaComponentModel;
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.facades.component.data.GPImagetextComponentdata;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Populator for populating image tile data from image tile component model.
 */
public class GPBannerImageTextPopulator implements Populator<GPBannerComponentModel, GPImagetextComponentdata> {
	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public void populate(GPBannerComponentModel component, GPImagetextComponentdata gpImagetextComponentdata)
			throws ConversionException {
		if(null != component.getHeadline())
		{
			gpImagetextComponentdata.setHeader(component.getHeadline());
		}
		if(null != component.getHeaderColor())
		{
			gpImagetextComponentdata.setHeaderColor(component.getHeaderColor());
		}
		if(null != component.getContent())
		{
			gpImagetextComponentdata.setText(component.getContent());
		}
		if(null != component.getContentColor())
		{
			gpImagetextComponentdata.setTextColor(component.getContentColor());
		}
		if(null != component.getCtaColor())
		{
			gpImagetextComponentdata.setCtaColor(component.getCtaColor());
		}
		if(null != component.getCtaText())
		{
			gpImagetextComponentdata.setCtaText(component.getCtaText());
		}
		if(null != component.getUrlLink())
		{
			gpImagetextComponentdata.setCtaLink(component.getUrlLink());
		}
		if (null != component.getIsExternalLink())
		{
			gpImagetextComponentdata.setIsExternalLink(component.getIsExternalLink());
		}
		if(null != component.getTextalignment())
		{
			gpImagetextComponentdata.setTextAlignment(component.getTextalignment());
		}
		if(null != component.getBackgroundColor())
		{
			gpImagetextComponentdata.setBackgroundColor(component.getBackgroundColor());
		}
		if(null != component.getCtaStyle())
		{
			gpImagetextComponentdata.setCtaStyle(component.getCtaStyle());
		}
		if (null != component.getComponentTheme())
		{
			gpImagetextComponentdata.setComponentTheme(component.getComponentTheme());
		}
		
		final GPMediaComponentModel backgroudMedia = component.getMediaForText();
		if (null != backgroudMedia)
		{
			final List<ImageData> mediaForText = responsiveMediaFacade
					.getImagesFromMediaContainer(backgroudMedia.getMedia(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(mediaForText))
			{
				for (final ImageData imageData : mediaForText)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						gpImagetextComponentdata.setBackgroundImageD(imageData.getUrl());
					}
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
					{
						gpImagetextComponentdata.setBackgroundImageM(imageData.getUrl());
					}
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
					{
						gpImagetextComponentdata.setBackgroundImageT(imageData.getUrl());
					}
				}
			}
		}
	}
	
}
