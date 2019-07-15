package com.gp.commerce.facades.process.email.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.spockframework.mock.MockImplementation;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class NotPickedUpConsignmentCanceledEmailContextTest {
	
	@InjectMocks
	NotPickedUpConsignmentCanceledEmailContext context = new NotPickedUpConsignmentCanceledEmailContext();
	@Mock
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
	@Mock
	private PriceDataFactory priceDataFactory;
	@Mock
    ConsignmentProcessModel consignmentProcessModel;
	@Mock
	EmailPageModel emailPageModel;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
	ConsignmentData data = new ConsignmentData();
	PriceData refundAmount = new PriceData();
	
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		 emailPageModel = new EmailPageModel();
		 consignmentProcessModel = new ConsignmentProcessModel();
		ConsignmentModel consignment = new ConsignmentModel();
		AbstractOrderModel order = new AbstractOrderModel();
		order.setCode("test");
		order.setGuid("guid");
		CustomerModel user = new CustomerModel();
		user.setType(CustomerType.GUEST);
		order.setUser(user);
		consignment.setOrder(order);
		Set<ConsignmentEntryModel> entries = new HashSet<>();
		ConsignmentEntryModel entry = new ConsignmentEntryModel();
		AbstractOrderEntryModel value = new AbstractOrderEntryModel();
		value.setTotalPrice(22.2);
		entry.setOrderEntry(value);
		entries.add(entry);
		consignment.setConsignmentEntries(entries);
		consignmentProcessModel.setConsignment(consignment);
		LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		emailPageModel = new EmailPageModel();
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		emailPageModel.setFromEmail("test@tes.com", Locale.ENGLISH);
		List<ConsignmentEntryData> ee = new ArrayList<>();
		ConsignmentEntryData cd = new ConsignmentEntryData();
		OrderEntryData orderEntry = new OrderEntryData();
		PriceData totalPrice = new PriceData();
		orderEntry.setTotalPrice(totalPrice);
		cd.setOrderEntry(orderEntry);
		ee.add(cd);
		data.setEntries(ee);
		
	}
	
	@Test
	public void initTest() {

		NotPickedUpConsignmentCanceledEmailContext spy = Mockito.spy(new NotPickedUpConsignmentCanceledEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<ConsignmentProcessModel>) spy).init(consignmentProcessModel,
				emailPageModel);
		Mockito.when(consignmentConverter.convert(consignmentProcessModel.getConsignment())).thenReturn(data);
		Mockito.when(priceDataFactory.create(Mockito.any(PriceDataType.class), Mockito.any(BigDecimal.class),
				Mockito.anyString())).thenReturn(refundAmount);
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(Mockito.any(CustomerModel.class))).thenReturn("abc@xyz.com");
		context.init(consignmentProcessModel, emailPageModel);
	}

}
