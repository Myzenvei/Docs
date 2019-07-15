/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;
import com.gpintegration.dto.out.paypal.Amount;

public class PayPalCaptureResponse {
	private String reason_code;

	private Amount amount;

	private String id;

	private String parent_payment;

	private String update_time;

	private String state;

	private String create_time;

	private String is_final_capture;

	private Links[] links;

	private Transaction_fee transaction_fee;

	public String getReason_code() {
		return reason_code;
	}

	public void setReason_code(String reason_code) {
		this.reason_code = reason_code;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_payment() {
		return parent_payment;
	}

	public void setParent_payment(String parent_payment) {
		this.parent_payment = parent_payment;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getIs_final_capture() {
		return is_final_capture;
	}

	public void setIs_final_capture(String is_final_capture) {
		this.is_final_capture = is_final_capture;
	}

	public Links[] getLinks() {
		return links;
	}

	public void setLinks(Links[] links) {
		this.links = links;
	}

	public Transaction_fee getTransaction_fee() {
		return transaction_fee;
	}

	public void setTransaction_fee(Transaction_fee transaction_fee) {
		this.transaction_fee = transaction_fee;
	}

}
