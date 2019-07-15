/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import java.util.Locale;

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
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class UpdateProfileEmailContextTest {

	@InjectMocks
	private UpdateProfileEmailContext updateProfileEmail = new UpdateProfileEmailContext();
	@Mock
	private Converter<UserModel, CustomerData> customerConverter;
	private CustomerData customerData;

	@Mock
	private CustomerModel customerModel;
	@Mock
	private UserModel userModel;
	private EmailPageModel emailPageModel;

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

		emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);
	}

	@Test
	public void testInit() throws CommerceCartRestorationException {
		UpdateProfileEmailContext spy = Mockito.spy(new UpdateProfileEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<StoreFrontCustomerProcessModel>) spy)
				.init(storeFrontCustomerProcessModel, emailPageModel);
		Mockito.doReturn(customerModel).when(spy).getCustomer(storeFrontCustomerProcessModel);
		Mockito.doReturn(customerData).when(customerConverter).convert(customerModel);
		updateProfileEmail.init(storeFrontCustomerProcessModel, emailPageModel);
	}

}
