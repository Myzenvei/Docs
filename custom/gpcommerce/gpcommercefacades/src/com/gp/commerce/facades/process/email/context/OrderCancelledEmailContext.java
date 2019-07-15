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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;


/**
 * Velocity context for a order cancelled email.
 */
public class OrderCancelledEmailContext extends GPAbstractEmailContext<OrderProcessModel>
{
	private static final String CANCELLED_QTY = "cancelledQty";
	private static final String SPLIT_ENTRIES = "splitEntry";
	private static final String CONTACT_US = "/contact-us";
	private static final String CONTACT_US_URL = "contactUsPageUrl";
	public static final String CREATEDDATE = "createdDate";
	public static final String TOTAL_DELIVERY_COST = "totalDeliveryCost";
	private static final String TOTAL_SPLIT_QTY = "splitEntriesMap";
    private static final String MULTIPLE_SHIPPING = "multipleShipping";
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private String orderCode;
	private String orderGuid;
	private int cancelledQty ;
	private int entryQty ;
	private boolean guest;
	private String storeName;
	private ConsignmentEntryData consignmentEntryData;
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	final Map<Integer, Integer> map = new HashMap<>();

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		cancelledQty = 0;
		super.init(orderProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		if (null != orderData.getDeliveryCost()) {
			put(TOTAL_DELIVERY_COST, orderData.getDeliveryCost().getFormattedValue());
		}
		if (null != orderData.getEntries()) {
			orderData.getEntries().forEach(entry -> {
				entryQty=0;
				if (null != entry.getSplitEntries()) {
					entry.getSplitEntries().forEach(splitEntry -> {
						cancelledQty += Integer.valueOf(splitEntry.getQty());
						put(SPLIT_ENTRIES, entry.getSplitEntries());
						put(MULTIPLE_SHIPPING, Boolean.FALSE.toString());
						if(splitEntry.getDeliveryMode()!= null)
						{
								put(TOTAL_DELIVERY_COST,orderData.getTotalDeliveryCost());
								entryQty += Integer.valueOf(splitEntry.getQty());
							put(MULTIPLE_SHIPPING, Boolean.TRUE.toString());
						}
					});

				}
				map.put(entry.getEntryNumber(), entryQty);
			});
		}
		put(TOTAL_SPLIT_QTY,map);
		put(CANCELLED_QTY,cancelledQty);
		orderCode = orderData.getCode();
		orderGuid = orderData.getGuid();
		guest = CustomerType.GUEST.equals(getCustomer(orderProcessModel).getType());
		storeName = orderProcessModel.getOrder().getStore()	.getName();
		if (null != orderProcessModel.getConsignmentEntry()) {
			consignmentEntryData = getConsignmentEntryConverter().convert(orderProcessModel.getConsignmentEntry());
		}
		put(CONTACT_US_URL, getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(),true, CONTACT_US));
		put(CREATEDDATE, getCreateDate(orderData.getCreated()));
		put(GpcommerceCoreConstants.CONTACT_NUMBER, getCustomerNumber());
	}

	private String getCreateDate(final Date date) {
	    final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
	    final String createdDate= formatter.format(date);
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

	public Converter<ConsignmentEntryModel, ConsignmentEntryData> getConsignmentEntryConverter()
	{
		return consignmentEntryConverter;
	}

	@Required
	public void setConsignmentEntryConverter(final Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter)
	{
		this.consignmentEntryConverter = consignmentEntryConverter;
	}

}
