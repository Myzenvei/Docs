package com.gp.commerce.populators;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

@UnitTest
public class GPDeliveryModePopulatorTest {

	@InjectMocks
	GPDeliveryModePopulator populator = new GPDeliveryModePopulator();

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void populateTest() {
		DeliveryModeModel source = new DeliveryModeModel();
		DeliveryModeData target = new DeliveryModeData();
		source.setDeliveryPrice("66");
		populator.populate(source, target);

	}

}
