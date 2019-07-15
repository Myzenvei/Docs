/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;

public class Transactions {
	private Amount amount;

	private Payment_options payment_options;

	private Item_list item_list;

	private String description;

	private String soft_descriptor;

	private String invoice_number;

	private String custom;
	
	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public Payment_options getPayment_options() {
		return payment_options;
	}

	public void setPayment_options(Payment_options payment_options) {
		this.payment_options = payment_options;
	}

	public Item_list getItem_list() {
		return item_list;
	}

	public void setItem_list(Item_list item_list) {
		this.item_list = item_list;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSoft_descriptor() {
		return soft_descriptor;
	}

	public void setSoft_descriptor(String soft_descriptor) {
		this.soft_descriptor = soft_descriptor;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
}
