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
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;


/**
 * Velocity context for a Order Reject Email.
 */
public class GPOrderRejectedEmailContext extends GPAbstractEmailContext<OrderProcessModel>
{
	private static final String SPLIT_ENTRIES = "splitEntry";
    private static final String MULTIPLE_SHIPPING = "multipleShipping";
	private static final String TOTAL_SPLIT_QTY = "splitEntriesMap";
	private Converter<OrderModel, OrderData> orderConverter;
	private String token;
	private int expiresInMinutes = 24;
	private OrderData orderData;
	private String orderCode;
	private String orderGuid;
	private boolean guest;
	private String storeName;
	public static final String CREATEDDATE = "createdDate";
	private int entryQty ;
	final Map<Integer, Integer> map = new HashMap<>();

	public int getExpiresInMinutes() {
		return expiresInMinutes;
	}

	public void setExpiresInMinutes(final int expiresInMinutes) {
		this.expiresInMinutes = expiresInMinutes;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(final String token)
	{
		this.token = token;
	}

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		setOrderData(getOrderConverter().convert(orderProcessModel.getOrder()));
		setOrderCode(orderProcessModel.getOrder().getCode());
		setOrderGuid(orderProcessModel.getOrder().getGuid());
		setGuest(CustomerType.GUEST.equals(getCustomer(orderProcessModel).getType()));
		setStoreName(orderProcessModel.getOrder().getStore().getName());
		put(CREATEDDATE, getCreateDate(orderData.getCreated()));
		if (null != orderData.getEntries()) {
			orderData.getEntries().forEach(entry -> {
				entryQty=0;
				if (null != entry.getSplitEntries()) {
					entry.getSplitEntries().forEach(splitEntry -> {

						put(SPLIT_ENTRIES, entry.getSplitEntries());
						put(MULTIPLE_SHIPPING, Boolean.FALSE.toString());
						if(splitEntry.getDeliveryMode()!= null)
						{
								entryQty += Integer.valueOf(splitEntry.getQty());
							put(MULTIPLE_SHIPPING, Boolean.TRUE.toString());
						}
					});

				}
				map.put(entry.getEntryNumber(), entryQty);
			});
		}
		put(TOTAL_SPLIT_QTY,map);
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());
	}

	private String getCreateDate(final Date date) {
	    final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
	    final String createdDate= formatter.format(date);
		return createdDate;
	}

	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrderData(final OrderData orderData) {
		this.orderData = orderData;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(final String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderGuid() {
		return orderGuid;
	}

	public void setOrderGuid(final String orderGuid) {
		this.orderGuid = orderGuid;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(final boolean guest) {
		this.guest = guest;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(final String storeName) {
		this.storeName = storeName;
	}

	public Converter<OrderModel, OrderData> getOrderConverter() {
		return orderConverter;
	}

	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter) {
		this.orderConverter = orderConverter;
	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel businessProcessModel) {
		return businessProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel businessProcessModel) {
		return (CustomerModel) businessProcessModel.getOrder().getUser();
	}

	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel businessProcessModel) {
		return businessProcessModel.getOrder().getLanguage();
	}
}
