/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.ticket;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.data.TicketListData;

/**
 * The Interface GPSupportTicketFacade.
 */
public interface GPSupportTicketFacade {

	/**
	 * Creates the ticket.
	 *
	 * @param ticketData the ticket data
	 * @return the ticket data
	 */
	TicketData createTicket(TicketData ticketData);

	/**
	 * Save attachment.
	 *
	 * @param files the files
	 * @param ticketNumber the ticket number
	 */
	void saveAttachment(List<MultipartFile> files, String ticketNumber);

	/**
	 * Gets the ticket details for B 2 B.
	 *
	 * @param b2bunit the b 2 bunit
	 * @param sort the sort
	 * @return the ticket details for B 2 B
	 */
	TicketListData getTicketDetailsForB2B(String b2bunit, String sort);

	/**
	 * Gets the ticket details.
	 *
	 * @param sortcode the sortcode
	 * @return the ticket details
	 */
	TicketListData getTicketDetails(String sortcode);

}
