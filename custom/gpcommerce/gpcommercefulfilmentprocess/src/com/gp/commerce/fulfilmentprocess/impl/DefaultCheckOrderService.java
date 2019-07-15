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
package com.gp.commerce.fulfilmentprocess.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.util.GPSiteConfigUtils;
import com.gp.commerce.fulfilmentprocess.CheckOrderService;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Default implementation of {@link CheckOrderService}
 */
public class DefaultCheckOrderService implements CheckOrderService
{
	private static final Logger LOG = Logger.getLogger(DefaultCheckOrderService.class);
	private static final String DISPENSER = "Dispenser";
	
	enum FRAUDTYPE{
		MAX_ORDER_COUNT,MAX_DISP_COUNT,MAX_DOLLAR_AMT;
	}
	@Override
	public boolean check(final OrderModel order)
	{
		if (!order.getCalculated().booleanValue())
		{
			LOG.error("FULFILLMENT_ERROR | Order must be calculated for order :" +order.getCode());
			return false;
		}
		else if (order.getEntries().isEmpty())
		{
			LOG.error("FULFILLMENT_ERROR | Order must have some lines for order :" +order.getCode());
			return false;
		}
		else if (order.getPaymentInfo() == null)
		{
			LOG.error("FULFILLMENT_ERROR | Order must have some payment infos for order :" +order.getCode());
			return false;
		}
		else if(order.getCalculated().booleanValue() && !order.getEntries().isEmpty())
		{
			return checkGPOrderLimits(order);
		}
		else
		{
			return checkDeliveryOptions(order);
		}
	}

	protected boolean checkGPOrderLimits(final OrderModel order) {
		boolean status  = true;
		Double orderTotal = order.getTotalPrice();
		if(LOG.isDebugEnabled()){
			LOG.debug("Order Total is: " + orderTotal);
		}
		Date orderDate = order.getDate();
		CustomerModel customer = (CustomerModel) order.getUser();
		final Double maxDollarAmount = order.getSite().getMaxDollarAmount();
		final Integer maxOrderCount = order.getSite().getMaxOrderCount();
		final Integer maxDispenserCount = order.getSite().getMaxDispenserCount();
		int orderCount = 0;
		int dispenserCount = 0;
		
		// Calculating Total Order Count per day per user account
		for (OrderModel orderModel : customer.getOrders()) {
			if(DateUtils.isSameDay(orderDate, orderModel.getDate()))
			{
				orderCount++;
			}
		}
		LOG.info("Total Order Count per day per user account is: " + orderCount);
		
		// Calculating Total Dispenser Count
		if(GPSiteConfigUtils.isB2BSite(order.getSite())) {
			for (AbstractOrderEntryModel orderEntry : order.getEntries()) {
				if(DISPENSER.equalsIgnoreCase(orderEntry.getProduct().getAssetCode()))
				{
					dispenserCount = orderEntry.getQuantity().intValue();
				}
			}
			LOG.info("Total Dispenser count is: " + dispenserCount);
		}

		if(maxOrderCount != null && orderCount > maxOrderCount)
		{
			LOG.info("Max Order Count limit is surpassed for order :" + order.getCode());
			status = false;
		}
		if(maxDollarAmount != null && orderTotal > maxDollarAmount)
		{
			LOG.info("Max Dollar Amount limit is surpassed for order :" + order.getCode());
			status = false;
		}
		if(maxDispenserCount != null && dispenserCount > maxDispenserCount)
		{
			LOG.info("Max Dispenser Count limit is surpassed for order :" + order.getCode());
			status = false;
		}
		return status;
	}
	
	protected boolean checkDeliveryOptions(final OrderModel order)
	{
		if (order.getDeliveryMode() == null)
		{
			LOG.error("FULFILLMENT_ERROR | Order must have an overall delivery mode for order :" + order.getCode()); 
			return false;
		}
		return true;
	}

	@Override
	public String checkFraudType(OrderModel order) {
		Double orderTotal = order.getTotalPrice();
		if(LOG.isDebugEnabled()){
			LOG.debug("Order Total is: " + orderTotal);
		}
		Date orderDate = order.getDate();
		CustomerModel customer = (CustomerModel) order.getUser();
		final Double maxDollarAmount = order.getSite().getMaxDollarAmount();
		final Integer maxOrderCount = order.getSite().getMaxOrderCount();
		final Integer maxDispenserCount = order.getSite().getMaxDispenserCount();
		int orderCount = 0;
		int dispenserCount = 0;
		
		// Calculating Total Order Count per day per user account
		for (OrderModel orderModel : customer.getOrders()) {
			if(DateUtils.isSameDay(orderDate, orderModel.getDate()))
			{
				orderCount++;
			}
		}
		LOG.info("Total Order Count per day per user account is: " + orderCount);
		
		// Calculating Total Dispenser Count
		if(GPSiteConfigUtils.isB2BSite(order.getSite())) {
			for (AbstractOrderEntryModel orderEntry : order.getEntries()) {
				if(DISPENSER.equalsIgnoreCase(orderEntry.getProduct().getAssetCode()))
				{
					dispenserCount = orderEntry.getQuantity().intValue();
				}
			}
			LOG.info("Total Dispenser count is: " + dispenserCount);
		}
		if(maxOrderCount != null && orderCount > maxOrderCount)
		{
			LOG.info("Max Order Count limit is surpassed for order :" + order.getCode());
			return FRAUDTYPE.MAX_ORDER_COUNT.toString();
		}
		if(maxDollarAmount != null && orderTotal > maxDollarAmount)
		{
			LOG.info("Max Dollar Amount limit is surpassed for order :" + order.getCode());
			return FRAUDTYPE.MAX_DOLLAR_AMT.toString();
		}
		if(maxDispenserCount != null && dispenserCount > maxDispenserCount)
		{
			LOG.info("Max Dispenser Count limit is surpassed for order :" + order.getCode());
			return FRAUDTYPE.MAX_DISP_COUNT.toString();
		}
		return null;
	}

}
