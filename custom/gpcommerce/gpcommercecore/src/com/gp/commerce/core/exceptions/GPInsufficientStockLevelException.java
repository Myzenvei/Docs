/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.exceptions;

import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;

public class GPInsufficientStockLevelException extends InvalidCartException{

	private String code;
	
	public GPInsufficientStockLevelException(String code,String message) {
		super(message);
		this.code = code;
		// TODO Auto-generated constructor stub
	}
	
	public GPInsufficientStockLevelException(String code,String message,Throwable cause) {
		super(message);
		this.code = code;
		// TODO Auto-generated constructor stub
	}
	
	public String getCode() {
		return code;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
}
