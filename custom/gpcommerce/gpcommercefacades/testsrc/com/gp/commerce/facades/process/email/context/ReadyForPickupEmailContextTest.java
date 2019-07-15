/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class ReadyForPickupEmailContextTest {

	@InjectMocks
	private ReadyForPickupEmailContext readyForPickupEmail = new ReadyForPickupEmailContext();
	@Mock
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
	private ConsignmentData consignmentData;
	private String orderCode;
	private String orderGuid;
	private boolean guest;

	private OrderData orderData;

	@Mock
	private CustomerModel customerModel;
	@Mock
	private UserModel userModel;
	private EmailPageModel emailPageModel;
	@Mock
	private AbstractEmailContext emailContext;

	private OrderModel orderModel;

	private BaseSiteModel site;

	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private UrlEncoderService urlEncoderService;
	private LanguageModel language;

	private ConsignmentProcessModel consignmentProcessModel;
	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		consignmentData = new ConsignmentData();
		consignmentModel = new ConsignmentModel();
		consignmentProcessModel = new ConsignmentProcessModel();
		language = new LanguageModel();
		language.setIsocode("UTF-8");
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel = new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		site = new BaseSiteModel();
		ItemModelContextImpl storeItemModelContext = (ItemModelContextImpl) site.getItemModelContext();
		storeItemModelContext.setLocaleProvider(localeProvider);

		orderData = new OrderData();
		customerModel = new CustomerModel();
		customerModel.setType(CustomerType.GUEST);
		customerModel.setUid("test@tes.com");
		orderModel = new OrderModel();

		site.setName("store name", Locale.ENGLISH);
		orderModel.setCode("testOrder");
		orderModel.setUser(customerModel);
		orderModel.setSite(site);
		orderModel.setLanguage(language);
		consignmentModel.setOrder(orderModel);
		consignmentProcessModel.setConsignment(consignmentModel);
		emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);

	}

	@Test
	public void testInit() throws CommerceCartRestorationException {
		ReadyForPickupEmailContext spy = Mockito.spy(new ReadyForPickupEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<ConsignmentProcessModel>) spy).init(consignmentProcessModel,
				emailPageModel);
		Mockito.doReturn(consignmentData).when(consignmentConverter).convert(consignmentProcessModel.getConsignment());
		orderCode = consignmentProcessModel.getConsignment().getOrder().getCode();
		orderGuid = consignmentProcessModel.getConsignment().getOrder().getGuid();
		Mockito.doReturn(customerModel).when(spy).getCustomer(consignmentProcessModel);
		Assert.assertEquals(CustomerType.GUEST, customerModel.getType());

		readyForPickupEmail.init(consignmentProcessModel, emailPageModel);
	}

}
