package com.gp.commerce.core.order.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.order.dao.GpOrderDao;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import com.gp.commerce.core.order.service.impl.GPOrderServiceImpl;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;

@UnitTest
public class GPOrderServiceImplTest {

	@InjectMocks
	GPOrderServiceImpl service = new GPOrderServiceImpl();
	@Mock
	private GpOrderDao gpOrderDao;
	@Mock
	private CMSSiteModel cmsSiteModel;
	@Mock
	private OrderStatus orderStatus;
	@Mock
	private ModelService modelService;
	@Mock
	private OrderModel orderModel;
	@Mock
	private ConsignmentModel consignmentModel;
 	@Mock
 	private ConsignmentStatus consignmentStatus;
 	@Mock
 	private GPEndUserLegalTermsModel gpEndUserLegalTermsModel;
 	@Mock
 	private ShippingNotificationModel shippingNotificationModel;
 	
	@Before()
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
 		ReflectionTestUtils.setField(service, "modelService", modelService);
 		service.setGpOrderDao(gpOrderDao);
	}
	
	@Test
	public void getFailedOrdersTest() {
		List<OrderModel> list = new ArrayList<OrderModel>();
		list.add(orderModel);
		Mockito.when(gpOrderDao.getOrdersInError(cmsSiteModel, orderStatus)).thenReturn(list);
		assertEquals(service.getFailedOrders(cmsSiteModel, orderStatus), list);
		assertNotNull(service.getFailedOrders(cmsSiteModel, orderStatus));
		
	}
	@Test
	public void getFailedConsignmentsTest() {
		List<ConsignmentModel> list = new ArrayList<ConsignmentModel>();
		list.add(consignmentModel);
		Mockito.when(gpOrderDao.getConsignmentsInError(cmsSiteModel, consignmentStatus)).thenReturn(list);
		assertEquals(service.getFailedConsignments(cmsSiteModel, consignmentStatus), list);
		assertNotNull(service.getFailedConsignments(cmsSiteModel, consignmentStatus));
	}
	@Test
	public void getLeaseAgreementByIdTest() {
		List<GPEndUserLegalTermsModel> list = new ArrayList<GPEndUserLegalTermsModel>();
		list.add(gpEndUserLegalTermsModel);
		Mockito.when(gpOrderDao.getLeaseAgreementById("123456")).thenReturn(list);
		assertEquals(service.getLeaseAgreementById("123456"), list);
		assertNotNull(service.getLeaseAgreementById("123456"));
	}
	@Test
	public void updateOrderConsignmentTest() {
		service.updateOrderConsignment(consignmentModel);
		Mockito.verify(modelService).save(consignmentModel);
	}
	@Test
	public void saveShippingNotificationTest() {
		service.saveShippingNotification(shippingNotificationModel);
		assertNotNull(service.getGpOrderDao());
		Mockito.verify(modelService).save(shippingNotificationModel);
	}

	
 }
