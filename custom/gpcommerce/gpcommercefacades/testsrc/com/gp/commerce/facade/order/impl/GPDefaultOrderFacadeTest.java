package com.gp.commerce.facade.order.impl;

import org.junit.Test;

import de.hybris.bootstrap.annotations.UnitTest;

import de.hybris.platform.consignmenttrackingservices.service.ConsignmentTrackingService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import static org.mockito.Mockito.mock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.spockframework.mock.MockImplementation;
import org.mockito.InjectMocks;

import com.gp.commerce.core.services.GPConsignmentTrackingService;
import com.gp.commerce.facade.data.NsConsignmentData;
import com.gp.commerce.facade.data.NsItemData;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.AssertTrue;

import org.apache.commons.configuration.Configuration;

@UnitTest
public class GPDefaultOrderFacadeTest {

	@InjectMocks
	GPDefaultOrderFacade gpDefaultOrderFacade = new GPDefaultOrderFacade();

	@Mock
	private ConfigurationService configurationService;

	@Mock
	Configuration configuration;

	@Mock
	private GPConsignmentTrackingService gpConsignmentTrackingService;

	@Mock
	private ModelService modelService;

	public GPDefaultOrderFacadeTest() {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void updateOrderStatusTest() {

		NsConsignmentData consignmentData = new NsConsignmentData();
		ConsignmentModel consignmentModel =mock(ConsignmentModel.class);
		List<NsItemData> items = new ArrayList<>();
		NsItemData item = new NsItemData();
		item.setConsignmentEntryNumber(5);
		item.setQuantityConfirmed(4);
		item.setQuantityRejected(2);
		item.setQuantityShipped(3);
		item.setTrackingId("trackingid");
		item.setTrackingUrl("url");
		items.add(item);
		consignmentData.setItems(items);
		consignmentData.setHybrisConsignmentId("testid");
		consignmentData.setNsPaymentId("paymentId");
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(gpConsignmentTrackingService.getConsignmentByCode("testid")).thenReturn(consignmentModel);
		// Mockito.doNothing().when(modelService).save(consignmentModel);
		Mockito.doNothing().when(modelService).save(Mockito.any());
		NsConsignmentData data = gpDefaultOrderFacade.updateOrderStatus(consignmentData);
		boolean value = (data.isStatus() == true ? true : false);
		assertTrue("value should be true", value);
	}

	@Test
	public void updateOrderStatusNullTest() {

		NsConsignmentData consignmentData = new NsConsignmentData();
		ConsignmentModel consignmentModel = new ConsignmentModel();
		consignmentModel = null;
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString("gp.error.invalid.consignment.code")).thenReturn("102");
		when(configuration.getString("gp.error.invalid.consignment.msg")).thenReturn("Invalid Consignment");
		when(gpConsignmentTrackingService.getConsignmentByCode("testid")).thenReturn(consignmentModel);
		NsConsignmentData data = gpDefaultOrderFacade.updateOrderStatus(consignmentData);
		assertEquals("102", data.getErrorCode());
		assertEquals("Invalid Consignment", data.getErrorMessage());
	}
}
