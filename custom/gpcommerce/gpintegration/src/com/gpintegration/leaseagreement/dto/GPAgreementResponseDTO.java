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
	"AgreementId",
	"Status",
	"ErrorMessage",
	"ErrorCode"
})
public class GPAgreementResponseDTO {

	@JsonProperty("AgreementId")
	private String agreementId;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("ErrorMessage")
	private String errorMessage;
	@JsonProperty("ErrorCode")
	private String errorCode;
	@JsonProperty("time")
	private String time;
	@JsonProperty("messageID")
	private String messageID;

	@JsonProperty("AgreementId")
	public String getAgreementId() {
		return agreementId;
	}

	@JsonProperty("AgreementId")
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
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

	/**
	 * @return the time
	 */
	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	/**
	 * @return the messageID
	 */
	@JsonProperty("messageID")
	public String getMessageID() {
		return messageID;
	}

	/**
	 * @param time the time to set
	 */
	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @param messageID the messageID to set
	 */
	@JsonProperty("messageID")
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	
	

}
