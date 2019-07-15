/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * Interface for shell order replication.
 */
public interface GPCRMShellOrderReplicationService {

	/**
	 * boolean to replicate CRM shell order.
	 *
	 * @param orderModel the order model
	 * @return status
	 */
	boolean replicateCRMShellOrder(OrderModel orderModel);

	/**
	 * Updates CRM shell order.
	 *
	 * @param orderModel the order model
	 */
	void updateCRMShellOrder(OrderModel orderModel);


}
