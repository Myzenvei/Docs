package com.gp.commerce.order.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.flexiblesearch.impl.DefaultPagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import java.util.Date;

public class GPCustomerAccountDaoImplTest 
{
	@InjectMocks
	GPCustomerAccountDaoImpl gpCustomerAccountDaoImpl = new GPCustomerAccountDaoImpl();

	@Mock
	private PagedFlexibleSearchService pagedFlexibleSearchService;
	
	private OrderStatus[] orderStatus = new OrderStatus[4];

	private OrderStatus[] orderStatus1 = new OrderStatus[0];

	private OrderModel orderModel;

	private PageableData pageable;

	BaseStoreModel store=mock(BaseStoreModel.class);
	CustomerModel customerModel=mock(CustomerModel.class);
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final Date creationTime = new Date();
		pageable = new PageableData();
		pageable.setCurrentPage(2);
		pageable.setPageSize(2);
		pageable.setSort("asc");
		//pageable.setSelectedDate(creationTime);

		gpCustomerAccountDaoImpl.setPagedFlexibleSearchService(pagedFlexibleSearchService);

		List<OrderStatus> filterOrderStatusList = new ArrayList<>();
		filterOrderStatusList.add(OrderStatus.APPROVED);
		filterOrderStatusList.add(OrderStatus.COMPLETED);
		filterOrderStatusList.add(OrderStatus.CREATED);
		filterOrderStatusList.add(OrderStatus.CHECKED_VALID);
		gpCustomerAccountDaoImpl.setFilterOrderStatusList(filterOrderStatusList);
	}

	@Test
	public void testFindOrdersByCustomerAndStore()
	{
		orderStatus[0]=OrderStatus.APPROVED;
		orderStatus[1]=OrderStatus.COMPLETED;
		orderStatus[2]=OrderStatus.CREATED;
		orderStatus[3]=OrderStatus.CHECKED_VALID;
		final SearchPageData<OrderModel> searchPageData = new SearchPageData<OrderModel>();	
		SearchPageData<OrderModel> resultData = new SearchPageData<OrderModel>();
		orderModel = new OrderModel();
		orderModel.setCode("123456");
		orderModel.setName("dummy");

		List<OrderModel> result = new ArrayList<>();
		result.add(orderModel);
		searchPageData.setResults(result);

		Mockito.when(pagedFlexibleSearchService.search(Mockito.anyList(),Mockito.anyString(),Mockito.anyMap(),Mockito.anyObject())).thenReturn(searchPageData);

		resultData = gpCustomerAccountDaoImpl.findOrdersByCustomerAndStore(customerModel,store,orderStatus,pageable);
		assertNotNull(resultData);
		assertEquals(searchPageData, resultData);
	}

	@Test
	public void testFindOrdersByCustomerAndStoreWithNullStatus()
	{
		final SearchPageData<OrderModel> searchPageData = new SearchPageData<OrderModel>();	
		SearchPageData<OrderModel> resultData1 = new SearchPageData<OrderModel>();	
		orderModel = new OrderModel();
		orderModel.setCode("123456");
		orderModel.setName("dummy");

		List<OrderModel> result = new ArrayList<>();
		result.add(orderModel);
		searchPageData.setResults(result);

		Mockito.when(pagedFlexibleSearchService.search(Mockito.anyList(),Mockito.anyString(),Mockito.anyMap(),Mockito.anyObject())).thenReturn(searchPageData);

		resultData1 = gpCustomerAccountDaoImpl.findOrdersByCustomerAndStore(customerModel,store,orderStatus1,pageable);
		assertNotNull(resultData1);
		assertEquals(searchPageData, resultData1);

	}
}
