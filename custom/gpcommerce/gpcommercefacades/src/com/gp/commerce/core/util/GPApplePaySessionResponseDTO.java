/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

public class GPApplePaySessionResponseDTO {
	private String epochTimestamp;
	private String expiresAt;
	private String merchantSessionIdentifier;
	private String nonce;
	private String merchantIdentifier;
	private String domainName;
	private String displayName;
	private String signature;
	
	public String getEpochTimestamp() {
		return epochTimestamp;
	}
	public void setEpochTimestamp(String epochTimestamp) {
		this.epochTimestamp = epochTimestamp;
	}
	public String getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}
	public String getMerchantSessionIdentifier() {
		return merchantSessionIdentifier;
	}
	public void setMerchantSessionIdentifier(String merchantSessionIdentifier) {
		this.merchantSessionIdentifier = merchantSessionIdentifier;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getMerchantIdentifier() {
		return merchantIdentifier;
	}
	public void setMerchantIdentifier(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
