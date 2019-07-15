package com.gp.commerce.facades.process.email.context;

import java.util.Date;
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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class GPOrderRejectedEmailContextTest {

	@InjectMocks
	GPOrderRejectedEmailContext context = new GPOrderRejectedEmailContext();
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;
	@Mock
	OrderProcessModel businessProcessModel;
	@Mock
	EmailPageModel abstractPageModel;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	OrderData orderData = new OrderData();
	private BaseSiteModel site;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		 abstractPageModel = new EmailPageModel();
		 businessProcessModel = new OrderProcessModel();
		OrderModel order = new OrderModel();
		order.setCode("test");
		order.setGuid("guid");
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		BaseStoreModel store = new BaseStoreModel();
		
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) abstractPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		
		
		ItemModelContextImpl itemModelContextOrder = (ItemModelContextImpl) order.getItemModelContext();
		itemModelContextOrder.setLocaleProvider(localeProvider);

		site = new BaseSiteModel();
		ItemModelContextImpl storeItemModelContext = (ItemModelContextImpl) store.getItemModelContext();
		storeItemModelContext.setLocaleProvider(localeProvider);
		
		CustomerModel user = new CustomerModel();
		user.setType(CustomerType.GUEST);
		order.setUser(user);
		
		store.setName("test");
		order.setStore(store);
		businessProcessModel.setOrder(order);
		Date created = new Date();
		orderData.setCreated(created);
	}

	@Test
	public void initTest() {
		GPOrderRejectedEmailContext spy = Mockito.spy(new GPOrderRejectedEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<OrderProcessModel>) spy).init(businessProcessModel,
				abstractPageModel);
		Mockito.doReturn(orderData).when(orderConverter).convert(businessProcessModel.getOrder());
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(Mockito.any(CustomerModel.class)))
		.thenReturn("test");
		context.init(businessProcessModel, abstractPageModel);

	}

}
