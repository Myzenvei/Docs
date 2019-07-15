/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.ticket.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.model.GPSupportTicketModel;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 * This interface is used for GP Support Ticket service
 */
public interface GPSupportTicketService {

	/**
	 * Saves the Ticket attachments
	 * 
	 * @param files        the multipart files
	 * @param ticketNumber the ticket number
	 */
	void saveTicketAttachment(List<MultipartFile> files, String ticketNumber);
	/**
	 * Fetches the ticket details for the given user, base site, and sort code
	 * 
	 * @param currentUser     the user
	 * @param currentBaseSite the base site
	 * @param sortcode        the sort code
	 * @return list of {@link GPSupportTicketModel}s
	 */
	List<GPSupportTicketModel> getTicketDetails(UserModel currentUser, BaseSiteModel currentBaseSite, String sortcode);

	/**
	 * Fetches the ticket details for B2b for the given user, base site, b2b unit and sort code
	 * 
	 * @param currentUser     the user
	 * @param currentBaseSite the base site
	 * @param b2bunit         the b2b unit
	 * @param sortcode        the sort code
	 * @return list of {@link GPSupportTicketModel}s
	 */
	List<GPSupportTicketModel> getTicketDetailsForB2B(UserModel currentUser, BaseSiteModel currentBaseSite,
			String b2bunit, String sortcode);

	 
}
