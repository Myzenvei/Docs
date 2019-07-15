/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.sfdc.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "SourceId", "AttachDetail" })

public class AttachmentDetails {

	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("AttachDetail")
	private List<AttachDetail> attachDetail = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("SourceId")
	public String getSourceId() {
		return sourceId;
	}

	@JsonProperty("SourceId")
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@JsonProperty("AttachDetail")
	public List<AttachDetail> getAttachDetail() {
		return attachDetail;
	}

	@JsonProperty("AttachDetail")
	public void setAttachDetail(List<AttachDetail> attachDetail) {
		this.attachDetail = attachDetail;
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
		return "AttachmentDetails [sourceId=" + sourceId + ", attachDetail=" + attachDetail + "]";
	}
}