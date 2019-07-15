/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;


import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
 

public class DeliveryModeReversePopulator   implements Populator<DeliveryModeData, DeliveryModeModel>{
	@Override
	public void populate(final DeliveryModeData source, final DeliveryModeModel target)
	{
		target.setCode(source.getCode()+"_"+ System.currentTimeMillis());
		source.setDeliveryPrice(source.getDeliveryCost().getFormattedValue());
		target.setDeliveryPrice(source.getDeliveryCost().getFormattedValue()) ;
		 target.setDeliveryFormattedPrice(source.getDeliveryCost().getValue().doubleValue());
	 }

 
}
