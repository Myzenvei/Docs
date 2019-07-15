/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.sfdc.GPProductRelationshipRequestDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPProductReferenceReplicationService;

import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * The Class GPDefaultProductReferenceReplicationService.
 */
public class GPDefaultProductReferenceReplicationService implements GPProductReferenceReplicationService{

	private ConfigurationService configurationService;
	
	private static final Logger LOG = Logger.getLogger(GPDefaultProductReferenceReplicationService.class);
	
	@Resource
	private  GPCRMService gpCRMService;
	
	/**
	 * Replicate product reference.
	 *
	 * @param productReferenceDTO the product reference DTO
	 * @return the response entity
	 * @throws GPIntegrationException the GP integration exception
	 */
	@Override
	public ResponseEntity<String> replicateProductReference(GPProductRelationshipRequestDTO productReferenceDTO) throws GPIntegrationException{
		final ObjectMapper requestMapper = new ObjectMapper();
		ResponseEntity<String> referenceRepResEntity = null;
		String referenceReplicationURL = configurationService.getConfiguration().getString(GpintegrationConstants.SCPI_PRODUCT_REFERENCE_REPLICATION_URL); 
		final HttpHeaders headers = gpCRMService.getSCPIHeaders();
		final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		final RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        restTemplate.getMessageConverters().add(getJacksonSupportsMoreTypes());
        try {
        	HttpEntity<String> referenceReplicationReqEntity = new HttpEntity<>(requestMapper.writeValueAsString(productReferenceDTO), headers);
        	LOG.info("Request Object for SCPI Replication Service: {}"+requestMapper.writeValueAsString(productReferenceDTO));
        	referenceRepResEntity = restTemplate.exchange(referenceReplicationURL,
                    HttpMethod.POST, referenceReplicationReqEntity, String.class);
        } catch(Exception e) {
        	throw new GPIntegrationException("Product reference replication failed with exception", e);
        }
		return referenceRepResEntity;
	}

	/**
	 * Gets the jackson supports more types.
	 *
	 * @return the jackson supports more types
	 */
	@SuppressWarnings("rawtypes")
	private HttpMessageConverter getJacksonSupportsMoreTypes() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(MediaType.parseMediaType(GpintegrationConstants.HTTP_MIME_TYPE_APPLICATION_JSON), MediaType.APPLICATION_OCTET_STREAM));
		return converter;
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
}