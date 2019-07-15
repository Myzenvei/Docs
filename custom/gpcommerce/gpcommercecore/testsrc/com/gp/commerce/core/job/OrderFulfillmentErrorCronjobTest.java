package com.gp.commerce.core.job;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import com.gp.commerce.core.model.FailedOrderCronjobModel;
import com.gp.commerce.core.model.FailedOrderEmailProcessModel;
import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.core.order.service.GpOrderService;
import com.gp.commerce.core.services.GPConsignmentService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.model.ModelService;

@PowerMockIgnore(
{ "org.apache.logging.log4j.*" }) 
@UnitTest
public class OrderFulfillmentErrorCronjobTest {
 
@InjectMocks
OrderFulfillmentErrorCronjob cron = new OrderFulfillmentErrorCronjob();
private FailedOrderCronjobModel failedcronjob ;
private BusinessProcessService businessProcessService ;
private static final String SEND_FAILED_ORDER_EMAIL = "sendFailedOrderEmail";
private static final String SEND_FAILED_ORDER_EMAIL_LABEL = "sendFailedOrderEmail - ";
private OrderStatus orderProcessingStatus ;
private CMSSiteModel site ;
private GpOrderService gpOrderService;
private ModelService modelService ;
private FailedOrderEmailProcessModel failedOrderEmailProcessModel ;
private ConsignmentStatus consignProcessingStatus ;
 
 
List<OrderModel> failedOrders = new ArrayList<>();
List<ConsignmentModel> failedConsignments = new ArrayList<>();

		@Before
		public void setUp() throws Exception {
			site = Mockito.mock(CMSSiteModel.class);
			orderProcessingStatus=Mockito.mock(OrderStatus.class);
			businessProcessService=Mockito.mock(BusinessProcessService.class);
			gpOrderService=Mockito.mock(GpOrderService.class);
			 modelService= Mockito.mock(ModelService.class);
			failedOrderEmailProcessModel=Mockito.mock(FailedOrderEmailProcessModel.class);
			failedcronjob = Mockito.mock(FailedOrderCronjobModel.class);
			consignProcessingStatus=Mockito.mock(ConsignmentStatus.class);
			failedcronjob.setSite(site);
			cron.setBusinessProcessService(businessProcessService);
			cron.setGpOrderService(gpOrderService);
			cron.setModelService(modelService);
 
			
		}
		@Test
		public void testPerform() {
			Mockito.when(failedcronjob.getSite()).thenReturn(site);
			Mockito.when(failedOrderEmailProcessModel.getProcessState()).thenReturn(ProcessState.SUCCEEDED);
			Mockito.when(failedcronjob.getOrderProcessingStatus()).thenReturn(orderProcessingStatus);
			Mockito.when(failedcronjob.getConsignProcessingStatus()).thenReturn(consignProcessingStatus);
			Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString())).thenReturn(failedOrderEmailProcessModel) ;
			Mockito.when(gpOrderService.getFailedOrders(site,orderProcessingStatus)).thenReturn(failedOrders);
			Mockito.when(gpOrderService.getFailedConsignments(site,consignProcessingStatus)).thenReturn(failedConsignments);
			Assert.assertEquals(cron.perform(failedcronjob).getResult().getCode(), "SUCCESS") ;
 
			}
		
		@Test
		public void testPerformError() {
			Mockito.when(failedcronjob.getSite()).thenReturn(site);
			Mockito.when(failedOrderEmailProcessModel.getProcessState()).thenReturn(ProcessState.ERROR);
			Mockito.when(failedcronjob.getOrderProcessingStatus()).thenReturn(orderProcessingStatus);
			Mockito.when(failedcronjob.getConsignProcessingStatus()).thenReturn(consignProcessingStatus);
			Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString())).thenReturn(failedOrderEmailProcessModel) ;
			Mockito.when(gpOrderService.getFailedOrders(site,orderProcessingStatus)).thenReturn(failedOrders);
			Mockito.when(gpOrderService.getFailedConsignments(site,consignProcessingStatus)).thenReturn(failedConsignments);
			Assert.assertEquals(cron.perform(failedcronjob).getResult().getCode(), "ERROR") ;
			}
		
 
 
		
}
