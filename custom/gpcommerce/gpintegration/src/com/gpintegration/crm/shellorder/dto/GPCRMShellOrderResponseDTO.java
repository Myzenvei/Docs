/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.crm.shellorder.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"Order"
})
public class GPCRMShellOrderResponseDTO {
	
	@JsonProperty("status")
	private String status;
	
	
	@JsonProperty("message")
	private String message;
	
	
	@JsonProperty("CaseNumber")
	private String caseNumber;

	@JsonProperty("CaseId")
	private String caseId;
	
	@JsonProperty("errorstatuscode")
	private String errorstatuscode;
	

	/**
	 * @return the status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}
	

	
	/**
	 * @return the message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * @return the caseNumber
	 */
	@JsonProperty("CaseNumber")
	public String getCaseNumber() {
		return caseNumber;
	}

	/**
	 * @return the caseId
	 */
	@JsonProperty("CaseId")
	public String getCaseId() {
		return caseId;
	}

	/**
	 * @param status the status to set
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param message the message to set
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param caseNumber the caseNumber to set
	 */
	@JsonProperty("CaseNumber")
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	/**
	 * @param caseId the caseId to set
	 */
	@JsonProperty("CaseId")
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	
	
	
	/**
	 * @return the errorstatuscode
	 */
	@JsonProperty("errorstatuscode")
	public String getErrorstatuscode() {
		return errorstatuscode;
	}



	/**
	 * @param errorstatuscode the errorstatuscode to set
	 */
	@JsonProperty("errorstatuscode")
	public void setErrorstatuscode(String errorstatuscode) {
		this.errorstatuscode = errorstatuscode;
	}



	@Override
	public String toString() {
	return new ToStringBuilder(this).append("caseNumber", caseNumber).append("message", message).append("status", status).append("caseId", caseId).toString();
	}
	

}
