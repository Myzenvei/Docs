/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import javax.annotation.Resource;

import com.gp.commerce.core.enums.DispenserTypeEnum;
import com.gp.commerce.core.model.GPDispenserKeyTicketModel;
import com.gp.commerce.core.model.GPTicketModel;
import de.hybris.platform.enumeration.EnumerationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.facades.data.TopicOfInquiryData;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.service.TicketService;

public class GPTicketReversePopulator implements Populator<TicketData, GPTicketModel> {

	private static final String HEADLINE = "Support Ticket";
	private static final String CASE_NUMBER = "ID in Process";
	private static final String STATUS = "Created";
	private static final String OWNER = "N/A";

	@Resource(name = "ticketService")
	private TicketService ticketService;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;



	@Override
	public void populate(TicketData source, GPTicketModel target) throws ConversionException {


		Assert.notNull(target, "Parameter target cannot be null.");
		Assert.notNull(source, "Parameter target cannot be null.");


		target.setOrder(ticketService.getAssociatedObject(source.getAssociatedTo(), null, null));
		target.setTicketComments(source.getTicketComments());
		target.setPriority(CsTicketPriority.MEDIUM);
		target.setCategory(CsTicketCategory.ENQUIRY);
		target.setHeadline(HEADLINE);
		if (StringUtils.isNotBlank(source.getCustomerId())) {
			target.setCustomer(userService.getCurrentUser());
		}
		if (StringUtils.isNotEmpty(source.getCompanyName()) && StringUtils.isNotEmpty(source.getJobTitle())) {
			target.setCompanyName(source.getCompanyName());
			target.setJobTitle(source.getJobTitle());
		}
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setPhone(source.getPhone());

		target.setCountry(source.getCountry());
		target.setStreetAddress(source.getStreetAddress());
		target.setCity(source.getCity());
		target.setRegion(source.getRegion());
		target.setPostalCode(source.getPostalCode());
		target.setBaseSite(getBaseSiteService().getCurrentBaseSite());
		target.setWebsiteURL(source.getWebsiteURL());
		target.setSfdcStatus(STATUS);
		target.setSfdcCaseNumber(CASE_NUMBER);
		target.setSfdcServiceAgent(OWNER);

		if (target instanceof GPSupportTicketModel) {

			GPSupportTicketModel supportTicketModel=(GPSupportTicketModel) target;

			populateContactDetails(source,supportTicketModel);
		}

		if (target instanceof GPDispenserKeyTicketModel) {

			GPDispenserKeyTicketModel gpDispenserKeyTicketModel=(GPDispenserKeyTicketModel) target;

			populateDispenserDetails(source,gpDispenserKeyTicketModel);
		}

	}

	private void populateContactDetails(TicketData source, GPSupportTicketModel target){

		target.setOrderNumber(source.getOrderNumber());

		target.setTopicOfInquiryValue(source.getTopicOfInquiry().getValue());

		target.setTopicOfInquiry(source.getTopicOfInquiry().getKey());
	}

	private void populateDispenserDetails(TicketData source, GPDispenserKeyTicketModel target){

		DispenserTypeEnum dispenserTypeEnum=enumerationService.getEnumerationValue(DispenserTypeEnum.class,source.getDispenserType());

		target.setDispenserType(dispenserTypeEnum);

		target.setKeyQuantities(source.getKeyQuantities());

	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

}
