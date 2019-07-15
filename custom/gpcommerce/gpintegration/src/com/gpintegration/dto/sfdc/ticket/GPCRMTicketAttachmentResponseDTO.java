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
@JsonPropertyOrder({ "status", "message", "CaseNumber", "CaseId" })
public class GPCRMTicketAttachmentResponseDTO {

	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("CaseNumber")
	private String caseNumber;
	@JsonProperty("CaseId")
	private String caseId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("CaseNumber")
	public String getCaseNumber() {
		return caseNumber;
	}

	@JsonProperty("CaseNumber")
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	@JsonProperty("CaseId")
	public String getCaseId() {
		return caseId;
	}

	@JsonProperty("CaseId")
	public void setCaseId(String caseId) {
		this.caseId = caseId;
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
		return "GPCRMTicketAttachmentResponseDTO [status=" + status + ", message=" + message + ", caseNumber="
				+ caseNumber + ", caseId=" + caseId + "]";
	}
}