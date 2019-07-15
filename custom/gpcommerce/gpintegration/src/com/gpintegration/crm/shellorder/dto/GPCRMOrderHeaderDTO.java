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
"EcommerceOrderNumber",
"ServiceLevelofEcommerceOrder",
"TrackingId",
"EcommerceOrderStatus",
"OrderTotalAmount",
"SourceProductList",
"SourceUrl",
"ShippingDate"
})
public class GPCRMOrderHeaderDTO {

@JsonProperty("EcommerceOrderNumber")
private String ecommerceOrderNumber;
@JsonProperty("ServiceLevelofEcommerceOrder")
private String serviceLevelofEcommerceOrder;
@JsonProperty("TrackingId")
private String trackingId;
@JsonProperty("EcommerceOrderStatus")
private String ecommerceOrderStatus;
@JsonProperty("OrderTotalAmount")
private String orderTotalAmount;
@JsonProperty("SourceProductList")
private String sourceProductList;
@JsonProperty("SourceUrl")
private String sourceUrl;
@JsonProperty("ShippingDate")
private String shippingDate;

@JsonProperty("EcommerceOrderNumber")
public String getEcommerceOrderNumber() {
return ecommerceOrderNumber;
}

@JsonProperty("EcommerceOrderNumber")
public void setEcommerceOrderNumber(String ecommerceOrderNumber) {
this.ecommerceOrderNumber = ecommerceOrderNumber;
}

@JsonProperty("ServiceLevelofEcommerceOrder")
public String getServiceLevelofEcommerceOrder() {
return serviceLevelofEcommerceOrder;
}

@JsonProperty("ServiceLevelofEcommerceOrder")
public void setServiceLevelofEcommerceOrder(String serviceLevelofEcommerceOrder) {
this.serviceLevelofEcommerceOrder = serviceLevelofEcommerceOrder;
}

@JsonProperty("TrackingId")
public String getTrackingId() {
return trackingId;
}

@JsonProperty("TrackingId")
public void setTrackingId(String trackingId) {
this.trackingId = trackingId;
}

@JsonProperty("EcommerceOrderStatus")
public String getEcommerceOrderStatus() {
return ecommerceOrderStatus;
}

@JsonProperty("EcommerceOrderStatus")
public void setEcommerceOrderStatus(String ecommerceOrderStatus) {
this.ecommerceOrderStatus = ecommerceOrderStatus;
}

@JsonProperty("OrderTotalAmount")
public String getOrderTotalAmount() {
return orderTotalAmount;
}

@JsonProperty("OrderTotalAmount")
public void setOrderTotalAmount(String orderTotalAmount) {
this.orderTotalAmount = orderTotalAmount;
}

@JsonProperty("SourceProductList")
public String getSourceProductList() {
return sourceProductList;
}

@JsonProperty("SourceProductList")
public void setSourceProductList(String sourceProductList) {
this.sourceProductList = sourceProductList;
}

@JsonProperty("SourceUrl")
public String getSourceUrl() {
return sourceUrl;
}

@JsonProperty("SourceUrl")
public void setSourceUrl(String sourceUrl) {
this.sourceUrl = sourceUrl;
}

@JsonProperty("ShippingDate")
public String getShippingDate() {
return shippingDate;
}

@JsonProperty("ShippingDate")
public void setShippingDate(String shippingDate) {
this.shippingDate = shippingDate;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("ecommerceOrderNumber", ecommerceOrderNumber).append("serviceLevelofEcommerceOrder", serviceLevelofEcommerceOrder).append("TrackingId", trackingId).append("ecommerceOrderStatus", ecommerceOrderStatus).append("orderTotalAmount", orderTotalAmount).append("sourceProductList", sourceProductList).append("sourceUrl", sourceUrl).append("shippingDate", shippingDate).toString();
}

}