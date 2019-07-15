/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.service;

import com.gpintegration.dto.sfdc.ticket.GPCRMTicketAttachmentRequestDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketAttachmentResponseDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketRequestDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketResponseDTO;
import com.gpintegration.exception.GPIntegrationException;

/**
 * The Interface GPCRMTicketService.
 *
 * @author spandiyan
 */
public interface GPCRMTicketService {

	/**
	 * Replicate CRM ticket.
	 *
	 * @param crmTicketRequest the crm ticket request
	 * @return the GPCRM ticket response DTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	public GPCRMTicketResponseDTO replicateCRMTicket(GPCRMTicketRequestDTO crmTicketRequest) throws GPIntegrationException;
	
	/**
	 * Replicate CRM ticket attachment.
	 *
	 * @param ticketAttachmentRequest the ticket attachment request
	 * @return the GPCRM ticket attachment response DTO
	 * @throws GPIntegrationException the GP integration exception
	 */
	public GPCRMTicketAttachmentResponseDTO replicateCRMTicketAttachment(GPCRMTicketAttachmentRequestDTO ticketAttachmentRequest) throws GPIntegrationException;
	
	/**
	 * Update case details.
	 *
	 * @param b2bUnit the b 2 b unit
	 */
	public void updateCaseDetails(String b2bUnit);
}
