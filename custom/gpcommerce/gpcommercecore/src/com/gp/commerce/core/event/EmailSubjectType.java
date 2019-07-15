/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;

public enum EmailSubjectType
{
	USER_PENDING("User Pending"),
   USER_APPROVED("User Approved"),
   L3_ADMIN_APPROVED("Your Request has been approved"),
   NEW_USER_REVIEW("New User Review"),
   INVITE_USER("Account Invitation"),
	ADDRESS_SUBMITTED("Address Submitted For Approval"),
	ADDRESS_APPROVED("Address Approved by Admin"),
	ADDRESS_REJECTED("Address Rejected by Admin"),
	CUSTOMER_REGISTRATION("Customer Registration");

   
   private final String subject;

	private EmailSubjectType(String subject)
	{
		this.subject = subject;
	}

	public String getSubject()
	{
		return subject;
	}
	
}
