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
package com.gpreturnorder.hybris.inbound.events;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class CreditMemoEvent extends AbstractEvent
{
	private String returnRequestCode;

	public CreditMemoEvent(final String returnRequestCode)
	{
		this.returnRequestCode = returnRequestCode;

	}

	public String getReturnRequestCode()
	{
		return returnRequestCode;
	}

	public void setReturnRequestCode(final String returnRequestCode)
	{
		this.returnRequestCode = returnRequestCode;
	}



}
