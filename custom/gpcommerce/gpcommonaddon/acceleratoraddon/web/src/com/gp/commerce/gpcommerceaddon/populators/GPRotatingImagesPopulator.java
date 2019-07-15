/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.GPImageComponentModel;
import com.gp.commerce.core.model.GPMediaComponentModel;
import com.gp.commerce.core.model.GPVideoBannerComponentModel;
import com.gp.commerce.facades.component.data.Rotatingbannerdata;

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPRotatingImagesPopulator implements Populator<GPImageComponentModel, List<Rotatingbannerdata>> {

	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public void populate(GPImageComponentModel gpImageComponentModel, List<Rotatingbannerdata> rotatingbannerListdata) throws ConversionException {
		final Rotatingbannerdata rotatingbannerdata = new Rotatingbannerdata();
		rotatingbannerdata.setComponetType("divWrapper");
		if (gpImageComponentModel.getHeadingText() != null)
		{
			rotatingbannerdata.setHeadingText(gpImageComponentModel.getHeadingText());
		}
		if (gpImageComponentModel.getUrlLink() != null)
		{
			rotatingbannerdata.setLinkTo(gpImageComponentModel.getUrlLink());
			if (gpImageComponentModel.getUrlLink().trim().startsWith("#"))
			{
				rotatingbannerdata.setJumpToHtml(Boolean.TRUE);
			}
			else
			{
				rotatingbannerdata.setJumpToHtml(Boolean.FALSE);
			}
		}
		if (gpImageComponentModel.getIsExternalLink() != null)
		{
			rotatingbannerdata.setIsExternalLink(gpImageComponentModel.getIsExternalLink());
		}
		if (gpImageComponentModel.getCtaText() != null)
		{
			rotatingbannerdata.setCtaText(gpImageComponentModel.getCtaText());
		}
		if (gpImageComponentModel.getCtaColor() != null)
		{
			rotatingbannerdata.setCtaColor(gpImageComponentModel.getCtaColor());
		}
		if (gpImageComponentModel.getCtaBgColor() != null)
		{
			rotatingbannerdata.setCtaBgColor(gpImageComponentModel.getCtaBgColor());
		}
		if (gpImageComponentModel.getSubHeadBgColor() != null)
		{
			rotatingbannerdata.setSubHeadBgColor(gpImageComponentModel.getSubHeadBgColor());
		}
		if (gpImageComponentModel.getHeadingColor() != null)
		{
			rotatingbannerdata.setHeadingColor(gpImageComponentModel.getHeadingColor());
		}
		if (gpImageComponentModel.getSubHeadingColor() != null)
		{
			rotatingbannerdata.setSubHeadingColor(gpImageComponentModel.getSubHeadingColor());
		}
		if (gpImageComponentModel.getTextPosition() != null)
		{
			rotatingbannerdata.setTextPosition(gpImageComponentModel.getTextPosition());
		}
		if (gpImageComponentModel.getBackgroundColor() != null)
		{
			rotatingbannerdata.setWrapperBg(gpImageComponentModel.getBackgroundColor());
		}
		if (gpImageComponentModel.getSubHeadingText() != null)
		{
			rotatingbannerdata.setSubHeadingText(gpImageComponentModel.getSubHeadingText());
		}
		if (null != gpImageComponentModel.getShowTextFullWidth())
		{
			rotatingbannerdata.setShowTextFullWidth(gpImageComponentModel.getShowTextFullWidth());
		}
		if (null != gpImageComponentModel.getCtaStyle())
		{
			rotatingbannerdata.setCtaStyle(gpImageComponentModel.getCtaStyle());
		}
		if (null != gpImageComponentModel.getComponentTheme())
		{
			rotatingbannerdata.setComponentTheme(gpImageComponentModel.getComponentTheme());
		}
		rotatingbannerListdata.add(rotatingbannerdata);

		
	}
	
	public void getImageData(final List<ImageData> mediaList, final GPImageComponentModel gpImageComponentModel,
			final List<Rotatingbannerdata> rotatingbannerListdata)
	{
		final Rotatingbannerdata rotatingbannerdata = new Rotatingbannerdata();
		rotatingbannerdata.setComponetType("imageWrapper");
		for (final ImageData imageData : mediaList)
		{
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
			{
				rotatingbannerdata.setBackgroundImageD(imageData.getUrl());
				rotatingbannerdata
						.setBackgroundImageAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
			}
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
			{
				rotatingbannerdata.setBackgroundImageM(imageData.getUrl());
				rotatingbannerdata
						.setBackgroundImageAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

			}
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
			{
				rotatingbannerdata.setBackgroundImageT(imageData.getUrl());
				rotatingbannerdata
						.setBackgroundImageAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
			}
		}

		final GPMediaComponentModel heroLogo = gpImageComponentModel.getHeroLogo();
		if (null != heroLogo)
		{
			final List<ImageData> heroMediaContainer = responsiveMediaFacade
					.getImagesFromMediaContainer(heroLogo.getMedia(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(heroMediaContainer))
			{
				for (final ImageData imageData : heroMediaContainer)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						rotatingbannerdata.setHeaderLogo(imageData.getUrl());
						rotatingbannerdata
								.setHeaderLogoImageAltText(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
					}
				}
			}
		}

		if (gpImageComponentModel.getHeadingText() != null)
		{
			rotatingbannerdata.setHeadingText(gpImageComponentModel.getHeadingText());
		}
		if (gpImageComponentModel.getUrlLink() != null)
		{
			rotatingbannerdata.setLinkTo(gpImageComponentModel.getUrlLink());
			if (gpImageComponentModel.getUrlLink().trim().startsWith("#"))
			{
				rotatingbannerdata.setJumpToHtml(Boolean.TRUE);
			}
			else
			{
				rotatingbannerdata.setJumpToHtml(Boolean.FALSE);
			}
		}
		if (gpImageComponentModel.getIsExternalLink() != null)
		{
			rotatingbannerdata.setIsExternalLink(gpImageComponentModel.getIsExternalLink());
		}
		if (gpImageComponentModel.getCtaText() != null)
		{
			rotatingbannerdata.setCtaText(gpImageComponentModel.getCtaText());
		}
		if (gpImageComponentModel.getCtaColor() != null)
		{
			rotatingbannerdata.setCtaColor(gpImageComponentModel.getCtaColor());
		}
		if (gpImageComponentModel.getCtaBgColor() != null)
		{
			rotatingbannerdata.setCtaBgColor(gpImageComponentModel.getCtaBgColor());
		}
		if (gpImageComponentModel.getSubHeadBgColor() != null)
		{
			rotatingbannerdata.setSubHeadBgColor(gpImageComponentModel.getSubHeadBgColor());
		}
		if (gpImageComponentModel.getHeadingColor() != null)
		{
			rotatingbannerdata.setHeadingColor(gpImageComponentModel.getHeadingColor());
		}
		if (gpImageComponentModel.getSubHeadingColor() != null)
		{
			rotatingbannerdata.setSubHeadingColor(gpImageComponentModel.getSubHeadingColor());
		}
		if (gpImageComponentModel.getTextPosition() != null)
		{
			rotatingbannerdata.setTextPosition(gpImageComponentModel.getTextPosition());
		}
		if (gpImageComponentModel.getSubHeadingText() != null)
		{
			rotatingbannerdata.setSubHeadingText(gpImageComponentModel.getSubHeadingText());
		}
		if (null != gpImageComponentModel.getCtaStyle())
		{
			rotatingbannerdata.setCtaStyle(gpImageComponentModel.getCtaStyle());
		}
		if (null != gpImageComponentModel.getComponentTheme())
		{
			rotatingbannerdata.setComponentTheme(gpImageComponentModel.getComponentTheme());
		}
		if (null != gpImageComponentModel.getImageLink())
		{
			rotatingbannerdata.setImageLink(gpImageComponentModel.getImageLink());
		}
		if (null != gpImageComponentModel.getIsExternalImage())
		{
			rotatingbannerdata.setIsExternalImage(gpImageComponentModel.getIsExternalImage());
		}
		rotatingbannerListdata.add(rotatingbannerdata);
	}

	/*
	 * Get GP Video Data
	 */
	public void getGpVideoData(final List<Rotatingbannerdata> rotatingbannerListdata,
			final SimpleResponsiveBannerComponentModel banner)
	{

		final GPVideoBannerComponentModel videoComponentModel = (GPVideoBannerComponentModel) banner;
		final Rotatingbannerdata rotatingbannerdata = new Rotatingbannerdata();
		rotatingbannerdata.setComponetType("videoWrapper");
		if (videoComponentModel.getBodyText() != null)
		{
			rotatingbannerdata.setBodyText(videoComponentModel.getBodyText());
		}
		if (videoComponentModel.getBodyTextColor() != null)
		{
			rotatingbannerdata.setBodyTextColor(videoComponentModel.getBodyTextColor());
		}
		if (videoComponentModel.getVideoSrc() != null)
		{
			rotatingbannerdata.setVideoSrc(videoComponentModel.getVideoSrc());
		}
		if (videoComponentModel.getCtaBackgroundColor() != null)
		{
			rotatingbannerdata.setCtaBgColor(videoComponentModel.getCtaBackgroundColor());
		}
		if (videoComponentModel.getHeaderText() != null)
		{
			rotatingbannerdata.setHeadingText(videoComponentModel.getHeaderText());
		}
		if (videoComponentModel.getHeaderTextColor() != null)
		{
			rotatingbannerdata.setHeadingColor(videoComponentModel.getHeaderTextColor());
		}
		if (videoComponentModel.getCtaText() != null)
		{
			rotatingbannerdata.setCtaText(videoComponentModel.getCtaText());
		}
		if (videoComponentModel.getCtaTextColor() != null)
		{
			rotatingbannerdata.setCtaColor(videoComponentModel.getCtaTextColor());
		}
		if (videoComponentModel.getPlayIconColor() != null)
		{
			rotatingbannerdata.setPlayIconColor(videoComponentModel.getPlayIconColor());
		}
		if (videoComponentModel.getTextPosition() != null)
		{
			rotatingbannerdata.setTextPosition(videoComponentModel.getTextPosition());
		}
		if (videoComponentModel.getSubHeadingText() != null)
		{
			rotatingbannerdata.setSubHeadingText(videoComponentModel.getSubHeadingText());
		}
		if (videoComponentModel.getSubHeadingColor() != null)
		{
			rotatingbannerdata.setSubHeadingColor(videoComponentModel.getSubHeadingColor());
		}
		if (null != videoComponentModel.getUrlLink())
		{
			rotatingbannerdata.setLinkTo(videoComponentModel.getUrlLink());
			if (videoComponentModel.getUrlLink().trim().startsWith("#"))
			{
				rotatingbannerdata.setJumpToHtml(Boolean.TRUE);
			}
			else
			{
				rotatingbannerdata.setJumpToHtml(Boolean.FALSE);
			}
		}
		if (null != videoComponentModel.getCtaStyle())
		{
			rotatingbannerdata.setCtaStyle(videoComponentModel.getCtaStyle());
		}
		if (null != videoComponentModel.getComponentTheme())
		{
			rotatingbannerdata.setComponentTheme(videoComponentModel.getComponentTheme());
		}
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(banner.getMedia(commerceCommonI18NService.getCurrentLocale()));

		if (CollectionUtils.isNotEmpty(mediaList))
		{
			for (final ImageData imageData : mediaList)
			{
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					rotatingbannerdata.setBackgroundImageD(imageData.getUrl());
					rotatingbannerdata
							.setBackgroundImageAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
				{
					rotatingbannerdata.setBackgroundImageM(imageData.getUrl());
					rotatingbannerdata
							.setBackgroundImageAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
				{
					rotatingbannerdata.setBackgroundImageT(imageData.getUrl());
					rotatingbannerdata
							.setBackgroundImageAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
			}

		}
		rotatingbannerListdata.add(rotatingbannerdata);
	}

	/*
	 * Get GP Image Or Wrapper Data
	 */
	public void getGpImageOrWrapperData(final List<Rotatingbannerdata> rotatingbannerListdata,
			final SimpleResponsiveBannerComponentModel banner)
	{
		final GPImageComponentModel gpImageComponentModel = (GPImageComponentModel) banner;
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(gpImageComponentModel.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if (CollectionUtils.isNotEmpty(mediaList))
		{
			getImageData(mediaList, gpImageComponentModel, rotatingbannerListdata);
		}
		else
		{
			populate(gpImageComponentModel, rotatingbannerListdata);
		}
	}

}
