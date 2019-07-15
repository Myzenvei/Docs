/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.product.converters.populator;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commercefacades.product.converters.populator.ProductGalleryImagesPopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

public class GPProductGalleryImagesPopulator extends ProductGalleryImagesPopulator<ProductModel, ProductData>  {

	@Override
	public void populate(final ProductModel productModel, final ProductData productData) throws ConversionException
	{
		final List<MediaContainerModel> mediaContainers = new ArrayList<MediaContainerModel>();
		collectMediaContainers(productModel, mediaContainers);
		
		final List<ImageData> imageList = new ArrayList<ImageData>();
		int galleryIndex = 0;
		for (final MediaContainerModel mediaContainer : mediaContainers)
		{
			final String imageFormat="product";
			final ImageDataType imageType = ImageDataType.GALLERY;
			try
			{
				final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
				if (mediaFormatQualifier != null)
				{
					final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
					if (mediaFormat != null)
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
						if (media != null)
						{
							final ImageData imageData = getImageConverter().convert(media);
							imageData.setFormat(imageFormat);
							imageData.setImageType(imageType);
							if (ImageDataType.GALLERY.equals(imageType))
							{
								imageData.setGalleryIndex(Integer.valueOf(galleryIndex));
							}
							imageList.add(imageData);
						}
					}
				}
			}
			catch (final ModelNotFoundException ignore)
			{
				// Ignore
			}
			
		}
		productData.setDamGalleryImages(imageList);
		
	}
}
