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
        "documentLineLocationTypeId",
        "documentLineId",
        "documentAddressId",
        "locationTypeCode"
})
public class AvalaraResponseLineLocationTypeDTO {
    @JsonProperty("documentLineLocationTypeId")
    private Long documentLineLocationTypeId;
    @JsonProperty("documentLineId")
    private Long documentLineId;
    @JsonProperty("documentAddressId")
    private Long documentAddressId;
    @JsonProperty("locationTypeCode")
    private String locationTypeCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("documentLineLocationTypeId")
    public Long getDocumentLineLocationTypeId() {
        return documentLineLocationTypeId;
    }

    @JsonProperty("documentLineLocationTypeId")
    public void setDocumentLineLocationTypeId(Long documentLineLocationTypeId) {
        this.documentLineLocationTypeId = documentLineLocationTypeId;
    }

    @JsonProperty("documentLineId")
    public Long getDocumentLineId() {
        return documentLineId;
    }

    @JsonProperty("documentLineId")
    public void setDocumentLineId(Long documentLineId) {
        this.documentLineId = documentLineId;
    }

    @JsonProperty("documentAddressId")
    public Long getDocumentAddressId() {
        return documentAddressId;
    }

    @JsonProperty("documentAddressId")
    public void setDocumentAddressId(Long documentAddressId) {
        this.documentAddressId = documentAddressId;
    }

    @JsonProperty("locationTypeCode")
    public String getLocationTypeCode() {
        return locationTypeCode;
    }

    @JsonProperty("locationTypeCode")
    public void setLocationTypeCode(String locationTypeCode) {
        this.locationTypeCode = locationTypeCode;
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

