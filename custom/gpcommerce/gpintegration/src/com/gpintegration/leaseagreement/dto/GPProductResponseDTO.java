/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.leaseagreement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"Id",
	"Status",
	"ErrorMessage",
	"ErrorCode"
})
public class GPProductResponseDTO {

	@JsonProperty("Id")
	private String id;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("ErrorMessage")
	private String errorMessage;
	@JsonProperty("ErrorCode")
	private String errorCode;

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("Status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("ErrorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}

	@JsonProperty("ErrorMessage")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@JsonProperty("ErrorCode")
	public String getErrorCode() {
		return errorCode;
	}

	@JsonProperty("ErrorCode")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}