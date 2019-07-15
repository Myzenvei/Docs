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
        "code",
        "companyId",
        "date",
        "paymentDate",
        "status",
        "type",
        "batchCode",
        "currencyCode",
        "customerUsageType",
        "entityUseCode",
        "customerVendorCode",
        "customerCode",
        "exemptNo",
        "reconciled",
        "purchaseOrderNo",
        "referenceCode",
        "salespersonCode",
        "taxOverrideType",
        "taxOverrideAmount",
        "taxOverrideReason",
        "totalAmount",
        "totalExempt",
        "totalDiscount",
        "totalTax",
        "totalTaxable",
        "totalTaxCalculated",
        "adjustmentReason",
        "adjustmentDescription",
        "locked",
        "region",
        "country",
        "version",
        "softwareVersion",
        "exchangeRateEffectiveDate",
        "exchangeRate",
        "isSellerImporterOfRecord",
        "description",
        "businessIdentificationNo",
        "modifiedDate",
        "modifiedUserId",
        "taxDate",
        "lines",
        "addresses",
        "locationTypes",
        "summary",
        "parameters"
})
public class AvalaraTaxResponseDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("companyId")
    private Long companyId;
    @JsonProperty("date")
    private String date;
    @JsonProperty("paymentDate")
    private String paymentDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private String type;
    @JsonProperty("batchCode")
    private String batchCode;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("customerUsageType")
    private String customerUsageType;
    @JsonProperty("entityUseCode")
    private String entityUseCode;
    @JsonProperty("customerVendorCode")
    private String customerVendorCode;
    @JsonProperty("customerCode")
    private String customerCode;
    @JsonProperty("exemptNo")
    private String exemptNo;
    @JsonProperty("reconciled")
    private Boolean reconciled;
    @JsonProperty("purchaseOrderNo")
    private String purchaseOrderNo;
    @JsonProperty("referenceCode")
    private String referenceCode;
    @JsonProperty("salespersonCode")
    private String salespersonCode;
    @JsonProperty("taxOverrideType")
    private String taxOverrideType;
    @JsonProperty("taxOverrideAmount")
    private Long taxOverrideAmount;
    @JsonProperty("taxOverrideReason")
    private String taxOverrideReason;
    @JsonProperty("totalAmount")
    private Long totalAmount;
    @JsonProperty("totalExempt")
    private Long totalExempt;
    @JsonProperty("totalDiscount")
    private Long totalDiscount;
    @JsonProperty("totalTax")
    private String totalTax;
    @JsonProperty("totalTaxable")
    private Long totalTaxable;
    @JsonProperty("totalTaxCalculated")
    private Double totalTaxCalculated;
    @JsonProperty("adjustmentReason")
    private String adjustmentReason;
    @JsonProperty("adjustmentDescription")
    private String adjustmentDescription;
    @JsonProperty("locked")
    private Boolean locked;
    @JsonProperty("region")
    private String region;
    @JsonProperty("country")
    private String country;
    @JsonProperty("version")
    private Long version;
    @JsonProperty("softwareVersion")
    private String softwareVersion;
    @JsonProperty("exchangeRateEffectiveDate")
    private String exchangeRateEffectiveDate;
    @JsonProperty("exchangeRate")
    private Long exchangeRate;
    @JsonProperty("isSellerImporterOfRecord")
    private Boolean isSellerImporterOfRecord;
    @JsonProperty("description")
    private String description;
    @JsonProperty("businessIdentificationNo")
    private String businessIdentificationNo;
    @JsonProperty("modifiedDate")
    private String modifiedDate;
    @JsonProperty("modifiedUserId")
    private Long modifiedUserId;
    @JsonProperty("taxDate")
    private String taxDate;
    @JsonProperty("lines")
    private List<AvalaraResponseLineItemDTO> lines = null;
    @JsonProperty("addresses")
    private List<AvalaraResponseAddressDTO> addresses = null;
    @JsonProperty("locationTypes")
    private List<Object> locationTypes = null;
    @JsonProperty("summary")
    private List<AvalaraResponseSummaryDTO> summary = null;

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

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("companyId")
    public Long getCompanyId() {
        return companyId;
    }

    @JsonProperty("companyId")
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("paymentDate")
    public String getPaymentDate() {
        return paymentDate;
    }

    @JsonProperty("paymentDate")
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("batchCode")
    public String getBatchCode() {
        return batchCode;
    }

    @JsonProperty("batchCode")
    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    @JsonProperty("customerVendorCode")
    public String getCustomerVendorCode() {
        return customerVendorCode;
    }

    @JsonProperty("customerVendorCode")
    public void setCustomerVendorCode(String customerVendorCode) {
        this.customerVendorCode = customerVendorCode;
    }

    @JsonProperty("customerCode")
    public String getCustomerCode() {
        return customerCode;
    }

    @JsonProperty("customerCode")
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @JsonProperty("exemptNo")
    public String getExemptNo() {
        return exemptNo;
    }

    @JsonProperty("exemptNo")
    public void setExemptNo(String exemptNo) {
        this.exemptNo = exemptNo;
    }

    @JsonProperty("reconciled")
    public Boolean getReconciled() {
        return reconciled;
    }

    @JsonProperty("reconciled")
    public void setReconciled(Boolean reconciled) {
        this.reconciled = reconciled;
    }

    @JsonProperty("purchaseOrderNo")
    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    @JsonProperty("purchaseOrderNo")
    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    @JsonProperty("referenceCode")
    public String getReferenceCode() {
        return referenceCode;
    }

    @JsonProperty("referenceCode")
    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    @JsonProperty("salespersonCode")
    public String getSalespersonCode() {
        return salespersonCode;
    }

    @JsonProperty("salespersonCode")
    public void setSalespersonCode(String salespersonCode) {
        this.salespersonCode = salespersonCode;
    }

    @JsonProperty("taxOverrideType")
    public String getTaxOverrideType() {
        return taxOverrideType;
    }

    @JsonProperty("taxOverrideType")
    public void setTaxOverrideType(String taxOverrideType) {
        this.taxOverrideType = taxOverrideType;
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

    @JsonProperty("totalAmount")
    public Long getTotalAmount() {
        return totalAmount;
    }

    @JsonProperty("totalAmount")
    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    @JsonProperty("totalExempt")
    public Long getTotalExempt() {
        return totalExempt;
    }

    @JsonProperty("totalExempt")
    public void setTotalExempt(Long totalExempt) {
        this.totalExempt = totalExempt;
    }

    @JsonProperty("totalDiscount")
    public Long getTotalDiscount() {
        return totalDiscount;
    }

    @JsonProperty("totalDiscount")
    public void setTotalDiscount(Long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @JsonProperty("totalTax")
    public String getTotalTax() {
        return totalTax;
    }

    @JsonProperty("totalTax")
    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    @JsonProperty("totalTaxable")
    public Long getTotalTaxable() {
        return totalTaxable;
    }

    @JsonProperty("totalTaxable")
    public void setTotalTaxable(Long totalTaxable) {
        this.totalTaxable = totalTaxable;
    }

    @JsonProperty("totalTaxCalculated")
    public Double getTotalTaxCalculated() {
        return totalTaxCalculated;
    }

    @JsonProperty("totalTaxCalculated")
    public void setTotalTaxCalculated(Double totalTaxCalculated) {
        this.totalTaxCalculated = totalTaxCalculated;
    }

    @JsonProperty("adjustmentReason")
    public String getAdjustmentReason() {
        return adjustmentReason;
    }

    @JsonProperty("adjustmentReason")
    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }

    @JsonProperty("adjustmentDescription")
    public String getAdjustmentDescription() {
        return adjustmentDescription;
    }

    @JsonProperty("adjustmentDescription")
    public void setAdjustmentDescription(String adjustmentDescription) {
        this.adjustmentDescription = adjustmentDescription;
    }

    @JsonProperty("locked")
    public Boolean getLocked() {
        return locked;
    }

    @JsonProperty("locked")
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("version")
    public Long getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Long version) {
        this.version = version;
    }

    @JsonProperty("softwareVersion")
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    @JsonProperty("softwareVersion")
    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    @JsonProperty("exchangeRateEffectiveDate")
    public String getExchangeRateEffectiveDate() {
        return exchangeRateEffectiveDate;
    }

    @JsonProperty("exchangeRateEffectiveDate")
    public void setExchangeRateEffectiveDate(String exchangeRateEffectiveDate) {
        this.exchangeRateEffectiveDate = exchangeRateEffectiveDate;
    }

    @JsonProperty("exchangeRate")
    public Long getExchangeRate() {
        return exchangeRate;
    }

    @JsonProperty("exchangeRate")
    public void setExchangeRate(Long exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @JsonProperty("isSellerImporterOfRecord")
    public Boolean getIsSellerImporterOfRecord() {
        return isSellerImporterOfRecord;
    }

    @JsonProperty("isSellerImporterOfRecord")
    public void setIsSellerImporterOfRecord(Boolean isSellerImporterOfRecord) {
        this.isSellerImporterOfRecord = isSellerImporterOfRecord;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("businessIdentificationNo")
    public String getBusinessIdentificationNo() {
        return businessIdentificationNo;
    }

    @JsonProperty("businessIdentificationNo")
    public void setBusinessIdentificationNo(String businessIdentificationNo) {
        this.businessIdentificationNo = businessIdentificationNo;
    }

    @JsonProperty("modifiedDate")
    public String getModifiedDate() {
        return modifiedDate;
    }

    @JsonProperty("modifiedDate")
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @JsonProperty("modifiedUserId")
    public Long getModifiedUserId() {
        return modifiedUserId;
    }

    @JsonProperty("modifiedUserId")
    public void setModifiedUserId(Long modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    @JsonProperty("taxDate")
    public String getTaxDate() {
        return taxDate;
    }

    @JsonProperty("taxDate")
    public void setTaxDate(String taxDate) {
        this.taxDate = taxDate;
    }

    @JsonProperty("lines")
    public List<AvalaraResponseLineItemDTO> getLines() {
        return lines;
    }

    @JsonProperty("lines")
    public void setLines(List<AvalaraResponseLineItemDTO> lines) {
        this.lines = lines;
    }

    @JsonProperty("addresses")
    public List<AvalaraResponseAddressDTO> getAddresses() {
        return addresses;
    }

    @JsonProperty("addresses")
    public void setAddresses(List<AvalaraResponseAddressDTO> addresses) {
        this.addresses = addresses;
    }

    @JsonProperty("locationTypes")
    public List<Object> getLocationTypes() {
        return locationTypes;
    }

    @JsonProperty("locationTypes")
    public void setLocationTypes(List<Object> locationTypes) {
        this.locationTypes = locationTypes;
    }

    @JsonProperty("summary")
    public List<AvalaraResponseSummaryDTO> getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(List<AvalaraResponseSummaryDTO> summary) {
        this.summary = summary;
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
