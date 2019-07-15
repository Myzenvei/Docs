/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.ymarketing;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactsDTO {

	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("IdOrigin")
	private String idOrigin;
	
	@JsonProperty("Timestamp")
	private String timestamp;
	
	@JsonProperty("EMailAddress")
	private String emailAddress;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("FullName")
	private String fullName;
	
	@JsonProperty("IsConsumer")
	private boolean isConsumer;
	
	@JsonProperty("IsContact")
	private boolean isContact;
	
	@JsonProperty("LastName")
	private String lastName;
	
	@JsonProperty("MobilePhoneNumber")
	private String mobilePhoneNumber;
	
	@JsonProperty("TitleDescription")
	private String titleDescription;
	
	@JsonProperty("PostalCode")
	private String postalCode;
	
	
	@JsonProperty("MarketingPermissions")
	private List<ContactMarketingPermissionsDTO> marketingPermissions;

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("IdOrigin")
	public String getIdOrigin() {
		return idOrigin;
	}

	@JsonProperty("IdOrigin")
	public void setIdOrigin(String idOrigin) {
		this.idOrigin = idOrigin;
	}

	@JsonProperty("Timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	@JsonProperty("Timestamp")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty("EMailAddress")
	public String getEmailAddress() {
		return emailAddress;
	}

	@JsonProperty("EMailAddress")
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@JsonProperty("FirstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("FirstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("FullName")
	public String getFullName() {
		return fullName;
	}

	@JsonProperty("FullName")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonProperty("IsConsumer")
	public boolean getIsConsumer() {
		return isConsumer;
	}

	@JsonProperty("IsConsumer")
	public void setIsConsumer(boolean isConsumer) {
		this.isConsumer = isConsumer;
	}

	@JsonProperty("IsContact")
	public boolean getIsContact() {
		return isContact;
	}

	@JsonProperty("IsContact")
	public void setIsContact(boolean isContact) {
		this.isContact = isContact;
	}

	@JsonProperty("LastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("LastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("MobilePhoneNumber")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	@JsonProperty("MobilePhoneNumber")
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@JsonProperty("TitleDescription")
	public String getTitleDescription() {
		return titleDescription;
	}

	@JsonProperty("TitleDescription")
	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}
	
	@JsonProperty("PostalCode")
	public String getPostalCode() {
		return postalCode;
	}

	@JsonProperty("PostalCode")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	

	@JsonProperty("MarketingPermissions")
	public List<ContactMarketingPermissionsDTO> getMarketingPermissions() {
		return marketingPermissions;
	}

	
	@JsonProperty("MarketingPermissions")
	public void setMarketingPermissions(List<ContactMarketingPermissionsDTO> marketingPermissions) {
		this.marketingPermissions = marketingPermissions;
	}
	
	@Override
	public String toString() {
	return new ToStringBuilder(this).append("Id",id).append("IdOrigin",idOrigin).append("Timestamp",timestamp).append("EMailAddress",emailAddress).append("FirstName",firstName).append("FullName",fullName).append("IsConsumer",isConsumer).append("IsContact",isContact).append("LastName",lastName).append("MobilePhoneNumber",mobilePhoneNumber).append("TitleDescription",titleDescription).append("PostalCode",postalCode).append("MarketingPermissions",marketingPermissions).toString();
	}
}
