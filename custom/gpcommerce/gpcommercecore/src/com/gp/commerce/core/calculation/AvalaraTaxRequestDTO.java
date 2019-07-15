/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.calculation;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class AvalaraTaxRequestDTO {
    @JsonProperty("lines")
    private List<AvalaraLineItemDTO> lines;

	@JsonProperty("type")
    private String type;

    @JsonProperty("companyCode")
    private String companyCode;

    @JsonProperty("date")
    private String date;

    @JsonProperty("customerCode")
    private String customerCode;

    @JsonProperty("purchaseOrderNo")
    private String purchaseOrderNo;

    @JsonProperty("commit")
    private boolean commit;
    
    @JsonProperty("addresses")
    private AvalaraRequestAddressesDTO addresses;

    @JsonProperty("taxOverride")
    private TaxOverrideDTO taxOverride;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("code")
    private String code;

    public List<AvalaraLineItemDTO> getLines() {
		return lines;
	}

	public void setLines(List<AvalaraLineItemDTO> lines) {
		this.lines = lines;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public boolean isCommit() {
        return commit;
    }

    public void setCommit(boolean commit) {
        this.commit = commit;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}