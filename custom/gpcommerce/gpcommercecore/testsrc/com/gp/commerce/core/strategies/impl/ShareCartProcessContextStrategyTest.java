package com.gp.commerce.core.strategies.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.spockframework.compiler.model.SetupBlock;

import com.gp.commerce.core.model.CartProcessModel;
import com.gp.commerce.core.services.GPDeliveryService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

@UnitTest
public class ShareCartProcessContextStrategyTest {

	@InjectMocks
	ShareCartProcessContextStrategy shareCartStrategy = new ShareCartProcessContextStrategy();
	
	@Mock
	private BusinessProcessModel businessProcessModel;
	@Mock
	private CartProcessModel cartProcessModel;
	@Mock
	private BaseSiteModel site;
	@Mock
	private CartModel cart;
	@Mock
	CustomerModel customer;
	
	@Before
	public void setup() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testGetCmsSite()
	{
		Mockito.when(cartProcessModel.getSite()).thenReturn(site);
		Assert.assertTrue(shareCartStrategy.getCmsSite(cartProcessModel).equals(site));
	}
	
	@Test
	public void testGetCustomer()
	{
		Mockito.when(cartProcessModel.getCart()).thenReturn(cart);
		Mockito.when(cartProcessModel.getCart().getUser()).thenReturn(customer);
		
		Assert.assertTrue(shareCartStrategy.getCustomer(cartProcessModel).equals(customer));
	}
	
}
