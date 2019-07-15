/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.dao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

public interface GPB2BCustomerAccountDao {

	/**
	 * Method to fetch Orders on the basis of Customer, Store and B2BUnit
 	 *
 	 * @param customerModel
	 * @param store
	 * @param status
	 * @param pageableData
	 * @param b2bUnit
 	 *
 	 * @return SearchPageData<OrderModel>
 	 */
	SearchPageData<OrderModel> findOrdersByCustomerAndUnit(CustomerModel customerModel,
			BaseStoreModel store, OrderStatus[] status, PageableData pageableData, List<B2BUnitModel> b2bUnit) ;

	/**
	 * Method to fetch Order details on the basis of Customer, Code and Store
 	 *
 	 * @param customerModel
 	 * @param code
 	 * @param store
 	 *
 	 * @return OrderModel
 	 */
	OrderModel findOrderByB2BCustomerAndCodeAndStore(final CustomerModel customerModel, final String code,
			final BaseStoreModel store) ;

	/**
	 * Method to fetch Orders on the basis of Customer, Store and B2BUnit
 	 *
 	 * @param customerModel
	 * @param store
	 * @param status
	 * @param pageableData
	 * @param b2bUnit
 	 *
 	 * @return SearchPageData<OrderModel>
 	 */
	SearchPageData<OrderModel> findOrdersForAdmin(CustomerModel customerModel,
			BaseStoreModel store, OrderStatus[] status,
			PageableData pageableData, List<B2BUnitModel> b2bUnit);
}
