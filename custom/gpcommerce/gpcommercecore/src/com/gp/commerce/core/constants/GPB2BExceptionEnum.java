/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.constants;

/**
 * B2B Org Exception
 */
@SuppressWarnings("javadoc")
public enum GPB2BExceptionEnum
{

	PERMISSION("9100", "Permission ID already exists"), UNIT("9101", "Unit ID already exists"), USER("9102",
			"User ID already exists"), USERGROUP("9103",
					"User Group with the same ID already exists"), INVITE_USER("9104",
							"This user already has an account in our system. Select \"Invite\" to resend login instructions."), ORDERSTATUS(
									"9106", "Error occured while updating Order Status"), USER_UNDERREVIEW(
									"9105", "User Approval Status is Under Review"),ADDRESSAPPROVALSTATUS("9107","Error occured with address while placing Order");
	private final String code;
	private final String errMsg;

	GPB2BExceptionEnum(final String code, final String errMsg)
	{
		this.code = code;
		this.errMsg = errMsg;
	}


	/**
	 * @return error code
	 */
	public String getCode()
	{
		return code;
	}


	/**
	 * @return error Message
	 */
	public String getErrMsg()
	{
		return errMsg;
	}
}
