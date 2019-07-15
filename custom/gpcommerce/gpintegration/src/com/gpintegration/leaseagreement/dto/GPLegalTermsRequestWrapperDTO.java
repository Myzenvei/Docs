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
@JsonPropertyOrder({ "LegalTermsRequest" })
public class GPLegalTermsRequestWrapperDTO {
	@JsonProperty("LegalTermsRequest")
	private GPLegalTermsRequestDTO LegalTermsRequest;

	@JsonProperty("LegalTermsRequest")
	public GPLegalTermsRequestDTO getLegalTermsRequestDTO() {
		return LegalTermsRequest;
	}

	@JsonProperty("LegalTermsRequest")
	public void setLegalTermsRequest(GPLegalTermsRequestDTO legalTermsRequest) {
		LegalTermsRequest = legalTermsRequest;
	}
	

}
