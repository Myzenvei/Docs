/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.user.impl;

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
    "CompanyName",
    "FirstName",
    "LastName",
    "additionalAddressLine",
    "ContactEmail",
    "CustomerType",
    "Phone",
    "Cellphone",
    "Title",
    "OriginalUID",
    "RegisteredSite",
    "CustomerStatus",
    "Address"
})
public class Customer {

	@JsonProperty("CompanyName")
    private String companyName;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("CustomerStatus")
    private String customerStatus;
    @JsonProperty("ContactEmail")
    private String contactEmail;
    @JsonProperty("CustomerType")
    private String customerType;
    @JsonProperty("Phone")
    private String phone;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Cellphone")
    private String cellphone;
    @JsonProperty("RegisteredSite")
    private String registeredSite;
    @JsonProperty("OriginalUID")
    private String originalUid;
    @JsonProperty("Address")
    private List<Address> address;
    
    @JsonProperty("Address")
    public List<Address> getAddress() {
		return address;
	}
    @JsonProperty("Address")
	public void setAddress(List<Address> address) {
		this.address = address;
	}

	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CompanyName")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("CompanyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    @JsonProperty("CustomerStatus")
    public String getCustomerStatus() {
        return customerStatus;
    }

    @JsonProperty("CustomerStatus")
    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }


    @JsonProperty("ContactEmail")
    public String getContactEmail() {
        return contactEmail;
    }

    @JsonProperty("ContactEmail")
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @JsonProperty("CustomerType")
    public String getCustomerType() {
        return customerType;
    }

    @JsonProperty("CustomerType")
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    
    @JsonProperty("OriginalUID")
    public String getOriginalUid() {
        return originalUid;
    }

    @JsonProperty("OriginalUID")
    public void setOriginalUid(String originalUid) {
        this.originalUid = originalUid;
    }

    @JsonProperty("RegisteredSite")
    public String getRegisteredSite() {
        return registeredSite;
    }

    @JsonProperty("RegisteredSite")
    public void setRegisteredSite(String registeredSite) {
        this.registeredSite = registeredSite;
    }
    
    @JsonProperty("Phone")
    public String getPhone() {
		return phone;
	}
    
    @JsonProperty("Phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    @JsonProperty("Title")
	public String getTitle() {
		return title;
	}
    
    @JsonProperty("Title")
	public void setTitle(String title) {
		this.title = title;
	}
    
    @JsonProperty("Cellphone")
	public String getCellphone() {
		return cellphone;
	}
    
    @JsonProperty("Cellphone")
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
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
