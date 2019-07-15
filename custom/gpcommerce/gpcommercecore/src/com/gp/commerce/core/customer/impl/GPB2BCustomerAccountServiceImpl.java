/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.List;


import com.gp.commerce.core.customer.GPB2BCustomerAccountService;
import com.gp.commerce.core.dao.GPB2BCustomerAccountDao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import de.hybris.platform.store.BaseStoreModel;

public class GPB2BCustomerAccountServiceImpl implements  GPB2BCustomerAccountService  {

	private GPB2BCustomerAccountDao  gpB2BCustomerAccountDao ;
	
	@Override
	public SearchPageData<OrderModel> getB2BOrderList(CustomerModel currentCustomer, BaseStoreModel currentBaseStore,
			OrderStatus[] statuses, PageableData pageableData, List<B2BUnitModel> b2bUnitList) {
	
		validateParameterNotNull(currentCustomer, "Customer model cannot be null");
		validateParameterNotNull(currentBaseStore, "Store must not be null");
		validateParameterNotNull(pageableData, "PageableData must not be null");
		validateParameterNotNull(b2bUnitList, "b2bUnit must not be null");
		return getGpB2BCustomerAccountDao().findOrdersByCustomerAndUnit(currentCustomer, currentBaseStore, statuses, pageableData,b2bUnitList);
	}
	

	@Override
	public SearchPageData<OrderModel> getB2BOrderListForAdmin(CustomerModel currentCustomer, BaseStoreModel currentBaseStore,
			OrderStatus[] statuses, PageableData pageableData, List<B2BUnitModel> b2bUnitList) {
	
		validateParameterNotNull(currentCustomer, "Customer model cannot be null");
		validateParameterNotNull(currentBaseStore, "Store must not be null");
		validateParameterNotNull(pageableData, "PageableData must not be null");
		validateParameterNotNull(b2bUnitList, "b2bUnit must not be null");
		return getGpB2BCustomerAccountDao().findOrdersForAdmin(currentCustomer, currentBaseStore, statuses, pageableData,b2bUnitList);
	}
	
	@Override
	public OrderModel getOrderForCode(final CustomerModel customerModel, final String code, final BaseStoreModel store)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(code, "Order code cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		return getGpB2BCustomerAccountDao().findOrderByB2BCustomerAndCodeAndStore(customerModel, code, store);
	}
	public GPB2BCustomerAccountDao getGpB2BCustomerAccountDao() {
		return gpB2BCustomerAccountDao;
	}
	public void setGpB2BCustomerAccountDao(GPB2BCustomerAccountDao gpB2BCustomerAccountDao) {
		this.gpB2BCustomerAccountDao = gpB2BCustomerAccountDao;
	}

}
