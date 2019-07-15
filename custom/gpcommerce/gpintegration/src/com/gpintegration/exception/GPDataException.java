/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.exception;

/**
 * Thrown when any data from the parameter field is missing.
 */
public class GPDataException extends RuntimeException{
	public static final String ERROR_SOCIAL_LOGIN_DATA_PARSING="ERROR in parsing social login data.";
	public static final String GOOGLE_DATA_ERROR ="ERROR in obtaining Google login data. ";
	public static final String FACEBOOK_DATA_ERROR ="ERROR in getting FB graph data. ";
	

	public GPDataException(final String message) {
		super(message);
	}
}
