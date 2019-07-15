/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.leaseagreement.dto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"OrderDate",
"LeaseTermsID",
"AgreementID",
"FirstName",
"LastName",
"ContactEmail",
"ContactPhone",
"Street",
"City",
"State",
"Country",
"CompanyName",
"Material"
})
public class GPLeaseAgreementDTO {

@JsonProperty("OrderDate")
private String orderDate;
@JsonProperty("LeaseTermsID")
private String leaseTermsID;
@JsonProperty("AgreementID")
private String agreementID;
@JsonProperty("FirstName")
private String firstName;
@JsonProperty("LastName")
private String lastName;
@JsonProperty("ContactEmail")
private String contactEmail;
@JsonProperty("ContactPhone")
private String contactPhone;
@JsonProperty("Street")
private String street;
@JsonProperty("City")
private String city;
@JsonProperty("State")
private String state;
@JsonProperty("Country")
private String country;
@JsonProperty("CompanyName")
private String companyName;
@JsonProperty("Material")
private List<GPLeaseAgreementMaterialDTO> material = null;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("OrderDate")
public String getOrderDate() {
return orderDate;
}

@JsonProperty("OrderDate")
public void setOrderDate(String orderDate) {
this.orderDate = orderDate;
}

@JsonProperty("LeaseTermsID")
public String getLeaseTermsID() {
return leaseTermsID;
}

@JsonProperty("LeaseTermsID")
public void setLeaseTermsID(String leaseTermsID) {
this.leaseTermsID = leaseTermsID;
}

@JsonProperty("AgreementID")
public String getAgreementID() {
return agreementID;
}

@JsonProperty("AgreementID")
public void setAgreementID(String agreementID) {
this.agreementID = agreementID;
}

@JsonProperty("FirstName")
public String getFirstName() {
return firstName;
}

@JsonProperty("FirstName")
public void setFirstName(String firstName) {
this.firstName = firstName;
}

@JsonProperty("LastName")
public String getLastName() {
return lastName;
}

@JsonProperty("LastName")
public void setLastName(String lastName) {
this.lastName = lastName;
}

@JsonProperty("ContactEmail")
public String getContactEmail() {
return contactEmail;
}

@JsonProperty("ContactEmail")
public void setContactEmail(String contactEmail) {
this.contactEmail = contactEmail;
}

@JsonProperty("ContactPhone")
public String getContactPhone() {
return contactPhone;
}

@JsonProperty("ContactPhone")
public void setContactPhone(String contactPhone) {
this.contactPhone = contactPhone;
}

@JsonProperty("Street")
public String getStreet() {
return street;
}

@JsonProperty("Street")
public void setStreet(String street) {
this.street = street;
}

@JsonProperty("City")
public String getCity() {
return city;
}

@JsonProperty("City")
public void setCity(String city) {
this.city = city;
}

@JsonProperty("State")
public String getState() {
return state;
}

@JsonProperty("State")
public void setState(String state) {
this.state = state;
}

@JsonProperty("Country")
public String getCountry() {
return country;
}

@JsonProperty("Country")
public void setCountry(String country) {
this.country = country;
}

@JsonProperty("CompanyName")
public String getCompanyName() {
return companyName;
}

@JsonProperty("CompanyName")
public void setCompanyName(String companyName) {
this.companyName = companyName;
}

@JsonProperty("Material")
public List<GPLeaseAgreementMaterialDTO> getMaterial() {
return material;
}

@JsonProperty("Material")
public void setMaterial(List<GPLeaseAgreementMaterialDTO> material) {
this.material = material;
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