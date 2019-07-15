/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import org.springframework.http.ResponseEntity;

import com.gpintegration.dto.sfdc.GPProductReplicationRequestDTO;
import com.gpintegration.exception.GPIntegrationException;


/**
 * The Interface GPSFDCProductReplicationService.
 */
public interface GPSFDCProductReplicationService {
	
	/**
	 * Replicate product.
	 *
	 * @param productReplicationRequest the product replication request
	 * @return the response entity
	 * @throws GPIntegrationException the GP integration exception
	 */
	ResponseEntity<String> replicateProduct(GPProductReplicationRequestDTO productReplicationRequest) throws GPIntegrationException;
}
