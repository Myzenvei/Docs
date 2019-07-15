/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.user.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "AddressType", "Street", "City", "State", "Country", "AdditionalAddressLine" })
public class Address {

	@JsonProperty("AddressType")
	private String addressType;
	@JsonProperty("Street")
	private String street;
	@JsonProperty("City")
	private String city;
	@JsonProperty("State")
	private String state;
	@JsonProperty("CountryCode")
	private String countryCode;
	@JsonProperty("PostalCode")
	private String postalCode;
	@JsonProperty("AdditionalAddressLine")
	private String additionalAddressLine;

	@JsonProperty("AdditionalAddressLine")
	public String getAdditionalAddressLine() {
		return additionalAddressLine;
	}

	@JsonProperty("AdditionalAddressLine")
	public void setAdditionalAddressLine(String additionalAddressLine) {
		this.additionalAddressLine = additionalAddressLine;
	}

	
	@JsonProperty("AddressType")
	public String getAddressType() {
		return addressType;
	}

	@JsonProperty("AddressType")
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	@JsonProperty("Street")
	public String getStreet() {
		return street;
	}

	@JsonProperty("Street")
	public void setStreet(String street) {
		this.street = street;
	}

	@JsonProperty("City")
	public String getCity() {
		return city;
	}

	@JsonProperty("City")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("State")
	public String getState() {
		return state;
	}

	@JsonProperty("State")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("CountryCode")
	public String getCountryCode() {
		return countryCode;
	}

	@JsonProperty("CountryCode")
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@JsonProperty("PostalCode")
	public String getPostalCode() {
		return postalCode;
	}

	@JsonProperty("PostalCode")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	

}
