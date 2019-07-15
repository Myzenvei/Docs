/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gp.commerce.core.model.GPBannerComponentModel;
import com.gp.commerce.core.model.GPImageTileComponentModel;
import com.gp.commerce.facades.component.data.GPBannerComponentdata;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;


/**
 * Populator for populating image tile data from image tile component model.
 */
public class GPImageTilePopulator implements Populator<GPImageTileComponentModel, GPImagetileComponentdata>
{

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	private static final String DEFAULT_COLUMNS = "3";

	public void populate(final GPImageTileComponentModel component, final GPImagetileComponentdata gpImagetileComponentdata)
			throws ConversionException
	{
		if (null != component.getComponentHeader())
		{
			gpImagetileComponentdata.setComponentHeader(component.getComponentHeader());
		}
		if (null != component.getComponentHeaderColor())
		{
			gpImagetileComponentdata.setComponentHeaderColor(component.getComponentHeaderColor());
		}
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if (CollectionUtils.isNotEmpty(mediaList))
		{
			for (final ImageData imageData : mediaList)
			{
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					gpImagetileComponentdata.setBackgroundImageD(imageData.getUrl());
					gpImagetileComponentdata
							.setBackgroundImageAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
				{
					gpImagetileComponentdata
							.setBackgroundImageAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
					gpImagetileComponentdata.setBackgroundImageM(imageData.getUrl());
				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
				{
					gpImagetileComponentdata.setBackgroundImageT(imageData.getUrl());
					gpImagetileComponentdata
							.setBackgroundImageAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
			}
		}
		if (null != component.getBackgroundColor())
		{
			gpImagetileComponentdata.setBackgroundColor(component.getBackgroundColor());
		}
		if (null != component.getComponentTheme())
		{
			gpImagetileComponentdata.setComponentTheme(component.getComponentTheme());
		}
		if (null != component.getUrlLink())
		{
			gpImagetileComponentdata.setCtaLink(component.getUrlLink());
			if (component.getUrlLink().trim().startsWith("#"))
			{
				gpImagetileComponentdata.setJumpToHtml(Boolean.TRUE);
			}
			else
			{
				gpImagetileComponentdata.setJumpToHtml(Boolean.FALSE);
			}
		}
		if (null != component.getCtaText())
		{
			gpImagetileComponentdata.setCtaText(component.getCtaText());
		}
		if (null != component.getCtaTextColor())
		{
			gpImagetileComponentdata.setCtaColor(component.getCtaTextColor());
		}
		if (null != component.getCtaStyle())
		{
			gpImagetileComponentdata.setCtaStyle(component.getCtaStyle());
		}
		if (null != component.getIsCarousel())
		{
			gpImagetileComponentdata.setIsCarousel(component.getIsCarousel());
		}
		if (null != component.getSubHeaderText())
		{
			gpImagetileComponentdata.setSubHeaderText(component.getSubHeaderText());
		}
		if (null != component.getHighlighterText())
		{
			gpImagetileComponentdata.setHighlighterText(component.getHighlighterText());
		}
		if (null != component.getIsExternalLink())
		{
			gpImagetileComponentdata.setIsExternal(component.getIsExternalLink());
		}
		gpImagetileComponentdata.setComponentId(component.getUid());
		getNoOfColumns(component, gpImagetileComponentdata);
		populateGpBanner(component, gpImagetileComponentdata, mediaList);
	}

	private void getNoOfColumns(final GPImageTileComponentModel component, final GPImagetileComponentdata gpImagetileComponentdata)
	{
		final String noOfColumns = component.getNoOfColumns();
		if (null != noOfColumns && StringUtils.isNumeric(noOfColumns) && Integer.parseInt(noOfColumns) < 6)
		{
			gpImagetileComponentdata.setNoOfColumns(noOfColumns.trim());
		}
		else
		{
			gpImagetileComponentdata.setNoOfColumns(DEFAULT_COLUMNS);
		}
	}

	private void populateGpBanner(final GPImageTileComponentModel component,
			final GPImagetileComponentdata gpImagetileComponentdata, final List<ImageData> mediaList)
	{
		final List<GPBannerComponentdata> banners = new ArrayList<GPBannerComponentdata>();
		final List<GPBannerComponentModel> gpBanners = (List<GPBannerComponentModel>) component.getBanners();
		for (final GPBannerComponentModel gpBanner : gpBanners)
		{
			final GPBannerComponentdata banner = new GPBannerComponentdata();
			final List<ImageData> medias = responsiveMediaFacade
					.getImagesFromMediaContainer(gpBanner.getMedia(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(medias))
			{
				for (final ImageData imageData : medias)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						banner.setImgSrcD(imageData.getUrl());
						banner.setImgSrcAltTextD(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
					}
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
					{
						banner.setImgSrcM(imageData.getUrl());
						banner.setImgSrcAltTextM(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
					}
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
					{
						banner.setImgSrcT(imageData.getUrl());
						banner.setImgSrcAltTextT(null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

					}
					if (null != imageData.getAltText())
					{
						banner.setImageAltText(imageData.getAltText());
					}
				}
			}
			if(null != gpBanner.getMediaForText())
			{
				final MediaContainerModel media = gpBanner.getMediaForText().getMedia(commerceCommonI18NService.getCurrentLocale());
				if (null != media)
				{
					final List<ImageData> secondaryMedia = responsiveMediaFacade.getImagesFromMediaContainer(media);
					if (CollectionUtils.isNotEmpty(secondaryMedia))
					{
						for (final ImageData imageData : secondaryMedia)
						{
							if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
							{
								banner.setSecondaryImgD(imageData.getUrl());
								banner.setSecondaryimgAltTextD(
										null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
							}
							if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
							{
								banner.setSecondaryImgM(imageData.getUrl());
								banner.setSecondaryimgAltTextM(
										null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
							}
							if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
							{
								banner.setSecondaryImgT(imageData.getUrl());
								banner.setSecondaryimgAltTextT(
										null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
							}
						}
					}
				}

			}
			if (null != gpBanner.getBackgroundColor())
			{
				banner.setBackgroundColor(gpBanner.getBackgroundColor());
			}
			if (null != gpBanner.getHeadline())
			{
				banner.setHeader(gpBanner.getHeadline());
			}
			if (null != gpBanner.getHeaderColor())
			{
				banner.setHeaderColor(gpBanner.getHeaderColor());
			}
			if (null != gpBanner.getContent())
			{
				banner.setTileText(gpBanner.getContent());
			}
			if (null != gpBanner.getContentColor())
			{
				banner.setTextColor(gpBanner.getContentColor());
			}
			if (null != gpBanner.getUrlLink())
			{
				banner.setCtaLink(gpBanner.getUrlLink());
				if (gpBanner.getUrlLink().trim().startsWith("#"))
				{
					banner.setJumpToHtml(Boolean.TRUE);
				}
				else
				{
					banner.setJumpToHtml(Boolean.FALSE);
				}
			}
			if (null != gpBanner.getIsExternalLink())
			{
				banner.setIsExternalLink(gpBanner.getIsExternalLink());
			}
			if (null != gpBanner.getCtaText())
			{
				banner.setCtaText(gpBanner.getCtaText());
			}
			if (null != gpBanner.getCtaColor())
			{
				banner.setCtaColor(gpBanner.getCtaColor());
			}
			if (null != gpBanner.getIsVideo())
			{
				banner.setIsVideo(gpBanner.getIsVideo().toString());
			}
			if (null != gpBanner.getBackgroundVideo())
			{
				banner.setVideoSrc(gpBanner.getBackgroundVideo());
			}
			if (null != gpBanner.getPlayIconColor())
			{
				banner.setPlayIconColor(gpBanner.getPlayIconColor());
			}
			if (null != gpBanner.getImageLink())
			{
				banner.setImageLink(gpBanner.getImageLink());
				if (gpBanner.getImageLink().trim().startsWith("#"))
				{
					banner.setJumpToImage(Boolean.TRUE);
				}
				else
				{
					banner.setJumpToImage(Boolean.FALSE);
				}
			}
			if (null != gpBanner.getIsExternalImageLink())
			{
				banner.setIsExternalImage(gpBanner.getIsExternalImageLink());
			}
			if (null != gpBanner.getCtaStyle())
			{
				banner.setCtaStyle(gpBanner.getCtaStyle());
			}
			if (null != gpBanner.getIsVideoCtaLink())
			{
				banner.setIsVideoCta(gpBanner.getIsVideoCtaLink());
			}
			banners.add(banner);
		}
		gpImagetileComponentdata.setTiles(banners);

	}

}
