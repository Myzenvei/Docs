/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import static org.junit.Assert.*;

import org.drools.core.command.assertion.AssertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.facade.data.TrackingData;

import de.hybris.bootstrap.annotations.UnitTest;

/**
 * @author akapandey
 */
@UnitTest
public class GPConsigmentTrackingPopulatorTest {

	private static final String CARRIER = "consignment";
	private static final String TRACKINGID = "11019";
	private static final String TRACKINGURL = "abc@xyz.com";
	
	@InjectMocks
	GPConsigmentTrackingPopulator populator = new GPConsigmentTrackingPopulator();

	TrackingModel source;

	TrackingData target;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		source= new TrackingModel();
		source.setCarrier(CARRIER);
		source.setTrackingID(TRACKINGID);
		source.setTrackingURL(TRACKINGURL);
		target=new TrackingData();
	}
	
	@Test
	public void testPopulate()
	{
		populator.populate(source, target);
		assertEquals(target.getCarrier(),source.getCarrier());
		assertEquals(target.getTrackingID(), source.getTrackingID());
		assertEquals(target.getTrackingURL(), source.getTrackingURL());
		
	}
}
