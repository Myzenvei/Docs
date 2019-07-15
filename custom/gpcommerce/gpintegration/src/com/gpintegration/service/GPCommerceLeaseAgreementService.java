/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementResponseDTO;

/**
 * The Interface GPCommerceLeaseAgreementService.
 *
 * @author srikrishnamoorthy
 */
public interface GPCommerceLeaseAgreementService  {
	
	/**
	 * Creates lease agreement.
	 *
	 * @param agreement the agreement
	 * @return lease agreement response dto
	 * @throws GPIntegrationException the GP integration exception
	 */
	GPLeaseAgreementResponseDTO createLeaseAgreement(GPLeaseAgreementDTO agreement) throws GPIntegrationException;
}
