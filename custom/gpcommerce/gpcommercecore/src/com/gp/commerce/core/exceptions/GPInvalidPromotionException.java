/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.exceptions;

public class GPInvalidPromotionException extends GPException{

	public GPInvalidPromotionException(String code, String message) {
		super(code, message);
	}
	
	
	public GPInvalidPromotionException(String code ,String message, Throwable cause) {
		super(code,message,cause);
	}

}
