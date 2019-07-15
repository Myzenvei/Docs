/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gpintegration.knowledgecenter.dto.GPKnowledgeCenterSkuResponse;
import com.gpintegration.service.GPKnowledgeCenterService;

import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * Default implementation of {@link GPKnowledgeCenterService}
 */
public class GPDefaultKnowledgeCenterService implements GPKnowledgeCenterService {
	private static final String ACCEPT = "Accept";
	@Resource
	private ConfigurationService configurationService;
	private static final Logger LOG = Logger.getLogger(GPDefaultKnowledgeCenterService.class);

	@Override
	@Cacheable(value = "knowledgeCenterCache", key = "T(de.hybris.platform.webservicescommons.cache.CacheKeyGenerator).generateKey(false,false,'getSKUDataFromKC',#value)")
	public GPKnowledgeCenterSkuResponse getSKUDataFromKC(String sku) {
		GPKnowledgeCenterSkuResponse responseBody = null;
		ResponseEntity<GPKnowledgeCenterSkuResponse> kcResponse=null;
		RestTemplate restTemplate = new RestTemplate();
		try {
			String endPointUrlScpi = configurationService.getConfiguration().getString("gp.knowledge.center.endpoint");
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endPointUrlScpi).queryParam("sku", sku);
			HttpHeaders headers = new HttpHeaders();
			headers.set(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			kcResponse = restTemplate.exchange(builder.toUriString(),
					HttpMethod.GET, entity, GPKnowledgeCenterSkuResponse.class);
			LOG.debug("Response from knowledge center:" + kcResponse);
			if (null != kcResponse && null != kcResponse.getBody()) {
				responseBody = kcResponse.getBody();
				return responseBody;
			}
				
		} catch (Exception e) {
			LOG.error("Exeception occured at Knowledge center API call for SKU ->"+sku, e);
			
		}
		return responseBody;
	}

}
