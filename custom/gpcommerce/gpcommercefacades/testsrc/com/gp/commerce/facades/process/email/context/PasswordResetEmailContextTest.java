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
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class PasswordResetEmailContextTest {


	@InjectMocks
	private PasswordResetEmailContext passwordResetEmailContext = new PasswordResetEmailContext();
	@Mock
	private Converter<UserModel, CustomerData> customerConverter;
	private CustomerData customerData;
	
	@Mock
	private CustomerModel customerModel;
	@Mock
	private EmailPageModel emailPageModel;

	@Mock
	private StoreFrontCustomerProcessModel storeFrontCustomerProcessModel;
	
    
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		customerData=new CustomerData();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel=new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);

		customerModel=new CustomerModel();
		storeFrontCustomerProcessModel=new StoreFrontCustomerProcessModel();
	}



	@Test
	public void testInit() throws CommerceCartRestorationException {
		PasswordResetEmailContext spy = Mockito.spy(new PasswordResetEmailContext());
		Mockito.doReturn(customerModel).when(spy).getCustomer(storeFrontCustomerProcessModel);
		Mockito.doReturn(customerData).when(customerConverter).convert(customerModel);
		passwordResetEmailContext.init(storeFrontCustomerProcessModel,emailPageModel);
	}

}
