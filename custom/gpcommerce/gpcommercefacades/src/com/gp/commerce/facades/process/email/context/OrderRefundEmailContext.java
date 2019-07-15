/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;


/**
 * Velocity context for a order refund email.
 */
public class OrderRefundEmailContext extends GPAbstractEmailContext<OrderProcessModel>
{
	private static final String CONTACT_US= "/contact-us";
	private static final String CONTACT_US_URL = "contactUsPageUrl";
	public static final String CREATEDDATE = "createdDate";
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private String orderCode;
	private String orderGuid;
	private boolean guest;
	private String storeName;
	private BigDecimal refundAmount;

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		orderCode = orderProcessModel.getOrder().getCode();
		orderGuid = orderProcessModel.getOrder().getGuid();
		guest = CustomerType.GUEST.equals(getCustomer(orderProcessModel).getType());
		storeName = orderProcessModel.getOrder().getStore().getName();
		orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		if (null != orderProcessModel.getRefundAmount()) {
			refundAmount = orderProcessModel.getRefundAmount().setScale(2, BigDecimal.ROUND_FLOOR);
		}
		put(CONTACT_US_URL, getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), true, CONTACT_US));
		put(CREATEDDATE, getCreateDate(orderData.getCreated()));
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());
	}
	
	private String getCreateDate(Date date) {
	    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");  
	    String createdDate= formatter.format(date);  
		return createdDate;
	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderData getOrder()
	{
		return orderData;
	}

	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}

	public OrderData getOrderData()
	{
		return orderData;
	}

	public void setOrderData(final OrderData orderData)
	{
		this.orderData = orderData;
	}

	public String getOrderCode()
	{
		return orderCode;
	}

	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getOrderGuid()
	{
		return orderGuid;
	}

	public void setOrderGuid(final String orderGuid)
	{
		this.orderGuid = orderGuid;
	}

	public boolean isGuest()
	{
		return guest;
	}

	public void setGuest(final boolean guest)
	{
		this.guest = guest;
	}

	public String getStoreName()
	{
		return storeName;
	}

	public void setStoreName(final String storeName)
	{
		this.storeName = storeName;
	}

	public BigDecimal getRefundAmount()
	{
		return refundAmount;
	}
}
