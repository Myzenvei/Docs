/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.facades.data.TopicOfInquiryData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class GPTopicOfInquiryPopulator implements Populator<GPSupportTicketModel, TopicOfInquiryData>{

	@Override
	public void populate(GPSupportTicketModel source, TopicOfInquiryData target) throws ConversionException {
		target.setKey(source.getTopicOfInquiry());
		target.setValue(source.getTopicOfInquiryValue());
		
	}

}
