/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
public class GPImageComponentPopulator implements Populator<GPImageTextComponentModel, GPImageComponentdata> {
	/** The commerce common I 18 N service. */
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	/** The responsive media facade. */
	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	private static final String imageWidthDefault = "50";
	private static final String fullImage = "100";
	private static final String fullText = "0";

	@Override
	public void populate(GPImageTextComponentModel component, GPImageComponentdata gpImageComponentdata)
			throws ConversionException {
		final List<ImageData> mediaList = responsiveMediaFacade
				.getImagesFromMediaContainer(component.getMedia(commerceCommonI18NService.getCurrentLocale()));
		if (CollectionUtils.isNotEmpty(mediaList)) {
			for (final ImageData imageData : mediaList) {
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString())) {
					gpImageComponentdata.setImageSrcD(imageData.getUrl());
					gpImageComponentdata.setImageSrcAltTextD(
							null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString())) {
					gpImageComponentdata.setImageSrcM(imageData.getUrl());
					gpImageComponentdata.setImageSrcAltTextM(
							null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());

				}
				if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString())) {
					gpImageComponentdata.setImageSrcT(imageData.getUrl());
					gpImageComponentdata.setImageSrcAltTextT(
							null != imageData.getAltText() ? imageData.getAltText() : imageData.getName());
				}
			}
		}
		if (null != component.getImageAlignment()) {
			gpImageComponentdata.setImageAlignment(component.getImageAlignment());
		}
		if (null != component.getImageLink()) {
			gpImageComponentdata.setImageLink(component.getImageLink());
		}
		if (null != component.getIsExternalImageLink()) {
			gpImageComponentdata.setIsExternalImage(component.getIsExternalImageLink());
		}

		if (null != component.getIsVideo()) {
			gpImageComponentdata.setIsVideo(component.getIsVideo());
		}
		if (null != component.getBackgroundVideo()) {
			gpImageComponentdata.setVideoSrc(component.getBackgroundVideo());
		}
		if (null != component.getPlayIconColor()) {
			gpImageComponentdata.setPlayIconColor(component.getPlayIconColor());
		}
		getImageWidth(component, gpImageComponentdata);
		gpImageComponentdata.setComponentId(component.getUid());
	}

	private void getImageWidth(final GPImageTextComponentModel component,
			final GPImageComponentdata gpImageComponentdata) {
		final String imageWidth = component.getImageWidth();
		if (null != imageWidth && StringUtils.isNumeric(imageWidth)) {
			gpImageComponentdata.setImageWidth(imageWidth);
		} else if (null != imageWidth && StringUtils.isNumeric(imageWidth) && Integer.parseInt(imageWidth) < 0) {
			gpImageComponentdata.setImageWidth(fullText);
		} else if (null != imageWidth && StringUtils.isNumeric(imageWidth) && Integer.parseInt(imageWidth) > 100) {
			gpImageComponentdata.setImageWidth(fullImage);
		} else if (StringUtils.isBlank(imageWidth) || !StringUtils.isNumeric(imageWidth)) {
			gpImageComponentdata.setImageWidth(imageWidthDefault);
		}
	}
}
