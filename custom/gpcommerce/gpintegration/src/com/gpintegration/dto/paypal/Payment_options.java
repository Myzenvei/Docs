/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;

public class Payment_options {
	private String allowed_payment_method;
	private boolean recurring_flag;
	private boolean skip_fmf;
	
	public boolean isSkip_fmf() {
		return skip_fmf;
	}

	public void setSkip_fmf(boolean skip_fmf) {
		this.skip_fmf = skip_fmf;
	}

	public String getAllowed_payment_method() {
		return allowed_payment_method;
	}

	public void setAllowed_payment_method(String allowed_payment_method) {
		this.allowed_payment_method = allowed_payment_method;
	}
	
	public boolean isRecurring_flag() {
		return recurring_flag;
	}

	public void setRecurring_flag(boolean recurring_flag) {
		this.recurring_flag = recurring_flag;
	}

}