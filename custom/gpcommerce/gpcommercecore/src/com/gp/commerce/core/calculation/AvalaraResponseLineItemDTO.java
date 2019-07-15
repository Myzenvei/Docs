/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.core.calculation;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "transactionId",
        "lineNumber",
        "boundaryOverrideId",
        "customerUsageType",
        "entityUseCode",
        "description",
        "destinationAddressId",
        "originAddressId",
        "discountAmount",
        "exemptAmount",
        "exemptCertId",
        "exemptNo",
        "isItemTaxable",
        "isSSTP",
        "itemCode",
        "lineAmount",
        "quantity",
        "ref1",
        "ref2",
        "reportingDate",
        "revAccount",
        "sourcing",
        "tax",
        "taxableAmount",
        "taxCalculated",
        "taxCode",
        "taxCodeId",
        "taxDate",
        "taxEngine",
        "taxOverrideType",
        "businessIdentificationNo",
        "taxOverrideAmount",
        "taxOverrideReason",
        "taxIncluded",
        "details",
        "nonPassthroughDetails",
        "lineLocationTypes",
        "parameters",
        "hsCode",
        "vatCode",
        "vatNumberTypeId"
})
public class AvalaraResponseLineItemDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("transactionId")
    private Long transactionId;
    @JsonProperty("lineNumber")
    private String lineNumber;
    @JsonProperty("boundaryOverrideId")
    private Long boundaryOverrideId;
    @JsonProperty("customerUsageType")
    private String customerUsageType;
    @JsonProperty("entityUseCode")
    private String entityUseCode;
    @JsonProperty("description")
    private String description;
    @JsonProperty("destinationAddressId")
    private Long destinationAddressId;
    @JsonProperty("originAddressId")
    private Long originAddressId;
    @JsonProperty("discountAmount")
    private Long discountAmount;
    @JsonProperty("exemptAmount")
    private Long exemptAmount;
    @JsonProperty("exemptCertId")
    private Long exemptCertId;
    @JsonProperty("exemptNo")
    private String exemptNo;
    @JsonProperty("isItemTaxable")
    private Boolean isItemTaxable;
    @JsonProperty("isSSTP")
    private Boolean isSSTP;
    @JsonProperty("itemCode")
    private String itemCode;
    @JsonProperty("lineAmount")
    private Long lineAmount;
    @JsonProperty("quantity")
    private Long quantity;
    @JsonProperty("ref1")
    private String ref1;
    @JsonProperty("ref2")
    private String ref2;
    @JsonProperty("reportingDate")
    private String reportingDate;
    @JsonProperty("revAccount")
    private String revAccount;
    @JsonProperty("sourcing")
    private String sourcing;
    @JsonProperty("tax")
    private Double tax;
    @JsonProperty("taxableAmount")
    private Long taxableAmount;
    @JsonProperty("taxCalculated")
    private Double taxCalculated;
    @JsonProperty("taxCode")
    private String taxCode;
    @JsonProperty("taxCodeId")
    private Long taxCodeId;
    @JsonProperty("taxDate")
    private String taxDate;
    @JsonProperty("taxEngine")
    private String taxEngine;
    @JsonProperty("taxOverrideType")
    private String taxOverrideType;
    @JsonProperty("businessIdentificationNo")
    private String businessIdentificationNo;
    @JsonProperty("taxOverrideAmount")
    private Long taxOverrideAmount;
    @JsonProperty("taxOverrideReason")
    private String taxOverrideReason;
    @JsonProperty("taxIncluded")
    private Boolean taxIncluded;
    @JsonProperty("details")
    private List<AvalaraResponseDetailDTO> details = null;
    @JsonProperty("nonPassthroughDetails")
    private List<Object> nonPassthroughDetails = null;
    @JsonProperty("lineLocationTypes")
    private List<AvalaraResponseLineLocationTypeDTO> lineLocationTypes = null;
    @JsonProperty("hsCode")
    private String hsCode;
    @JsonProperty("vatCode")
    private String vatCode;
    @JsonProperty("vatNumberTypeId")
    private Long vatNumberTypeId;
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

    @JsonProperty("transactionId")
    public Long getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transactionId")
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("lineNumber")
    public String getLineNumber() {
        return lineNumber;
    }

    @JsonProperty("lineNumber")
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    @JsonProperty("boundaryOverrideId")
    public Long getBoundaryOverrideId() {
        return boundaryOverrideId;
    }

    @JsonProperty("boundaryOverrideId")
    public void setBoundaryOverrideId(Long boundaryOverrideId) {
        this.boundaryOverrideId = boundaryOverrideId;
    }

    @JsonProperty("customerUsageType")
    public String getCustomerUsageType() {
        return customerUsageType;
    }

    @JsonProperty("customerUsageType")
    public void setCustomerUsageType(String customerUsageType) {
        this.customerUsageType = customerUsageType;
    }

    @JsonProperty("entityUseCode")
    public String getEntityUseCode() {
        return entityUseCode;
    }

    @JsonProperty("entityUseCode")
    public void setEntityUseCode(String entityUseCode) {
        this.entityUseCode = entityUseCode;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("destinationAddressId")
    public Long getDestinationAddressId() {
        return destinationAddressId;
    }

    @JsonProperty("destinationAddressId")
    public void setDestinationAddressId(Long destinationAddressId) {
        this.destinationAddressId = destinationAddressId;
    }

    @JsonProperty("originAddressId")
    public Long getOriginAddressId() {
        return originAddressId;
    }

    @JsonProperty("originAddressId")
    public void setOriginAddressId(Long originAddressId) {
        this.originAddressId = originAddressId;
    }

    @JsonProperty("discountAmount")
    public Long getDiscountAmount() {
        return discountAmount;
    }

    @JsonProperty("discountAmount")
    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    @JsonProperty("exemptAmount")
    public Long getExemptAmount() {
        return exemptAmount;
    }

    @JsonProperty("exemptAmount")
    public void setExemptAmount(Long exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    @JsonProperty("exemptCertId")
    public Long getExemptCertId() {
        return exemptCertId;
    }

    @JsonProperty("exemptCertId")
    public void setExemptCertId(Long exemptCertId) {
        this.exemptCertId = exemptCertId;
    }

    @JsonProperty("exemptNo")
    public String getExemptNo() {
        return exemptNo;
    }

    @JsonProperty("exemptNo")
    public void setExemptNo(String exemptNo) {
        this.exemptNo = exemptNo;
    }

    @JsonProperty("isItemTaxable")
    public Boolean getIsItemTaxable() {
        return isItemTaxable;
    }

    @JsonProperty("isItemTaxable")
    public void setIsItemTaxable(Boolean isItemTaxable) {
        this.isItemTaxable = isItemTaxable;
    }

    @JsonProperty("isSSTP")
    public Boolean getIsSSTP() {
        return isSSTP;
    }

    @JsonProperty("isSSTP")
    public void setIsSSTP(Boolean isSSTP) {
        this.isSSTP = isSSTP;
    }

    @JsonProperty("itemCode")
    public String getItemCode() {
        return itemCode;
    }

    @JsonProperty("itemCode")
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @JsonProperty("lineAmount")
    public Long getLineAmount() {
        return lineAmount;
    }

    @JsonProperty("lineAmount")
    public void setLineAmount(Long lineAmount) {
        this.lineAmount = lineAmount;
    }

    @JsonProperty("quantity")
    public Long getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("ref1")
    public String getRef1() {
        return ref1;
    }

    @JsonProperty("ref1")
    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    @JsonProperty("ref2")
    public String getRef2() {
        return ref2;
    }

    @JsonProperty("ref2")
    public void setRef2(String ref2) {
        this.ref2 = ref2;
    }

    @JsonProperty("reportingDate")
    public String getReportingDate() {
        return reportingDate;
    }

    @JsonProperty("reportingDate")
    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    @JsonProperty("revAccount")
    public String getRevAccount() {
        return revAccount;
    }

    @JsonProperty("revAccount")
    public void setRevAccount(String revAccount) {
        this.revAccount = revAccount;
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
    public Double getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(Double tax) {
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

    @JsonProperty("taxCalculated")
    public Double getTaxCalculated() {
        return taxCalculated;
    }

    @JsonProperty("taxCalculated")
    public void setTaxCalculated(Double taxCalculated) {
        this.taxCalculated = taxCalculated;
    }

    @JsonProperty("taxCode")
    public String getTaxCode() {
        return taxCode;
    }

    @JsonProperty("taxCode")
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @JsonProperty("taxCodeId")
    public Long getTaxCodeId() {
        return taxCodeId;
    }

    @JsonProperty("taxCodeId")
    public void setTaxCodeId(Long taxCodeId) {
        this.taxCodeId = taxCodeId;
    }

    @JsonProperty("taxDate")
    public String getTaxDate() {
        return taxDate;
    }

    @JsonProperty("taxDate")
    public void setTaxDate(String taxDate) {
        this.taxDate = taxDate;
    }

    @JsonProperty("taxEngine")
    public String getTaxEngine() {
        return taxEngine;
    }

    @JsonProperty("taxEngine")
    public void setTaxEngine(String taxEngine) {
        this.taxEngine = taxEngine;
    }

    @JsonProperty("taxOverrideType")
    public String getTaxOverrideType() {
        return taxOverrideType;
    }

    @JsonProperty("taxOverrideType")
    public void setTaxOverrideType(String taxOverrideType) {
        this.taxOverrideType = taxOverrideType;
    }

    @JsonProperty("businessIdentificationNo")
    public String getBusinessIdentificationNo() {
        return businessIdentificationNo;
    }

    @JsonProperty("businessIdentificationNo")
    public void setBusinessIdentificationNo(String businessIdentificationNo) {
        this.businessIdentificationNo = businessIdentificationNo;
    }

    @JsonProperty("taxOverrideAmount")
    public Long getTaxOverrideAmount() {
        return taxOverrideAmount;
    }

    @JsonProperty("taxOverrideAmount")
    public void setTaxOverrideAmount(Long taxOverrideAmount) {
        this.taxOverrideAmount = taxOverrideAmount;
    }

    @JsonProperty("taxOverrideReason")
    public String getTaxOverrideReason() {
        return taxOverrideReason;
    }

    @JsonProperty("taxOverrideReason")
    public void setTaxOverrideReason(String taxOverrideReason) {
        this.taxOverrideReason = taxOverrideReason;
    }

    @JsonProperty("taxIncluded")
    public Boolean getTaxIncluded() {
        return taxIncluded;
    }

    @JsonProperty("taxIncluded")
    public void setTaxIncluded(Boolean taxIncluded) {
        this.taxIncluded = taxIncluded;
    }

    @JsonProperty("details")
    public List<AvalaraResponseDetailDTO> getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(List<AvalaraResponseDetailDTO> details) {
        this.details = details;
    }

    @JsonProperty("nonPassthroughDetails")
    public List<Object> getNonPassthroughDetails() {
        return nonPassthroughDetails;
    }

    @JsonProperty("nonPassthroughDetails")
    public void setNonPassthroughDetails(List<Object> nonPassthroughDetails) {
        this.nonPassthroughDetails = nonPassthroughDetails;
    }

    @JsonProperty("lineLocationTypes")
    public List<AvalaraResponseLineLocationTypeDTO> getLineLocationTypes() {
        return lineLocationTypes;
    }

    @JsonProperty("lineLocationTypes")
    public void setLineLocationTypes(List<AvalaraResponseLineLocationTypeDTO> lineLocationTypes) {
        this.lineLocationTypes = lineLocationTypes;
    }

    @JsonProperty("hsCode")
    public String getHsCode() {
        return hsCode;
    }

    @JsonProperty("hsCode")
    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    @JsonProperty("vatCode")
    public String getVatCode() {
        return vatCode;
    }

    @JsonProperty("vatCode")
    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    @JsonProperty("vatNumberTypeId")
    public Long getVatNumberTypeId() {
        return vatNumberTypeId;
    }

    @JsonProperty("vatNumberTypeId")
    public void setVatNumberTypeId(Long vatNumberTypeId) {
        this.vatNumberTypeId = vatNumberTypeId;
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
