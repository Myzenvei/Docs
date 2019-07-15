/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.calculation;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "country",
        "region",
        "jurisType",
        "jurisCode",
        "jurisName",
        "taxAuthorityType",
        "stateAssignedNo",
        "taxType",
        "taxName",
        "rateType",
        "taxable",
        "rate",
        "tax",
        "taxCalculated",
        "nonTaxable",
        "exemption"
})
public class AvalaraResponseSummaryDTO {
    @JsonProperty("country")
    private String country;
    @JsonProperty("region")
    private String region;
    @JsonProperty("jurisType")
    private String jurisType;
    @JsonProperty("jurisCode")
    private String jurisCode;
    @JsonProperty("jurisName")
    private String jurisName;
    @JsonProperty("taxAuthorityType")
    private Long taxAuthorityType;
    @JsonProperty("stateAssignedNo")
    private String stateAssignedNo;
    @JsonProperty("taxType")
    private String taxType;
    @JsonProperty("taxName")
    private String taxName;
    @JsonProperty("rateType")
    private String rateType;
    @JsonProperty("taxable")
    private Long taxable;
    @JsonProperty("rate")
    private Double rate;
    @JsonProperty("tax")
    private Long tax;
    @JsonProperty("taxCalculated")
    private Long taxCalculated;
    @JsonProperty("nonTaxable")
    private Long nonTaxable;
    @JsonProperty("exemption")
    private Long exemption;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("jurisType")
    public String getJurisType() {
        return jurisType;
    }

    @JsonProperty("jurisType")
    public void setJurisType(String jurisType) {
        this.jurisType = jurisType;
    }

    @JsonProperty("jurisCode")
    public String getJurisCode() {
        return jurisCode;
    }

    @JsonProperty("jurisCode")
    public void setJurisCode(String jurisCode) {
        this.jurisCode = jurisCode;
    }

    @JsonProperty("jurisName")
    public String getJurisName() {
        return jurisName;
    }

    @JsonProperty("jurisName")
    public void setJurisName(String jurisName) {
        this.jurisName = jurisName;
    }

    @JsonProperty("taxAuthorityType")
    public Long getTaxAuthorityType() {
        return taxAuthorityType;
    }

    @JsonProperty("taxAuthorityType")
    public void setTaxAuthorityType(Long taxAuthorityType) {
        this.taxAuthorityType = taxAuthorityType;
    }

    @JsonProperty("stateAssignedNo")
    public String getStateAssignedNo() {
        return stateAssignedNo;
    }

    @JsonProperty("stateAssignedNo")
    public void setStateAssignedNo(String stateAssignedNo) {
        this.stateAssignedNo = stateAssignedNo;
    }

    @JsonProperty("taxType")
    public String getTaxType() {
        return taxType;
    }

    @JsonProperty("taxType")
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    @JsonProperty("taxName")
    public String getTaxName() {
        return taxName;
    }

    @JsonProperty("taxName")
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @JsonProperty("rateType")
    public String getRateType() {
        return rateType;
    }

    @JsonProperty("rateType")
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    @JsonProperty("taxable")
    public Long getTaxable() {
        return taxable;
    }

    @JsonProperty("taxable")
    public void setTaxable(Long taxable) {
        this.taxable = taxable;
    }

    @JsonProperty("rate")
    public Double getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(Double rate) {
        this.rate = rate;
    }

    @JsonProperty("tax")
    public Long getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(Long tax) {
        this.tax = tax;
    }

    @JsonProperty("taxCalculated")
    public Long getTaxCalculated() {
        return taxCalculated;
    }

    @JsonProperty("taxCalculated")
    public void setTaxCalculated(Long taxCalculated) {
        this.taxCalculated = taxCalculated;
    }

    @JsonProperty("nonTaxable")
    public Long getNonTaxable() {
        return nonTaxable;
    }

    @JsonProperty("nonTaxable")
    public void setNonTaxable(Long nonTaxable) {
        this.nonTaxable = nonTaxable;
    }

    @JsonProperty("exemption")
    public Long getExemption() {
        return exemption;
    }

    @JsonProperty("exemption")
    public void setExemption(Long exemption) {
        this.exemption = exemption;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
