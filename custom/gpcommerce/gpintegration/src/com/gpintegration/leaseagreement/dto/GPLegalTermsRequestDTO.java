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
@JsonPropertyOrder({ "Country" })
public class GPLegalTermsRequestDTO {
	
	@JsonProperty("Country")
	private String Country;

	@JsonProperty("Country")
	public String getCountry() {
		return Country;
	}

	@JsonProperty("Country")
	public void setCountry(String country) {
		Country = country;
	}
	
	

}
