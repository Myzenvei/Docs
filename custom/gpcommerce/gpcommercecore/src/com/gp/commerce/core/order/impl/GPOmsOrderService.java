/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.impl;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.core.enums.OrderStatus;

import java.util.List;
import java.util.Map;

import com.gp.commerce.core.enums.GPConsignmentEntryStatus;

/**
 * Interface class to perform Service functionalities for GP Order
 */
public interface GPOmsOrderService {

	/**
	 *  Method to update order status to cancel.
	 *
	 * @param order the order
	 */
	void updateOrderStatusToCancel(OrderModel order);

	/**
	 * Method to update order by the order entry status
	 *
	 * @param order the order
	 * @param status the order entry status
	 */
	void updateEntryStatus(OrderModel order,OrderEntryStatus status);

	/**
	 * Method to update consignment status to cancel
	 *
	 * @param order the order
	 */
	void updateConsignmentStatusToCancel(OrderModel order);

	/**
	 * Method to create partial cancel request by its consignment,consignment entry
	 * and declined quantity
	 * 
	 * @param qtyDeclined      the declined quanitty
	 * @param consignmentModel the consignment
	 * @param entryMode        the consignment entry
	 * @param reason           the reason
	 */
	void partialCancelConsignment(Long qtyDeclined, ConsignmentModel consignmentModel, ConsignmentEntryModel entryMode,String reason);

	/**
	 * Method to update order status and consignment status
	 *
	 * @param order       the order
	 * @param consignment the consignment
	 */
	void updateOrderAndConsignmentStatus(OrderModel order, ConsignmentModel consignment);

	/**
	 * Method to update processing status for consignment entry, consignment and order
	 *
	 * @param entryModel  the consignment entry
	 * @param entryStatus the consignment entry status
	 */
	void updateProcessingStatus(ConsignmentEntryModel entryModel, GPConsignmentEntryStatus entryStatus);

	/**
	 * Method to updates order status based on consignment status
	 *
	 * @param order the order
	 */
	void updateOrderStatus(OrderModel order);

	/**
	 * Method to update status for consignment entry, consignment and order
	 *
	 * @param entryModel  the consignment entry
	 * @param entryStatus the consignment entry status
	 */
	void updateConsignmentEntryStatus(ConsignmentEntryModel entryModel, GPConsignmentEntryStatus entryStatus);

	/**
	 * Update Order Process
	 *
	 * @param order the order
	 * @return {@link OrderProcessModel}
	 */
	OrderProcessModel updateOrderProcess(OrderModel order);


	/**
	 * Update Order
	 *
	 * @param order the order
	 */
	void updateOrder(OrderModel order);
	
	/**
	 * Update the order based on provided status
	 * 
	 * @param order       the order
	 * @param orderstatus the orderStatus
	 */
	void updateOrderByStatus(OrderModel order, OrderStatus orderstatus);
	
	/**
	 * Gets the order snapshots.
	 *
	 * @param params the params
	 * @return the order snapshots
	 */
	List<OrderModel> getOrderSnapshots(Map<String, String> params);
	
}

