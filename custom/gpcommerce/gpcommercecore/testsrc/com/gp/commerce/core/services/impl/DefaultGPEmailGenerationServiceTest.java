package com.gp.commerce.core.services.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageTemplateModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.process.email.context.EmailContextFactory;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.util.Config;

import static org.mockito.Mockito.when;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
@PowerMockIgnore("org.apache.logging.log4j.*")
public class DefaultGPEmailGenerationServiceTest {

	private static final String SHARE_PRODUCT = "SHARE_PRODUCT";
	private static final String SHARE_CART = "SHARE_CART";
	private static final String RECIPIENTS_EMAIL_ID = "RECIPIENTS_EMAIL_ID";
	private static final String INVITEDUSEREMAIL = "INVITEDUSEREMAIL";
	private static final String APPROVAL_EMAIL_TO_ADMINS = "APPROVAL_EMAIL_TO_ADMINS";
	private static final String INVITE_EMAIL_TO_USER = "INVITE_EMAIL_TO_USER";
	private static final String QUICK_ORDER = "QUICK_ORDER";
	private static final String FAILED_ORDER_CONSIGNMENT = "FAILED_ORDER_CONSIGNMENT";
	private static final String ORDER_ADMIN_EMAIL = "ORDER_ADMIN_EMAIL";
	private static final String CSR_EMAIL_LIST = "CSR_EMAIL_LIST";
	private static final String B2B_USER_UPDATE_EMAILS_TO_ADMINS = "B2B_USER_UPDATE_EMAILS_TO_ADMINS";
	private static final String BCCEMAIL = "BCCEMAIL";
	private static final String GP_EMAIL_CC_LIST = "gp.email.cc.list";
	private static final String ATTACHMENTS = "ATTACHMENTS";
	private static final String ATTACHMENT_SHARE_PRODUCT = "ATTACHMENT_SHARE_PRODUCT";
	private static final String ATTACHMENT_QUICK_ORDER = "ATTACHMENT_QUICK_ORDER";
	private static final String ATTACHMENT_FAILED_ORDER = "ATTACHMENT_FAILED_ORDER";
	private static final String EMAIL = "email";
	private static final String DISPLAY_NAME = "displayName";
	private DefaultEmailGenerationService emailGenerationService;

	@InjectMocks
	DefaultGPEmailGenerationService defaultGPEmailGenerationService = new DefaultGPEmailGenerationService();
	
	@Mock
	private EmailService emailService;
	@Mock
	private RendererService rendererService;

	@Mock
	private EmailContextFactory emailContextFactory;

	String emailSubject = "test";
	AbstractEmailContext<BusinessProcessModel> emailContext = Mockito.mock(AbstractEmailContext.class);
	String emailBody = "test";
	final EmailMessageModel emailMessageModel = mock(EmailMessageModel.class);
	List<EmailAttachmentModel> models = new ArrayList();
	EmailAddressModel emailAddressModel = mock(EmailAddressModel.class);

	List<EmailAddressModel> toAddresses = new ArrayList<>();
	List<EmailAddressModel> ccAddresses = new ArrayList<>();
	List<EmailAddressModel> bccAddresses = new ArrayList<>();
	EmailAddressModel fromAddress = mock(EmailAddressModel.class);
	String replyToAddress = "test";
	String subject = "test";
	String body = "test";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		emailGenerationService = new DefaultEmailGenerationService();
		emailGenerationService.setEmailService(emailService);
		emailGenerationService.setRendererService(rendererService);
		emailGenerationService.setEmailContextFactory(emailContextFactory);

		models.add(Mockito.mock(EmailAttachmentModel.class));
		PowerMockito.mockStatic(Config.class);

		String email = "gpcommerce@gp.com";
		String name = "Sample Name";
		given(emailContext.getToEmail()).willReturn(email);
		given(emailContext.getToDisplayName()).willReturn(name);
		given(emailContext.getFromEmail()).willReturn(email);
		given(emailContext.getFromDisplayName()).willReturn(name);

		when(emailService.getOrCreateEmailAddressForEmail(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(emailAddressModel);

		given(emailContext.get("RECIPIENTS_EMAIL_ID")).willReturn(toAddresses);
		given(emailContext.get("INVITEDUSEREMAIL")).willReturn("test");
		given(emailContext.get("CSR_EMAIL_LIST")).willReturn(toAddresses);
		given(emailContext.get("BCCEMAIL")).willReturn("bcc");

		given(emailContext.get("ATTACHMENTS")).willReturn(models);
		given(emailContext.get("ATTACHMENT_SHARE_PRODUCT")).willReturn(models);
		given(emailContext.get("ATTACHMENT_QUICK_ORDER")).willReturn(models);
		given(emailContext.get("ATTACHMENT_FAILED_ORDER")).willReturn(models);

		Mockito.when(Config.getParameter(GP_EMAIL_CC_LIST)).thenReturn("gpccEmail@email.com");
		Mockito.when(emailService.createEmailMessage(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(emailMessageModel);

	}

	@Test
	public void testGenerate() {
		final EmailMessageModel emailMessageModel = mock(EmailMessageModel.class);

		final EmailPageModel emailPageModel = mock(EmailPageModel.class);
		final StoreFrontCustomerProcessModel businessProcessModel = mock(StoreFrontCustomerProcessModel.class);
		final EmailPageTemplateModel emailPageTemplateModel = mock(EmailPageTemplateModel.class);
		final RendererTemplateModel renderTemplate = mock(RendererTemplateModel.class);
		final AbstractEmailContext emailContext = mock(AbstractEmailContext.class);
		final RendererTemplateModel subjectRenderTemplate = mock(RendererTemplateModel.class);
		final EmailAddressModel address = mock(EmailAddressModel.class);

		given(emailPageModel.getMasterTemplate()).willReturn(emailPageTemplateModel);
		given(emailPageTemplateModel.getHtmlTemplate()).willReturn(renderTemplate);
		given(emailContextFactory.create(businessProcessModel, emailPageModel, renderTemplate))
				.willReturn(emailContext);
		given(emailPageTemplateModel.getSubject()).willReturn(subjectRenderTemplate);
		given(renderTemplate.getContextClass()).willReturn("TestEmailContext");
		given(emailContext.getToEmail()).willReturn("a@b.com");
		given(emailContext.getToDisplayName()).willReturn("a b");
		given(emailContext.getFromEmail()).willReturn("customerservices@hybris.com");
		given(emailContext.getFromDisplayName()).willReturn("Customer Services");
		given(emailService.getOrCreateEmailAddressForEmail(any(String.class), any(String.class))).willReturn(address);
		given(emailService.createEmailMessage(Mockito.any(List.class), Mockito.any(List.class), Mockito.any(List.class),
				any(EmailAddressModel.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any(List.class))).willReturn(emailMessageModel);

		EmailMessageModel result = null;
		try {
			result = emailGenerationService.generate(businessProcessModel, emailPageModel);
		} catch (final RuntimeException e) {
			Assert.fail("RuntimeException was thrown");
		}
		verify(rendererService, times(2)).render(any(RendererTemplateModel.class), any(Map.class),
				any(StringWriter.class));
		Assert.assertEquals(emailMessageModel, result);
	}

	@Test
	public void testCreateEmailMessageForSharedProduct() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.TRUE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel,model);

	}
	@Test
	public void testCreateEmailMessageForSharedCart() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.TRUE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	@Test
	public void testCreateEmailMessageForInvite() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.TRUE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	@Test
	public void testCreateEmailMessageForAdminApproval() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.TRUE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	@Test
	public void testCreateEmailMessageForFailedConsignment() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.TRUE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	
	@Test
	public void testCreateEmailMessageForQuickOrder() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.TRUE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	@Test
	public void testCreateEmailMessage() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	@Test
	public void testCreateEmailMessageForPendingOrder() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.TRUE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.FALSE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	@Test
	public void testCreateEmailMessageForB2bUpdate() {
		given(emailContext.get(SHARE_PRODUCT)).willReturn(Boolean.FALSE);
		given(emailContext.get("SHARE_CART")).willReturn(Boolean.FALSE);
		given(emailContext.get("APPROVAL_EMAIL_TO_ADMINS")).willReturn(Boolean.FALSE);
		given(emailContext.get("INVITE_EMAIL_TO_USER")).willReturn(Boolean.FALSE);
		given(emailContext.get("QUICK_ORDER")).willReturn(Boolean.FALSE);
		given(emailContext.get("FAILED_ORDER_CONSIGNMENT")).willReturn(Boolean.FALSE);
		given(emailContext.get(ORDER_ADMIN_EMAIL)).willReturn(Boolean.FALSE);
		given(emailContext.get(B2B_USER_UPDATE_EMAILS_TO_ADMINS)).willReturn(Boolean.TRUE);
		EmailMessageModel model = defaultGPEmailGenerationService.createEmailMessage(emailSubject, emailBody,
				emailContext);
		Assert.assertEquals(emailMessageModel, model);

	}
	
}
