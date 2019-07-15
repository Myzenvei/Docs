package com.gp.commerce.facades.process.email.context;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

@UnitTest
public class OrderPartiallyCanceledEmailContextTest {

	@InjectMocks
	OrderPartiallyCanceledEmailContext context = new OrderPartiallyCanceledEmailContext();

	@Test
	public void setUp() {

		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void getCanceledEntriesTest() {

		List<OrderEntryData> data = new ArrayList<>();
		OrderEntryData order = new OrderEntryData();
		data.add(order);
		OrderPartiallyCanceledEmailContext spy = Mockito.spy(new OrderPartiallyCanceledEmailContext());
		Mockito.doReturn(data).when((OrderPartiallyModifiedEmailContext) spy).getModifiedEntries();
		context.getCanceledEntries();

	}

}
