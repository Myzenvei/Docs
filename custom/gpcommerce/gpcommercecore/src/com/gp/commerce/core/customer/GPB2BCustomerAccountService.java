/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.customer;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

/**
 * A service around B2BCustomerAccountService.
 * 
 */
public interface GPB2BCustomerAccountService {


	/**
	 * This method gets b2b order list
	 * 
	 * @param currentCustomer
	 * 			the current customer
	 * @param currentBaseStore
	 * 			the current base store
	 * @param statuses
	 * 			the status
	 * @param pageableData
	 * 			the data for pagination
	 * @param b2bUnit
	 * 			the b2b unit
	 * @return b2b order list
	 */
	SearchPageData<OrderModel> getB2BOrderList(CustomerModel currentCustomer, BaseStoreModel currentBaseStore,
			OrderStatus[] statuses, PageableData pageableData,  List<B2BUnitModel> b2bUnit);

	/**
	 * This method gets order for code
	 * @param currentUser
	 * 			the current user
	 * @param code
	 * 			the code
	 * @param baseStoreModel
	 * 			the base store model
	 * @return the order model for code
	 */
	OrderModel getOrderForCode(CustomerModel currentUser, String code, BaseStoreModel baseStoreModel);

	/**
	 * This method gets b2b order list for admin
	 * @param currentCustomer
	 * 			the current customer
	 * @param currentBaseStore
	 * 			the current base store
	 * @param statuses
	 * 			the statuses
	 * @param pageableData
	 * 			the data for pagination
	 * @param b2bUnitList	
	 * 			the b2b unit list
	 * @return the order list for admin
	 */
	SearchPageData<OrderModel> getB2BOrderListForAdmin(
			CustomerModel currentCustomer, BaseStoreModel currentBaseStore,
			OrderStatus[] statuses, PageableData pageableData,
			List<B2BUnitModel> b2bUnitList);

}
