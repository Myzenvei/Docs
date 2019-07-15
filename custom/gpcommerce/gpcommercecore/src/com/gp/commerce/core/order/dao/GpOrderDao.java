/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.order.dao;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gp.commerce.facade.order.data.LeaseAgreementData;

public interface GpOrderDao {

	/**
	 * retrieves orders in error state
	 * @param site
	 * @param orderProcessingStatus
	 * @return List of orders
	 */
	List<OrderModel> getOrdersInError(CMSSiteModel site, OrderStatus orderProcessingStatus);
	
	/**
	 * retrieves orders in error state
	 * @param site
	 * @param orderProcessingStatus
	 * @return List of orders
	 */
	List<OrderModel> getOrdersByStatus(OrderStatus orderProcessingStatus);

	/**
	 * retrieves lease data
	 * @param leaseId
	 * @return lease data
	 */
	List<GPEndUserLegalTermsModel> getLeaseAgreementById(String leaseId);

	/**
	 * retrieves consignments in error state
	 * @param site
	 * @param consignProcessingStatus
	 * @return
	 */
	List<ConsignmentModel> getConsignmentsInError(CMSSiteModel site, ConsignmentStatus consignProcessingStatus);
	
	/** Retrieves orders in error state for Netsuite
	 * and payment failure.
	 * @param site
	 * @return
	 */
	List<OrderModel> getOrdersInErrorOfNetsuiteAndPayment(CMSSiteModel site);
	
	
	/**
	 * Retrieves order with code for the given base store
	 * @param code
	 * @param baseStoreModel
	 * @return
	 */
	List<OrderModel> getOrderForCode(String code, BaseStoreModel baseStoreModel);


}
