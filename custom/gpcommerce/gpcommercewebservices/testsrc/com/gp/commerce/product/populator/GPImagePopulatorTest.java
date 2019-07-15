/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.product.populator;

import java.util.ArrayList;

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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class GPImagePopulatorTest {

	@InjectMocks
	private GPImagePopulator gpImagePopulator = new GPImagePopulator();
	@Mock
	private Converter<MediaModel, ImageData> imageConverter;

	ArrayList<ImageData> documentList;
	ArrayList<ImageData> videoList;
	DataSheetsData datasheets;
	ProductResourcesVideosData videos;

	MediaModel source;
	ImageData target;
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		source=new MediaModel();
		source.setMime("jpg");
		
		source.setRealfilename("test.csv");
		source.setCode("test");

	}

	@Test
	public void testPopulate() {
		ImageData target = Mockito.mock(ImageData.class);
		MediaModel source = Mockito.mock(MediaModel.class);
		GPImagePopulator spy = Mockito.spy(new GPImagePopulator());
		Mockito.doNothing().when(spy).populate(source, target);
		gpImagePopulator.populate(source, target);
	}

}
