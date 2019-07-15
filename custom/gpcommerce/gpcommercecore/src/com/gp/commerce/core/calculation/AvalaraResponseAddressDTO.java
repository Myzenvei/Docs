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
        "transactionId",
        "boundaryLevel",
        "line1",
        "line2",
        "line3",
        "city",
        "region",
        "postalCode",
        "country",
        "taxRegionId",
        "latitude",
        "longitude"
})
public class AvalaraResponseAddressDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("transactionId")
    private Long transactionId;
    @JsonProperty("boundaryLevel")
    private String boundaryLevel;
    @JsonProperty("line1")
    private String line1;
    @JsonProperty("line2")
    private String line2;
    @JsonProperty("line3")
    private String line3;
    @JsonProperty("city")
    private String city;
    @JsonProperty("region")
    private String region;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("country")
    private String country;
    @JsonProperty("taxRegionId")
    private Long taxRegionId;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
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

    @JsonProperty("boundaryLevel")
    public String getBoundaryLevel() {
        return boundaryLevel;
    }

    @JsonProperty("boundaryLevel")
    public void setBoundaryLevel(String boundaryLevel) {
        this.boundaryLevel = boundaryLevel;
    }

    @JsonProperty("line1")
    public String getLine1() {
        return line1;
    }

    @JsonProperty("line1")
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    @JsonProperty("line2")
    public String getLine2() {
        return line2;
    }

    @JsonProperty("line2")
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    @JsonProperty("line3")
    public String getLine3() {
        return line3;
    }

    @JsonProperty("line3")
    public void setLine3(String line3) {
        this.line3 = line3;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("taxRegionId")
    public Long getTaxRegionId() {
        return taxRegionId;
    }

    @JsonProperty("taxRegionId")
    public void setTaxRegionId(Long taxRegionId) {
        this.taxRegionId = taxRegionId;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
