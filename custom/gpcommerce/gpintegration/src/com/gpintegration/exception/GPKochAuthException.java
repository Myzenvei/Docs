/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * @author spandiyan
 *
 */
public class GPKochAuthException extends SystemException {
	public GPKochAuthException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public GPKochAuthException(final String message) {
		super(message);
	}
	
	public GPKochAuthException(final Throwable cause) {
		super(cause);
	}
}
