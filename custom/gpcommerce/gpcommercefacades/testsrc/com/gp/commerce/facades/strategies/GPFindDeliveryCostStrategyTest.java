/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;

@UnitTest
public class GPFindDeliveryCostStrategyTest {
	
	GPFindDeliveryCostStrategy gpFindCostStrategy=new GPFindDeliveryCostStrategy() ;
	private ModelService modelService;
	private AbstractOrderModel order ;
	private AbstractOrder  orderItem ;
	private DeliveryModeModel deliveryModeModel ;
	private DeliveryMode  deliveryMode ;
	private PriceValue calculatedPrice ;
	private ZoneDeliveryModeValueModel zoneValueModel ;
	private ZoneDeliveryModeModel zoneModel ;
	private CurrencyModel currency ;
	private   PriceValue newPrice ;
	
	
	@Before
	public void setUp() throws Exception {
		   newPrice =new PriceValue("US", 0.0, true);
		ReflectionTestUtils.setField(gpFindCostStrategy, "modelService", modelService);
		modelService=Mockito.mock(ModelService.class) ;
		deliveryMode=Mockito.mock(DeliveryMode.class) ;
		order=Mockito.mock(AbstractOrderModel.class) ;
		deliveryModeModel=Mockito.mock(DeliveryModeModel.class) ;
		calculatedPrice=Mockito.mock(PriceValue.class) ;
		zoneValueModel=Mockito.mock(ZoneDeliveryModeValueModel.class) ;
		currency=Mockito.mock(CurrencyModel.class) ;
		zoneModel=Mockito.mock(ZoneDeliveryModeModel.class) ;
		gpFindCostStrategy.setModelService(modelService);

	}

	@Test
	public void getDeliveryCostTest() throws JaloDeliveryModeException {
	Mockito.when( order.getDeliveryMode()).thenReturn(deliveryModeModel) ;
	Mockito.when( order.getCurrency()).thenReturn(currency) ;
	Mockito.when( currency.getIsocode() ).thenReturn(Mockito.anyString()) ;
	Mockito.when( modelService.getSource(order)).thenReturn(orderItem) ;
	Mockito.when( modelService.getSource(deliveryModeModel)).thenReturn(deliveryMode) ;
	Mockito.when(deliveryMode.getCost(orderItem)).thenReturn(calculatedPrice);
	Assert.assertEquals(gpFindCostStrategy.getDeliveryCost(order),newPrice);
	

}
	
	@Test
	public void calculateDeliveryCostTest() {
		final Set<ZoneDeliveryModeValueModel> zoneValueList =new HashSet<>();
		zoneValueList.add(zoneValueModel);
		Mockito.when( order.getDeliveryMode()).thenReturn(deliveryModeModel) ;
		Mockito.when(order.getDeliveryMode()).thenReturn(zoneModel) ;
		Mockito.when( order.getCurrency()).thenReturn(currency) ;
		Mockito.when( currency.getIsocode() ).thenReturn("US") ;
		Mockito.when(zoneModel.getValues()).thenReturn(zoneValueList);
		Mockito.when(zoneValueModel.getDeliveryModeType()).thenReturn("percentage");
		Mockito.when(order.getTotalPrice()).thenReturn(0.0d);
		Mockito.when(calculatedPrice.getValue()).thenReturn(0.0d);
		Mockito.when(order.getNet().booleanValue()).thenReturn(true);
		Assert.assertEquals(gpFindCostStrategy.calculateDeliveryCost(order, calculatedPrice),newPrice) ;
	}
}
