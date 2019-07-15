/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import com.gpintegration.dto.netsuite.GPNetsuiteCCAckDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteCustomerDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderUpdateDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteResponseDTO;
import com.gpintegration.exception.GPIntegrationException;

/**
 * The Interface GPNetsuiteReplicationService.
 *
 * @author spandiyan
 */
public interface GPNetsuiteReplicationService {

	/**
	 * Replicates customer data.
	 *
	 * @param customerData the customer data
	 * @return GPNetsuiteResponseDTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	GPNetsuiteResponseDTO replicateCustomerData(GPNetsuiteCustomerDTO customerData) throws GPIntegrationException;

	/**
	 * Replicates order data.
	 *
	 * @param orderData the order data
	 * @return GPNetsuiteResponseDTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	GPNetsuiteResponseDTO replicateOrderData(GPNetsuiteOrderDTO orderData) throws GPIntegrationException;

	/**
	 * Acknowledges Credit card of payment.
	 *
	 * @param ccAckPaymentData the cc ack payment data
	 * @return GPNetsuiteResponseDTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	GPNetsuiteResponseDTO acknowledgeCCOfPayment(GPNetsuiteCCAckDTO ccAckPaymentData) throws GPIntegrationException;

	/**
	 * Gets shipped orders.
	 *
	 * @return GPNetsuiteOrderUpdateDTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	GPNetsuiteOrderUpdateDTO getShippedOrders() throws GPIntegrationException;

}
