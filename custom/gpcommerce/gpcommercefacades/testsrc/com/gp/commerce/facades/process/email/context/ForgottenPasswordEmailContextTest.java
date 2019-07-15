package com.gp.commerce.facades.process.email.context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

@UnitTest
public class ForgottenPasswordEmailContextTest {
	@InjectMocks
	ForgottenPasswordEmailContext forgottenPasswordEmailContext = new ForgottenPasswordEmailContext();
	private BaseSiteModel site;
	@Mock
	private Converter<UserModel, CustomerData> customerConverter;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void initTest() {
		site = new BaseSiteModel();
		
		ForgottenPasswordEmailContext spy = Mockito.spy(new ForgottenPasswordEmailContext());
		ForgottenPasswordProcessModel storeFrontCustomerProcessModel = new ForgottenPasswordProcessModel();
		CustomerModel user = new CustomerModel();
		storeFrontCustomerProcessModel.setCustomer(user);
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(user)).thenReturn("abc@xyz.com");
		storeFrontCustomerProcessModel.setToken("test");
		EmailPageModel emailPageModel = new EmailPageModel();
		emailPageModel.setFromEmail("test@test.com", Locale.ENGLISH);
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
        ItemModelContextImpl storeItemModelContext = (ItemModelContextImpl) site.getItemModelContext();
		storeItemModelContext.setLocaleProvider(localeProvider);
		Mockito.doNothing().when((CustomerEmailContext) spy).init(storeFrontCustomerProcessModel, emailPageModel);
		CustomerData data = new CustomerData();
		Mockito.when(customerConverter.convert(Mockito.any(CustomerModel.class))).thenReturn(data);
		forgottenPasswordEmailContext.init(storeFrontCustomerProcessModel, emailPageModel);
	}

}
