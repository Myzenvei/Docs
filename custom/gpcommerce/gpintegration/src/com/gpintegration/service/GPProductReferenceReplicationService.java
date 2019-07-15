/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import org.springframework.http.ResponseEntity;

import com.gpintegration.dto.sfdc.GPProductRelationshipRequestDTO;
import com.gpintegration.exception.GPIntegrationException;

/**
 * The Interface GPProductReferenceReplicationService.
 */
public interface GPProductReferenceReplicationService {

	/**
	 * Replicate product reference.
	 *
	 * @param productReferenceDTO the product reference DTO
	 * @return the response entity
	 * @throws GPIntegrationException the GP integration exception
	 */
	ResponseEntity<String> replicateProductReference(GPProductRelationshipRequestDTO productReferenceDTO) throws GPIntegrationException;
}
