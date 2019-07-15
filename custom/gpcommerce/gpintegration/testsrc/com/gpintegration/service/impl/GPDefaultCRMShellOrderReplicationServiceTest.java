package com.gpintegration.service.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gpintegration.crm.shellorder.dto.GPCRMShellOrderResponseDTO;
import com.gpintegration.exception.GPIntegrationException;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;

@UnitTest
public class GPDefaultCRMShellOrderReplicationServiceTest {
	
	@Mock
	private GPDefaultCRMShellOrderReplicationService gpShellOrderService;
	
	@InjectMocks
	private OrderModel orderModel;
	
	@InjectMocks
	private GPCRMShellOrderResponseDTO crmResponse;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void replicateCRMShellOrderTest() throws GPIntegrationException {
		when(gpShellOrderService.replicateCRMShellOrder(Mockito.anyObject())).thenReturn(true);
		assertTrue(gpShellOrderService.replicateCRMShellOrder(orderModel));
		
	}
	
	
	@Test
	public void updateCRMShellOrderTest() throws GPIntegrationException {
		gpShellOrderService.updateCRMShellOrder(Mockito.anyObject());
		
	}
	
	

}
