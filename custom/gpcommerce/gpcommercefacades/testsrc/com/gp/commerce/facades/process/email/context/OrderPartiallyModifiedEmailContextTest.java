package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.orderprocessing.model.OrderModificationProcessModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class OrderPartiallyModifiedEmailContextTest {

	@InjectMocks
	OrderPartiallyModifiedEmailContext context = new OrderPartiallyModifiedEmailContext();	
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;
	@Mock
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;
    @Mock
	OrderModificationProcessModel orderProcessModel;
    @Mock
	EmailPageModel emailPageModel;
    @Mock
	private CustomerEmailResolutionService customerEmailResolutionService;

	OrderData orderData = new OrderData();

	OrderEntryData orderEntryData = new OrderEntryData();

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		OrderModel order = new OrderModel();
		emailPageModel = new EmailPageModel();
		orderProcessModel = new OrderModificationProcessModel();
		order.setCode("code");
		order.setGuid("guid");
		CustomerModel user = new CustomerModel();
		user.setType(CustomerType.GUEST);
		order.setUser(user);
		BaseStoreModel store = new BaseStoreModel();
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
	    ItemModelContextImpl itemModelContext = (ItemModelContextImpl) store.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		ItemModelContextImpl itemModel = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModel.setLocaleProvider(localeProvider);
		
		store.setName("test", Locale.ENGLISH);
		order.setStore(store);
		orderProcessModel.setOrder(order);
		OrderModificationRecordEntryModel orderModification = new OrderModificationRecordEntryModel();
		List<OrderEntryModificationRecordEntryModel> entries = new ArrayList<>();
		OrderEntryModificationRecordEntryModel entryModel = new OrderEntryModificationRecordEntryModel();
		OrderEntryModel entryValue = new OrderEntryModel();
		entryModel.setOriginalOrderEntry(entryValue);
		entries.add(entryModel);
		orderModification.setOrderEntriesModificationEntries(entries);
		orderProcessModel.setOrderModificationRecordEntry(orderModification);
		emailPageModel.setFromEmail("test@test.com", Locale.ENGLISH);

	}

	@Test
	public void testInit(){
		OrderPartiallyModifiedEmailContext spy = Mockito.spy(new OrderPartiallyModifiedEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<OrderModificationProcessModel>) spy).init(orderProcessModel, emailPageModel);
		Mockito.when(orderConverter.convert(orderProcessModel.getOrder())).thenReturn(orderData);
		Mockito.when(orderEntryConverter.convert(Mockito.any(OrderEntryModel.class))).thenReturn(orderEntryData);
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(Mockito.any(CustomerModel.class))).thenReturn("abc@xyz.com");
		context.init(orderProcessModel, emailPageModel);

	}


}
