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
public class CreateContactRequestDTO {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Timestamp")
	private String timestamp;

	@JsonProperty("UserName")
	private String username;

	@JsonProperty("SourceSystemType")
	private String sourceSystemType;

	@JsonProperty("SourceSystemId")
	private String sourceSystemId;

	@JsonProperty("Contacts")
	List<ContactsDTO> contacts;

	@JsonProperty("ContactMarketingPermissions")
	private List<ContactMarketingPermissionsDTO> contactMarketingPermissions;

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(final String id) {
		this.id = id;
	}

	@JsonProperty("Timestamp")
	public String getTimeStamp() {
		return timestamp;
	}

	@JsonProperty("Timestamp")
	public void setTimeStamp(final String timeStamp) {
		this.timestamp = timeStamp;
	}

	@JsonProperty("UserName")
	public String getUsername() {
		return username;
	}

	@JsonProperty("UserName")
	public void setUsername(final String username) {
		this.username = username;
	}

	@JsonProperty("SourceSystemType")
	public String getSourceSystemType() {
		return sourceSystemType;
	}

	@JsonProperty("SourceSystemType")
	public void setSourceSystemType(final String sourceSystemType) {
		this.sourceSystemType = sourceSystemType;
	}

	@JsonProperty("SourceSystemId")
	public String getSourceSystemId() {
		return sourceSystemId;
	}

	@JsonProperty("SourceSystemId")
	public void setSourceSystemId(final String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	@JsonProperty("Contacts")
	public List<ContactsDTO> getContacts() {
		return contacts;
	}

	@JsonProperty("Contacts")
	public void setContacts(final List<ContactsDTO> contacts) {
		this.contacts = contacts;
	}

	@JsonProperty("ContactMarketingPermissions")
	public List<ContactMarketingPermissionsDTO> getContactMarketingPermissions()
	{
		return contactMarketingPermissions;
	}

	@JsonProperty("ContactMarketingPermissions")
	public void setContactMarketingPermissions(final List<ContactMarketingPermissionsDTO> contactMarketingPermissions)
	{
		this.contactMarketingPermissions = contactMarketingPermissions;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("Id", id).append("Timestamp", timestamp).append("UserName", username)
				.append("SourceSystemType", sourceSystemType).append("SourceSystemId", sourceSystemId).append("Contacts", contacts)
				.append("ContactMarketingPermissions",contactMarketingPermissions).toString();
	}
}
