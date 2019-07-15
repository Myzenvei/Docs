/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import java.util.List;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Listener for address event.
 */

public class GPEmailItemEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private AddressModel address;
	private CustomerModel invitedCustomer;
	private String bccEmail;
	private List<String> toEmails;
	private CustomerModel adminModel;
	private String emailSubject;
	private CustomerModel customerModel;
	private boolean isBackOfficeUser;
	private String token;
	private String adminName;
	
	
	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public boolean isBackOfficeUser()
	{
		return isBackOfficeUser;
	}

	public void setBackOfficeUser(boolean isBackOfficeUser)
	{
		this.isBackOfficeUser = isBackOfficeUser;
	}

	public CustomerModel getCustomerModel()
	{
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel)
	{
		this.customerModel = customerModel;
	}

	public CustomerModel getAdminModel()
	{
		return adminModel;
	}

	public void setAdminModel(CustomerModel adminModel)
	{
		this.adminModel = adminModel;
	}

	public String getEmailSubject()
	{
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject)
	{
		this.emailSubject = emailSubject;
	}


	public List<String> getToEmails() {
		return toEmails;
	}

	public void setToEmails(List<String> toEmails) {
		this.toEmails = toEmails;
	}

	public String getBccEmail()
	{
		return bccEmail;
	}

	public void setBccEmail(String bccEmail)
	{
		this.bccEmail = bccEmail;
	}


	public CustomerModel getInvitedCustomer()
	{
		return invitedCustomer;
	}

	public void setInvitedCustomer(final CustomerModel invitedCustomer)
	{
		this.invitedCustomer = invitedCustomer;
	}

	public AddressModel getAddress()
	{
		return address;
	}

	public void setAddress(final AddressModel address)
	{
		this.address = address;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	
}
