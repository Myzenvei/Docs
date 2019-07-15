/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.exceptions;

public class GPWishlistException extends RuntimeException {

	String code;

	public GPWishlistException() {
		super();
	}

	public GPWishlistException(String code,String message) {
	        super(message);
	        this.code = code;
	    }

	public GPWishlistException(String code,String message, Throwable cause) {
	        super(message, cause);
	        this.code = code;
	    }
	
	public String getCode() {
		return code;
	}
}
