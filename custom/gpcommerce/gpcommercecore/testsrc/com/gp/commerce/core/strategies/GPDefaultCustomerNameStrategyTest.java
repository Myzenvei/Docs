package com.gp.commerce.core.strategies;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;


@UnitTest
public class GPDefaultCustomerNameStrategyTest {

	@InjectMocks
	private final GPDefaultCustomerNameStrategy strategy = new GPDefaultCustomerNameStrategy();

	private final String NAME = "First|Last";
	private final String FIRST_NAME = "First";
	private final String LAST_NAME = "Last";

	@Test
	public void getName()
	{
		Assert.assertEquals("First Last", strategy.getName(NAME));
		Assert.assertEquals("First|Last", strategy.getName(FIRST_NAME, LAST_NAME));
	}
}
