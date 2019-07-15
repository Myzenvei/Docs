package com.gp.commerce.core.customer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import org.junit.Before;

import com.gp.commerce.core.dao.GPB2BCustomerAccountDao;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

@UnitTest
public class GPB2BCustomerAccountServiceImplTest {
	
	@InjectMocks
	private GPB2BCustomerAccountServiceImpl service= new GPB2BCustomerAccountServiceImpl();
	
	@Mock
	private GPB2BCustomerAccountDao  gpB2BCustomerAccountDao;
	
	@Mock
	private SearchPageData<OrderModel> searchPageData;
	
	@Mock
	private CustomerModel currentCustomer;
	
	@Mock
	private OrderModel order;
	
	@Mock
	private BaseStoreModel currentBaseStore;
	
	@Mock
	private PageableData pageableData;
	
	@Before
	public void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		service.setGpB2BCustomerAccountDao(gpB2BCustomerAccountDao);
		order= new OrderModel();
		order.setCode("code");
		final ArrayList orders = new ArrayList<OrderModel>();
		orders.add(order);
		searchPageData = new SearchPageData<OrderModel>();
		searchPageData.setResults(orders);
	}
	
	@Test
	public void testGetB2BOrderList()
	{
		OrderStatus[] statuses = new OrderStatus[] {};
		List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		Mockito.when(gpB2BCustomerAccountDao.findOrdersByCustomerAndUnit(currentCustomer, currentBaseStore, statuses, pageableData, b2bUnitList)).thenReturn(searchPageData);
		Assert.assertNotNull(service.getB2BOrderList(currentCustomer, currentBaseStore, statuses, pageableData, b2bUnitList));
		SearchPageData<OrderModel> b2bOrderList = service.getB2BOrderList(currentCustomer, currentBaseStore, statuses, pageableData, b2bUnitList);
		Assert.assertEquals(1, b2bOrderList.getResults().size());
	}
	
	@Test
	public void testGetB2BOrderListForAdmin()
	{
		OrderStatus[] statuses = new OrderStatus[] {};
		List<B2BUnitModel> b2bUnitList = new ArrayList<>();
		Mockito.when(gpB2BCustomerAccountDao.findOrdersForAdmin(currentCustomer, currentBaseStore, statuses, pageableData,b2bUnitList)).thenReturn(searchPageData);
		Assert.assertNotNull(service.getB2BOrderListForAdmin(currentCustomer, currentBaseStore, statuses, pageableData, b2bUnitList));
		SearchPageData<OrderModel> b2bOrderListForAdmin = service.getB2BOrderListForAdmin(currentCustomer, currentBaseStore, statuses, pageableData, b2bUnitList);
		Assert.assertNotEquals(0, b2bOrderListForAdmin.getResults().size());
	}
	
	@Test
	public void testGetOrderForCode()
	{
		String code= "code";
		Mockito.when(gpB2BCustomerAccountDao.findOrderByB2BCustomerAndCodeAndStore(currentCustomer, code, currentBaseStore)).thenReturn(order);
		Assert.assertNotNull(service.getOrderForCode(currentCustomer, code, currentBaseStore));
		OrderModel orderForCode = service.getOrderForCode(currentCustomer, code, currentBaseStore);
		Assert.assertEquals("code", orderForCode.getCode());
	}
	
	@Test
	public void testGetGpB2BCustomerAccountDao() {
		Assert.assertNotNull(service.getGpB2BCustomerAccountDao());
	}

}
