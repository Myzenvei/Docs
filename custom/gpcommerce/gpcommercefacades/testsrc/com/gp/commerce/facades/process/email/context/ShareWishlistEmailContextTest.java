/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.WishlistProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.data.WishlistData;
import com.gp.commerce.core.model.WishlistProcessModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

@UnitTest
public class ShareWishlistEmailContextTest {

	@InjectMocks
	private ShareWishlistEmailContext shareWishlistEmailContext= new ShareWishlistEmailContext();
	private EmailPageModel emailPageModel;
	@Mock
	private AbstractEmailContext<?> emailContext;
	@Mock
	private UrlEncoderService urlEncoderService;
	@Mock
	private Converter<Wishlist2Model, WishlistData> wishlistConverter;
	private LanguageModel language;

	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	@Mock
	private StoreFrontCustomerProcessModel storeFrontCustomerProcessModel;
	@Mock
	private WishlistProcessModel businessProcessModel;
	@Mock
	private Wishlist2Model wishlist;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private GPCMSSiteService cmsSiteService;
	@Mock
	private UserModel userModel;
	@Mock
	private EmailService emailService;
	@Mock
	private ConfigurationService configurationService;;
	
	WishlistData wishlistData = new WishlistData();
	
	private static final String SHARE_WISHLIST_ENABLED="customShareWishlistEnabled";
	private static final String GPXPRESS_REDIRECT_URL = "gpxpress.redirect.url";
	private String siteUid;
	private String subject;
	private String senderMessage;
	private Boolean soldTo;
	private Boolean addLink;
	private String gpxpressRedirectUrl;
	

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		language = new LanguageModel();
		language.setIsocode("UTF-8");
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel = new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		
		userModel=new UserModel();
		wishlist=new Wishlist2Model();
		wishlist.setWishlistUid("12345");
		wishlist.setUser(userModel);
		businessProcessModel.setWishlist(wishlist);
		
		emailPageModel.setFromEmail("test@test.com", Locale.ENGLISH);
		((WishlistProcessModel) businessProcessModel).setToEmail("test1@test.com");
		((WishlistProcessModel) businessProcessModel).setSubject("test test");
		((WishlistProcessModel) businessProcessModel).setSenderName("sender name");
		((WishlistProcessModel) businessProcessModel).setSenderEmail("test@test.com");
		((WishlistProcessModel) businessProcessModel).setSendersMessage("test message");
		((WishlistProcessModel) businessProcessModel).setAddLink(Boolean.TRUE);
		((WishlistProcessModel) businessProcessModel).setAttachPDF(Boolean.TRUE);
		 EmailAttachmentModel attachment=new EmailAttachmentModel();
		 attachment.setRealFileName("test");
		 
		 List<EmailAttachmentModel> attachmentList=new ArrayList<EmailAttachmentModel>();
		 attachmentList.add(attachment);
		 ((WishlistProcessModel) businessProcessModel).setEmailAttachmentModelList(attachmentList);
	}

	@Test
	public void testInit() throws CommerceCartRestorationException {
		Mockito.doReturn(wishlistData).when(wishlistConverter).convert(businessProcessModel.getWishlist());
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(new CMSSiteModel());
		Mockito.when(cmsSiteService.getSiteConfig(SHARE_WISHLIST_ENABLED)).thenReturn("true");
		Mockito.when(emailService.getOrCreateEmailAddressForEmail(Mockito.anyString(), Mockito.anyString())).thenReturn(new EmailAddressModel());
		Mockito.when(configurationService.getConfiguration().getString(GPXPRESS_REDIRECT_URL)).thenReturn("https://gpxpress.local:9002");
		shareWishlistEmailContext.init(businessProcessModel, emailPageModel);
	}

}
