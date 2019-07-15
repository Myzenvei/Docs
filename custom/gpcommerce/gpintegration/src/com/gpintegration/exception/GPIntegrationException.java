/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.exception;

/**
 * Thrown when any exception occurs in Integration services.
 */
public class GPIntegrationException extends Exception{
	public static final String ADDRESS_VERIFICATION_SERVICE_ERROR="Error occured during BODS Service call to validate Address";
	public static final String ADDRESS_SUGGESTIONS_SERVICE_ERROR="Error occured during BODS Service call to suggest Addresses";

	public static final String CUSTOMER_LEASE_AGREEMENT_ERROR="Error in creating customer lease agreement";
	public static final String CUSTOMER_LEASE_AGREEMENT_EMPTY_ERROR="Error in creating customer lease agreement : Agreement id not generated";

	public static final String ONE_SOURCE_TAX_SERVICE_ERROR = "OneSource tax calculation service failed with execption";
	
	public static final String QUPLES_ERROR= "Error occurred in quples integration";
	
	public GPIntegrationException(final String message) {
		super(message);
	}
	
	public GPIntegrationException(final String message,Throwable cause) {
		super(message,cause);
	}

}
