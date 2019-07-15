/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.netsuite;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.commerce.facade.data.NsConsignmentData;

public class GPNetsuiteOrderUpdateDTO {
	@JsonProperty("status")
	private String status;
	@JsonProperty("shipments")
	private List<NsConsignmentData> shipments;
	
	public List<NsConsignmentData> getShipments() {
		return shipments;
	}
	public void setShipments(List<NsConsignmentData> shipments) {
		this.shipments = shipments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
