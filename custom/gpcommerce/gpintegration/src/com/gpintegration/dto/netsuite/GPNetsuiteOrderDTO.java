/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.netsuite;

import java.io.Serializable;
import java.util.List;

/**
 * @author spandiyan
 *
 */
public class GPNetsuiteOrderDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String hybrisOrderId;
	
	private String hybrisConsignmentId;
	
	private String customerId;
	
	private Double taxAmount;
	
	private String tranDate;
	
	private String headerPromoCode;
	
	private String headerCampaignCode;
	
	private String headerCouponCode;
	
	private String shipMethod;
	
	private Double shippingCost;
	
	private List<GPNetsuiteOrderItemDTO> items;

	public String getHybrisOrderId() {
		return hybrisOrderId;
	}

	public void setHybrisOrderId(String hybrisOrderId) {
		this.hybrisOrderId = hybrisOrderId;
	}

	public String getHybrisConsignmentId() {
		return hybrisConsignmentId;
	}

	public void setHybrisConsignmentId(String hybrisConsignmentId) {
		this.hybrisConsignmentId = hybrisConsignmentId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getHeaderPromoCode() {
		return headerPromoCode;
	}

	public void setHeaderPromoCode(String headerPromoCode) {
		this.headerPromoCode = headerPromoCode;
	}

	public String getHeaderCampaignCode() {
		return headerCampaignCode;
	}

	public void setHeaderCampaignCode(String headerCampaignCode) {
		this.headerCampaignCode = headerCampaignCode;
	}

	public String getHeaderCouponCode() {
		return headerCouponCode;
	}

	public void setHeaderCouponCode(String headerCouponCode) {
		this.headerCouponCode = headerCouponCode;
	}

	public String getShipMethod() {
		return shipMethod;
	}

	public void setShipMethod(String shipMethod) {
		this.shipMethod = shipMethod;
	}

	public Double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}

	public List<GPNetsuiteOrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<GPNetsuiteOrderItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "GPNetsuiteOrderDTO [hybrisOrderId=" + hybrisOrderId + ", hybrisConsignmentId=" + hybrisConsignmentId
				+ ", customerId=" + customerId + ", taxAmount=" + taxAmount + ", tranDate=" + tranDate
				+ ", headerPromoCode=" + headerPromoCode + ", headerCampaignCode=" + headerCampaignCode
				+ ", headerCouponCode=" + headerCouponCode + ", shipMethod=" + shipMethod + ", shippingCost="
				+ shippingCost + ", items=" + items + "]";
	}
}