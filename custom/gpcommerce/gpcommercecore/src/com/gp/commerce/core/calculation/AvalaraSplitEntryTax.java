/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.calculation;

import de.hybris.platform.util.TaxValue;

import java.util.List;

public class AvalaraSplitEntryTax {
    private Double splitEntryTotalTax;
    private List<TaxValue> splitEntryTaxValue;
    private Double splitEntryShipmentTax;
    private Double splitEntryMerchantTax;


    public Double getSplitEntryMerchantTax() {
		return splitEntryMerchantTax;
	}

	public void setSplitEntryMerchantTax(Double splitEntryMerchantTax) {
		this.splitEntryMerchantTax = splitEntryMerchantTax;
	}

	public Double getSplitEntryShipmentTax() {
		return splitEntryShipmentTax;
	}

	public void setSplitEntryShipmentTax(Double splitEntryShipmentTax) {
		this.splitEntryShipmentTax = splitEntryShipmentTax;
	}

	public Double getSplitEntryTotalTax() {
        return splitEntryTotalTax;
    }

    public void setSplitEntryTotalTax(Double splitEntryTotalTax) {
        this.splitEntryTotalTax = splitEntryTotalTax;
    }

    public List<TaxValue> getSplitEntryTaxValue() {
        return splitEntryTaxValue;
    }

    public void setSplitEntryTaxValue(List<TaxValue> splitEntryTaxValue) {
        this.splitEntryTaxValue = splitEntryTaxValue;
    }
}
