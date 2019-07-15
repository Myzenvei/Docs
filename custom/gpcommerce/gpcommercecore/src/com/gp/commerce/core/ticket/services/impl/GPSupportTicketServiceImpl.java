/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.ticket.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.core.ticket.dao.GPSupportTicketDao;
import com.gp.commerce.core.ticket.services.GPSupportTicketService;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;
import de.hybris.platform.ticket.service.impl.DefaultTicketBusinessService;

public class GPSupportTicketServiceImpl  extends DefaultTicketBusinessService implements  GPSupportTicketService{

	private GPSupportTicketDao  gpSupportTicketDao ;
	@Resource
	private TicketService ticketService;
	
protected static final Logger LOG = Logger.getLogger(GPSupportTicketServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.gp.commerce.core.ticket.services.GPSupportTicketService#saveTicketAttachment(java.util.List, java.lang.String)
	 */
	@Override
	public void saveTicketAttachment(List<MultipartFile> files,String ticketNumber) {
		GPSupportTicketModel ticket = (GPSupportTicketModel) getTicketService().getTicketForTicketId(ticketNumber);
		List<CommentAttachmentModel> attachments = new ArrayList<>();
		if (files != null && !files.isEmpty()) {
			Iterator<MultipartFile> creationEvent = files.iterator();
			while (creationEvent.hasNext()) {
				MultipartFile event = (MultipartFile) creationEvent.next();
				try {
					CommentAttachmentModel e = (CommentAttachmentModel) this.getModelService()
							.create(CommentAttachmentModel.class);
					e.setItem(this.getTicketAttachmentsService().createAttachment(event.getOriginalFilename(),
							event.getContentType(), event.getBytes(), this.getUserService().getCurrentUser()));
					attachments.add(e);
					ticket.setTicketAttachments(attachments);
				} catch (IOException arg5) {
					LOG.error(arg5.getMessage(), arg5);
				}
			}
		}
		getModelService().save(ticket);
	
	}

	
	public List<GPSupportTicketModel> getTicketDetails(UserModel currentUser, BaseSiteModel currentBaseSite,String sortcode){
		return getGpSupportTicketDao().getTicketDetails(currentUser,currentBaseSite,sortcode) ;
		
	}
	
	public List<GPSupportTicketModel> getTicketDetailsForB2B(UserModel currentUser, BaseSiteModel currentBaseSite,
			String b2bunit,String sortcode){
		return getGpSupportTicketDao().getTicketDetailsForB2B(currentUser,currentBaseSite,b2bunit,sortcode) ;
	}
 

	public GPSupportTicketDao getGpSupportTicketDao() {
		return gpSupportTicketDao;
	}

	public void setGpSupportTicketDao(GPSupportTicketDao gpSupportTicketDao) {
		this.gpSupportTicketDao = gpSupportTicketDao;
	}



	public TicketService getTicketService() {
		return ticketService;
	}



	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	 
}


