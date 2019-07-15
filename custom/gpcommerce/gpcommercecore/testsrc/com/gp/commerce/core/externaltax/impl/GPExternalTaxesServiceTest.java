/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.externaltax.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.externaltax.CalculateExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.DecideExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.RecalculateExternalTaxesStrategy;
import de.hybris.platform.commerceservices.externaltax.impl.DefaultExternalTaxesService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ApplyExternalTaxesStrategy;
import de.hybris.platform.externaltax.ExternalTaxDocument;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.TaxValue;

@UnitTest
public class GPExternalTaxesServiceTest {

	@InjectMocks
	private GPExternalTaxesService gpExternalTaxesService = new GPExternalTaxesService();
	@Mock
	private CalculateExternalTaxesStrategy calculateExternalTaxesStrategy;
	@Mock
	private ApplyExternalTaxesStrategy applyExternalTaxesStrategy;
	@Mock
	private DecideExternalTaxesStrategy decideExternalTaxesStrategy;
	@Mock
	private RecalculateExternalTaxesStrategy recalculateExternalTaxesStrategy;
	@Mock
	private ModelService modelService;
	@Mock
	private SessionService sessionService;
	@Mock
	private ExternalTaxDocument exTaxDocument;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		exTaxDocument = Mockito.mock(ExternalTaxDocument.class);
		List<TaxValue> shippingCostTaxes = new ArrayList<TaxValue>();
		TaxValue taxValue = Mockito.mock(TaxValue.class);
		shippingCostTaxes.add(taxValue);
		exTaxDocument.setShippingCostTaxes(shippingCostTaxes);
	}

	@Test
	public void calculateExternalTaxesTest() {
		AbstractOrderModel abstractOrder = Mockito.mock(AbstractOrderModel.class);

		Mockito.doReturn(true).when(decideExternalTaxesStrategy).shouldCalculateExternalTaxes(abstractOrder);
		Mockito.doReturn(true).when(recalculateExternalTaxesStrategy).recalculate(abstractOrder);

		Mockito.doReturn(exTaxDocument).when(calculateExternalTaxesStrategy).calculateExternalTaxes(abstractOrder);

		Assert.notNull(exTaxDocument, "ExternalTaxDocument should not be null");
		Mockito.doNothing().when(applyExternalTaxesStrategy).applyExternalTaxes(abstractOrder, exTaxDocument);
		Mockito.doNothing().when(sessionService).setAttribute("test", exTaxDocument);

		Mockito.doNothing().when(modelService).save(abstractOrder);
		boolean flag=gpExternalTaxesService.calculateExternalTaxes(abstractOrder);
		assertEquals(flag,false);

	}

	@Test
	public void calculateExternalTaxesWithEmptyExTaxTest() {
		AbstractOrderModel abstractOrder = Mockito.mock(AbstractOrderModel.class);
		GPExternalTaxesService spy = Mockito.spy(new GPExternalTaxesService());
		Mockito.doReturn(true).when(decideExternalTaxesStrategy).shouldCalculateExternalTaxes(abstractOrder);
		Mockito.doReturn(true).when(recalculateExternalTaxesStrategy).recalculate(abstractOrder);

		Mockito.doReturn(exTaxDocument).when(calculateExternalTaxesStrategy).calculateExternalTaxes(abstractOrder);

		Mockito.doNothing().when(sessionService).removeAttribute("orderRecalculationHash");
		Mockito.doNothing().when((DefaultExternalTaxesService) spy).clearSessionTaxDocument();

		Mockito.doNothing().when(modelService).save(abstractOrder);
		boolean flag=gpExternalTaxesService.calculateExternalTaxes(abstractOrder);
		assertEquals(flag,false);
	}

	@Test
	public void calculateExternalTaxesWithNoRecalculationTest() {
		AbstractOrderModel abstractOrder = Mockito.mock(AbstractOrderModel.class);
		Mockito.doReturn(true).when(decideExternalTaxesStrategy).shouldCalculateExternalTaxes(abstractOrder);

		Mockito.doReturn(false).when(recalculateExternalTaxesStrategy).recalculate(abstractOrder);

		Mockito.doNothing().when(sessionService).setAttribute("test", exTaxDocument);

		Mockito.doNothing().when(applyExternalTaxesStrategy).applyExternalTaxes(abstractOrder, exTaxDocument);

		Mockito.doNothing().when(modelService).save(abstractOrder);
		boolean flag=gpExternalTaxesService.calculateExternalTaxes(abstractOrder);
		assertEquals(flag,true);
	}
}
