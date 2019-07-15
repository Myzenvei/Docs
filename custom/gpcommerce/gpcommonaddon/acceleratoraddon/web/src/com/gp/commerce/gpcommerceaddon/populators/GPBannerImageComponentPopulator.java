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
import com.gp.commerce.facades.component.data.GPImageComponentdata;
import de.hybris.platform.acceleratorfacades.device.ResponsiveMediaFacade;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Populator for populating image tile data from image tile component model.
 */
public class GPBannerImageComponentPopulator implements Populator<GPBannerComponentModel, GPImageComponentdata> {
	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	@Override
	public void populate(GPBannerComponentModel component, GPImageComponentdata gpImageComponentdata)
			throws ConversionException {

		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if(CollectionUtils.isNotEmpty(mediaList))
		{
			for (final ImageData imageData : mediaList)
			{
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
				{
					gpImageComponentdata.setImageSrcD(imageData.getUrl());
				}
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
				{
					gpImageComponentdata.setImageSrcM(imageData.getUrl());
				}
				if(imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
				{
					gpImageComponentdata.setImageSrcT(imageData.getUrl());
				}
			}
		}
		if(null != component.getImageAlignment())
		{
			gpImageComponentdata.setImageAlignment(component.getImageAlignment());
		}
		if(null != component.getImageLink())
		{
			gpImageComponentdata.setImageLink(component.getImageLink());
		}
		if (null != component.getIsExternalImageLink())
		{
			gpImageComponentdata.setIsExternalImage(component.getIsExternalImageLink());
		}
		
	}
}
