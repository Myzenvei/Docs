package com.gp.commerce.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gp.commerce.core.dao.GPCartDao;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.CartModel;
import com.gp.commerce.core.model.CartProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.EventService;

/**
 * JUnit test suite for {@link DefaultGPCartServiceTest}
 */
@UnitTest
public class DefaultGPCartServiceTest {
	
	@InjectMocks
	private final DefaultGPCartService defaultGPCartService = new DefaultGPCartService();
	
	@Mock
	private EventService eventService;
	
	@Mock
	private BusinessProcessService businessProcessService;
	
	@Mock
	private GPCartDao gpCartDao;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultGPCartService.setEventService(eventService);
		defaultGPCartService.setBusinessProcessService(businessProcessService);
		defaultGPCartService.setGpCartDao(gpCartDao);
	}

	@Test
	public void testGetEventService()
	{
		Assert.assertNotNull(defaultGPCartService.getEventService());
	}
	
	@Test
	public void testGetBusinessProcessService()
	{
		Assert.assertNotNull(defaultGPCartService.getBusinessProcessService());
	}
	
	@Test
	public void testGetGpCartDao()
	{
		Assert.assertNotNull(defaultGPCartService.getGpCartDao());
	}
	
	@Test
	public void testShareCart()
	{
		String toEmail = "abc@xyz.com";
		CartModel cart = Mockito.mock(CartModel.class);
		cart.setCode("Cart1");	
		final CartProcessModel cartProcessModel = new CartProcessModel();
		Mockito.when(businessProcessService.createProcess(Mockito.anyString(), 
				Mockito.anyString(), Mockito.anyMap())).thenReturn(cartProcessModel);
		defaultGPCartService.shareCart(toEmail, toEmail, toEmail, cart);
	}
	
	@Test
	public void testFetchShippingRestrictions()
	{
		Assert.assertNotNull(defaultGPCartService.fetchShippingRestrictions("ProductCode","US","Texas"));
	}
	@Test
	public void testGetLeaseAgreementForCountry()
	{
		GPEndUserLegalTermsModel leaseAgreementData = Mockito.mock(GPEndUserLegalTermsModel.class);
		List<GPEndUserLegalTermsModel> GPEndUserLegalTerms = new ArrayList<>();
		GPEndUserLegalTerms.add(leaseAgreementData);
		Mockito.when(gpCartDao.getLeaseAgreementForCountry(Mockito.anyString())).thenReturn(GPEndUserLegalTerms);
		Assert.assertNotNull(defaultGPCartService.getLeaseAgreementForCountry("US"));
	}
	
}