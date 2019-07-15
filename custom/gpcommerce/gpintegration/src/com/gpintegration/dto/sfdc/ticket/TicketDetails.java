/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.sfdc.ticket;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "TopicofEnquiry", "TicketComments", "TicketNumber", "TicketStatus", "FirstName", "LastName",
		"Email", "Address1", "Address2", "PhoneNumber", "City", "State", "Country", "WebsiteURL", "ZipCode", "OrderNumber",
		"CustomerType","keyQuantities","dispenserType" })
public class TicketDetails {

	@JsonProperty("KeyQuantities")
	private int keyQuantities;

	@JsonProperty("DispenserType")
	private String dispenserType;

	@JsonProperty("TopicofEnquiry")
	private String topicofEnquiry;
	@JsonProperty("TicketComments")
	private String ticketComments;
	@JsonProperty("TicketNumber")
	private String ticketNumber;
	@JsonProperty("TicketStatus")
	private String ticketStatus;
	@JsonProperty("FirstName")
	private String firstName;
	@JsonProperty("LastName")
	private String lastName;
	@JsonProperty("Email")
	private String email;
	@JsonProperty("Address1")
	private String address1;
	@JsonProperty("Address2")
	private String address2;
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	@JsonProperty("City")
	private String city;
	@JsonProperty("State")
	private String state;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("WebsiteURL")
	private String websiteURL;
	@JsonProperty("ZipCode")
	private String zipCode;
	@JsonProperty("OrderNumber")
	private String orderNumber;
	@JsonProperty("CustomerType")
	private String customerType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("TopicofEnquiry")
	public String getTopicofEnquiry() {
		return topicofEnquiry;
	}

	@JsonProperty("TopicofEnquiry")
	public void setTopicofEnquiry(String topicofEnquiry) {
		this.topicofEnquiry = topicofEnquiry;
	}

	@JsonProperty("TicketComments")
	public String getTicketComments() {
		return ticketComments;
	}

	@JsonProperty("TicketComments")
	public void setTicketComments(String ticketComments) {
		this.ticketComments = ticketComments;
	}

	@JsonProperty("TicketNumber")
	public String getTicketNumber() {
		return ticketNumber;
	}

	@JsonProperty("TicketNumber")
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	@JsonProperty("TicketStatus")
	public String getTicketStatus() {
		return ticketStatus;
	}

	@JsonProperty("TicketStatus")
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	@JsonProperty("FirstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("FirstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("LastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("LastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("Email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("Email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("Address1")
	public String getAddress1() {
		return address1;
	}

	@JsonProperty("Address1")
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@JsonProperty("Address2")
	public String getAddress2() {
		return address2;
	}

	@JsonProperty("Address2")
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@JsonProperty("PhoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty("PhoneNumber")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	@JsonProperty("Country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("Country")
	public void setCountry(String country) {
		this.country = country;
	}

	@JsonProperty("WebsiteURL")
	public String getWebsiteURL() {
		return websiteURL;
	}

	@JsonProperty("WebsiteURL")
	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

	@JsonProperty("ZipCode")
	public String getZipCode() {
		return zipCode;
	}

	@JsonProperty("ZipCode")
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@JsonProperty("OrderNumber")
	public String getOrderNumber() {
		return orderNumber;
	}

	@JsonProperty("KeyQuantities")
	public int getKeyQuantities() {
		return keyQuantities;
	}

	@JsonProperty("KeyQuantities")
	public void setKeyQuantities(int keyQuantities) {
		this.keyQuantities = keyQuantities;
	}

	@JsonProperty("DispenserType")
	public String getDispenserType() {
		return dispenserType;
	}

	@JsonProperty("DispenserType")
	public void setDispenserType(String dispenserType) {
		this.dispenserType = dispenserType;
	}

	@JsonProperty("OrderNumber")
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@JsonProperty("CustomerType")
	public String getCustomerType() {
		return customerType;
	}

	@JsonProperty("CustomerType")
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}


	@Override
	public String toString() {
		return "TicketDetails [topicofEnquiry=" + topicofEnquiry + ", ticketComments=" + ticketComments
				+ ", ticketNumber=" + ticketNumber + ", ticketStatus=" + ticketStatus + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", address1=" + address1 + ", address2=" + address2
				+ ", phoneNumber=" + phoneNumber + ", city=" + city + ", state=" + state + ", country=" + country
				+ ", websiteURL=" + websiteURL + ", zipCode=" + zipCode + ", orderNumber=" + orderNumber
				+ ", customerType=" + customerType + "]";
	}
}