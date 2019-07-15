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
        "id",
        "transactionLineId",
        "transactionId",
        "addressId",
        "country",
        "region",
        "stateFIPS",
        "exemptAmount",
        "exemptReasonId",
        "inState",
        "jurisCode",
        "jurisName",
        "jurisdictionId",
        "signatureCode",
        "stateAssignedNo",
        "jurisType",
        "jurisdictionType",
        "nonTaxableAmount",
        "nonTaxableRuleId",
        "nonTaxableType",
        "rate",
        "rateRuleId",
        "rateSourceId",
        "serCode",
        "sourcing",
        "tax",
        "taxableAmount",
        "taxType",
        "taxSubTypeId",
        "taxTypeGroupId",
        "taxName",
        "taxAuthorityTypeId",
        "taxRegionId",
        "taxCalculated",
        "taxOverride",
        "rateType",
        "rateTypeCode",
        "taxableUnits",
        "nonTaxableUnits",
        "exemptUnits",
        "unitOfBasis",
        "isNonPassThru"
})
public class AvalaraResponseDetailDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("transactionLineId")
    private Long transactionLineId;
    @JsonProperty("transactionId")
    private Long transactionId;
    @JsonProperty("addressId")
    private Long addressId;
    @JsonProperty("country")
    private String country;
    @JsonProperty("region")
    private String region;
    @JsonProperty("stateFIPS")
    private String stateFIPS;
    @JsonProperty("exemptAmount")
    private Long exemptAmount;
    @JsonProperty("exemptReasonId")
    private Long exemptReasonId;
    @JsonProperty("inState")
    private Boolean inState;
    @JsonProperty("jurisCode")
    private String jurisCode;
    @JsonProperty("jurisName")
    private String jurisName;
    @JsonProperty("jurisdictionId")
    private Long jurisdictionId;
    @JsonProperty("signatureCode")
    private String signatureCode;
    @JsonProperty("stateAssignedNo")
    private String stateAssignedNo;
    @JsonProperty("jurisType")
    private String jurisType;
    @JsonProperty("jurisdictionType")
    private String jurisdictionType;
    @JsonProperty("nonTaxableAmount")
    private Long nonTaxableAmount;
    @JsonProperty("nonTaxableRuleId")
    private Long nonTaxableRuleId;
    @JsonProperty("nonTaxableType")
    private String nonTaxableType;
    @JsonProperty("rate")
    private Double rate;
    @JsonProperty("rateRuleId")
    private Long rateRuleId;
    @JsonProperty("rateSourceId")
    private Long rateSourceId;
    @JsonProperty("serCode")
    private String serCode;
    @JsonProperty("sourcing")
    private String sourcing;
    @JsonProperty("tax")
    private Long tax;
    @JsonProperty("taxableAmount")
    private Long taxableAmount;
    @JsonProperty("taxType")
    private String taxType;
    @JsonProperty("taxSubTypeId")
    private String taxSubTypeId;
    @JsonProperty("taxTypeGroupId")
    private String taxTypeGroupId;
    @JsonProperty("taxName")
    private String taxName;
    @JsonProperty("taxAuthorityTypeId")
    private Integer taxAuthorityTypeId;
    @JsonProperty("taxRegionId")
    private Long taxRegionId;
    @JsonProperty("taxCalculated")
    private Long taxCalculated;
    @JsonProperty("taxOverride")
    private Long taxOverride;
    @JsonProperty("rateType")
    private String rateType;
    @JsonProperty("rateTypeCode")
    private String rateTypeCode;
    @JsonProperty("taxableUnits")
    private Long taxableUnits;
    @JsonProperty("nonTaxableUnits")
    private Long nonTaxableUnits;
    @JsonProperty("exemptUnits")
    private Long exemptUnits;
    @JsonProperty("unitOfBasis")
    private String unitOfBasis;
    @JsonProperty("isNonPassThru")
    private Boolean isNonPassThru;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("transactionLineId")
    public Long getTransactionLineId() {
        return transactionLineId;
    }

    @JsonProperty("transactionLineId")
    public void setTransactionLineId(Long transactionLineId) {
        this.transactionLineId = transactionLineId;
    }

    @JsonProperty("transactionId")
    public Long getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transactionId")
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("addressId")
    public Long getAddressId() {
        return addressId;
    }

    @JsonProperty("addressId")
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

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

    @JsonProperty("stateFIPS")
    public String getStateFIPS() {
        return stateFIPS;
    }

    @JsonProperty("stateFIPS")
    public void setStateFIPS(String stateFIPS) {
        this.stateFIPS = stateFIPS;
    }

    @JsonProperty("exemptAmount")
    public Long getExemptAmount() {
        return exemptAmount;
    }

    @JsonProperty("exemptAmount")
    public void setExemptAmount(Long exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    @JsonProperty("exemptReasonId")
    public Long getExemptReasonId() {
        return exemptReasonId;
    }

    @JsonProperty("exemptReasonId")
    public void setExemptReasonId(Long exemptReasonId) {
        this.exemptReasonId = exemptReasonId;
    }

    @JsonProperty("inState")
    public Boolean getInState() {
        return inState;
    }

    @JsonProperty("inState")
    public void setInState(Boolean inState) {
        this.inState = inState;
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

    @JsonProperty("jurisdictionId")
    public Long getJurisdictionId() {
        return jurisdictionId;
    }

    @JsonProperty("jurisdictionId")
    public void setJurisdictionId(Long jurisdictionId) {
        this.jurisdictionId = jurisdictionId;
    }

    @JsonProperty("signatureCode")
    public String getSignatureCode() {
        return signatureCode;
    }

    @JsonProperty("signatureCode")
    public void setSignatureCode(String signatureCode) {
        this.signatureCode = signatureCode;
    }

    @JsonProperty("stateAssignedNo")
    public String getStateAssignedNo() {
        return stateAssignedNo;
    }

    @JsonProperty("stateAssignedNo")
    public void setStateAssignedNo(String stateAssignedNo) {
        this.stateAssignedNo = stateAssignedNo;
    }

    @JsonProperty("jurisType")
    public String getJurisType() {
        return jurisType;
    }

    @JsonProperty("jurisType")
    public void setJurisType(String jurisType) {
        this.jurisType = jurisType;
    }

    @JsonProperty("jurisdictionType")
    public String getJurisdictionType() {
        return jurisdictionType;
    }

    @JsonProperty("jurisdictionType")
    public void setJurisdictionType(String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
    }

    @JsonProperty("nonTaxableAmount")
    public Long getNonTaxableAmount() {
        return nonTaxableAmount;
    }

    @JsonProperty("nonTaxableAmount")
    public void setNonTaxableAmount(Long nonTaxableAmount) {
        this.nonTaxableAmount = nonTaxableAmount;
    }

    @JsonProperty("nonTaxableRuleId")
    public Long getNonTaxableRuleId() {
        return nonTaxableRuleId;
    }

    @JsonProperty("nonTaxableRuleId")
    public void setNonTaxableRuleId(Long nonTaxableRuleId) {
        this.nonTaxableRuleId = nonTaxableRuleId;
    }

    @JsonProperty("nonTaxableType")
    public String getNonTaxableType() {
        return nonTaxableType;
    }

    @JsonProperty("nonTaxableType")
    public void setNonTaxableType(String nonTaxableType) {
        this.nonTaxableType = nonTaxableType;
    }

    @JsonProperty("rate")
    public Double getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(Double rate) {
        this.rate = rate;
    }

    @JsonProperty("rateRuleId")
    public Long getRateRuleId() {
        return rateRuleId;
    }

    @JsonProperty("rateRuleId")
    public void setRateRuleId(Long rateRuleId) {
        this.rateRuleId = rateRuleId;
    }

    @JsonProperty("rateSourceId")
    public Long getRateSourceId() {
        return rateSourceId;
    }

    @JsonProperty("rateSourceId")
    public void setRateSourceId(Long rateSourceId) {
        this.rateSourceId = rateSourceId;
    }

    @JsonProperty("serCode")
    public String getSerCode() {
        return serCode;
    }

    @JsonProperty("serCode")
    public void setSerCode(String serCode) {
        this.serCode = serCode;
    }

    @JsonProperty("sourcing")
    public String getSourcing() {
        return sourcing;
    }

    @JsonProperty("sourcing")
    public void setSourcing(String sourcing) {
        this.sourcing = sourcing;
    }

    @JsonProperty("tax")
    public Long getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(Long tax) {
        this.tax = tax;
    }

    @JsonProperty("taxableAmount")
    public Long getTaxableAmount() {
        return taxableAmount;
    }

    @JsonProperty("taxableAmount")
    public void setTaxableAmount(Long taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    @JsonProperty("taxType")
    public String getTaxType() {
        return taxType;
    }

    @JsonProperty("taxType")
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    @JsonProperty("taxSubTypeId")
    public String getTaxSubTypeId() {
        return taxSubTypeId;
    }

    @JsonProperty("taxSubTypeId")
    public void setTaxSubTypeId(String taxSubTypeId) {
        this.taxSubTypeId = taxSubTypeId;
    }

    @JsonProperty("taxTypeGroupId")
    public String getTaxTypeGroupId() {
        return taxTypeGroupId;
    }

    @JsonProperty("taxTypeGroupId")
    public void setTaxTypeGroupId(String taxTypeGroupId) {
        this.taxTypeGroupId = taxTypeGroupId;
    }

    @JsonProperty("taxName")
    public String getTaxName() {
        return taxName;
    }

    @JsonProperty("taxName")
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @JsonProperty("taxAuthorityTypeId")
    public Integer getTaxAuthorityTypeId() {
        return taxAuthorityTypeId;
    }

    @JsonProperty("taxAuthorityTypeId")
    public void setTaxAuthorityTypeId(Integer taxAuthorityTypeId) {
        this.taxAuthorityTypeId = taxAuthorityTypeId;
    }

    @JsonProperty("taxRegionId")
    public Long getTaxRegionId() {
        return taxRegionId;
    }

    @JsonProperty("taxRegionId")
    public void setTaxRegionId(Long taxRegionId) {
        this.taxRegionId = taxRegionId;
    }

    @JsonProperty("taxCalculated")
    public Long getTaxCalculated() {
        return taxCalculated;
    }

    @JsonProperty("taxCalculated")
    public void setTaxCalculated(Long taxCalculated) {
        this.taxCalculated = taxCalculated;
    }

    @JsonProperty("taxOverride")
    public Long getTaxOverride() {
        return taxOverride;
    }

    @JsonProperty("taxOverride")
    public void setTaxOverride(Long taxOverride) {
        this.taxOverride = taxOverride;
    }

    @JsonProperty("rateType")
    public String getRateType() {
        return rateType;
    }

    @JsonProperty("rateType")
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    @JsonProperty("rateTypeCode")
    public String getRateTypeCode() {
        return rateTypeCode;
    }

    @JsonProperty("rateTypeCode")
    public void setRateTypeCode(String rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }

    @JsonProperty("taxableUnits")
    public Long getTaxableUnits() {
        return taxableUnits;
    }

    @JsonProperty("taxableUnits")
    public void setTaxableUnits(Long taxableUnits) {
        this.taxableUnits = taxableUnits;
    }

    @JsonProperty("nonTaxableUnits")
    public Long getNonTaxableUnits() {
        return nonTaxableUnits;
    }

    @JsonProperty("nonTaxableUnits")
    public void setNonTaxableUnits(Long nonTaxableUnits) {
        this.nonTaxableUnits = nonTaxableUnits;
    }

    @JsonProperty("exemptUnits")
    public Long getExemptUnits() {
        return exemptUnits;
    }

    @JsonProperty("exemptUnits")
    public void setExemptUnits(Long exemptUnits) {
        this.exemptUnits = exemptUnits;
    }

    @JsonProperty("unitOfBasis")
    public String getUnitOfBasis() {
        return unitOfBasis;
    }

    @JsonProperty("unitOfBasis")
    public void setUnitOfBasis(String unitOfBasis) {
        this.unitOfBasis = unitOfBasis;
    }

    @JsonProperty("isNonPassThru")
    public Boolean getIsNonPassThru() {
        return isNonPassThru;
    }

    @JsonProperty("isNonPassThru")
    public void setIsNonPassThru(Boolean isNonPassThru) {
        this.isNonPassThru = isNonPassThru;
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
