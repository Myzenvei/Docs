/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.knowledgecenter.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GPKnowledgeCenterSkuResponse {
	
	@JsonProperty("infoResources")
	List<InfoResourcesDTO> infoResources ;
	
	@JsonProperty("supportResources")
	List<SupportResourcesDTO> supportResources;

	public List<InfoResourcesDTO> getInfoResources() {
		return infoResources;
	}

	public void setInfoResources(List<InfoResourcesDTO> infoResources) {
		this.infoResources = infoResources;
	}

	public List<SupportResourcesDTO> getSupportResources() {
		return supportResources;
	}

	public void setSupportResources(List<SupportResourcesDTO> supportResources) {
		this.supportResources = supportResources;
	}
	
	
}
