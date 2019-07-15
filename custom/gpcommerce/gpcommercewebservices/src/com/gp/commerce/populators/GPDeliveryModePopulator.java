/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.populators;

import org.springframework.util.Assert;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;


public  class GPDeliveryModePopulator   implements Populator<DeliveryModeModel,DeliveryModeData>{

	@Override
	public void populate(DeliveryModeModel source, DeliveryModeData target) throws ConversionException {
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setDeliveryPrice(source.getDeliveryPrice());
		target.setDeliveryFormattedPrice(source.getDeliveryFormattedPrice());
	}


	
}

 