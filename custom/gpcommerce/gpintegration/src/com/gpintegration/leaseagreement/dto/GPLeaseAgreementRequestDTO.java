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
	"Agreement"
})

public class GPLeaseAgreementRequestDTO {


	@JsonProperty("Agreement")
	private GPLeaseAgreementDTO agreement;

	@JsonProperty("Agreement")
	public GPLeaseAgreementDTO getAgreement() {
		return agreement;
	}

	@JsonProperty("Agreement")
	public void setAgreement(GPLeaseAgreementDTO agreement) {
		this.agreement = agreement;
	}


}
