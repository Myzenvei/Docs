/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import org.springframework.http.HttpHeaders;
import com.gpintegration.exception.GPIntegrationException;

/**
 * The Interface GPCRMService.
 */
public interface GPCRMService {
	
	/**
	 * Gets the CRM access token.
	 *
	 * @return the CRM access token
	 */
	public String getCRMAccessToken();
	
	/**
	 * Gets the SCPI headers.
	 *
	 * @return the SCPI headers
	 * @throws GPIntegrationException the GP integration exception
	 */
	public  HttpHeaders getSCPIHeaders() throws GPIntegrationException ;

}
