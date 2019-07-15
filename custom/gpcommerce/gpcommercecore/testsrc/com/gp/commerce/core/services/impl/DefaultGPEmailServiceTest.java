package com.gp.commerce.core.services.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.DataInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

public class DefaultGPEmailServiceTest {
	
	public static final String EMAIL_ATTACHMENT_NAME = "cart.csv";
	private static final String QUICK_ORDER = "quickorder";
	private static final String QUICK_ORDER_ATTACHMENT = "quickorder.csv";
	private static final String SHARE_WISHLIST = "wishlist";
	private static final String FAILED_ORDER = "failedOrder";
	private static final String FAILED_CONSIGN= "failedConsignment";
	private static final String FAILED_ORDER_ATTACHMENT = "failedOrders.csv";
	private static final String FAILED_CONSIGN_ATTACHMENT = "failedConsignments.csv";
	private static final String GP_TAX = "Test.pdf|taxEmail";

	
	private DefaultGPEmailService emailService;

	@Mock
	private MediaService mediaService;
	@Mock
	CatalogService catalogService;
	@Mock
	CatalogModel catalogModel;
	@Mock
	MediaFolderModel  folderModel;
	@Mock
	private ModelService modelService;
	

	
	final DataInputStream inputStream = mock(DataInputStream.class);
	final EmailAttachmentModel emailAttachmentModel = mock(EmailAttachmentModel.class);
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		emailService = new DefaultGPEmailService();
		emailService.setModelService(modelService);
		emailService.setEmailAttachmentsMediaFolderName("/media/folder");
		emailService.setCatalogService(catalogService);
		emailService.setMediaService(mediaService);
		given(modelService.create(EmailAttachmentModel.class)).willReturn(emailAttachmentModel);
		given(mediaService.getFolder("/media/folder")).willReturn(folderModel);
		given(mediaService.getFolder(any(String.class))).willReturn(folderModel);
		given(catalogService.getDefaultCatalog()).willReturn(catalogModel);
		given(catalogModel.getActiveCatalogVersion()).willReturn(Mockito.mock(CatalogVersionModel.class));
	}

	
	@Test
	public void testCreateEmailAttachmentForQuickOrder()
	{
		Assert.assertTrue(emailService.createEmailAttachment(inputStream,QUICK_ORDER_ATTACHMENT, "Image").equals(emailAttachmentModel));
		
	}
	@Test
	public void testCreateEmailAttachmentForShareWishList()
	{
		Assert.assertTrue(emailService.createEmailAttachment(inputStream,SHARE_WISHLIST, "Image").equals(emailAttachmentModel));

	}
	@Test
	public void testCreateEmailAttachmentForFailedOrder()
	{
		Assert.assertTrue(emailService.createEmailAttachment(inputStream,FAILED_ORDER_ATTACHMENT, "Image").equals(emailAttachmentModel));

	}
	@Test
	public void testCreateEmailAttachmentForFailedConsign()
	{
		Assert.assertTrue(emailService.createEmailAttachment(inputStream,FAILED_CONSIGN_ATTACHMENT, "Image").equals(emailAttachmentModel));

	}
	@Test
	public void testCreateEmailAttachment()
	{
		Assert.assertTrue(emailService.createEmailAttachment(inputStream,"Nothing", "Image").equals(emailAttachmentModel));

	}
	@Test
	public void testCreateEmailAttachmentForTax()
	{
		Assert.assertTrue(emailService.createEmailAttachment(inputStream,GP_TAX, "Image").equals(emailAttachmentModel));

	}
}
