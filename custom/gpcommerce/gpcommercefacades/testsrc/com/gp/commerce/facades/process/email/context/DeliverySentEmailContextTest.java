package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.Date;
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

import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.facade.data.TrackingData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

@UnitTest
public class DeliverySentEmailContextTest {

	
	@InjectMocks
	DeliverySentEmailContext context = new DeliverySentEmailContext();
	@Mock
	private Converter<OrderModel, OrderData> orderConverter;
	@Mock
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
	@Mock
	public Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	@Mock
	public Converter<TrackingModel, TrackingData> gpConsignmentTrackingConverter;
	@Mock
	private CustomerEmailResolutionService customerEmailResolutionService;
    @Mock	
	ConsignmentProcessModel consignmentProcessModel;
    @Mock
    EmailPageModel emailPageModel;	
    OrderData orderData = new OrderData();
    ConsignmentData consignmentData = new ConsignmentData();
    TrackingData trackingData = new TrackingData();
     ConsignmentEntryData consignmentEntryData = new ConsignmentEntryData();
    private BaseSiteModel site;
	
    @Before
    public void setup(){

    	MockitoAnnotations.initMocks(this);
    	site = new BaseSiteModel();
    	consignmentProcessModel = new ConsignmentProcessModel();
    	emailPageModel = new EmailPageModel();
    	ConsignmentModel consignmentModel = new ConsignmentModel();
    	OrderModel order = new OrderModel();
    	CustomerModel user = new CustomerModel();
    	user.setType(CustomerType.GUEST);
    	order.setUser(user);
    	consignmentModel.setOrder(order);
    	consignmentProcessModel.setConsignment(consignmentModel);
    	List<ConsignmentData> consignments = new ArrayList<>();
    	ConsignmentData data = new ConsignmentData();
    	List<ConsignmentEntryData> ent = new ArrayList<>();
    	ConsignmentEntryData cdata = new ConsignmentEntryData();
    	ent.add(cdata);
    	data.setEntries(ent);
    	consignments.add(data);
    	orderData.setConsignments(consignments);
    	DeliveryModeData deliveryMode = new DeliveryModeData();
    	deliveryMode.setDescription("description");
    	orderData.setDeliveryMode(deliveryMode);
    	List<TrackingModel> trackingList = new ArrayList<>();
    	TrackingModel trackingEntries = new TrackingModel();
    	trackingList.add(trackingEntries);
    	consignmentProcessModel.setTrackings(trackingList);
    	Date created = new Date();
        orderData.setCreated(created);
        emailPageModel.setFromEmail("test@test.com", Locale.ENGLISH);
        LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		
		ItemModelContextImpl itemModelContext = (ItemModelContextImpl) emailPageModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
        ItemModelContextImpl storeItemModelContext = (ItemModelContextImpl) site.getItemModelContext();
		storeItemModelContext.setLocaleProvider(localeProvider);
    }
	
	@Test
	public void initTest(){
		
		DeliverySentEmailContext spy = Mockito.spy(new DeliverySentEmailContext());
		Mockito.doNothing().when((AbstractEmailContext<ConsignmentProcessModel>) spy).init(consignmentProcessModel,emailPageModel);
		Mockito.doReturn(orderData).when(orderConverter).convert((OrderModel) consignmentProcessModel.getConsignment().getOrder());
		
		Mockito.when(customerEmailResolutionService.getEmailForCustomer(Mockito.any(CustomerModel.class))).thenReturn("test");
		Mockito.doReturn(consignmentData).when(consignmentConverter).convert(consignmentProcessModel.getConsignment());
		ConsignmentEntryData consignmentEntryData = new ConsignmentEntryData();
		Mockito.when(consignmentEntryConverter.convert(Mockito.any(ConsignmentEntryModel.class))).thenReturn(consignmentEntryData);
		TrackingData trackingData = new TrackingData();
		Mockito.when(gpConsignmentTrackingConverter.convert(Mockito.any(TrackingModel.class))).thenReturn(trackingData);
		context.init(consignmentProcessModel, emailPageModel);	
		
	}
	
}
