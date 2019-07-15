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
@JsonPropertyOrder({ "AttachmentDetails" })
public class GPCRMTicketAttachmentRequestDTO {

	@JsonProperty("AttachmentDetails")
	private AttachmentDetails attachmentDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("AttachmentDetails")
	public AttachmentDetails getAttachmentDetails() {
		return attachmentDetails;
	}

	@JsonProperty("AttachmentDetails")
	public void setAttachmentDetails(AttachmentDetails attachmentDetails) {
		this.attachmentDetails = attachmentDetails;
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
		return "GPCRMTicketAttachmentRequestDTO [attachmentDetails=" + attachmentDetails + "]";
	}
}