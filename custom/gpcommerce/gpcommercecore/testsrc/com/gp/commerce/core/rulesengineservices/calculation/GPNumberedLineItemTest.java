/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.rulesengineservices.calculation;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;

@UnitTest
public class GPNumberedLineItemTest {
	
	private GPNumberedLineItem item;
	private final Currency curr = new Currency("euro", 2);

	@Test
	public void testWithValue()
	{
		int numberOfUnits=2;
		item = new GPNumberedLineItem(new Money(BigDecimal.valueOf(5),curr));
		Assert.assertNotEquals(1, item);
		item = new GPNumberedLineItem(new Money(BigDecimal.ZERO, curr),numberOfUnits);
		Assert.assertEquals("2x 0.00 euro", item.toString());
	}
	
	@Test
	public void setMapPriceTest()
	{
		Money mapPrice =new Money(BigDecimal.valueOf(5), curr);
		GPNumberedLineItem gpNumberedLineItem = new GPNumberedLineItem(new Money(BigDecimal.valueOf(5), curr));
		gpNumberedLineItem.setMapPrice(mapPrice);
		Money price = gpNumberedLineItem.getMapPrice();
		Assert.assertEquals(mapPrice, price);
	}
	
}
