/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.core.cronjob;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.gp.commerce.core.model.GPDispenserKeyTicketModel;
import com.gp.commerce.core.model.GPTicketModel;
import com.gpintegration.service.GPTicketService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.gp.commerce.core.enums.GPNetsuiteOrderExportStatus;
import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.sfdc.ticket.AttachDetail;
import com.gpintegration.dto.sfdc.ticket.AttachmentDetails;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketAttachmentRequestDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketAttachmentResponseDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketRequestDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketResponseDTO;
import com.gpintegration.dto.sfdc.ticket.TicketDetails;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.impl.GPDefaultCRMTicketService;

import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

/**
 *
 * @author spandiyan
 *
 */
public class GPCRMTicketReplicationJob extends AbstractJobPerformable<CronJobModel> {

	private ConfigurationService configurationService;

	private SessionService sessionService;

	private GPDefaultCRMTicketService gpCRMTicketService;

	@Resource
	private GPTicketService gpTicketService;

	private static final Logger LOG = Logger.getLogger(GPCRMTicketReplicationJob.class);

	private static final String TICKETS_FOR_SFDC_REPLICAION_QUERY = "SELECT {st.pk} FROM {GPSupportTicket AS st} WHERE {st.caseReplicationStatus} = ?ticketReplicationStatus";

	private static final String TICKET_OR_ATTACHMENT_REPLICATION_STATUS = "200 OK";

	private static final String FOLDER_SYS_MASTER = "/sys_master/";

	@Override
	public PerformResult perform(CronJobModel cronjobModel) {

		LOG.info("CRM Ticket replication job started successfully.");

		if (clearAbortRequestedIfNeeded(cronjobModel))
		{
			LOG.debug("The job is aborted.");

			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		sendSupportTicketsToCRM();

		sendSDispenserTicketsToCRM();

		LOG.info("CRM Ticket replication job completed successfully.");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private void sendSupportTicketsToCRM(){

		List<GPSupportTicketModel> ticketModelList = getTicketsForReplication();

		try {

			if(CollectionUtils.isNotEmpty(ticketModelList)) {

				LOG.info("Total number of records matched for ticket and attachment replication:"+ticketModelList.size());

				for(GPSupportTicketModel ticketModel: ticketModelList) {


					replicateTicket(ticketModel);
				}
			} else {

				LOG.info("No tickets found for replication.");
			}
		} catch(GPIntegrationException e) {

			LOG.error("CRM Ticket or attachment replication failed with exception:", e);
		}
	}

	/**
	 *  Send dispenser tickets to SFDC
	 */
	private void sendSDispenserTicketsToCRM(){

		List<GPDispenserKeyTicketModel> ticketModelList = gpTicketService.getDispenserTicketsByCaseReplicationStatus( GPNetsuiteOrderExportStatus.NOTEXPORTED);

		if(CollectionUtils.isNotEmpty(ticketModelList)) {

			LOG.info("Total number of records matched for ticket replication: "+ticketModelList.size());

			ticketModelList.forEach(gpDispenserKeyTicketModel -> {

				try {

					replicateTicket(gpDispenserKeyTicketModel);

				}catch (Exception e){

					LOG.error("Could not replicate GPDispenserKeyTicket "+gpDispenserKeyTicketModel.getTicketID(), e);
				}
			});

		} else {

			LOG.info("No tickets found for replication.");
		}
	}


	public List<GPSupportTicketModel> getTicketsForReplication(){
		List<GPSupportTicketModel> ticketsForReplication = null;
		final FlexibleSearchQuery ticketsForReplicationSearch = new FlexibleSearchQuery(TICKETS_FOR_SFDC_REPLICAION_QUERY);
		ticketsForReplicationSearch.addQueryParameter("ticketReplicationStatus", GPNetsuiteOrderExportStatus.NOTEXPORTED);
		SearchResult<GPSupportTicketModel> ticketsSearchResult = getFlexibleSearchService().search(ticketsForReplicationSearch);
		if(ticketsSearchResult.getCount() > 0) {
			ticketsForReplication = ticketsSearchResult.getResult();
		}
		return ticketsForReplication;
	}

	public void replicateTicket(GPTicketModel ticketModel) throws GPIntegrationException {
		LOG.info("Replicating ticke to SFDC.");
		final int maxRetryCount = configurationService.getConfiguration().getInt("gp.tickets.attachment.replication.retry.count");
		int retryCount = ticketModel.getCaseRetryCount();
		boolean isTicketReplicated = false;
		GPCRMTicketResponseDTO ticketReplicationResponse = null;
		do {
			try {
				ticketReplicationResponse = gpCRMTicketService.replicateCRMTicket(createTicketReplicationRequest(ticketModel));
			} catch(GPIntegrationException e) {
				setTicketAndAttachmentReplicationStatus(ticketModel, null, null, GPNetsuiteOrderExportStatus.FAILURE, retryCount, true, "System error while processing.");
				LOG.error("Ticket replication failed with exception:", e);
			}
			retryCount++;
			if(null != ticketReplicationResponse) {
				LOG.info("Ticket replication response from SCPI:"+ticketReplicationResponse.toString());
				if(TICKET_OR_ATTACHMENT_REPLICATION_STATUS.equalsIgnoreCase(ticketReplicationResponse.getStatus())) {
					setTicketAndAttachmentReplicationStatus(ticketModel, ticketReplicationResponse, null, GPNetsuiteOrderExportStatus.EXPORTED, retryCount, true, null);
					isTicketReplicated = true;
				}

				if(isTicketReplicated) {
					break;
				}
			}
		} while(retryCount < maxRetryCount);

		if(null != ticketReplicationResponse) {
			if(TICKET_OR_ATTACHMENT_REPLICATION_STATUS.equalsIgnoreCase(ticketReplicationResponse.getStatus())) {
				if(CollectionUtils.isNotEmpty(ticketModel.getTicketAttachments())) {
					replicateTicketAttachment(ticketModel, ticketReplicationResponse.getCaseId());
				} else {
					LOG.info("Ticket doesn't have attachment - continuing with next record for replication.");
					return;
				}
			} else {
				if(retryCount < maxRetryCount) {
					setTicketAndAttachmentReplicationStatus(ticketModel, ticketReplicationResponse, null, GPNetsuiteOrderExportStatus.FAILURE, retryCount, true, null);
				} else {
					LOG.info("Not able to replciation tickets even after max retry count. Please replicate manually.");
					setTicketAndAttachmentReplicationStatus(ticketModel, ticketReplicationResponse, null, GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS, retryCount, true, null);
				}
			}
		}

	}

	private GPCRMTicketRequestDTO createTicketReplicationRequest(GPTicketModel ticketModel) {
		GPCRMTicketRequestDTO ticketRequest = new GPCRMTicketRequestDTO();
		TicketDetails ticketDetails = new TicketDetails();
		ticketDetails.setAddress1(ticketModel.getStreetAddress());
		ticketDetails.setCity(ticketModel.getCity());
		ticketDetails.setCountry(ticketModel.getCountry());
		if(null != ticketModel.getJobTitle()) {
			ticketDetails.setCustomerType(GpintegrationConstants.CUSTOMER_B2B);
		} else {
			ticketDetails.setCustomerType(GpintegrationConstants.CUSTOMER_B2C);
		}
		ticketDetails.setEmail(ticketModel.getEmail());
		ticketDetails.setFirstName(ticketModel.getFirstName());
		ticketDetails.setLastName(ticketModel.getLastName());
		ticketDetails.setPhoneNumber(ticketModel.getPhone());
		ticketDetails.setState(ticketModel.getRegion());
		ticketDetails.setTicketComments(ticketModel.getTicketComments());
		ticketDetails.setTicketNumber(ticketModel.getTicketID());
		ticketDetails.setTicketStatus(ticketModel.getState().getCode());

		ticketDetails.setWebsiteURL(ticketModel.getWebsiteURL());
		ticketDetails.setZipCode(ticketModel.getPostalCode());
		ticketRequest.setTicketDetails(ticketDetails);

		if(ticketModel instanceof GPSupportTicketModel){

			ticketDetails.setOrderNumber(((GPSupportTicketModel)ticketModel).getOrderNumber());
			ticketDetails.setTopicofEnquiry(((GPSupportTicketModel)ticketModel).getTopicOfInquiry());
		}

		if(ticketModel instanceof GPDispenserKeyTicketModel){

			ticketDetails.setKeyQuantities(((GPDispenserKeyTicketModel)ticketModel).getKeyQuantities());
			ticketDetails.setDispenserType(((GPDispenserKeyTicketModel)ticketModel).getDispenserType().getCode());
		}

		LOG.info("Ticket replication request:"+ticketRequest.toString());
		return ticketRequest;
	}

	public void replicateTicketAttachment(GPTicketModel ticketModel, String sfdcCaseId) throws GPIntegrationException {
		LOG.info("Ticket has an attachment(s) - replicating attachments to SFDC.");
		GPCRMTicketAttachmentResponseDTO attachmentReplicationRes = null;
		final int maxRetryCount = configurationService.getConfiguration().getInt("gp.tickets.attachment.replication.retry.count");;
		int retryCount = ticketModel.getAttachmentRetryCount();
		boolean isAttachmentReplicated = false;
		do {

			try {
				attachmentReplicationRes = gpCRMTicketService.replicateCRMTicketAttachment(createTicketAttachmentRequest(ticketModel.getTicketAttachments(), sfdcCaseId));
			} catch(GPIntegrationException e) {
				setTicketAndAttachmentReplicationStatus(ticketModel, null, null, GPNetsuiteOrderExportStatus.FAILURE, retryCount, false, "System error while processing.");
				LOG.error("Ticket attachment replication failed with exception:"+e);
			}
			retryCount++;
			if(null != attachmentReplicationRes) {
				LOG.info("Attachment replication response from SCPI:"+attachmentReplicationRes.toString());
				if(TICKET_OR_ATTACHMENT_REPLICATION_STATUS.equalsIgnoreCase(attachmentReplicationRes.getStatus())) {
					isAttachmentReplicated = true;
				}
			}

			if(isAttachmentReplicated) {
				break;
			}
		} while(retryCount < maxRetryCount);

		if(null != attachmentReplicationRes) {
			if(TICKET_OR_ATTACHMENT_REPLICATION_STATUS.equalsIgnoreCase(attachmentReplicationRes.getStatus())) {
				setTicketAndAttachmentReplicationStatus(ticketModel, null, attachmentReplicationRes, GPNetsuiteOrderExportStatus.EXPORTED, retryCount, false, null);
			} else {
				if(retryCount < maxRetryCount) {
					setTicketAndAttachmentReplicationStatus(ticketModel, null, attachmentReplicationRes, GPNetsuiteOrderExportStatus.FAILURE, retryCount, false, null);
				} else {
					setTicketAndAttachmentReplicationStatus(ticketModel, null, attachmentReplicationRes, GPNetsuiteOrderExportStatus.NOT_ABLE_TO_PROCESS, retryCount, false, null);
				}
			}
		}
	}

	private GPCRMTicketAttachmentRequestDTO createTicketAttachmentRequest(List<CommentAttachmentModel> attachmentModelList, String sfdcCaseId) {
		String serverMediaPath = configurationService.getConfiguration().getString("media.read.dir")+FOLDER_SYS_MASTER;
		GPCRMTicketAttachmentRequestDTO attachmentRequest = new GPCRMTicketAttachmentRequestDTO();
		AttachmentDetails attachmentDetail = new AttachmentDetails();
		attachmentDetail.setSourceId(sfdcCaseId);
		List<AttachDetail> attachDetailList = new ArrayList<>();
		try {
			for(CommentAttachmentModel attachmentModel: attachmentModelList) {
				AttachDetail attachDetails = new AttachDetail();
				attachDetails.setAttachmentID(attachmentModel.getItem().getProperty(GpintegrationConstants.TICKET_ATTACHMENT_FILE_NAME));
				attachDetails.setAttachmentType(attachmentModel.getItem().getProperty(GpintegrationConstants.TICKET_ATTACHMENT_FILE_MIME));
				LOG.info("Ticket attachment file location:"+attachmentModel.getItem().getProperty(GpintegrationConstants.TICKET_ATTACHMENT_FILE_LOCATION));
				attachDetails.setAttachmentFile(getBase64String(serverMediaPath, attachmentModel.getItem().getProperty(GpintegrationConstants.TICKET_ATTACHMENT_FILE_LOCATION)));
				attachDetailList.add(attachDetails);
			}
		} catch(Exception e) {
			LOG.error("Execption occurred while creating ticket attachment request:",e);
		}
		attachmentDetail.setAttachDetail(attachDetailList);
		attachmentRequest.setAttachmentDetails(attachmentDetail);
		LOG.info("Attachment request DTO:"+attachmentRequest.toString());
		return attachmentRequest;
	}

	private String getBase64String(String serverMediaPath, String attachmentFilePath){
		String base64 = "";
		File attachmentFile = new File(serverMediaPath+attachmentFilePath);
		try {
			byte[] attachmentAsByteArray = FileUtils.readFileToByteArray(attachmentFile);
			base64 = Base64.getEncoder().encodeToString(attachmentAsByteArray);
		} catch(Exception exception) {
			LOG.error("Base64 conversion for ticket attachment failed with exception:",exception);
		}
		return base64;
	}

	private void setTicketAndAttachmentReplicationStatus(GPTicketModel ticketModel, GPCRMTicketResponseDTO ticketResponse, GPCRMTicketAttachmentResponseDTO attachmentResponse, GPNetsuiteOrderExportStatus ticketAndAttachmentRepStatus, int retryCount, boolean isTicektReplication, String exceptionMessage) {
		if(null != ticketResponse) {
			ticketModel.setCaseRetryCount(retryCount);
			ticketModel.setSfdcCaseId(ticketResponse.getCaseId());
			ticketModel.setSfdcCaseNumber(ticketResponse.getCaseNumber());
			ticketModel.setCaseReplicationStatus(ticketAndAttachmentRepStatus);
		}

		if(null != attachmentResponse){
			ticketModel.setAttachmentRetryCount(retryCount);
			ticketModel.setAttachmentStatusMessage(attachmentResponse.getMessage());
			ticketModel.setAttachmentReplicationStatus(ticketAndAttachmentRepStatus);
		}

		if(null != exceptionMessage) {
			if(isTicektReplication) {
				ticketModel.setCaseReplicationStatus(ticketAndAttachmentRepStatus);
				ticketModel.setCaseRetryCount(retryCount);
			} else {
				ticketModel.setAttachmentReplicationStatus(ticketAndAttachmentRepStatus);
				ticketModel.setAttachmentRetryCount(retryCount);
			}
			ticketModel.setSfdcError(exceptionMessage);
		}
		modelService.save(ticketModel);
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public GPDefaultCRMTicketService getGpCRMTicketService() {
		return gpCRMTicketService;
	}

	public void setGpCRMTicketService(GPDefaultCRMTicketService gpCRMTicketService) {
		this.gpCRMTicketService = gpCRMTicketService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
	@Override
	public boolean isAbortable() {
		return true;
	}

}