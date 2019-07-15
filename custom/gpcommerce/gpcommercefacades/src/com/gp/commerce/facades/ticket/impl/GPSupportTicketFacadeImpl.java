/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.ticket.impl;
 
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.core.ticket.services.GPSupportTicketService;
import com.gp.commerce.facades.populators.GPSupportTicketPopulator;
import com.gp.commerce.facades.ticket.GPSupportTicketFacade;
import com.gpintegration.service.impl.GPDefaultCRMTicketService;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.data.TicketListData;
import org.apache.commons.collections4.CollectionUtils ;

public class GPSupportTicketFacadeImpl implements GPSupportTicketFacade {

	protected static final Logger LOG = Logger.getLogger(GPSupportTicketFacadeImpl.class);
	private GPSupportTicketService gpSupportTicketService;
	private Converter<TicketData, GPSupportTicketModel> gpSupportTicketReverseConverter;
	private Converter<GPSupportTicketModel, TicketData> gpSupportTicketConverter;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private GPDefaultCRMTicketService gpCRMTicketService;
	@Resource
	private GPSupportTicketPopulator gpSupportTicketPopulator;

	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.ticket.GPSupportTicketFacade#createTicket(de.hybris.platform.customerticketingfacades.data.TicketData)
	 */
	@Override
	public TicketData createTicket(TicketData ticketData) {
		try{
		GPSupportTicketModel ticket = getGpSupportTicketReverseConverter().convert(ticketData);
		modelService.save(ticket);
		gpSupportTicketPopulator.populate(ticket, ticketData);
		}catch(ModelSavingException e) {
			LOG.error("Error while creating support ticket"+e.getMessage(),e);
		}
		return ticketData;

	}

	/* (non-Javadoc)
	 * @see com.gp.commerce.facades.ticket.GPSupportTicketFacade#saveAttachment(java.util.List, java.lang.String)
	 */
	@Override
	public void saveAttachment(List<MultipartFile> attachments, String ticketNumber) {
		getGpSupportTicketService().saveTicketAttachment(attachments, ticketNumber);
	}
	
	@Override
	public TicketListData getTicketDetails(String sortcode) {
		TicketListData ticketDataList =new TicketListData();
		gpCRMTicketService.updateCaseDetails(null);
		 List<GPSupportTicketModel> ticketListModel =getGpSupportTicketService().getTicketDetails(getUserService().getCurrentUser(), getBaseSiteService().getCurrentBaseSite(), sortcode);
		if(CollectionUtils.isNotEmpty(ticketListModel)) {
			ticketDataList.setTickets(getGpSupportTicketConverter().convertAll(ticketListModel));
		}
		 return ticketDataList ;
	}
	
	@Override	
	public TicketListData getTicketDetailsForB2B(String b2bunit,String sortcode) {
		TicketListData ticketDataList =new TicketListData();
		 gpCRMTicketService.updateCaseDetails(b2bunit);
		 List<GPSupportTicketModel> ticketListModel =getGpSupportTicketService().getTicketDetailsForB2B(getUserService().getCurrentUser(), getBaseSiteService().getCurrentBaseSite(),b2bunit,sortcode);
			if(CollectionUtils.isNotEmpty(ticketListModel)) {
				ticketDataList.setTickets(getGpSupportTicketConverter().convertAll(ticketListModel));
			}
		 return ticketDataList ;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	public Converter<TicketData, GPSupportTicketModel> getGpSupportTicketReverseConverter() {
		return gpSupportTicketReverseConverter;
	}

	public void setGpSupportTicketReverseConverter(
			Converter<TicketData, GPSupportTicketModel> gpSupportTicketReverseConverter) {
		this.gpSupportTicketReverseConverter = gpSupportTicketReverseConverter;
	}

	public Converter<GPSupportTicketModel, TicketData> getGpSupportTicketConverter() {
		return gpSupportTicketConverter;
	}

	public void setGpSupportTicketConverter(Converter<GPSupportTicketModel, TicketData> gpSupportTicketConverter) {
		this.gpSupportTicketConverter = gpSupportTicketConverter;
	}

	public GPSupportTicketService getGpSupportTicketService() {
		return gpSupportTicketService;
	}

	public void setGpSupportTicketService(GPSupportTicketService gpSupportTicketService) {
		this.gpSupportTicketService = gpSupportTicketService;
	}

	/**
	 * @return the gpCRMTicketService
	 */
	public GPDefaultCRMTicketService getGpCRMTicketService() {
		return gpCRMTicketService;
	}

	/**
	 * @param gpCRMTicketService the gpCRMTicketService to set
	 */
	public void setGpCRMTicketService(GPDefaultCRMTicketService gpCRMTicketService) {
		this.gpCRMTicketService = gpCRMTicketService;
	}

	
 

}
