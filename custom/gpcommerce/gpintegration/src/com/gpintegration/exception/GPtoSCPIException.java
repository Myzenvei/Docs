/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;

/**
 * @author vdannina
 *
 */
public class GPtoSCPIException extends SystemException
{
	public GPtoSCPIException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public GPtoSCPIException(final String message) {
		super(message);
	}
	
	public GPtoSCPIException(final Throwable cause) {
		super(cause);
	}

}
