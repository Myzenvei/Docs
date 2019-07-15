/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.gpcommerceaddon.populators;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import com.gp.commerce.facades.component.data.GPImageComponentdata;
import com.gp.commerce.gpcommerceaddon.facades.GPBannerImageData;


/**
 * Populator for GP Banner Image Data
 *
 */
public class GPBannerImageDataPopulator implements Populator<List<ImageData>, GPBannerImageData>
{
	@Override
	public void populate(final List<ImageData> source, final GPBannerImageData target)
	{
		final List<GPImageComponentdata> images = new ArrayList<>();

		for (final ImageData imageData : source)
		{
			final GPImageComponentdata image = new GPImageComponentdata();
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
			{
				image.setImageSrcD(imageData.getUrl());
			}

			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
			{
				image.setImageSrcM(imageData.getUrl());
			}
			if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
			{
				image.setImageSrcT(imageData.getUrl());

			}
			if (null != imageData.getAltText())
			{
				image.setImageAltText(imageData.getAltText());
			}
			images.add(image);
		}
		target.setImages(images);
	}

}
