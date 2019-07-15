/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.order;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;

/**
 * The Interface GPB2BOrderFacade.
 */
public interface GPB2BOrderFacade {

	/**
	 * Method to get Paged Order History Data by its b2bunit and order status
	 *
	 * @param pageableData
	 * 			pojo of search query pagination
	 * @param b2bUnit
	 * 			the b2b unit
	 * @param statuses
	 * 			the status
	 * @return SearchPageData<OrderHistoryData>
	 */
	SearchPageData<OrderHistoryData> getPagedOrderHistoryForStatuses(PageableData pageableData,
			 String b2bUnit, final OrderStatus... statuses);

	/**
	 * Method to get the Order Data by its code
	 *
	 * @param code
	 * 			the code
	 * @return OrderData
	 */
	OrderData getOrderDetailsForCode(String code);

	/**
	 * Method to get Page order history for admin
	 * @param pageableData
	 * 			pojo of search query pagination
	 * @param statuses
	 * 			the status
	 * @return Search page order history for admin
	 */
	SearchPageData<OrderHistoryData> getPagedOrderHistoryForAdmin(
			PageableData pageableData, final OrderStatus... statuses);

 }
