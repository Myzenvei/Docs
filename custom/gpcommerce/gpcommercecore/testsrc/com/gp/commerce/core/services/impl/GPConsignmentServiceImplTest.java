package com.gp.commerce.core.services.impl;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gp.commerce.core.dao.GPConsignmentDao;
import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.model.TrackingModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class GPConsignmentServiceImplTest {

	@Mock
	private GPConsignmentDao gpConsignmentDao;

	@InjectMocks
	GPConsignmentServiceImpl gpConsignmentService = new GPConsignmentServiceImpl();

	@Mock
	TrackingModel tracking;

	@Mock
	ShippingNotificationModel shippingNotification;

	@Mock
	ConsignmentEntryModel consignment;

	@Before
	public void setup() {

		gpConsignmentService.setGpConsignmentDao(gpConsignmentDao);
		Mockito.when(gpConsignmentDao.getConsignmentEntryByEntryNumber(12, "123456")).thenReturn(consignment);
		Mockito.when(gpConsignmentDao.getShippingNotificationDetails("shippingNotificationCode"))
				.thenReturn(shippingNotification);
	}

	@Test
	public void getShippingNotificationDetailsTest() {
		assertTrue(gpConsignmentService.getShippingNotificationDetails("shippingNotificationCode")
				.equals(shippingNotification));
	}

	@Test
	public void getConsignmentEntryByEntryNumberTest() {
		assertTrue(gpConsignmentService.getConsignmentEntryByEntryNumber(12, "123456").equals(consignment));
	}

	@Test
	public void getTrackingDetailsForEmailTest() {
		Mockito.when(gpConsignmentDao.getTrackingDetailsForEmail()).thenReturn(Collections.singletonList(tracking));

		assertTrue(!gpConsignmentService.getTrackingDetailsForEmail().isEmpty());
		assertTrue(gpConsignmentService.getTrackingDetailsForEmail().get(0).equals(tracking));
	}

	@Test
	public void getTrackingDetailsForEmailEmptyTest() {

		Mockito.when(gpConsignmentDao.getTrackingDetailsForEmail()).thenReturn(Collections.emptyList());
		assertTrue(gpConsignmentService.getTrackingDetailsForEmail().isEmpty());

	}
}
