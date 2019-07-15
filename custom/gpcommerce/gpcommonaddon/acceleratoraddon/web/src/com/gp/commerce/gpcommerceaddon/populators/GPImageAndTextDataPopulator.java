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

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.gpcommerceaddon.facades.GPImageAndTextData;
import com.gp.commerce.gpcommerceaddon.model.MardiGrasImageAndTextComponentModel;


/**
 * Populator for GP Image and Text Component Data
 *
 */
public class GPImageAndTextDataPopulator implements Populator<MardiGrasImageAndTextComponentModel, GPImageAndTextData>
{

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "responsiveMediaFacade")
	private ResponsiveMediaFacade responsiveMediaFacade;

	/**
	 * Populate GP Image and Text Component Data
	 */
	@Override
	public void populate(final MardiGrasImageAndTextComponentModel source, final GPImageAndTextData target)
	{
		if (null != source.getImage())
		{
			final List<ImageData> medias = responsiveMediaFacade
					.getImagesFromMediaContainer(source.getImage().getMedia(commerceCommonI18NService.getCurrentLocale()));
			if (CollectionUtils.isNotEmpty(medias))
			{
				for (final ImageData imageData : medias)
				{
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.DESKTOP.toString()))
					{
						target.setImgSrcD(imageData.getUrl());
					}
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.MOBILE.toString()))
					{
						target.setImgSrcM(imageData.getUrl());
					}
					if (imageData.getFormat().equalsIgnoreCase(UiExperienceLevel.TABLET.toString()))
					{
						target.setImgSrcT(imageData.getUrl());
					}
					if (null != imageData.getAltText())
					{
						target.setBannerDesc(imageData.getAltText());
					}
				}
			}
		}

		if (null != source.getHeadingText())
		{
			target.setHeadingText(source.getHeadingText());
		}

		if (null != source.getSubHeadingText())
		{
			target.setSubHeadingText(source.getSubHeadingText());
		}

		if (null != source.getInformationText())
		{
			target.setInformationText(source.getInformationText());
		}
	}
}
