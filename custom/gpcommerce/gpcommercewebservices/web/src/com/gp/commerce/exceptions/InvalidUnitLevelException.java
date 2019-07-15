/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.exceptions;

/**
 * @author snnagarajan
 *
 */
@SuppressWarnings("serial")
public class InvalidUnitLevelException extends RuntimeException
{

	/**
	 * default constructor 
	 */
	public InvalidUnitLevelException()
	{
		super();
	}

	/**
	 * @param message
	 * 			the exception message
	 */
	public InvalidUnitLevelException(String message)
	{
		super(message);
	}
	
	 /**
	 * @param message
	 * @param cause
	 */
	public InvalidUnitLevelException(String message, Throwable cause) {
       super(message, cause);
   }
}
