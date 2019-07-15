/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.price.strategies.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Assert;

@UnitTest
public class GPDefaultRetrieveChannelStrategyTest {

	@InjectMocks
	GPDefaultRetrieveChannelStrategy retrieveChannelStrategy = new GPDefaultRetrieveChannelStrategy();

	final String CURRENT_SITE = "currentSite";
	
	@Mock
	ModelService modelService;
	@Mock
	private EnumerationService enumerationService;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		retrieveChannelStrategy.setEnumerationService(enumerationService);
		retrieveChannelStrategy.setModelService(modelService);
		
	}
	
	@Test
	public void testGetChannel()
	{
		final String CHANNEL = "channel";
		SessionContext ctx = Mockito.mock(SessionContext.class);
		Mockito.when(ctx.getAttribute(CURRENT_SITE)).thenReturn(CURRENT_SITE);
		PriceRowChannel priceRowChannel = Mockito.mock(PriceRowChannel.class);
		Mockito.when(ctx.getAttribute(CHANNEL)).thenReturn(priceRowChannel);
		retrieveChannelStrategy.getModelService();
		retrieveChannelStrategy.getEnumerationService();
		
		Assert.assertEquals(priceRowChannel,retrieveChannelStrategy.getChannel(ctx));
	}

	@Test
	public void testGetChannelNull()
	{
		final String CHANNEL = "channel";
		final String PRICE_ROW_CHANNEL = "PriceRowChannel";
		CMSSite cmsSite = Mockito.mock(CMSSite.class);
		SessionContext ctx = Mockito.mock(SessionContext.class);
		Mockito.when(ctx.getAttribute(CURRENT_SITE)).thenReturn(cmsSite);
		PriceRowChannel priceRowChannel = Mockito.mock(PriceRowChannel.class);
		Mockito.when(ctx.getAttribute(CHANNEL)).thenReturn(null);
		Mockito.when(cmsSite.getUid()).thenReturn(CHANNEL);
		Mockito.when(enumerationService.getEnumerationValue(PRICE_ROW_CHANNEL, CHANNEL)).thenReturn(priceRowChannel);
		Mockito.when(retrieveChannelStrategy.getChannel(ctx)).thenReturn(priceRowChannel);
		
		Assert.assertEquals(priceRowChannel, retrieveChannelStrategy.getChannel(ctx));
	}
	
	@Test
	public void testGetChannelException()
	{
		final String CHANNEL = "channel";
		final String PRICE_ROW_CHANNEL = "PriceRowChannel";
		CMSSite cmsSite = Mockito.mock(CMSSite.class);
		SessionContext ctx = Mockito.mock(SessionContext.class);
		Mockito.when(ctx.getAttribute(CURRENT_SITE)).thenReturn(cmsSite);
		PriceRowChannel priceRowChannel = Mockito.mock(PriceRowChannel.class);
		Mockito.when(ctx.getAttribute(CHANNEL)).thenReturn(null);
		Mockito.when(cmsSite.getUid()).thenReturn(CHANNEL);
		Mockito.when(enumerationService.getEnumerationValue(PRICE_ROW_CHANNEL, CHANNEL)).thenThrow(new UnknownIdentifierException("exception"));
		Mockito.when(retrieveChannelStrategy.getChannel(ctx)).thenReturn(priceRowChannel);
		
		Assert.assertEquals(priceRowChannel, retrieveChannelStrategy.getChannel(ctx));
	}
	
}
