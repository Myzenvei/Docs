/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.leaseagreement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GPLegalTermsDTO {
	@JsonProperty("ID")
	private String id;
	
	@JsonProperty("Version")
	private String version;
	
	@JsonProperty("Country")
	private String country;
	
	@JsonProperty("LegalTermsText")
	private String legalTermsText;
	
	@JsonProperty("LegalTermName")
	private String legalTermName;

	@JsonProperty("LegalLanguage")
	private String legalLanguage;
	
	@JsonProperty("Approval_Date")
	private String approvalDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLegalTermsText() {
		return legalTermsText;
	}
	public void setLegalTermsText(String legalTermsText) {
		this.legalTermsText = legalTermsText;
	}
	public String getLegalTermName() {
		return legalTermName;
	}
	public void setLegalTermName(String legalTermName) {
		this.legalTermName = legalTermName;
	}
	public String getLegalLanguage() {
		return legalLanguage;
	}
	public void setLegalLanguage(String legalLanguage) {
		this.legalLanguage = legalLanguage;
	}
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	
	

}
