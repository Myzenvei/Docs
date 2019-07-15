/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.product.populator;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.facades.product.data.DataSheetsData;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class GPProductSpecificationPopulatorTest {

	@InjectMocks
	private GPProductSpecificationPopulator<ProductModel, ProductData> gpProductSpecificationPopulator = new GPProductSpecificationPopulator<ProductModel, ProductData>();
	@Mock
	private Converter<MediaModel, ImageData> imageConverter;

	private ProductModel productModel;

	private ProductData productData;
	ArrayList<ImageData> documentList;
	ArrayList<ImageData> videoList;
	DataSheetsData datasheets;
	ProductResourcesVideosData videos;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		documentList = new ArrayList<ImageData>();
		videoList = new ArrayList<ImageData>();
		datasheets = new DataSheetsData();
		videos = new ProductResourcesVideosData();
		productModel = new ProductModel();
		productData = new ProductData();

		Collection<MediaModel> value = new ArrayList<MediaModel>();
		productModel.setData_sheet(value);

		productModel.setProductResourceVideos(value);

	}

	@Test
	public void testPopulate() {

		ImageData imageData = Mockito.mock(ImageData.class);
		MediaModel media = Mockito.mock(MediaModel.class);
		Mockito.when(imageConverter.convert(media)).thenReturn(imageData);
		documentList.add(imageData);

		Mockito.when(imageConverter.convert(media)).thenReturn(imageData);
		videoList.add(imageData);
		datasheets.setDataSheets(documentList);
		productData.setDataSheets(datasheets);

		videos.setVideo(videoList);
		productData.setProductResourceVideos(videos);

		gpProductSpecificationPopulator.populate(productModel, productData);

	}

}
