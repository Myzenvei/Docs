package com.gp.commerce.core.order.hooks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.strategies.BusinessProcessStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;


@UnitTest
@PowerMockIgnore(
{ "org.apache.logging.log4j.*" })
public class GPB2BApprovalBusinessProcessCreationPlaceOrderMethodHookTest
{
	@Mock
	private BusinessProcessStrategy businessProcessCreationStrategy;

	@InjectMocks
	private GPB2BApprovalBusinessProcessCreationPlaceOrderMethodHook test;

	private CommerceCheckoutParameter commerceCheckoutParameter;

	private CommerceOrderResult commerceOrderResult;

	private AbstractOrderModel order;

	private B2BCustomerModel user;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		order = new AbstractOrderModel();
		user = new B2BCustomerModel();
		order.setUser(user);
	}

	@Test
	public void testB2BContext()
	{
		test.beforePlaceOrder(commerceCheckoutParameter);
		test.beforeSubmitOrder(commerceCheckoutParameter, commerceOrderResult);
		test.afterPlaceOrder(commerceCheckoutParameter, commerceOrderResult);
		test.getBusinessProcessCreationStrategy();
		assertTrue(test.isB2BContext(order));
	}

	@Test
	public void testB2BContextFalse()
	{
		final CustomerModel user1 = new CustomerModel();
		order.setUser(user1);
		assertFalse(test.isB2BContext(order));
	}

	@Test
	public void testB2BContextOrderNull()
	{
		assertFalse(test.isB2BContext(null));
	}

	@Test
	public void testB2BContextUserNull()
	{
		order.setUser(null);
		assertFalse(test.isB2BContext(order));
	}

}
