/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.leaseagreement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "legalTerms" })
public class GPLegalTermResponseDTO {
	
	@JsonProperty("LegalTerms")
	private List<GPLegalTermsDTO> legalTerms;

	@JsonProperty("LegalTerms")
	public List<GPLegalTermsDTO> getLegalTerms() {
		return legalTerms;
	}
	@JsonProperty("LegalTerms")
	public void setLegalTerms(List<GPLegalTermsDTO> legalTerms) {
		this.legalTerms = legalTerms;
	}
	
	
	

}
