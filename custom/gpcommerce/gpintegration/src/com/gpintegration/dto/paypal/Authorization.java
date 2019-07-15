/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;

public class Authorization {
	private String reason_code;

	private Amount amount;

	private String id;

	private String parent_payment;

	private String update_time;

	private String create_time;

	private String payment_mode;

	private String state;

	private Links[] links;

	private String valid_until;

	private String protection_eligibility_type;

	private String protection_eligibility;

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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getPayment_mode() {
		return payment_mode;
	}

	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Links[] getLinks() {
		return links;
	}

	public void setLinks(Links[] links) {
		this.links = links;
	}

	public String getValid_until() {
		return valid_until;
	}

	public void setValid_until(String valid_until) {
		this.valid_until = valid_until;
	}

	public String getProtection_eligibility_type() {
		return protection_eligibility_type;
	}

	public void setProtection_eligibility_type(String protection_eligibility_type) {
		this.protection_eligibility_type = protection_eligibility_type;
	}

	public String getProtection_eligibility() {
		return protection_eligibility;
	}

	public void setProtection_eligibility(String protection_eligibility) {
		this.protection_eligibility = protection_eligibility;
	}

}