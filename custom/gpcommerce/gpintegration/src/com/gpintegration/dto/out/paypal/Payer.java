/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.out.paypal;

import com.gpintegration.dto.paypal.Payer_info;

public class Payer {
	private String payment_method;
	private Payer_info payer_info;

	public Payer_info getPayer_info() {
		return payer_info;
	}

	public void setPayer_info(Payer_info payer_info) {
		this.payer_info = payer_info;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
}
