/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.netsuite;

import java.io.Serializable;
/**
 * @author spandiyan
 *
 */
public class GPNetsuiteOrderItemDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String sku;
	
	private Integer quantity;
	
	private Double rate;
	
	private Double amount;
	
	private String itemPromoCode;
	
	private String itemCampaignCode;
	
	private String itemCouponCode;
	
	private String consignmentEntryNumber;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getItemPromoCode() {
		return itemPromoCode;
	}

	public void setItemPromoCode(String itemPromoCode) {
		this.itemPromoCode = itemPromoCode;
	}

	public String getItemCampaignCode() {
		return itemCampaignCode;
	}

	public void setItemCampaignCode(String itemCampaignCode) {
		this.itemCampaignCode = itemCampaignCode;
	}

	public String getItemCouponCode() {
		return itemCouponCode;
	}

	public void setItemCouponCode(String itemCouponCode) {
		this.itemCouponCode = itemCouponCode;
	}
	

	/**
	 * @return the consignmentEntryNumber
	 */
	public String getConsignmentEntryNumber() {
		return consignmentEntryNumber;
	}

	/**
	 * @param consignmentEntryNumber the consignmentEntryNumber to set
	 */
	public void setConsignmentEntryNumber(String consignmentEntryNumber) {
		this.consignmentEntryNumber = consignmentEntryNumber;
	}

	@Override
	public String toString() {
		return "GPNetsuiteOrderItemDTO [sku=" + sku + "consignmentEntryNumber= "+consignmentEntryNumber + ", quantity=" + quantity + ", rate=" + rate + ", amount=" + amount
				+ ", itemPromoCode=" + itemPromoCode + ", itemCampaignCode=" + itemCampaignCode + ", itemCouponCode="
				+ itemCouponCode + "]";
	}

}