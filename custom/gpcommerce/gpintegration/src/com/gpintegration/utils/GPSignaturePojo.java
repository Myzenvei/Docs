/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.utils;

import javax.annotation.Resource;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.gpintegration.constants.GpintegrationConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GPSignaturePojo {
	
	@Resource
	GpintegrationConstants cybersourceMerchantAliasManager; 

	@JsonProperty("transaction_uuid")
	public String transactionUuid;

	@JsonProperty("signed_date_time")
	public String signedDateTime;

	@JsonProperty("locale")
	public String locale;

	@JsonProperty("transaction_type")
	public String transactionType;
	
	@JsonProperty("payment_token")
	public String payment_token;

	@JsonProperty("reference_number")
	public String referenceNumber;

	@JsonProperty("amount")
	public String amount;

	@JsonProperty("currency")
	public String currency;

	@JsonProperty("payment_method")
	public String paymentMethod;

	@JsonProperty("bill_to_forename")
	public String billToForename;

	@JsonProperty("bill_to_surname")
	public String billToSurname;

	@JsonProperty("bill_to_email")
	public String billToEmail;

	@JsonProperty("bill_to_phone")
	public String billToPhone;

	@JsonProperty("bill_to_address_line1")
	public String billToAddressLine1;

	@JsonProperty("bill_to_address_line2")
	public String billToAddressLine2;

	@JsonProperty("bill_to_address_city")
	public String billToAddressCity;

	@JsonProperty("bill_to_address_country")
	public String billToAddressCountry;

	@JsonProperty("bill_to_address_postal_code")
	public String billToAddressPostalCode;
	
	@JsonProperty("bill_to_address_state")
	public String billToAddressState;

	@JsonProperty("signed_field_names")
	public String signedFieldNames;
	
	@JsonProperty("unsigned_field_names")
	public String unsignedFieldNames;


	public String getReferenceNumber() {
		return referenceNumber;
	}

	@JsonSetter("reference_number")
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	@JsonSetter("transaction_type")
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	@JsonSetter("transaction_uuid")
	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public String getBillToAddressLine1() {
		return billToAddressLine1;
	}

	@JsonSetter("bill_to_address_line1")
	public void setBillToAddressLine1(String billToAddressLine1) {
		this.billToAddressLine1 = billToAddressLine1;
	}

	public String getBillToAddressLine2() {
		return billToAddressLine2;
	}

	@JsonSetter("bill_to_address_line2")
	public void setBillToAddressLine2(String billToAddressLine2) {
		this.billToAddressLine2 = billToAddressLine2;
	}

	public String getBillToAddressPostalCode() {
		return billToAddressPostalCode;
	}

	@JsonSetter("bill_to_address_postal_code")
	public void setBillToAddressPostalCode(String billToAddressPostalCode) {
		this.billToAddressPostalCode = billToAddressPostalCode;
	}

	public String getBillToAddressCity() {
		return billToAddressCity;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	@JsonSetter("bill_to_address_city")
	public void setBillToAddressCity(String billToAddressCity) {
		this.billToAddressCity = billToAddressCity;
	}

	public String getAmount() {
		return amount;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	@JsonSetter("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	@JsonSetter("currency")
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the firstName
	 */
	public String getBillToForename() {
		return billToForename;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	@JsonSetter("bill_to_forename")
	public void setBillToForename(String billToForename) {
		this.billToForename = billToForename;
	}

	/**
	 * @return the lastName
	 */
	public String getBillToSurname() {
		return billToSurname;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	@JsonSetter("bill_to_surname")
	public void setBillToSurname(String billToSurname) {
		this.billToSurname = billToSurname;
	}

	/**
	 * @return the email
	 */
	public String getBillToEmail() {
		return billToEmail;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	@JsonSetter("bill_to_email")
	public void setBillToEmail(String billToEmail) {
		this.billToEmail = billToEmail;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getBillToPhone() {
		return billToPhone;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	@JsonSetter("bill_to_phone")
	public void setBillToPhone(String billToPhone) {
		this.billToPhone = billToPhone;
	}

	/**
	 * @return the signedDateTime
	 */
	public String getSignedDateTime() {
		return signedDateTime;
	}

	/**
	 * @param signedDateTime the signedDateTime to set
	 */
	@JsonSetter("signed_date_time")
	public void setSignedDateTime(String signedDateTime) {
		this.signedDateTime = signedDateTime;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	@JsonSetter("locale")
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	@JsonSetter("payment_method")
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the billToAddressCountry
	 */
	public String getBillToAddressCountry() {
		return billToAddressCountry;
	}

	/**
	 * @param billToAddressCountry the billToAddressCountry to set
	 */
	@JsonSetter("bill_to_address_country")
	public void setBillToAddressCountry(String billToAddressCountry) {
		this.billToAddressCountry = billToAddressCountry;
	}

	/**
	 * @return the billToAddressState
	 */
	public String getBillToAddressState() {
		return billToAddressState;
	}

	/**
	 * @param billToAddressState the billToAddressState to set
	 */
	@JsonSetter("bill_to_address_state")
	public void setBillToAddressState(String billToAddressState) {
		this.billToAddressState = billToAddressState;
	}

	/**
	 * @return the signedFieldNames
	 */
	public String getSignedFieldNames() {
		return signedFieldNames;
	}

	/**
	 * @param signedFieldNames the signedFieldNames to set
	 */
	@JsonSetter("signed_field_names")
	public void setSignedFieldNames(String signedFieldNames) {
		this.signedFieldNames = signedFieldNames;
	}

	/**
	 * @return the unsignedFieldNames
	 */
	public String getUnsignedFieldNames() {
		return unsignedFieldNames;
	}

	/**
	 * @param unsignedFieldNames the unsignedFieldNames to set
	 */
	@JsonSetter("unsigned_field_names")
	public void setUnsignedFieldNames(String unsignedFieldNames) {
		this.unsignedFieldNames = unsignedFieldNames;
	}
	
	public String getPayment_token() {
		return payment_token;
	}

	@JsonSetter("payment_token")
	public void setPayment_token(String payment_token) {
		this.payment_token = payment_token;
	}
}
