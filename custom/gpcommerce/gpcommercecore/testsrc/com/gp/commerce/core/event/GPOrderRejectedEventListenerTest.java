package com.gp.commerce.core.event;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gp.commerce.core.services.event.GPOrderRejectedEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPOrderRejectedEventListenerTest {
	
	@Mock
	ModelService modelService;

	@Mock
	BusinessProcessService businessProcessService;
	
	GPOrderRejectedEvent itemEvent;
	
	@Mock
	OrderModel order;
	
	@Mock
	OrderProcessModel orderProcess;
	
	@InjectMocks
	public GPOrderRejectedEventListener gpOrderRejectedEventListener=new GPOrderRejectedEventListener();

	@Before
	public void setup() {

		gpOrderRejectedEventListener.setBusinessProcessService(businessProcessService);
		gpOrderRejectedEventListener.setModelService(modelService);
		
		itemEvent = new GPOrderRejectedEvent(orderProcess);
		BaseSiteModel site = new BaseSiteModel();
		site.setChannel(SiteChannel.B2C);
		
		Mockito.when(orderProcess.getOrder()).thenReturn(order);
		Mockito.when(order.getSite()).thenReturn(site);
		Mockito.when(order.getCode()).thenReturn("ER45678IJ");
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(orderProcess);

	}
	
	@Test
	public void onSiteEventTest() {
		gpOrderRejectedEventListener.onSiteEvent(itemEvent);
		Mockito.verify(businessProcessService).startProcess(orderProcess);
		Mockito.verify(modelService).save(orderProcess);
		;

	}

	@Test
	public void getSiteChannelForEventTest() {
		assertTrue(gpOrderRejectedEventListener.getSiteChannelForEvent(itemEvent).getCode()
				.equalsIgnoreCase(SiteChannel.B2C.getCode()));
	}

}
