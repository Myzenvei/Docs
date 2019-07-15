/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.calculation;

import org.codehaus.jackson.annotate.JsonProperty;

public class AvalaraLineItemDTO {
    @JsonProperty("number")
    private String number;

    @JsonProperty("quantity")
    private Long quantity;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("addresses")
    private AvalaraRequestAddressesDTO addresses;

    @JsonProperty("taxOverride")
    private TaxOverrideDTO taxOverride;

    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("itemCode")
    private String itemCode;

    @JsonProperty("description")
    private String description;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AvalaraRequestAddressesDTO getAddresses() {
        return addresses;
    }

    public void setAddresses(AvalaraRequestAddressesDTO addresses) {
        this.addresses = addresses;
    }

    public TaxOverrideDTO getTaxOverride() {
        return taxOverride;
    }

    public void setTaxOverride(TaxOverrideDTO taxOverride) {
        this.taxOverride = taxOverride;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
