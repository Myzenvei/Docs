/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import javax.annotation.Resource;
import org.springframework.util.Assert;

import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.facades.data.TopicOfInquiryData;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.service.TicketService;

public class GPSupportTicketPopulator implements Populator<GPSupportTicketModel, TicketData> {


	@Resource(name = "ticketService")
	private TicketService ticketService;

	@Resource(name = "userService")
	private UserService userService;
	
	private Converter<GPSupportTicketModel, TopicOfInquiryData> gpTopicOfInquiryConverter;
	

	@Override
	public void populate(GPSupportTicketModel source,TicketData target) throws ConversionException {

		Assert.notNull(target, "Parameter target cannot be null.");
		Assert.notNull(source, "Parameter target cannot be null.");

	
		target.setOrderNumber(source.getOrderNumber());
		target.setFirstName(source.getFirstName()); 
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setId(source.getTicketID());
		target.setSfdcCaseNumber(source.getSfdcCaseNumber());
		target.setSfdcStatus(source.getSfdcStatus());
		target.setSfdcServiceAgent(source.getSfdcServiceAgent());
		target.setSfdcResolutionSummary(source.getSfdcResolutionSummary());
		target.setWebsiteURL(source.getWebsiteURL());
		target.setCreationDate(source.getCreationtime());
		target.setTicketComments(source.getTicketComments());
		target.setTopicOfInquiry(getGpTopicOfInquiryConverter().convert(source));
		target.setId(source.getTicketID());
		target.setMessage("Support Ticket created Successfully");
	}


	public Converter<GPSupportTicketModel, TopicOfInquiryData> getGpTopicOfInquiryConverter() {
		return gpTopicOfInquiryConverter;
	}


	public void setGpTopicOfInquiryConverter(Converter<GPSupportTicketModel, TopicOfInquiryData> gpTopicOfInquiryConverter) {
		this.gpTopicOfInquiryConverter = gpTopicOfInquiryConverter;
	}



}
