/* 
	 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gp.commerce.core.model.GPSupportTicketModel;
import com.gp.commerce.core.ticket.dao.GPSupportTicketDao;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.crm.ticket.GPCaseDTO;
import com.gpintegration.dto.crm.ticket.GPCaseDetailsDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketAttachmentRequestDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketAttachmentResponseDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketRequestDTO;
import com.gpintegration.dto.sfdc.ticket.GPCRMTicketResponseDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPCRMTicketService;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

/**
 * The Class GPDefaultCRMTicketService.
 *
 * @author spandiyan
 */
public class GPDefaultCRMTicketService implements GPCRMTicketService{

	private ConfigurationService configurationService;

	private GPCRMService gpCRMService;

	private GPSupportTicketDao  gpSupportTicketDao ;

	@Resource
	private UserService userService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private ModelService modelService;


	private static final String CRM_GET_CASE_URL = "gp.scpi.getcasedetails.url";

	private static final String CASE_NUMBER = "(\"CaseNumber\":\"";

	private static final String CASE_HEADER = "caseHeader";

	private static final String RIGHT_PARANTHESIS = ")";

	private static final String LEFT_PARANTHESIS = "(";

	private static final String SOURCE_APP= "\"sourceapp\":\"";

	private static final String CRM_TICKET_SOURCE_APP_VALUE= "crm.ticket.source.app";

	public static final String CRM_GET_CASE_MAX_COUNT = "crm.get.case.max.count";

	public static final Integer DEFAULT_MAX_COUNT = 50;

	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultCRMTicketService.class);


	@Override
	public GPCRMTicketResponseDTO replicateCRMTicket(GPCRMTicketRequestDTO crmTicketRequest) throws GPIntegrationException {
		GPCRMTicketResponseDTO ticketCreationResponse = null;
		URI ticketReplicationURI;
		String ticketReplicationEndpoint = configurationService.getConfiguration().getString("gp.tickets.replication.url");
		try {
			ticketReplicationURI = new URI(ticketReplicationEndpoint);
		} catch (URISyntaxException uriException) {
			throw new GPIntegrationException ("Invalid ticket creation URL:", uriException);
		}

		final HttpHeaders scpiHeaders = gpCRMService.getSCPIHeaders();
		final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		final RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		restTemplate.getMessageConverters().add(getJacksonSupportsMoreTypes());
		RequestEntity<GPCRMTicketRequestDTO> ticketCreationReq = new RequestEntity<GPCRMTicketRequestDTO>(crmTicketRequest,scpiHeaders, HttpMethod.POST, ticketReplicationURI);
		ResponseEntity<GPCRMTicketResponseDTO> ticketCreationResponseEntity = null;
		try {
			ticketCreationResponseEntity = restTemplate.exchange(ticketCreationReq, GPCRMTicketResponseDTO.class);
			if(null != ticketCreationResponseEntity) {
				ticketCreationResponse = ticketCreationResponseEntity.getBody();
			}
		} catch(HttpClientErrorException httpClientException) {
			LOG.error("Error occurred in ticket replication call: {}",httpClientException);
			throw new GPIntegrationException("Ticket replication failed with exception:"+httpClientException.getMessage());
		} catch(Exception exception) {
			LOG.error("Error occurred in ticket replication call: {} ",exception);
			throw new GPIntegrationException("Ticket replication failed with exception:"+exception.getMessage());
		}
		return ticketCreationResponse;
	}

	@Override
	public GPCRMTicketAttachmentResponseDTO replicateCRMTicketAttachment(GPCRMTicketAttachmentRequestDTO ticketAttachmentRequest)
			throws GPIntegrationException {
		GPCRMTicketAttachmentResponseDTO attachmentResponse = null;
		String attachmentReplicationEndpoint = configurationService.getConfiguration().getString("gp.tickets.attachment.replication.url"); 
		URI attachmentReplicationURI = null;

		try {
			attachmentReplicationURI = new URI(attachmentReplicationEndpoint);
		} catch(URISyntaxException uriException) {
			throw new GPIntegrationException("Invalid attachment replication URL:", uriException);
		}

		final HttpHeaders scpiHeaders = gpCRMService.getSCPIHeaders();
		final ClientHttpRequestFactory httpRequestFactory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		final RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		restTemplate.getMessageConverters().add(getJacksonSupportsMoreTypes());
		RequestEntity<GPCRMTicketAttachmentRequestDTO> attachmentRequest = new RequestEntity<GPCRMTicketAttachmentRequestDTO>(ticketAttachmentRequest, scpiHeaders, HttpMethod.POST, attachmentReplicationURI);
		ResponseEntity<GPCRMTicketAttachmentResponseDTO> attachmentResponseEntity = null;
		try {
			attachmentResponseEntity = restTemplate.exchange(attachmentRequest, GPCRMTicketAttachmentResponseDTO.class);
			if(null != attachmentResponseEntity) {
				attachmentResponse = attachmentResponseEntity.getBody();
			}
		}catch(HttpClientErrorException httpClientException) {
			LOG.error("Error occurred in attachment replication call: {} ",httpClientException);
			throw new GPIntegrationException("Attachment replicaiton failed with exception:"+httpClientException.getMessage());
		} catch(Exception exception) {
			LOG.error("Error occurred in attachment replication call: {}",exception);
			throw new GPIntegrationException("TAttachment replicaiton failed with exception:"+exception.getMessage());
		}
		return attachmentResponse;
	}

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter getJacksonSupportsMoreTypes() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(MediaType.parseMediaType(GpintegrationConstants.HTTP_MIME_TYPE_APPLICATION_JSON), MediaType.APPLICATION_OCTET_STREAM));
		return converter;
	}

	/**
	 * Method to update case details from SFDC to Hybris for the current user
	 */
	@Override
	public void updateCaseDetails(String b2bUnit) {
		GPCaseDetailsDTO caseDetails = null;
		final String currentUser = getUserService().getCurrentUser().getUid();
		if(configurationService.getConfiguration().getBoolean(GpintegrationConstants.CRM_SFDC_TICKET_FLAG)) {
			try {
				final List<GPSupportTicketModel> supportTicketDetails = getSupportTicketsForUser(b2bUnit);
				final List<String> customerCases = getCaseNumbers(supportTicketDetails,currentUser);
				final Integer maxCaseCount = configurationService.getConfiguration().getInteger(CRM_GET_CASE_MAX_COUNT,DEFAULT_MAX_COUNT);
				//Filter cases to a max quantity to avoid limit of get request query string length
				if(CollectionUtils.isNotEmpty(customerCases)) {
					List<String> caseList = customerCases.size() > maxCaseCount ? customerCases.stream().limit(maxCaseCount).collect(Collectors.toList()) : customerCases;
					if(customerCases.size() > maxCaseCount) {
						LOG.warn(" Unable to process {} cases due to size limit" , customerCases.size()-maxCaseCount );
					}
					caseDetails = getCaseDetailsFromSFDC( caseList);
					processCaseDetails(supportTicketDetails,caseList,caseDetails);
				} else {
					LOG.warn(" No SFDC Cases found for user {}",currentUser);
				}
			}catch(GPIntegrationException e) {
				LOG.error(" Error in getting case details for user {} {} {}" +  currentUser, e.getCause().getMessage(),e);
			}catch(Exception e) {
				LOG.error("  Error in getting case details for user{} {} {} " , currentUser , e.getCause().getMessage(),e);
			}
		} else {
			LOG.debug(" CRM Tickets  is disabled");
		}

	}
	
	/**
	 * Method to get the support Ticket details for the user
	 * @param b2bunit , b2b unit of the user if applicable
	 * @return List<GPSupportTicketModel>, list of GPSupportTicketModel for the user
	 */
	private List<GPSupportTicketModel>  getSupportTicketsForUser(final String b2bunit) {
		if(StringUtils.isNotEmpty(b2bunit)) {
			 return getGpSupportTicketDao().getTicketDetailsForB2B(getUserService().getCurrentUser(), getBaseSiteService().getCurrentBaseSite(), b2bunit,null);
		} else {
			 return getGpSupportTicketDao().getTicketDetails(getUserService().getCurrentUser(), getBaseSiteService().getCurrentBaseSite(),null);
		}
	}

	/**
	 * Method to get the case numbers for the support ticket 
	 * @param supportTicketDetails  , List of Support ticket details
	 * @param currentUser, current user uid
	 * @return List<String> containing the SFDC case numbers for the given support tickets
	 */
	private List<String> getCaseNumbers(final List<GPSupportTicketModel> supportTicketDetails,final String currentUser){
		final List<String> caseNumbers = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(supportTicketDetails)) {
			supportTicketDetails.forEach(supportTicket ->{
				if(StringUtils.isNotEmpty(supportTicket.getSfdcCaseNumber())){
					caseNumbers.add(supportTicket.getSfdcCaseNumber()) ;
				}
			});
		} else {
				LOG.warn(" No support ticket available for user {} ",currentUser);
		}
		LOG.debug(" Case numbers {}", caseNumbers );
		return caseNumbers;
	}

	/**
	 * Method to process the case information received from SFDC 
	 * @param supportTicketDetails, List of Support tickets which has to be updated
	 * @param caseNumbers, List of case numbers which has to be updated
	 * @param caseDetails , Case Details for the given cases from SDFC
	 */
	private void processCaseDetails(final List<GPSupportTicketModel> supportTicketDetails,List<String> caseNumbers, GPCaseDetailsDTO caseDetails) {
		Map<String,GPCaseDTO> caseDetailMap = new HashMap<>();
		if(caseDetails != null && CollectionUtils.isNotEmpty(caseDetails.getCases())) {
			caseDetails.getCases().forEach(caseDetail -> 
			caseDetailMap.computeIfAbsent(caseDetail.getCaseNumber(), k->caseDetail));
			LOG.debug(" Added case detail {} " , caseDetailMap);
			updateCaseDetails(supportTicketDetails, caseDetailMap);
		} else {
			LOG.error("Unable to get details from SFDC for cases {} for user {}" , caseNumbers,getUserService().getCurrentUser().getUid() );
		}
	}

	/**
	 * Method to update the case details from SFDC to Hybris tickets
	 * @param supportTicketDetails, List containing support tickets for the user
	 * @param caseDetailMap , Map<String , GPCaseDTO> containing the details of each case number
	 */
	private void updateCaseDetails(final List<GPSupportTicketModel> supportTicketDetails ,Map<String,GPCaseDTO> caseDetailMap) {
		supportTicketDetails.forEach(supportTicket -> {
			final String caseNumber = supportTicket.getSfdcCaseNumber();
			if(caseDetailMap.containsKey(caseNumber)) {
				GPCaseDTO caseDetail = caseDetailMap.get(caseNumber);
				supportTicket.setSfdcStatus(caseDetail.getStatus());
				supportTicket.setSfdcServiceAgent(caseDetail.getOwnerName());
				supportTicket.setSfdcResolutionSummary(caseDetail.getResolutionSummary());
				modelService.save(supportTicket);
			}
		});


	}

	/**
	 * Method that retrieves case details from SFDC
	 * @param caseNumbers, List<String> containing casenumbers
	 * @return GPCaseDetailsDTO
	 * @throws GPIntegrationException
	 */
	private GPCaseDetailsDTO getCaseDetailsFromSFDC(List<String> caseNumbers) throws GPIntegrationException {
		GPCaseDetailsDTO response = null;
		try {
			final HttpHeaders headers = gpCRMService.getSCPIHeaders();
			final HttpEntity<?> requestEntity = new HttpEntity<>( headers);
			final MultiValueMap <String, String> paramMap = new LinkedMultiValueMap<>();
			StringBuilder paramValue = new StringBuilder(CASE_NUMBER);
			paramValue.append(String.join(GpintegrationConstants.COMMA_SYMBOL, caseNumbers));
			paramValue.append(GpintegrationConstants.QUOTE_SYMBOL).append(GpintegrationConstants.COMMA_SYMBOL);
			paramValue.append(SOURCE_APP).append(configurationService.getConfiguration().getString(CRM_TICKET_SOURCE_APP_VALUE));
			paramValue.append(GpintegrationConstants.QUOTE_SYMBOL).append(RIGHT_PARANTHESIS);
			paramMap.add(CASE_HEADER,paramValue.toString());
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(configurationService.getConfiguration().getString(CRM_GET_CASE_URL));
			builder.queryParams(paramMap).build(false);
			String getCaseURL = builder.toUriString().replace(LEFT_PARANTHESIS, URLEncoder.encode(LEFT_PARANTHESIS,"UTF-8")).replace(RIGHT_PARANTHESIS,URLEncoder.encode(RIGHT_PARANTHESIS,"UTF-8"));
			URI uri= URI.create(getCaseURL);
			final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			final RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			final ResponseEntity<GPCaseDetailsDTO> responseEntity =restTemplate.exchange(uri,	HttpMethod.GET, requestEntity, GPCaseDetailsDTO.class);
			response = responseEntity.getBody();
			LOG.debug(" CRM Get case response {} " , response.toString());
		} catch(HttpClientErrorException e ) {
			LOG.error("Http client Exception in getting case details for {}{} ", caseNumbers,  e);
			throw new GPIntegrationException(e.getMessage(),e);
		}catch(Exception e) {
			LOG.error("Exception in getting case details for {}{} ",caseNumbers, e);
			throw new GPIntegrationException(e.getMessage(),e);
		}

		return response;

	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public GPCRMService getGpCRMService() {
		return gpCRMService;
	}

	public void setGpCRMService(GPCRMService gpCRMService) {
		this.gpCRMService = gpCRMService;
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}


	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	/**
	 * @param baseSiteService the baseSiteService to set
	 */
	public void setBaseSiteService(BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the gpSupportTicketDao
	 */
	public GPSupportTicketDao getGpSupportTicketDao() {
		return gpSupportTicketDao;
	}

	/**
	 * @param gpSupportTicketDao the gpSupportTicketDao to set
	 */
	public void setGpSupportTicketDao(GPSupportTicketDao gpSupportTicketDao) {
		this.gpSupportTicketDao = gpSupportTicketDao;
	}
	

}