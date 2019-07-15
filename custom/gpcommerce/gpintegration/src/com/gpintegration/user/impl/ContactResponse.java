/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.user.impl;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
   
    "Status",
    "ErrorMessage",
    "ContactId",
    "ErrorCode"
})
public class ContactResponse {

	@JsonProperty("ErrorMessage")
    public String getErrorMessage() {
		return message;
	}

	@JsonProperty("Status")
	public boolean isStatus() {
		return status;
	}

	@JsonProperty("ErrorCode")
	public String getErrorCode() {
		return errorCode;
	}

	@JsonProperty("ContactId")
	public String getContactId() {
		return contactId;
	}

	@JsonProperty("Status")
    private boolean status;
    @JsonProperty("ErrorMessage")
    private String message;
    @JsonProperty("ErrorCode")
    private String errorCode;
    @JsonProperty("ContactId")
    private String contactId;
    
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    @JsonProperty("Status")
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    @JsonProperty("ErrorMessage")
    public void setErrorMessage(String message) {
        this.message = message;
    }
    
    @JsonProperty("ContactId")
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    
    @JsonProperty("ErrorCode")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
