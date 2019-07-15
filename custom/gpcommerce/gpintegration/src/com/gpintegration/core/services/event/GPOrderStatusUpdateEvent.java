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
package com.gpintegration.core.services.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


public class GPOrderStatusUpdateEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private static final long serialVersionUID = 1L;
	private String shippingNotificationCode;
	
	/**
	 * 
	 * @param shippingNotificationCode
	 */
	public GPOrderStatusUpdateEvent(final String shippingNotificationCode)
	{
		super();
		this.shippingNotificationCode=shippingNotificationCode;
	}

	/**
	 * 
	 * @return String
	 */
	public String getShippingNotificationCode() {
		return shippingNotificationCode;
	}

	/**
	 * 
	 * @param shippingNotificationCode
	 */
	public void setShippingNotificationCode(String shippingNotificationCode) {
		this.shippingNotificationCode = shippingNotificationCode;
	}
}
