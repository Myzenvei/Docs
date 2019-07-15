/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.leaseagreement.dto;


import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"MaterialNumber",
"MaterialDescription",
"Quantity",
"UnitofMeasurement"
})
public class GPLeaseAgreementMaterialDTO {

@JsonProperty("MaterialNumber")
private String materialNumber;
@JsonProperty("MaterialDescription")
private String materialDescription;
@JsonProperty("Quantity")
private String quantity;
@JsonProperty("UnitofMeasurement")
private String unitofMeasurement;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("MaterialNumber")
public String getMaterialNumber() {
return materialNumber;
}

@JsonProperty("MaterialNumber")
public void setMaterialNumber(String materialNumber) {
this.materialNumber = materialNumber;
}

@JsonProperty("MaterialDescription")
public String getMaterialDescription() {
return materialDescription;
}

@JsonProperty("MaterialDescription")
public void setMaterialDescription(String materialDescription) {
this.materialDescription = materialDescription;
}

@JsonProperty("Quantity")
public String getQuantity() {
return quantity;
}

@JsonProperty("Quantity")
public void setQuantity(String quantity) {
this.quantity = quantity;
}

@JsonProperty("UnitofMeasurement")
public String getUnitofMeasurement() {
return unitofMeasurement;
}

@JsonProperty("UnitofMeasurement")
public void setUnitofMeasurement(String unitofMeasurement) {
this.unitofMeasurement = unitofMeasurement;
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