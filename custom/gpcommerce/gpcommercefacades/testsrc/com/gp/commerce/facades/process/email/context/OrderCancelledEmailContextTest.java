package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.facade.order.data.SplitEntryData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class OrderCancelledEmailContextTest {
	@InjectMocks
	OrderCancelledEmailContext context = new OrderCancelledEmailContext();
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;
	@Mock
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	@Mock
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	@Mock
	OrderProcessModel orderProcessModel;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	@Mock
	EmailPageModel emailPageModel;
	
	OrderData orderData = new OrderData();
	
	private ConsignmentEntryData consignmentEntryData = new ConsignmentEntryData();
	
	@Before
	public void setUp(){
		
		MockitoAnnotations.initMocks(this);
		OrderModel order = new OrderModel();
		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(order);
		CustomerModel user = new CustomerModel();
		emailPageModel = new EmailPageModel();
		user.setType(CustomerType.GUEST);
		order.setUser(user);
		BaseStoreModel store = new BaseStoreModel();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		
		ItemModelContextImpl itemModelContextt = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContextt.setLocaleProvider(localeProvider);
		
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) store.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		store.setName("store", Locale.ENGLISH);
		order.setStore(store);
		List<OrderEntryData> entries = new ArrayList<>();
		OrderEntryData entry = new OrderEntryData();
		List<SplitEntryData> splitEntries = new ArrayList<>();
		SplitEntryData se = new SplitEntryData();
		se.setQty("77");
		splitEntries.add(se);
		entry.setSplitEntries(splitEntries);
		entries.add(entry);
		orderData.setEntries(entries);
		ConsignmentEntryModel consignmentEntries = new ConsignmentEntryModel();
		orderProcessModel.setConsignmentEntry(consignmentEntries);
		orderData.setCode("test");
		orderData.setGuid("guid");
		Date created = new Date();
		orderData.setCreated(created);
		context.setSiteBaseUrlResolutionService(siteBaseUrlResolutionService);
		emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);
		
	}
	@Test
	public void testInit(){
		
		String baseUrl = "test";
		OrderCancelledEmailContext spy = Mockito.spy(new OrderCancelledEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<OrderProcessModel>) spy).init(orderProcessModel,
				emailPageModel);
		Mockito.when(orderConverter.convert(orderProcessModel.getOrder())).thenReturn(orderData);
		Mockito.when(consignmentEntryConverter.convert(orderProcessModel.getConsignmentEntry())).thenReturn(consignmentEntryData);
		Mockito.when(siteBaseUrlResolutionService.getWebsiteUrlForSite(Mockito.any(BaseSiteModel.class), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(baseUrl);
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(Mockito.any(CustomerModel.class))).thenReturn("abc@xyz.com");
		context.init(orderProcessModel, emailPageModel);	
	}

}
