/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */

package com.gpintegration.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPAgreementResponseDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementRequestDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementResponseDTO;
import com.gpintegration.leaseagreement.dto.GPLeaseAgreementResponseWrapperDTO;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPCommerceLeaseAgreementService;

import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * Default implementation of {@link GPCommerceLeaseAgreementService}
 */
public class GPDefaultCommerceLeaseAgreementService implements GPCommerceLeaseAgreementService{

	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultCommerceLeaseAgreementService.class);

	private ConfigurationService configurationService;

	public  static final String CUSTOMER_LEASE_AGREEMENT_URI = "gp.scpi.leaseagreement.end.point.url";
	
	@Resource
	private  GPCRMService gpCRMService;

	@Override
	public GPLeaseAgreementResponseDTO createLeaseAgreement(GPLeaseAgreementDTO agreement) throws GPIntegrationException {
		if(configurationService.getConfiguration().getBoolean(GpcommerceCoreConstants.ENABLE_LEASE_AGREEMENT)) {
			try {
				final GPLeaseAgreementResponseDTO leaseAgreementResponse = getLeaseAgreementResponse(agreement);
				if(!(leaseAgreementResponse != null &&  leaseAgreementResponse.getAgreementResponse() != null && StringUtils.isNotEmpty(leaseAgreementResponse.getAgreementResponse().getAgreementId()))) {
					LOG.error(" Error getting lease agreement , agreement id is null " );
					if(leaseAgreementResponse != null && leaseAgreementResponse.getAgreementResponse() != null) {
						LOG.error(" Error in creating lease agreement id : errormessage{} , message Id {} ", leaseAgreementResponse.getAgreementResponse().getErrorMessage(), leaseAgreementResponse.getAgreementResponse().getMessageID());
					}
					throw new GPIntegrationException(GPIntegrationException.CUSTOMER_LEASE_AGREEMENT_EMPTY_ERROR);
				}
				return leaseAgreementResponse;
			} catch(GPIntegrationException e) {
				LOG.error(" Error in creating lease agreement for id {} {} " , agreement.getAgreementID(),e.getMessage(),e);;
				throw new GPIntegrationException(GPIntegrationException.CUSTOMER_LEASE_AGREEMENT_EMPTY_ERROR);
			}
		} else {
			//Return default response if SFDC is not enabled
			return createDefaultLeaseAgreementResponse(agreement);
		}
	}

	/**
	 * Method to create default Lease agreement response
	 * @param GPLeaseAgreementDTO agreement
	 * @return GPLeaseAgreementResponseDTO , with generated agreement id
	 */
	private GPLeaseAgreementResponseDTO createDefaultLeaseAgreementResponse(GPLeaseAgreementDTO agreement) {
		final GPLeaseAgreementResponseDTO defaultResponse =  new GPLeaseAgreementResponseDTO();
		final GPAgreementResponseDTO agreementResponse = new GPAgreementResponseDTO();
		agreementResponse.setAgreementId(agreement.getAgreementID());
		agreementResponse.setStatus(GpintegrationConstants.SERVICE_RUN_TRUE);
		defaultResponse.setAgreementResponse(agreementResponse);
		return defaultResponse;
	}



	/**
	 * Get lease agreement from SCPI
	 *
	 * @param agreementRequest , request object containing details
	 * @return GPLeaseAgreementResponseDTO , lease agreement response from SCPI system
	 * @throws GPIntegrationException
	 */
	private GPLeaseAgreementResponseDTO getLeaseAgreementResponse(GPLeaseAgreementDTO agreementRequest) throws GPIntegrationException {
		GPLeaseAgreementResponseDTO response = null;
		final GPLeaseAgreementRequestDTO leaseAgreementRequest = new GPLeaseAgreementRequestDTO();
		final Map<String, String> pathVariables =  new HashMap<>();
		try {
			final HttpHeaders headers = gpCRMService.getSCPIHeaders();
			leaseAgreementRequest.setAgreement(agreementRequest);
			final HttpEntity<GPLeaseAgreementRequestDTO> requestEntity = new HttpEntity<>(leaseAgreementRequest, headers);
			final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			final RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			ResponseEntity<GPLeaseAgreementResponseWrapperDTO> responseEntity =restTemplate.exchange(configurationService.getConfiguration().getString(CUSTOMER_LEASE_AGREEMENT_URI),
					HttpMethod.POST, requestEntity, GPLeaseAgreementResponseWrapperDTO.class, pathVariables);
			if(responseEntity.getBody() != null){
				response = responseEntity.getBody().getResponse();
			}
		} catch(HttpClientErrorException e ) {
			LOG.error("Exception in creating lease agreement", e);
			throw new GPIntegrationException(e.getMessage(),e);
		}catch(Exception e) {
			LOG.error("Exception in creating lease agreement", e);
			throw new GPIntegrationException(GPIntegrationException.CUSTOMER_LEASE_AGREEMENT_ERROR + ":" + e.getMessage(),e);
		}
		return response;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService the configurationService to set
	 */
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * @return the gpCRMService
	 */
	public GPCRMService getGpCRMService() {
		return gpCRMService;
	}

	/**
	 * @param gpCRMService the gpCRMService to set
	 */
	public void setGpCRMService(GPCRMService gpCRMService) {
		this.gpCRMService = gpCRMService;
	}



}
