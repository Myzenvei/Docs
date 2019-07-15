package com.gpsaporderexchangeb2b.service.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

     @UnitTest
     public class DefaultGpsaporderexchangeb2bServiceTest {


	@InjectMocks
	private final DefaultGpsaporderexchangeb2bService defaultGpsaporderexchangeb2bService = new DefaultGpsaporderexchangeb2bService();
	@Mock
	private MediaService mediaService;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private ModelService modelService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void getHybrisLogoUrlTest() {
		final MediaModel model = new MediaModel();
		model.setCode("test");
		model.setUrl("test");
		model.setURL("test");
		Mockito.when(mediaService.getMedia(Mockito.anyString())).thenReturn(model);
		final String url = defaultGpsaporderexchangeb2bService.getHybrisLogoUrl(Mockito.anyString());
		Assert.assertEquals(url, null);
	}

	@Test
	public void createLogoTest() throws IOException {

		final CatalogUnawareMediaModel mediaModel = new CatalogUnawareMediaModel();
		Mockito.when(flexibleSearchService.searchUnique(Mockito.anyObject())).thenReturn(mediaModel);
		final InputStream stubInputStream = IOUtils.toInputStream("some test data for my input stream", "UTF-8");
		Mockito.when(modelService.create(CatalogUnawareMediaModel.class)).thenReturn(mediaModel);
		Mockito.doNothing().when(mediaService).setStreamForMedia(Mockito.any(CatalogUnawareMediaModel.class),
				Mockito.any(InputStream.class));
		Mockito.doNothing().when(modelService).save(mediaModel);
		defaultGpsaporderexchangeb2bService.createLogo("test");
	}
}
