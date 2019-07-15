/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.exceptions;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

public class GPSampleCartException extends CommerceCartModificationException{


	private String code;
	
		public GPSampleCartException(String code ,String message) {
		super(message);
		this.code = code;
	}

		public GPSampleCartException(String code ,String message, Throwable cause) {
	        super(message, cause);
	        this.code = code;
	    }
		
		public String getCode() {
			return code;
		}
		
		@Override
		public String getMessage() {
			return super.getMessage();
		}
	

}
