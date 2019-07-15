/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.dto.onesource;

import java.util.List;

import de.hybris.platform.util.TaxValue;

public class GPOneSourceSplitEntryTax {
	
	private List<TaxValue> splitEntryTaxValue;
	private Double splitEntryTotalTax;
	private Double splitEntryShipmentTax;
	private Double splitEntryMerchantTax;

	public Double getSplitEntryMerchantTax() {
		return splitEntryMerchantTax;
	}

	public void setSplitEntryMerchantTax(Double splitEntryMerchantTax) {
		this.splitEntryMerchantTax = splitEntryMerchantTax;
	}

	public List<TaxValue> getSplitEntryTaxValue() {
		return splitEntryTaxValue;
	}
	public void setSplitEntryTaxValue(List<TaxValue> splitEntryTaxValue) {
		this.splitEntryTaxValue = splitEntryTaxValue;
	}
	public Double getSplitEntryTotalTax() {
		return splitEntryTotalTax;
	}
	public void setSplitEntryTotalTax(Double splitEntryTotalTax) {
		this.splitEntryTotalTax = splitEntryTotalTax;
	}
	public Double getSplitEntryShipmentTax() {
		return splitEntryShipmentTax;
	}
	public void setSplitEntryShipmentTax(Double splitEntryShipmentTax) {
		this.splitEntryShipmentTax = splitEntryShipmentTax;
	}
	@Override
	public String toString() {
		return "GPOneSourceSplitEntryTax [splitEntryTaxValue=" + splitEntryTaxValue + ", splitEntryTotalTax="
				+ splitEntryTotalTax + ", splitEntryShipmentTax=" + splitEntryShipmentTax + "]";
	}
}