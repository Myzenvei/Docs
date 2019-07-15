/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.strategies;

import java.util.Set;

import org.apache.log4j.Logger;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.order.strategies.calculation.impl.DefaultFindDeliveryCostStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.PriceValue;

public class GPFindDeliveryCostStrategy extends DefaultFindDeliveryCostStrategy{

	private transient ModelService modelService;
	
	private static final Logger LOG = Logger.getLogger(DefaultFindDeliveryCostStrategy.class);


	@Override
	public PriceValue getDeliveryCost(final AbstractOrderModel order)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		try
		{
			final DeliveryModeModel deliveryMode = order.getDeliveryMode();
			if( deliveryMode != null )
			{
   			final AbstractOrder orderItem = getModelService().getSource(order);
   			final DeliveryMode dModeJalo = getModelService().getSource(deliveryMode);
				final PriceValue calculatedPrice = dModeJalo.getCost(orderItem);
				final PriceValue deliverycost = calculateDeliveryCost(order, calculatedPrice);
				return deliverycost;
			}
			else
			{
				return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue());
			}
		}
		catch (final Exception e)
		{
			LOG.warn("Could not find deliveryCost for order [" + order.getCode() + "] due to : " + e + "... skipping!");
			return new PriceValue(order.getCurrency().getIsocode(), 0.0, order.getNet().booleanValue());
		}

	}

	public final PriceValue calculateDeliveryCost(final AbstractOrderModel order, PriceValue calculatedPrice)
		{
		String deliveryModeType = null;
		double updatedPrice = 0.0;
		final Set<ZoneDeliveryModeValueModel> zoneValueList = ((ZoneDeliveryModeModel) order.getDeliveryMode()).getValues();
		for (final ZoneDeliveryModeValueModel zoneValue : zoneValueList)
		{
			if (zoneValue.getValue().equals(calculatedPrice.getValue()))
			{
				deliveryModeType = zoneValue.getDeliveryModeType();
				}
		}
		if (null!=deliveryModeType && ("percentage").equalsIgnoreCase(deliveryModeType))
		{
			updatedPrice = ((order.getTotalPrice()) * (calculatedPrice.getValue()) / 100);
			calculatedPrice = new PriceValue(order.getCurrency().getIsocode(), updatedPrice, order.getNet().booleanValue());
		}

		return calculatedPrice;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}


}
