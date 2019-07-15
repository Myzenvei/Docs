/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.leaseagreement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "LegalTermResponses" })
public class GPLegalTermResponseWrapperDTO {

	@JsonProperty("LegalTermResponses")
	private  GPLegalTermResponseDTO LegalTermResponses;

	
	@JsonProperty("error")
	private  String error;
	
	@JsonProperty("LegalTermResponses")
	public GPLegalTermResponseDTO getLegalTermResponses() {
		return LegalTermResponses;
	}

	@JsonProperty("LegalTermResponses")
	public void setLegalTermResponses(GPLegalTermResponseDTO legalTermResponses) {
		LegalTermResponses = legalTermResponses;
	}

	/**
	 * @return the error
	 */
	@JsonProperty("error")
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}
	
	
	
	
}
