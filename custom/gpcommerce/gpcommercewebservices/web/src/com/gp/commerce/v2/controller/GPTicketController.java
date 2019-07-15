/*
O * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.v2.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.gp.commerce.core.exceptions.GPTicketException;
import com.gp.commerce.data.ticket.GPDispenserTicketData;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;
import com.gp.commerce.facades.ticket.GPTicketFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.dto.TicketListWsDTO;
import com.gp.commerce.dto.TopicOfInquiryWsDTO;
import com.gp.commerce.dto.ticket.GPDispenserTicketWSDTO;
import com.gp.commerce.facades.ticket.GPSupportTicketFacade;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.data.TicketListData;
import de.hybris.platform.customerticketingfacades.data.TicketWsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "Support Ticket")
@RequestMapping(value = "/{baseSiteId}/ticket")
public class GPTicketController extends BaseCommerceController {

	private final Logger LOG = Logger.getLogger(GPTicketController.class);

	@Resource(name = "gpSupportTicketFacade")
	private GPSupportTicketFacade ticketFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Resource
	private ConfigurationService configurationService;

	@Resource
	private GPTicketFacade gpTicketFacade;

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/createticket", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = " Creates a ticket", notes = "Creates a customer support Ticket. The attributes has ticket details")
	public TicketWsDTO addSupportTicket(
			@ApiParam(value = "Ticket's object", required = true) @RequestBody final TicketWsDTO ticket,final HttpServletRequest httpRequest)
			throws UnsupportedEncodingException {
		CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled()) {
			LOG.debug("addTicket: userId=" + customer.getUid());
		}
		TicketData ticketData = new TicketData();
		getDataMapper().map(ticket, ticketData, "topicOfInquiry,associatedTo,ticketComments,attachments", true);
		customer = customerFacade.getUserForUID(customer.getUid());
		if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
			ticketData.setCustomerId(customer.getCustomerId());
		}
		ticket.setWebsiteURL(httpRequest.getServerName());
		getDataMapper().map(ticket, ticketData,
				"firstName,lastName,email,phone,jobTitle,companyName,country,streetAddress,orderNumber,city,region,postalCode,websiteURL",
				true);
		TicketData supporTicket = ticketFacade.createTicket(ticketData);
		return getDataMapper().map(supporTicket, TicketWsDTO.class);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/saveTicketAttachment", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = " Saves the attachment to  ticket", notes = "Save the attachment attached in the ticket")
	public void saveTicketAttachments(
			@ApiParam(value = "file", required = true) @RequestParam("file") final List<MultipartFile> files,
			@RequestParam(required = true) final String ticketNumber) throws UnsupportedEncodingException {
		ticketFacade.saveAttachment(files, ticketNumber);
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/getTicketDetails", method = RequestMethod.GET)
	@ApiOperation(value = "Returns the ticket created by customer", notes = "Save the attachment attached in the ticket")
	@ResponseBody
	public TicketListWsDTO getTicketDetails(@ApiParam(value = "Response configuration (list of tickets, should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") 
	@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,@RequestParam (required = false) final String sortType) throws Exception {
		TicketListData ticketDataList =ticketFacade.getTicketDetails(sortType);
		return getDataMapper().map(ticketDataList, TicketListWsDTO.class);
	}
	

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/{b2bunit}/getTicketDetails", method = RequestMethod.GET)
	@ApiOperation(value = "Returns the ticket created by customer", notes = "Save the attachment attached in the ticket")
	@ResponseBody
	public TicketListWsDTO getTicketDetailsForB2B(@ApiParam(value = "Response configuration (list of tickets, should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") 
	@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields ,@PathVariable final String b2bunit,@RequestParam (required = false)  final String sortType) throws Exception {
		TicketListData ticketDataList =ticketFacade.getTicketDetailsForB2B(b2bunit,sortType);
		return getDataMapper().map(ticketDataList, TicketListWsDTO.class);
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/topicOfInquiry", method = RequestMethod.GET)
	@ApiOperation(value = "Returns the ticket created by customer", notes = "Save the attachment attached in the ticket")
	@ResponseBody
	public TopicOfInquiryWsDTO getTopicOfInquiry(@ApiParam(value = "Response configuration (list of tickets, should be returned in response)", allowableValues = "BASIC, DEFAULT, FULL") 
	@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws Exception {
		
		Map<String, String> topicOfInquiryMap = cmsSiteService.getTopicOfInquiry();
		TopicOfInquiryWsDTO topicOfInquiryDTO= new TopicOfInquiryWsDTO();
		topicOfInquiryDTO.setTopicOfInquiry(topicOfInquiryMap);
		return topicOfInquiryDTO;
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/getdispenserticketparams", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " Get Dispenser enum values ", notes = "Returns list of values supported by Dispenser enum type")
	public GPDispenserTicketWSDTO getDispenserTicketParams() throws GPTicketException
	{
		try{

			GPDispenserTicketData dispenserTicketData = gpTicketFacade.getDispenserTicketValues();
			return getDataMapper().map(dispenserTicketData, GPDispenserTicketWSDTO.class);

		}catch (Exception e){

			LOG.error("Exception in getDispenserTicketParams "+e.getMessage(), e);

			throw new GPTicketException(" Exception getting DispenserTicketParams ",e.getMessage());
		}

	}


	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/createdispenserticket", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = " Creates a ticket", notes = "Creates a customer dispenser Ticket. The attributes has ticket details")
	public String createDispenserTicket(@ApiParam(value = "Ticket's object", required = true) @RequestBody final TicketWsDTO ticketWsDTO,final HttpServletRequest httpRequest,
										 @RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws GPTicketException
	{
		TicketData ticketData= getDataMapper().map(ticketWsDTO,TicketData.class,fields);
		CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled()) {
			LOG.debug("addTicket: userId=" + customer.getUid());
		}
		customer = customerFacade.getUserForUID(customer.getUid());
		if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
			ticketData.setCustomerId(customer.getCustomerId());
		}
		ticketWsDTO.setWebsiteURL(httpRequest.getServerName());
		if(!gpTicketFacade.createGPDispenserTicket(ticketData)){

			throw new GPTicketException("Exception creating  GPDispenserKeyTicket",configurationService.getConfiguration().getString(GpcommerceFacadesConstants.DISPENSER_TICKET_FAILURE));
		}

		return configurationService.getConfiguration().getString(GpcommerceFacadesConstants.DISPENSER_TICKET_SUCCESS);
	}
}
