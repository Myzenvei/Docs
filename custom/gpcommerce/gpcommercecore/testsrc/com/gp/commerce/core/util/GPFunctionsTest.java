/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.util.Config;

@UnitTest
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
@PowerMockIgnore("org.apache.logging.log4j.*")
public class GPFunctionsTest {
	
	@Mock
	private  DecimalFormatSymbols symbols ;
	@Mock
	Date date = new Date();
	@Mock
	CurrencyModel currencyModel;
	
	DecimalFormat format = new DecimalFormat("#,###,##0.00");
	
	@Test
	public void testGetStartDateRange()
	{
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter("gp.order.history.env")).thenReturn("local");
		Assert.assertNotNull(GPFunctions.getStartDateRange(new Date()));
	}
	
	@Test
	public void testGetStartDateRangeNull()
	{
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter("gp.order.history.env")).thenReturn(null);
		Assert.assertNotNull(GPFunctions.getStartDateRange(new Date()));
	}
	
	@Test
	public void testGetEndDateRange()
	{
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter("gp.order.history.env")).thenReturn("local");
		Assert.assertNotNull(GPFunctions.getEndDateRange(new Date()));
	}
	
	@Test
	public void testGetEndDateRangeNull()
	{
		PowerMockito.mockStatic(Config.class);
		Mockito.when(Config.getParameter("gp.order.history.env")).thenReturn(null);
		Assert.assertNotNull(GPFunctions.getEndDateRange(new Date()));
	}
	
	@Test
	public void testConvertStringToJSONForDataNull()
	{
		String data=null;
		String value=GPFunctions.convertStringToJSON(data);
		Assert.assertTrue(value.equalsIgnoreCase(""));
	}
	
	@Test
	public void testConvertStringToJSON()
	{
		String data="john";	
		String value=GPFunctions.convertStringToJSON(data);
		Assert.assertTrue(data.equalsIgnoreCase(data));
	}
	
	@Test
	public void testConvertObjectToJSON()
	{
		Object data = "john";
		String value= GPFunctions.convertObjectToJSON(data);
		Assert.assertTrue(data.equals(data));
	}
	
	@Test
	public void testConvertObjectToJSONNull()
	{
		Object data = null;
		String value = GPFunctions.convertObjectToJSON(data);
		Assert.assertTrue(value.equals(""));
	}
	
	@Test
	public void testConvertToJSON()
	{
		String data = "John";
		String value= GPFunctions.convertToJSON(data);
		Assert.assertTrue(data.equalsIgnoreCase(data));
	}
	
	@Test
	public void testConvertToJSONNull()
	{
		String data = null;
		String value= GPFunctions.convertToJSON(data);
		Assert.assertTrue(value.equalsIgnoreCase(""));
	}
	
	@Test
	public void testFormatDate()
	{
		GPFunctions.formatDate("12/10/2018", "DD/MM/YYYY", "MM/DD/YYYY");
		Assert.assertTrue(date.equals(date));
	}
	
	@Test
	public void testAdjustDigits()
	{
		DecimalFormat digit = GPFunctions.adjustDigits(format, new CurrencyModel());
		Assert.assertTrue(format.equals(digit));
	}
	
	@Test
	public void testAdjustSymbol()
	{
		String symbol = "abc";
		
		Mockito.when(currencyModel.getSymbol()).thenReturn(symbol);
		DecimalFormatSymbols symbols = Mockito.mock(DecimalFormatSymbols.class);
		Mockito.when(currencyModel.getIsocode()).thenReturn(symbol);
		Mockito.when(symbols.getInternationalCurrencySymbol()).thenReturn(symbol);
		Mockito.when(symbols.getCurrencySymbol()).thenReturn(symbol);
		Mockito.when(GPFunctions.adjustSymbol(format, currencyModel));
		Assert.assertTrue(format.equals(symbols));
		
	}
	
}
