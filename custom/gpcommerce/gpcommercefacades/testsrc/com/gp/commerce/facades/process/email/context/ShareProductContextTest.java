/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.ShareProductEmailProcessModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class ShareProductContextTest {

	@InjectMocks
	private ShareProductContext shareProductContext= new ShareProductContext();
	@Mock
	private Converter<UserModel, CustomerData> customerConverter;
	@Mock
	private CustomerModel customerModel;
	@Mock
	private UserModel userModel;
	private EmailPageModel emailPageModel;
	@Mock
	private AbstractEmailContext<?> emailContext;

	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private UrlEncoderService urlEncoderService;
	private LanguageModel language;

	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	@Mock
	private StoreFrontCustomerProcessModel storeFrontCustomerProcessModel;
	@Mock
	private ShareProductEmailProcessModel businessProcessModel;
	private String productURL;
	private String productCode;
	private String productName;
	private String productDescription;
    private Boolean isAttached;
	private String subject;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		language = new LanguageModel();
		language.setIsocode("UTF-8");
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel = new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);


		customerModel = new CustomerModel();
		customerModel.setType(CustomerType.GUEST);
		customerModel.setUid("test@tes.com");
		isAttached=true;
		emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);
		((ShareProductEmailProcessModel) businessProcessModel).setSubject("test test");
		((ShareProductEmailProcessModel) businessProcessModel).setSenderName("sender name");
		((ShareProductEmailProcessModel) businessProcessModel).setSenderEmail("test@test.com");
		((ShareProductEmailProcessModel) businessProcessModel).setSenderMessage("test message");
		 ((ShareProductEmailProcessModel) businessProcessModel).setProductPageURL("test/url");
		 ((ShareProductEmailProcessModel) businessProcessModel).setPdtCode("test");
		 ((ShareProductEmailProcessModel) businessProcessModel).setPdtName("product name");
		 ((ShareProductEmailProcessModel) businessProcessModel).setProductDescription("sdsd");
		 EmailAttachmentModel attachment=new EmailAttachmentModel();
		 attachment.setRealFileName("test");
		 
		 List<EmailAttachmentModel> attachmentList=new ArrayList<EmailAttachmentModel>();
		 attachmentList.add(attachment);
		 ((ShareProductEmailProcessModel) businessProcessModel).setEmailAttachmentModelList(attachmentList);
	}

	@Test
	public void testInit() throws CommerceCartRestorationException {
		
		shareProductContext.init(businessProcessModel, emailPageModel);
	}

}
