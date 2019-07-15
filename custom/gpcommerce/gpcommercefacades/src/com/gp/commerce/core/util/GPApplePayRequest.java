/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

public class GPApplePayRequest {
	
	private String displayName;
	private String initiative;
	private String initiativeContext;
	private String merchantIdentifier;
	
	public String getInitiative() {
		return initiative;
	}
	public void setInitiative(String initiative) {
		this.initiative = initiative;
	}
	public String getInitiativeContext() {
		return initiativeContext;
	}
	public void setInitiativeContext(String initiativeContext) {
		this.initiativeContext = initiativeContext;
	}
	public String getMerchantIdentifier() {
		return merchantIdentifier;
	}
	public void setMerchantIdentifier(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
