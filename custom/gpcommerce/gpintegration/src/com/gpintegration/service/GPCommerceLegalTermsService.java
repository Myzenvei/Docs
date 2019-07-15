/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service;

import com.gpintegration.exception.GPIntegrationException;

/**
 * Interface for Commerce legal terms 
 */
public interface GPCommerceLegalTermsService  {
	
	/**
	 * Fetches legal terms.
	 *
	 * @throws GPIntegrationException the GP integration exception
	 */
	void fetchLegalTerms() throws GPIntegrationException;
}
