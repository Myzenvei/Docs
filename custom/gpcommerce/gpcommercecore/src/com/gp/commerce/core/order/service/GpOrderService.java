/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.service;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.core.model.ShippingNotificationModel;

/**
 *interface for order service
 */
public interface GpOrderService {

	/**
	 * retrieves orders in error state.
	 *
	 * @param site the site
	 * @param orderProcessingStatus the order processing status
	 * @return List of orders
	 */
	List<OrderModel> getFailedOrders(CMSSiteModel site, OrderStatus orderProcessingStatus);

	/**
	 * Retrieves lease agreement for the provided lease ID
	 * 
	 * @param leaseId the lease ID
	 * @return list of end user legal terms
	 */
	List<GPEndUserLegalTermsModel> getLeaseAgreementById(String leaseId);

	/**
	 * Retrieves consignments in error state
	 * 
	 * @param site the CMS site
	 * @param consignProcessingStatus the consignment status
	 * @return list of failed consignments
	 */
	List<ConsignmentModel> getFailedConsignments(CMSSiteModel site, ConsignmentStatus consignProcessingStatus);

	/**
	 * Update Order order consignment.
	 *
	 * @param consignment the consignment
	 */
	void updateOrderConsignment(ConsignmentModel consignment);

	/**
	 * Save Shipping notification
	 * 
	 * @param model the shipping notification
	 */
	void saveShippingNotification(ShippingNotificationModel model);

	/**
	 * Returns list of order based on the OrderStatus
	 * 
	 * @param orderProcessingStatus the order status
	 * @returnL list of failed orders
	 */
	List<OrderModel> getOrdersByStatus(OrderStatus orderProcessingStatus);
	
	/**
	 * Returns list of failed orders of NetSuite and payment, based on the CMS site provided
	 * 
	 * @param site the CMSSite
	 * @return List of failed orders
	 */
	List<OrderModel> getFailedOrdersOfNetSuiteAndPayment(CMSSiteModel site);

}
