/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facade.order;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.ordercancel.OrderCancelException;

/**
 * The Interface GpOmsOrderFacade.
 */
public interface GpOmsOrderFacade {

	/**
	 * Method to create order cancel request by order code
	 * 
	 * @param orderCode the order code
	 * @throws OrderCancelException on order cancel error
	 */
	void createRequestOrderCancel(String orderCode) throws OrderCancelException;

	/**
	 * Approve or reject order.
	 *
	 * @param orderCode        the order code
	 * @param status           the status
	 * @param approverComments the approver comments
	 * @return the order data
	 */
	OrderData approveOrRejectOrder(String orderCode, String status, String approverComments);

}
