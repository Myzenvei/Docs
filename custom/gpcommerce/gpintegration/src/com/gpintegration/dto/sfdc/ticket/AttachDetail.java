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
@JsonPropertyOrder({ "AttachmentID", "AttachmentFile", "AttachmentType" })
public class AttachDetail {
	@JsonProperty("AttachmentID")
	private String attachmentID;
	@JsonProperty("AttachmentFile")
	private String attachmentFile;
	@JsonProperty("AttachmentType")
	private String attachmentType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("AttachmentID")
	public String getAttachmentID() {
		return attachmentID;
	}

	@JsonProperty("AttachmentID")
	public void setAttachmentID(String attachmentID) {
		this.attachmentID = attachmentID;
	}

	@JsonProperty("AttachmentFile")
	public String getAttachmentFile() {
		return attachmentFile;
	}

	@JsonProperty("AttachmentFile")
	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	@JsonProperty("AttachmentType")
	public String getAttachmentType() {
		return attachmentType;
	}

	@JsonProperty("AttachmentType")
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
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
		return "AttachDetail [attachmentID=" + attachmentID + ", attachmentFile=" + attachmentFile + ", attachmentType="
				+ attachmentType + "]";
	}
}