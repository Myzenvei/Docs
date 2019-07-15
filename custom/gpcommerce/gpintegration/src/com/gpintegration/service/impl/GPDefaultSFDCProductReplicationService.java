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
import com.gpintegration.dto.sfdc.GPProductReplicationRequestDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPSFDCProductReplicationService;

import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * The Class GPDefaultSFDCProductReplicationService.
 */
public class GPDefaultSFDCProductReplicationService implements GPSFDCProductReplicationService{

	private ConfigurationService configurationService;

	@Resource
	private  GPCRMService gpCRMService;
	
	private static final Logger LOG = Logger.getLogger(GPDefaultSFDCProductReplicationService.class);
	
	/**
	 * Replicate product.
	 *
	 * @param productReplicationRequest the product replication request
	 * @return the response entity
	 * @throws GPIntegrationException the GP integration exception
	 */
	@Override
	public ResponseEntity<String> replicateProduct(GPProductReplicationRequestDTO productReplicationRequest) throws GPIntegrationException{
		ResponseEntity<String> replicationResEntity = null;
		final ObjectMapper requestMapper = new ObjectMapper();
        String productReplicationServiceEndpoint = configurationService.getConfiguration().getString(GpintegrationConstants.SCPI_PRODUCT_REPLICATION_URL);
        final HttpHeaders headers = gpCRMService.getSCPIHeaders();
		final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		final RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        restTemplate.getMessageConverters().add(getJacksonSupportsMoreTypes());
        try {
        	HttpEntity<String> entity = new HttpEntity<>(requestMapper.writeValueAsString(productReplicationRequest), headers);
            LOG.info("Request Object for SCPI Replication Service: {}"+requestMapper.writeValueAsString(productReplicationRequest));
            replicationResEntity = restTemplate.exchange(productReplicationServiceEndpoint,
                    HttpMethod.POST, entity, String.class);
        } catch(Exception e) {
        	throw new GPIntegrationException("Product replciation failed with exception:", e);
        }
    	return replicationResEntity;
	}

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