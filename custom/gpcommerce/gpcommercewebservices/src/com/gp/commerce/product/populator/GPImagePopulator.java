/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.product.populator;
import de.hybris.platform.commercefacades.product.converters.populator.ImagePopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.media.MediaModel;
public class GPImagePopulator extends ImagePopulator{
	/***
	 *
	 * @param source MediaModel
	 * @param target ImageData
	 * Added Mime Type to diff between Videos& Images for the Frontend | Product Carousel
	 */
	@Override
	public void populate(final MediaModel source, final ImageData target)
	{
		super.populate(source, target);
		target.setMimeType(source.getMime());
		if(null != source.getRealFileName() && source.getRealFileName().contains(".")) {
			target.setName(source.getRealFileName().substring(0, source.getRealFileName().lastIndexOf('.')));
		}else {
			target.setName(source.getCode());
		}
		if(null != source.getURL() ) {
				target.setThumbnailUrl(source.getURL());
			}
	}

}
