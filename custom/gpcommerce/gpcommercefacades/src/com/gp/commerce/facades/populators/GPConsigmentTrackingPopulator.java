/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.TrackingModel;
import com.gp.commerce.facade.data.TrackingData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPConsigmentTrackingPopulator implements Populator<TrackingModel, TrackingData>{

	@Override
	public void populate(TrackingModel source, TrackingData target) throws ConversionException {
	
		target.setCarrier(source.getCarrier());
		target.setTrackingID(source.getTrackingID());
		target.setQuantityShipped(source.getQuantityShipped());
		target.setTrackingURL(source.getTrackingURL());
	}
}
