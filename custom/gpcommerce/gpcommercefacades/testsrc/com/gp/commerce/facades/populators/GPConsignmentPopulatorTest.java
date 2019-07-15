/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
/**
 * @author akapandey
 */
package com.gp.commerce.facades.populators;
import java.util.Collections;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

@UnitTest
public class GPConsignmentPopulatorTest {

	@InjectMocks
	GPConsignmentPopulator populator = new GPConsignmentPopulator();
	
	private static final String CODE = "code";
	private static final String TRACKINGID = "trackingID";
	

	ConsignmentModel source = new ConsignmentModel();
	ConsignmentData target = new ConsignmentData();
	@Mock
	Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	@Mock
	ConsignmentEntryModel consignmentEntryModel;
	
	@Mock
	AddressModel addressModel;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		populator.setConsignmentEntryConverter(consignmentEntryConverter);
		source.setStatus(ConsignmentStatus.READY_FOR_PICKUP);
		source.setShippingAddress(addressModel);
		source.setConsignmentEntries(Collections.singleton(consignmentEntryModel));
		Mockito.when(consignmentEntryConverter.convert(consignmentEntryModel)).thenReturn(Mockito.mock(ConsignmentEntryData.class));
		source.setCode(CODE);
		source.setTrackingID(TRACKINGID);
		
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		Assert.assertEquals(target.getProcessingStatus(), source.getProcessingStatus());
		Assert.assertEquals(target.getCode(), source.getCode());
		Assert.assertEquals(target.getTrackingID(), source.getTrackingID());		
	}
}
