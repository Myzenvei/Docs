/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.gpcommerceaddon.constants;

/**
 * Global class for all Gpcommonaddon web constants. You can add global constants for your extension into this class.
 */
public final class GpcommonaddonWebConstants // NOSONAR
{
	private GpcommonaddonWebConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public static final String EXTENSION_NAME = "gpcommonaddon";
	public static final String MY_ACCOUNT = "/my-account/profile";
	public static final String CHECKOUT_URI = "checkout";
	public static final String CHECKOUT_CONFIRMATION_URI = "checkout/checkoutConfirmation";


}
