/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.crm.shellorder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"FirstName",
"LastName",
"CompanyName",
"Email",
"TelephoneNumber",
"StreetAddress",
"POBoxNumber",
"AppartmentNumber",
"BuildingNumber",
"City",
"StateCode",
"PostalCode",
"CountryCode"
})
public class GPCRMContactDetailsDTO {

@JsonProperty("FirstName")
private String firstName;
@JsonProperty("LastName")
private String lastName;
@JsonProperty("CompanyName")
private String companyName;
@JsonProperty("Email")
private String email;
@JsonProperty("TelephoneNumber")
private String telephoneNumber;
@JsonProperty("StreetAddress")
private String streetAddress;
@JsonProperty("POBoxNumber")
private String pOBoxNumber;
@JsonProperty("AppartmentNumber")
private String appartmentNumber;
@JsonProperty("BuildingNumber")
private String buildingNumber;
@JsonProperty("City")
private String city;
@JsonProperty("StateCode")
private String stateCode;
@JsonProperty("PostalCode")
private String postalCode;
@JsonProperty("CountryCode")
private String countryCode;

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

@JsonProperty("CompanyName")
public String getCompanyName() {
return companyName;
}

@JsonProperty("CompanyName")
public void setCompanyName(String companyName) {
this.companyName = companyName;
}

@JsonProperty("Email")
public String getEmail() {
return email;
}

@JsonProperty("Email")
public void setEmail(String email) {
this.email = email;
}

@JsonProperty("TelephoneNumber")
public String getTelephoneNumber() {
return telephoneNumber;
}

@JsonProperty("TelephoneNumber")
public void setTelephoneNumber(String telephoneNumber) {
this.telephoneNumber = telephoneNumber;
}

@JsonProperty("StreetAddress")
public String getStreetAddress() {
return streetAddress;
}

@JsonProperty("StreetAddress")
public void setStreetAddress(String streetAddress) {
this.streetAddress = streetAddress;
}

@JsonProperty("POBoxNumber")
public String getPOBoxNumber() {
return pOBoxNumber;
}

@JsonProperty("POBoxNumber")
public void setPOBoxNumber(String pOBoxNumber) {
this.pOBoxNumber = pOBoxNumber;
}

@JsonProperty("AppartmentNumber")
public String getAppartmentNumber() {
return appartmentNumber;
}

@JsonProperty("AppartmentNumber")
public void setAppartmentNumber(String appartmentNumber) {
this.appartmentNumber = appartmentNumber;
}

@JsonProperty("BuildingNumber")
public String getBuildingNumber() {
return buildingNumber;
}

@JsonProperty("BuildingNumber")
public void setBuildingNumber(String buildingNumber) {
this.buildingNumber = buildingNumber;
}

@JsonProperty("City")
public String getCity() {
return city;
}

@JsonProperty("City")
public void setCity(String city) {
this.city = city;
}

@JsonProperty("StateCode")
public String getStateCode() {
return stateCode;
}

@JsonProperty("StateCode")
public void setStateCode(String stateCode) {
this.stateCode = stateCode;
}

@JsonProperty("PostalCode")
public String getPostalCode() {
return postalCode;
}

@JsonProperty("PostalCode")
public void setPostalCode(String postalCode) {
this.postalCode = postalCode;
}

@JsonProperty("CountryCode")
public String getCountryCode() {
return countryCode;
}

@JsonProperty("CountryCode")
public void setCountryCode(String countryCode) {
this.countryCode = countryCode;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("firstName", firstName).append("lastName", lastName).append("companyName", companyName).append("email", email).append("telephoneNumber", telephoneNumber).append("streetAddress", streetAddress).append("pOBoxNumber", pOBoxNumber).append("appartmentNumber", appartmentNumber).append("buildingNumber", buildingNumber).append("city", city).append("stateCode", stateCode).append("postalCode", postalCode).append("countryCode", countryCode).toString();
}

}