/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;

/**
 * Exception is thrown if the social login user tries to update password.
 */

public class SocialLoginPasswordResetException extends SystemException {

	/**
	 * @param message
	 * @param cause
	 */
	public SocialLoginPasswordResetException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SocialLoginPasswordResetException(final String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public SocialLoginPasswordResetException(final Throwable cause)
	{
		super(cause);
	}

}
