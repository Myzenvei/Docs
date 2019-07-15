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
"AgreementResponse",
"ProductResponse"
})
public class GPLeaseAgreementResponseDTO {

	@JsonProperty("AgreementResponse")
	private GPAgreementResponseDTO agreementResponse;
	@JsonProperty("AgreementResponse")
	public GPAgreementResponseDTO getAgreementResponse() {
	return agreementResponse;
	}

	@JsonProperty("AgreementResponse")
	public void setAgreementResponse(GPAgreementResponseDTO agreementResponse) {
	this.agreementResponse = agreementResponse;
	}

}
