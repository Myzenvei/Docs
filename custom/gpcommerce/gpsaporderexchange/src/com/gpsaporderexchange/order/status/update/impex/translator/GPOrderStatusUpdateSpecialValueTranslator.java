/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gpsaporderexchange.order.status.update.impex.translator;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.security.JaloSecurityException;

import com.gpsaporderexchange.order.status.update.inbound.helper.DefaultGPOrderStatusUpdateInboundHelper;




/**
 * Abstract Translator for order status update event call
 */
public class GPOrderStatusUpdateSpecialValueTranslator extends AbstractSpecialValueTranslator
{

	@SuppressWarnings("javadoc")
	public static final String HELPER_BEAN = "gpOrderStatusUpadteInboundHelper";
	private final String helperBeanName;
	private DefaultGPOrderStatusUpdateInboundHelper inboundHelper;


	public GPOrderStatusUpdateSpecialValueTranslator()
	{
		this.helperBeanName = HELPER_BEAN;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		if (getInboundHelper() == null)
		{
			setInboundHelper((DefaultGPOrderStatusUpdateInboundHelper) Registry.getApplicationContext().getBean(helperBeanName));
		}
	}


	public String getCode(final Item processedItem) throws ImpExException
	{
		String shippingNotificationcode = null;

		try
		{
			shippingNotificationcode = processedItem.getAttribute("code").toString();
		}
		catch (final JaloSecurityException e)
		{
			throw new ImpExException(e);
		}
		return shippingNotificationcode;

	}

	@Override
	public void validate(final String paramString) throws HeaderValidationException
	{
		// Nothing to do
	}

	@Override
	public String performExport(final Item paramItem) throws ImpExException
	{
		return null;
	}
	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException {
		//Nothing to do
	}

	@Override
	public boolean isEmpty(final String paramString)
	{
		return false;
	}

	protected void setInboundHelper(final DefaultGPOrderStatusUpdateInboundHelper service)
	{
		this.inboundHelper = service;
	}

	protected DefaultGPOrderStatusUpdateInboundHelper getInboundHelper()
	{
		return inboundHelper;
	}
}
