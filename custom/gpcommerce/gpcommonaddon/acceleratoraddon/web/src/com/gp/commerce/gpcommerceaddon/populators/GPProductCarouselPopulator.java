/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.gpcommerceaddon.populators;

import java.util.Collection;

import com.gp.commerce.core.model.GPProductCarouselComponentModel;
import com.gp.commerce.facades.component.data.GPImagetileComponentdata;
import com.gp.commerce.facades.component.data.GPProductCarouseldata;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;

public class GPProductCarouselPopulator implements Populator<GPProductCarouselComponentModel, GPImagetileComponentdata> {
	
	
	public void populate(final GPProductCarouselComponentModel component, final GPImagetileComponentdata gpImagetileComponentdata)
	{	
		if (null != component.getTitle())
		{
			gpImagetileComponentdata.setComponentHeader(component.getTitle());
		}
		if (null != component.getComponentTheme())
		{
			gpImagetileComponentdata.setComponentTheme(component.getComponentTheme());
		}
		if (null != component.getCtaLink())
		{
			gpImagetileComponentdata.setCtaLink(component.getCtaLink());
		}
		if (null != component.getCtaText())
		{
			gpImagetileComponentdata.setCtaText(component.getCtaText());
		}
		if (null != component.getCtaStyle())
		{
			gpImagetileComponentdata.setCtaStyle(component.getCtaStyle());
		}
		if (null != component.getIsCarousel())
		{
			gpImagetileComponentdata.setIsCarousel(component.getIsCarousel());
		}
		if (null != component.getNoOfColumns())
		{
			gpImagetileComponentdata.setNoOfColumns(component.getNoOfColumns());
		}
		
		
	}
	
	public void populate(GPProductCarouseldata product, ProductData productData)
	{
		product.setName(productData.getName());
		product.setUrl(productData.getUrl());
		product.setCode(productData.getCode());
		if (null != productData.getPrice())
		{
			product.setPrice(productData.getPrice().getFormattedValue());
		}
		if (null != productData.getAverageRating())
		{
			product.setRating(String.valueOf(productData.getAverageRating()));
		}
		if (null != productData.getImages())
		{
			final Collection<ImageData> images = productData.getImages();
			for (final ImageData imageData : images)
			{
				if (imageData.getFormat().equalsIgnoreCase("product"))
				{
					product.setImageUrl(imageData.getUrl());
				}
			}
		}
		
	}

}
