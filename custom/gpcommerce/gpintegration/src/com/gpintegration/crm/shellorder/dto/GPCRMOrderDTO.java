/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.crm.shellorder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"OrderDate",
"OrderHeader",
"ContactDetails"
})
public class GPCRMOrderDTO {

@JsonProperty("OrderDate")
private String orderDate;
@JsonProperty("OrderHeader")
private GPCRMOrderHeaderDTO orderHeader;
@JsonProperty("ContactDetails")
private GPCRMContactDetailsDTO contactDetails;

@JsonProperty("OrderDate")
public String getOrderDate() {
return orderDate;
}

@JsonProperty("OrderDate")
public void setOrderDate(String orderDate) {
this.orderDate = orderDate;
}


/**
 * @return the orderHeader
 */
@JsonProperty("OrderHeader")
public GPCRMOrderHeaderDTO getOrderHeader() {
	return orderHeader;
}

/**
 * @param orderHeader the orderHeader to set
 */
@JsonProperty("OrderHeader")
public void setOrderHeader(GPCRMOrderHeaderDTO orderHeader) {
	this.orderHeader = orderHeader;
}

/**
 * @return the contactDetails
 */
@JsonProperty("ContactDetails")
public GPCRMContactDetailsDTO getContactDetails() {
	return contactDetails;
}

/**
 * @param contactDetails the contactDetails to set
 */
@JsonProperty("ContactDetails")
public void setContactDetails(GPCRMContactDetailsDTO contactDetails) {
	this.contactDetails = contactDetails;
}




@Override
public String toString() {
return new ToStringBuilder(this).append("orderDate", orderDate).append("orderHeader", orderHeader).append("contactDetails", contactDetails).toString();
}

}