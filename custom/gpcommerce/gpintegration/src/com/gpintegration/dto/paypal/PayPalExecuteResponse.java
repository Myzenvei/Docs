/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.paypal;
import com.gpintegration.dto.out.paypal.Transactions;
import com.gpintegration.dto.out.paypal.Payer;

public class PayPalExecuteResponse {
	private String id;

	private Transactions[] transactions;

	private Payer payer;

	private String create_time;

	private String state;

	private Links[] links;

	private String cart;

	private String intent;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Transactions[] getTransactions() {
		return transactions;
	}

	public void setTransactions(Transactions[] transactions) {
		this.transactions = transactions;
	}

	public Payer getPayer() {
		return payer;
	}

	public void setPayer(Payer payer) {
		this.payer = payer;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public String getCart() {
		return cart;
	}

	public void setCart(String cart) {
		this.cart = cart;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

}
