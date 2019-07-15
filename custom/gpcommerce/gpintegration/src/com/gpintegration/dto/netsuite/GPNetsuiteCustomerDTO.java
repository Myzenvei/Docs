/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gpintegration.dto.netsuite;

import java.io.Serializable;

/**
 * @author spandiyan
 *
 */
public class GPNetsuiteCustomerDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String hybrisCustomerId;
	
	private String department;
	
	private String firstName;
	
	private String lastName;
	
	private String companyName;
	
	private String email;
	
	private Boolean isCompany;
	
	private Boolean billingSameAsShipping;
	
	private GPNetsuiteAddressDTO billingAddress;
	
	private GPNetsuiteAddressDTO shippingAddress;

	public String getHybrisCustomerId() {
		return hybrisCustomerId;
	}

	public void setHybrisCustomerId(String hybrisCustomerId) {
		this.hybrisCustomerId = hybrisCustomerId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsCompany() {
		return isCompany;
	}

	public void setIsCompany(Boolean isCompany) {
		this.isCompany = isCompany;
	}

	public Boolean getBillingSameAsShipping() {
		return billingSameAsShipping;
	}

	public void setBillingSameAsShipping(Boolean billingSameAsShipping) {
		this.billingSameAsShipping = billingSameAsShipping;
	}

	public GPNetsuiteAddressDTO getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(GPNetsuiteAddressDTO billingAddress) {
		this.billingAddress = billingAddress;
	}

	public GPNetsuiteAddressDTO getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(GPNetsuiteAddressDTO shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		return "GPNetsuiteCustomerDTO [hybrisCustomerId=" + hybrisCustomerId + ", department=" + department
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", companyName=" + companyName + ", email="
				+ email + ", isCompany=" + isCompany + ", billingSameAsShipping=" + billingSameAsShipping
				+ ", billingAddress=" + billingAddress + ", shippingAddress=" + shippingAddress + "]";
	}
	
	
}