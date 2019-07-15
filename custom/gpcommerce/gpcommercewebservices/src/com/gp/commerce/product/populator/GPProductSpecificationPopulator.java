/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.product.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductImagePopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.ArrayList;
import org.apache.commons.collections.CollectionUtils;
import com.gp.commerce.facades.product.data.DataSheetsData;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;

public class GPProductSpecificationPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends
			AbstractProductImagePopulator<SOURCE, TARGET> {
	/***
	 * 
	 * @param productModel
	 * @param productData 
	 * Populator used to save Product resource Videos 
	 * @throws ConversionException
	 */
	public void populate(SOURCE productModel, TARGET productData) throws ConversionException {
		ArrayList documentList = new ArrayList<ImageData>();
		ArrayList videoList = new ArrayList<ImageData>();
		DataSheetsData datasheets = new DataSheetsData() ;
		ProductResourcesVideosData videos = new ProductResourcesVideosData(); 
		
		ImageData imageData;
		if (CollectionUtils.isNotEmpty(productModel.getData_sheet())) {
			for (MediaModel media : productModel.getData_sheet() ) {
				imageData = (ImageData) this.getImageConverter().convert(media);
				if (imageData.getAltText() == null) {
					imageData.setAltText(productModel.getName());
				}
				documentList.add(imageData);
			}
		}
	
		if (CollectionUtils.isNotEmpty(productModel.getProductResourceVideos())) {
			for (MediaModel media : productModel.getProductResourceVideos() ) {
				imageData = (ImageData) this.getImageConverter().convert(media);
				if (imageData.getAltText() == null) {
					imageData.setAltText(productModel.getName());
				}
				videoList.add(imageData);
			}
		}
		
		datasheets.setDataSheets(documentList);
		productData.setDataSheets(datasheets);
		
		videos.setVideo(videoList);
		productData.setProductResourceVideos(videos);
	}
}