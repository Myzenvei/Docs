package com.gp.commerce.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.services.impl.GPDefaultShareProductService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import com.gp.commerce.core.forms.ShareProductForm;
import com.gp.commerce.core.util.ShareProductEvent;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

/**
 * JUnit test suite for {@link GPDefaultShareProductServiceTest}
 */
@UnitTest
public class GPDefaultShareProductServiceTest {

	@InjectMocks
	private final GPDefaultShareProductService shareProductService = new GPDefaultShareProductService();
	
	@Mock
	private EventService eventService;
	
	@Mock
	private EmailService emailService;
	
	@Mock
	private MediaService mediaService;
	
	@Mock
	private ProductService productService;
	
	@Mock
	private KeyGenerator guidKeyGenerator;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		shareProductService.setProductService(productService);
		shareProductService.setGuidKeyGenerator(guidKeyGenerator);
		shareProductService.setEmailService(emailService);
		shareProductService.setMediaService(mediaService);
		shareProductService.setEventService(eventService);
	}
	
	@Test
	public void testGetProductService()
	{
		Assert.assertEquals(productService,shareProductService.getProductService());
	}
	
	@Test
	public void testGetGuidKeyGenerator()
	{
		Assert.assertEquals(guidKeyGenerator,shareProductService.getGuidKeyGenerator());
	}
	
	@Test
	public void testGetEmailService()
	{
		Assert.assertEquals(emailService,shareProductService.getEmailService());
	}
	
	@Test
	public void testGetMediaService()
	{
		Assert.assertEquals(mediaService,shareProductService.getMediaService());
	}
	
	@Test
	public void testGetEventService()
	{
		Assert.assertEquals(eventService,shareProductService.getEventService());
	}
	
	@Test
	public void testGetEmailAttachmentList()
	{
		ProductModel productModel = Mockito.mock(ProductModel.class);
		MediaModel dataSheet = Mockito.mock(MediaModel.class);
		dataSheet.setRealFileName("dataSheet");
		dataSheet.setMime("mime");
		List<MediaModel> mediaModels = new ArrayList<>();
		mediaModels.add(dataSheet);
		EmailAttachmentModel emailAttachmentModel = Mockito.mock(EmailAttachmentModel.class);
		
		Mockito.when(productModel.getData_sheet()).thenReturn(mediaModels);
		Mockito.when(guidKeyGenerator.generate()).thenReturn("key");
		Mockito.when(emailService.createEmailAttachment(Mockito.any(DataInputStream.class), Mockito.anyString(), Mockito.anyString())).thenReturn(emailAttachmentModel);
		
		Assert.assertNotNull(shareProductService.getEmailAttachmentList(productModel));
	}
	
	@Test
	public void testShareProductService()
	{
		ShareProductForm form = Mockito.mock(ShareProductForm.class);
		form.setRecipientEmails(Mockito.anyList());
		form.setSenderEmail("abc@xyz.com");
		form.setSenderName("senderName");
		form.setSubject("subject");
		form.setSenderMessage("senderMessage");
		form.setAddLink(false);
		form.setAttachPDF(false);
//		form.setProductPageURL("productPageURL");
//		form.setPdtCode("pdtCode");
		ProductModel productModel = Mockito.mock(ProductModel.class);
		productModel.setName("product");
		productModel.setDescription("description");
		
		Mockito.when(productService.getProductForCode(Mockito.anyString())).thenReturn(productModel);
		Mockito.doNothing().when(eventService).publishEvent(Mockito.any(ShareProductEvent.class));
		//shareProductService.shareProductService(form);
	}
}