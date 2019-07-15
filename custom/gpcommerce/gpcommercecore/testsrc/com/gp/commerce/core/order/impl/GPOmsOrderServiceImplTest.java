package com.gp.commerce.core.order.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.apache.commons.configuration.Configuration;
import static org.mockito.BDDMockito.given;

import com.gp.commerce.core.order.impl.GPOmsOrderServiceImpl;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelEntryData;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelRequestData;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.GPConsignmentEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class GPOmsOrderServiceImplTest {

 	@InjectMocks
	private GPOmsOrderServiceImpl service = new GPOmsOrderServiceImpl();
 	
	private static final String ORDER_CANCEL_LIMIT = "order.cancel.retry";
	
	@Mock
	private ModelService modelService;
	
	@Mock
	private OrderCancelService orderCancelService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ConfigurationService configurationService;
	
	@Mock
	private StockService stockService;
	
	@Mock
	private EnumerationService enumerationService;
	
	@Mock
	private EventService eventService;
	
	@Mock
	private Configuration configuration;
	
	@Before
	public void setUp() throws Exception {
		
 		MockitoAnnotations.initMocks(this);
 		service.setOrderCancelService(orderCancelService);
 		service.setUserService(userService);
 		service.setConfigurationService(configurationService);
 		service.setStockService(stockService);
 		service.setEnumerationService(enumerationService);
 		service.setEventService(eventService);
 		service.setModelService(modelService);
		given(configurationService.getConfiguration()).willReturn(configuration);
	}
	
	@Test
	public void testGetModelService()
	{
		Assert.assertEquals(modelService,service.getModelService());
	}
	
	@Test
	public void testGetEventService()
	{
		Assert.assertEquals(eventService,service.getEventService());
	}
	
	@Test
	public void testGetEnumerationService()
	{
		Assert.assertEquals(enumerationService,service.getEnumerationService());
	}
	
	@Test
	public void testGetStockService()
	{
		Assert.assertEquals(stockService,service.getStockService());
	}
	
	@Test
	public void testGetConfigurationService()
	{
		Assert.assertEquals(configurationService,service.getConfigurationService());
	}
	
	@Test
	public void testGetUserService()
	{
		Assert.assertNotNull(service.getUserService());
	}
	
	@Test
	public void testGetOrderCancelService()
	{
		Assert.assertEquals(orderCancelService,service.getOrderCancelService());
	}
	
	@Test
	public void testUpdateOrderStatusToCancel()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		
		OrderEntryStatus status = OrderEntryStatus.DEAD;
		
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(Mockito.mock(AbstractOrderEntryModel.class));
		Mockito.when(order.getEntries()).thenReturn(entries);
		Mockito.doNothing().when(modelService).saveAll();
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		
		Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
		ConsignmentEntryModel shippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		consignmentEntries.add(shippedConsignmentEntry);
		
		Mockito.when(shippedConsignment.getConsignmentEntries()).thenReturn(consignmentEntries);
		Mockito.doNothing().when(modelService).save(order);
		service.updateOrderStatusToCancel(order);
	}
	
	@Test
	public void testPartialCancelConsignment() throws OrderCancelException
	{
		final ConsignmentModel consignmentModel=Mockito.mock(ConsignmentModel.class);
		final ConsignmentEntryModel entryModel=Mockito.mock(ConsignmentEntryModel.class);
		final OrderModel orderModel=Mockito.mock(OrderModel.class);
		
		final OrderEntryModel orderEntryModel=Mockito.mock(OrderEntryModel.class);
		final OrderEntryModel orderEntryModel2=Mockito.mock(OrderEntryModel.class);
		
		final UserModel user=Mockito.mock(UserModel.class);
		
		Mockito.when(consignmentModel.getOrder()).thenReturn(orderModel);
		Mockito.when(entryModel.getOrderEntry()).thenReturn(orderEntryModel);
		Mockito.when(entryModel.getOrderEntry().getEntryNumber()).thenReturn(2);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configurationService.getConfiguration().getInt(ORDER_CANCEL_LIMIT, 1)).thenReturn(1);
		Mockito.when(userService.getUserForUID(GpcommerceCoreConstants.ADMIN)).thenReturn(user);
		Mockito.when(entryModel.getConsignment()).thenReturn(consignmentModel);
		Mockito.when(enumerationService.getEnumerationValue(CancelReason.class,"Nothing") ).thenReturn(CancelReason.LATEDELIVERY);
		
		Mockito.when(orderModel.getCode()).thenReturn("123456");
		Mockito.when(modelService.create(OrderProcessModel.class)).thenReturn(Mockito.mock(OrderProcessModel.class));
		
		Mockito.when(orderEntryModel.getQuantity()).thenReturn(1L);
		Mockito.when(orderEntryModel.getOrder()).thenReturn(orderModel);
		Mockito.when(orderEntryModel2.getOrder()).thenReturn(orderModel);
		Mockito.when(orderModel.getStore()).thenReturn(Mockito.mock(BaseStoreModel.class));
		Mockito.when(orderModel.getStore().getWarehouses()).thenReturn(Collections.singletonList(Mockito.mock(WarehouseModel.class)));
		
		List<AbstractOrderEntryModel> entries=new ArrayList<>();
		entries.add(orderEntryModel);
		entries.add(orderEntryModel2);
		Mockito.when(orderModel.getEntries()).thenReturn(entries);
		
		service.partialCancelConsignment(1L, consignmentModel, entryModel,"Nothing");
		Mockito.verify(eventService).publishEvent(Mockito.any());
		
	}
	
	@Test
	public void testPartialCancelConsignmentOrderException() throws OrderCancelException
	{
		final ConsignmentModel consignmentModel=Mockito.mock(ConsignmentModel.class);
		final ConsignmentEntryModel entryModel=Mockito.mock(ConsignmentEntryModel.class);
		final OrderModel orderModel=Mockito.mock(OrderModel.class);
		
		final OrderEntryModel orderEntryModel=Mockito.mock(OrderEntryModel.class);
		final OrderEntryModel orderEntryModel2=Mockito.mock(OrderEntryModel.class);
		
		final UserModel user=Mockito.mock(UserModel.class);
		
		Mockito.when(consignmentModel.getOrder()).thenReturn(orderModel);
		Mockito.when(entryModel.getOrderEntry()).thenReturn(orderEntryModel);
		Mockito.when(entryModel.getOrderEntry().getEntryNumber()).thenReturn(2);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configurationService.getConfiguration().getInt(ORDER_CANCEL_LIMIT, 1)).thenReturn(1);
		Mockito.when(userService.getUserForUID(GpcommerceCoreConstants.ADMIN)).thenReturn(user);
		Mockito.when(entryModel.getConsignment()).thenReturn(consignmentModel);
		Mockito.when(enumerationService.getEnumerationValue(CancelReason.class,"Nothing") ).thenReturn(CancelReason.LATEDELIVERY);
		
		Mockito.when(orderModel.getCode()).thenReturn("123456");
		Mockito.when(modelService.create(OrderProcessModel.class)).thenReturn(Mockito.mock(OrderProcessModel.class));
		
		Mockito.when(orderEntryModel.getQuantity()).thenReturn(1L);
		Mockito.when(orderEntryModel.getOrder()).thenReturn(orderModel);
		Mockito.when(orderEntryModel2.getOrder()).thenReturn(orderModel);
		Mockito.when(orderModel.getStore()).thenReturn(Mockito.mock(BaseStoreModel.class));
		Mockito.when(orderModel.getStore().getWarehouses()).thenReturn(Collections.singletonList(Mockito.mock(WarehouseModel.class)));
		
		List<AbstractOrderEntryModel> entries=new ArrayList<>();
		entries.add(orderEntryModel);
		entries.add(orderEntryModel2);
		Mockito.when(orderModel.getEntries()).thenThrow(OrderCancelException.class);
		service.partialCancelConsignment(1L, consignmentModel, entryModel, "NotInterested");
		Mockito.verifyZeroInteractions(eventService);
	}
	
	@Test
	public void testPartialCancelConsignmentException() throws Exception
	{
		final ConsignmentModel consignmentModel=Mockito.mock(ConsignmentModel.class);
		final ConsignmentEntryModel entryModel=Mockito.mock(ConsignmentEntryModel.class);
		final OrderModel orderModel=Mockito.mock(OrderModel.class);
		
		final OrderEntryModel orderEntryModel=Mockito.mock(OrderEntryModel.class);
		final OrderEntryModel orderEntryModel2=Mockito.mock(OrderEntryModel.class);
		
		final UserModel user=Mockito.mock(UserModel.class);
		
		Mockito.when(consignmentModel.getOrder()).thenReturn(orderModel);
		Mockito.when(entryModel.getOrderEntry()).thenReturn(orderEntryModel);
		Mockito.when(entryModel.getOrderEntry().getEntryNumber()).thenReturn(2);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configurationService.getConfiguration().getInt(ORDER_CANCEL_LIMIT, 1)).thenReturn(1);
		Mockito.when(userService.getUserForUID(GpcommerceCoreConstants.ADMIN)).thenReturn(user);
		Mockito.when(entryModel.getConsignment()).thenReturn(consignmentModel);
		Mockito.when(enumerationService.getEnumerationValue(CancelReason.class,"Nothing") ).thenReturn(CancelReason.LATEDELIVERY);
		
		Mockito.when(orderModel.getCode()).thenReturn("123456");
		Mockito.when(modelService.create(OrderProcessModel.class)).thenReturn(Mockito.mock(OrderProcessModel.class));
		
		Mockito.when(orderEntryModel.getQuantity()).thenReturn(1L);
		Mockito.when(orderEntryModel.getOrder()).thenReturn(orderModel);
		Mockito.when(orderEntryModel2.getOrder()).thenReturn(orderModel);
		Mockito.when(orderModel.getStore()).thenReturn(Mockito.mock(BaseStoreModel.class));
		Mockito.when(orderModel.getStore().getWarehouses()).thenReturn(Collections.singletonList(Mockito.mock(WarehouseModel.class)));
		
		List<AbstractOrderEntryModel> entries=new ArrayList<>();
		entries.add(orderEntryModel);
		entries.add(orderEntryModel2);
		Mockito.when(orderModel.getEntries()).thenThrow(Exception.class);
		service.partialCancelConsignment(1L, consignmentModel, entryModel, "NotInterested");
		Mockito.verifyZeroInteractions(eventService);
	}
	
	@Test
	public void testUpdateConsignmentStatusToCancel()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		
		Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
		ConsignmentEntryModel shippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		consignmentEntries.add(shippedConsignmentEntry);
		
		Mockito.when(shippedConsignment.getConsignmentEntries()).thenReturn(consignmentEntries);
		Mockito.doNothing().when(modelService).saveAll();
		service.updateConsignmentStatusToCancel(order);
	}
	
	@Test
	public void testUpdateProcessingStatus()
	{
		ConsignmentEntryModel consignEntry = Mockito.mock(ConsignmentEntryModel.class);
		GPConsignmentEntryStatus entryStatus = GPConsignmentEntryStatus.PAYMENT_ERROR;
		
		ConsignmentModel consignment = Mockito.mock(ConsignmentModel.class);
		Mockito.when(consignEntry.getConsignment()).thenReturn(consignment);
		
		AbstractOrderModel order = Mockito.mock(AbstractOrderModel.class);
		Mockito.when(consignment.getOrder()).thenReturn(order);
		Mockito.when(order.getCode()).thenReturn("code");
		
		Mockito.doNothing().when(modelService).saveAll(consignEntry, consignment, order);
		service.updateProcessingStatus(consignEntry, entryStatus);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBuildOrderCancelRequestExceptionCase()
	{
		OrderCancelRequestData orderCancelRequestData = Mockito.mock(OrderCancelRequestData.class);
		OrderModel order = Mockito.mock(OrderModel.class);
		Integer entryNumber = new Integer(1);
		
		List<OrderCancelEntryData> entries = new ArrayList<>();
		OrderCancelEntryData entryData = Mockito.mock(OrderCancelEntryData.class);
		entries.add(entryData);
		Mockito.when(orderCancelRequestData.getEntries()).thenReturn(entries);
		
		Mockito.when(entryData.getOrderEntryNumber()).thenReturn(entryNumber);
		
		List<AbstractOrderEntryModel> orderEntries = new ArrayList<>();
		AbstractOrderEntryModel orderEntry = Mockito.mock(AbstractOrderEntryModel.class);
		orderEntries.add(orderEntry);
		Mockito.when(order.getEntries()).thenReturn(orderEntries);
		Mockito.when(orderEntry.getEntryNumber()).thenReturn(entryNumber);
		
		Mockito.when(entryData.getCancelQuantity()).thenReturn(new Long(0));
		Mockito.when(entryData.getNotes()).thenReturn("note");
		
		service.buildOrderCancelRequest(orderCancelRequestData, order);
	}
	
	@Test
	public void testPrepareOrderCancelEntryData()
	{
		AbstractOrderModel order = Mockito.mock(AbstractOrderModel.class);
		Long qtyDeclined = new Long(10);
		ConsignmentEntryModel entryModel = Mockito.mock(ConsignmentEntryModel.class);
		String reason = "reason";
		
		AbstractOrderEntryModel orderEntry = Mockito.mock(AbstractOrderEntryModel.class);
		Mockito.when(entryModel.getOrderEntry()).thenReturn(orderEntry);
		Mockito.when(orderEntry.getEntryNumber()).thenReturn(new Integer(1));
		
		CancelReason cancelReasonEnum = CancelReason.CUSTOMERREQUEST;
		Mockito.when(enumerationService.getEnumerationValue(CancelReason.class, reason)).thenReturn(cancelReasonEnum);
		
		Assert.assertTrue(!service.prepareOrderCancelEntryData(order, qtyDeclined, entryModel, reason).isEmpty());
	}
	
	@Test
	public void testUpdateEntryStatus()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		OrderEntryStatus status = OrderEntryStatus.LIVING;
		
		List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(Mockito.mock(AbstractOrderEntryModel.class));
		Mockito.when(order.getEntries()).thenReturn(entries);
		Mockito.doNothing().when(modelService).saveAll();
		service.updateEntryStatus(order, status);
	}
	
	@Test
	public void testUpdateConsignmentEntryStatus()
	{
		ConsignmentEntryModel consignEntry = Mockito.mock(ConsignmentEntryModel.class);
		GPConsignmentEntryStatus entryStatus = GPConsignmentEntryStatus.PAYMENT_ERROR;
		
		ConsignmentModel consignment = Mockito.mock(ConsignmentModel.class);
		Mockito.when(consignEntry.getConsignment()).thenReturn(consignment);
		
		AbstractOrderModel order = Mockito.mock(AbstractOrderModel.class);
		Mockito.when(consignment.getOrder()).thenReturn(order);
		
		Mockito.doNothing().when(modelService).saveAll(consignment, order);
		Mockito.doNothing().when(modelService).save(consignEntry);
		service.updateConsignmentEntryStatus(consignEntry, entryStatus);
	}
	
	@Test
	public void testUpdateOrderAndConsignmentStatusConsignmentPartiallyShipped()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		ConsignmentModel consignment = Mockito.mock(ConsignmentModel.class);
		
		Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
		ConsignmentEntryModel shippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		ConsignmentEntryModel partiallyShippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		ConsignmentEntryModel cancelledConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		consignmentEntries.add(shippedConsignmentEntry);
		consignmentEntries.add(partiallyShippedConsignmentEntry);
		consignmentEntries.add(cancelledConsignmentEntry);
		
		Mockito.when(consignment.getConsignmentEntries()).thenReturn(consignmentEntries);
		Mockito.when(shippedConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.PARTIALLY_SHIPPED);
		Mockito.when(cancelledConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.CANCELLED);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.doNothing().when(modelService).save(order);
		Mockito.doNothing().when(modelService).saveAll(consignment, order);
		service.updateOrderAndConsignmentStatus(order, consignment);
	}
	
	@Test
	public void testUpdateOrderAndConsignmentStatusConsignmentCancelled()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		ConsignmentModel consignment = Mockito.mock(ConsignmentModel.class);
		
		Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
		ConsignmentEntryModel shippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		ConsignmentEntryModel partiallyShippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		ConsignmentEntryModel cancelledConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		consignmentEntries.add(shippedConsignmentEntry);
		consignmentEntries.add(partiallyShippedConsignmentEntry);
		consignmentEntries.add(cancelledConsignmentEntry);
		
		Mockito.when(consignment.getConsignmentEntries()).thenReturn(consignmentEntries);
		Mockito.when(shippedConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.CANCELLED);
		Mockito.when(partiallyShippedConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.CANCELLED);
		Mockito.when(cancelledConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.CANCELLED);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.doNothing().when(modelService).save(order);
		Mockito.doNothing().when(modelService).saveAll(consignment, order);
		service.updateOrderAndConsignmentStatus(order, consignment);
	}
	
	@Test
	public void testUpdateOrderAndConsignmentStatusConsignmentShipped()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		ConsignmentModel consignment = Mockito.mock(ConsignmentModel.class);
		
		Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
		ConsignmentEntryModel shippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		ConsignmentEntryModel partiallyShippedConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		ConsignmentEntryModel cancelledConsignmentEntry = Mockito.mock(ConsignmentEntryModel.class);
		consignmentEntries.add(shippedConsignmentEntry);
		consignmentEntries.add(partiallyShippedConsignmentEntry);
		consignmentEntries.add(cancelledConsignmentEntry);
		
		Mockito.when(consignment.getConsignmentEntries()).thenReturn(consignmentEntries);
		Mockito.when(shippedConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.SHIPPED);
		Mockito.when(cancelledConsignmentEntry.getConsignmentEntryStatus()).thenReturn(GPConsignmentEntryStatus.SHIPPED);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.doNothing().when(modelService).save(order);
		Mockito.doNothing().when(modelService).saveAll(consignment, order);
		service.updateOrderAndConsignmentStatus(order, consignment);
	}
	
	@Test
	public void testUpdateOrderStatusConsignmentPartiallyShipped()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.PARTIALLY_SHIPPED);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.PAYMENT_ERROR);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.CANCELLED);
		Mockito.doNothing().when(modelService).save(order);
		service.updateOrderStatus(order);
	}
	
	@Test
	public void testUpdateOrderStatusConsignmentCancelled()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.CANCELLED);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.CANCELLED);
		Mockito.doNothing().when(modelService).save(order);
		service.updateOrderStatus(order);
	}
	
	@Test
	public void testUpdateOrderStatusConsignmentCompleted()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.doNothing().when(modelService).save(order);
		service.updateOrderStatus(order);
	}
	
	@Test
	public void testUpdateOrderStatusConsignmentShipped()
	{
		OrderModel order = Mockito.mock(OrderModel.class);
		
		Set<ConsignmentModel> consignments = new HashSet<>();
		ConsignmentModel shippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel partiallyShippedConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel paymentErrorConsignment = Mockito.mock(ConsignmentModel.class);
		ConsignmentModel cancelledConsignment = Mockito.mock(ConsignmentModel.class);
		consignments.add(shippedConsignment);
		consignments.add(partiallyShippedConsignment);
		consignments.add(paymentErrorConsignment);
		consignments.add(cancelledConsignment);
		
		Mockito.when(order.getConsignments()).thenReturn(consignments);
		Mockito.when(shippedConsignment.getStatus()).thenReturn(ConsignmentStatus.SHIPPED);
		Mockito.when(partiallyShippedConsignment.getStatus()).thenReturn(ConsignmentStatus.PAYMENT_ERROR);
		Mockito.when(paymentErrorConsignment.getStatus()).thenReturn(ConsignmentStatus.PAYMENT_ERROR);
		Mockito.when(cancelledConsignment.getStatus()).thenReturn(ConsignmentStatus.CANCELLED);
		Mockito.doNothing().when(modelService).save(order);
		service.updateOrderStatus(order);
	}
	
	@Test
	public void testUpdateOrderProcess()
	{
		final OrderModel order=Mockito.mock(OrderModel.class);
		final OrderProcessModel orderProcess=Mockito.mock(OrderProcessModel.class);
		Mockito.when(modelService.create(OrderProcessModel.class)).thenReturn(orderProcess);
		Mockito.when(order.getCode()).thenReturn("Code");
		service.updateOrderProcess(order);
		Mockito.verify(modelService).save(orderProcess);

	}
	@Test
	public void testUpdateOrder()
	{
		final OrderModel order=Mockito.mock(OrderModel.class);
		service.updateOrder(order);
		Mockito.verify(modelService).save(order);

	}
}