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
"Order"
})
public class GPCRMShellOrderDTO {

@JsonProperty("Order")
private GPCRMOrderDTO order;




/**
 * @return the order
 */
@JsonProperty("Order")
public GPCRMOrderDTO getOrder() {
	return order;
}

/**
 * @param order the order to set
 */
@JsonProperty("Order")
public void setOrder(GPCRMOrderDTO order) {
	this.order = order;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("order", order).toString();
}


}