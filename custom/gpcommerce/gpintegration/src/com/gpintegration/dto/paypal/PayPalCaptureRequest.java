/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;
import com.gpintegration.dto.out.paypal.Amount;
public class PayPalCaptureRequest {
	private Amount amount;

	private boolean is_final_capture;

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public boolean getIs_final_capture() {
		return is_final_capture;
	}

	public void setIs_final_capture(boolean is_final_capture) {
		this.is_final_capture = is_final_capture;
	}

}
