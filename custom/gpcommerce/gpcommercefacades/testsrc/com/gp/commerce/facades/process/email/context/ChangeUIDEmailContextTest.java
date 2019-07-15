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
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class ChangeUIDEmailContextTest {

	@InjectMocks
	ChangeUIDEmailContext context = new ChangeUIDEmailContext();
	@Mock
	CustomerEmailContext CustomerEmailContext;
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

		ChangeUIDEmailContext spy = Mockito.spy(new ChangeUIDEmailContext());
		EmailPageModel abstractPageModel = new EmailPageModel();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) abstractPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		StoreFrontCustomerProcessModel businessProcessModel = new StoreFrontCustomerProcessModel();
		abstractPageModel.setFromEmail("test@test.com");
		CustomerModel customer = new CustomerModel();
		businessProcessModel.setCustomer(customer);
		CustomerData customerData = new CustomerData();
		Mockito.when(customerConverter.convert(Mockito.any(CustomerModel.class))).thenReturn(customerData);
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(Mockito.any(CustomerModel.class)))
				.thenReturn("test");
		context.init(businessProcessModel, abstractPageModel);
	}
	
	@Test
	public void getSiteTest(){
		StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = new StoreFrontCustomerProcessModel();
		BaseStoreModel site = new BaseStoreModel();
		storeFrontCustomerProcessModel.setStore(site);
		context.getSite(storeFrontCustomerProcessModel);
		}
	
	@Test
	public void getEmailLanguageTest(){
		StoreFrontCustomerProcessModel businessProcessModel = new StoreFrontCustomerProcessModel();
		LanguageModel language = new LanguageModel();
		businessProcessModel.setLanguage(language);
	    context.getEmailLanguage(businessProcessModel);
		}
	}
