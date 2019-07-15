/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.ymarketing;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactMarketingPermissionsDTO {

	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("IdOrigin")
	private String idOrigin;
	
	@JsonProperty("Timestamp")
	private String timestamp;
	
	@JsonProperty("OptIn")
	private String optIn;
	
	@JsonProperty("OutboundCommunicationMedium")
	private String outboundCommunicationMedium;
	
	@JsonProperty("CommunicationMedium")
	private String communicationMedium;
	
	@JsonProperty("CommunicationDirection")
	private String communicationDirection;
	
	@JsonProperty("MarketingAreaId")
	private String marketingAreaId;
	
	@JsonProperty("CommunicationCategoryId")
	private String  communicationCategoryId;

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("IdOrigin")
	public String getIdOrigin() {
		return idOrigin;
	}

	@JsonProperty("IdOrigin")
	public void setIdOrigin(String idOrigin) {
		this.idOrigin = idOrigin;
	}

	@JsonProperty("Timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	@JsonProperty("Timestamp")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty("OptIn")
	public String getOptIn() {
		return optIn;
	}

	@JsonProperty("OptIn")
	public void setOptIn(String optIn) {
		this.optIn = optIn;
	}

	@JsonProperty("OutboundCommunicationMedium")
	public String getOutboundCommunicationMedium() {
		return outboundCommunicationMedium;
	}

	@JsonProperty("OutboundCommunicationMedium")
	public void setOutboundCommunicationMedium(String outboundCommunicationMedium) {
		this.outboundCommunicationMedium = outboundCommunicationMedium;
	}

	@JsonProperty("CommunicationDirection")
	public String getCommunicationDirection() {
		return communicationDirection;
	}

	@JsonProperty("CommunicationDirection")
	public void setCommunicationDirection(String communicationDirection) {
		this.communicationDirection = communicationDirection;
	}
	
	@Override
	public String toString() {
	return new ToStringBuilder(this).append("Id",id).append("IdOrigin",idOrigin).append("Timestamp",timestamp).append("OptIn",optIn).append("OutboundCommunicationMedium",outboundCommunicationMedium).append("CommunicationDirection",communicationDirection).append("CommunicationCategoryId",communicationCategoryId).toString();
	}

	@JsonProperty("MarketingAreaId")
	public String getMarketingAreaId() {
		return marketingAreaId;
	}

	@JsonProperty("MarketingAreaId")
	public void setMarketingAreaId(String marketingAreaId) {
		this.marketingAreaId = marketingAreaId;
	}

	@JsonProperty("CommunicationMedium")
	public String getCommunicationMedium() {
		return communicationMedium;
	}

	@JsonProperty("CommunicationMedium")
	public void setCommunicationMedium(String communicationMedium) {
		this.communicationMedium = communicationMedium;
	}

	@JsonProperty("CommunicationCategoryId")
	public String getCommunicationCategoryId() {
		return communicationCategoryId;
	}

	@JsonProperty("CommunicationCategoryId")
	public void setCommunicationCategoryId(String communicationCategoryId) {
		this.communicationCategoryId = communicationCategoryId;
	}
	
	
}
