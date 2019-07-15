package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import junit.framework.Assert;

@UnitTest
public class GPDebitCalculationServiceImplTest {

	@Mock
	OrderModel order;

	@Mock
	ConsignmentModel consignment;

	@Mock
	OrderEntryModel orderEntry;

	@Mock
	private ConsignmentEntryModel consignmentEntry;

	@InjectMocks
	GPDebitCalculationServiceImpl gpDebitCalculationService = new GPDebitCalculationServiceImpl();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		Mockito.when(consignmentEntry.getOrderEntry()).thenReturn(orderEntry);
		Mockito.when(consignmentEntry.getConsignment()).thenReturn(consignment);
		Mockito.when(consignmentEntry.getDeliveryCost()).thenReturn(2.00);
		Mockito.when(consignmentEntry.getQuantity()).thenReturn((long)2);
		
		Mockito.when(orderEntry.getOrder()).thenReturn(order);
		Mockito.when(orderEntry.getQuantity()).thenReturn((long) 3);
		Set<ConsignmentEntryModel> set=new HashSet<>();
		set.add(consignmentEntry);
		Mockito.when(consignment.getConsignmentEntries()).thenReturn(set);
		Mockito.when(order.getEntries()).thenReturn((Collections.singletonList(orderEntry)));
		Mockito.when(orderEntry.getTotalPrice()).thenReturn(10.00);
		Mockito.when(orderEntry.getQuantity()).thenReturn((long) 3);
		Mockito.when(order.getTotalDiscounts()).thenReturn(5.00);
		Mockito.when(order.getDeliveryCost()).thenReturn(2.00);
		Mockito.when(consignmentEntry.getTotalTax()).thenReturn(1.00);
		
	}

	@Test
	public void testCalculateDebit() {
		BigDecimal bigD = gpDebitCalculationService.calculateDebit(2, consignmentEntry);
		assertTrue(bigD.intValue()==6);
	}

}
