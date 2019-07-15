/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.utils;

public class GPSignatureResponseDTO {
	private String profile_id;
	private String transaction_uuid; 
	private String cyber_source_url;
	private String reference_number;
	private String locale;
	private String currency;
	private String signature;
	private String signed_date_time;

	private String access_key;
	public String getAccess_key() {
		return access_key;
	}
	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}
	public String getProfile_id() {
		return profile_id;
	}
	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}
	public String getTransaction_uuid() {
		return transaction_uuid;
	}
	public void setTransaction_uuid(String transaction_uuid) {
		this.transaction_uuid = transaction_uuid;
	}
	public String getCyber_source_url() {
		return cyber_source_url;
	}
	public void setCyber_source_url(String cyber_source_url) {
		this.cyber_source_url = cyber_source_url;
	}
	public String getReference_number() {
		return reference_number;
	}
	public void setReference_number(String reference_number) {
		this.reference_number = reference_number;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSigned_date_time() {
		return signed_date_time;
	}
	public void setSigned_date_time(String signed_date_time) {
		this.signed_date_time = signed_date_time;
	}
	
	
}
