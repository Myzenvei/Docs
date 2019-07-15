/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.service.applepay.session.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.gp.commerce.core.service.applepay.session.GPApplePaySessionSerivce;
import com.gp.commerce.core.util.GPApplePayRequest;
import com.gp.commerce.core.util.GPApplePaySessionResponseDTO;
import com.gp.commerce.facades.constants.GpcommerceFacadesConstants;

import de.hybris.platform.util.Config;

public class GPApplePaySessionSerivceImpl implements GPApplePaySessionSerivce {
	
	Logger LOG = Logger.getLogger(GPApplePaySessionSerivceImpl.class);

	@Override
	public GPApplePaySessionResponseDTO validateSessionService(String validateURL, String baseSiteId) {
		
		LOG.info("******** Entering in getApplePaySession method *********");

		char[] password = Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_MERCHANT_ID_CERTIFICATE_PWD).toCharArray();

		   SSLContext sslContext = null;
		try {
		   sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore(Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_MERCHANT_ID_CERTIFICATE_FILE_PATH), password), password)
		         .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		} catch (Exception exp) {
			LOG.error("SSL exception occured ", exp);
		}

		final HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		   
		final GPApplePayRequest applePayServiceRequest = new GPApplePayRequest();
		applePayServiceRequest.setDisplayName(Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_DISPLAY_NAME));
		applePayServiceRequest.setInitiative(Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_INITIATIVE));
		applePayServiceRequest.setInitiativeContext(Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_INITIATIVE_CONTEXT));
		applePayServiceRequest.setMerchantIdentifier(Config.getParameter(GpcommerceFacadesConstants.APPLE_PAY_MERCHANT_IDENTIFIER+baseSiteId));

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<GPApplePayRequest> request = new HttpEntity<>(applePayServiceRequest,headers);
		GPApplePaySessionResponseDTO response = null;

		LOG.info("ApplePaySessionToken request ValidationURL : " + validateURL);

		try {
		   response = restTemplate.exchange(validateURL, HttpMethod.POST, request, GPApplePaySessionResponseDTO.class).getBody();
		   logResponse(response);
		} catch (Exception e) {
			LOG.error("Exception occured in  getApplePaySessionToken method", e);
		}

		LOG.info("******** Exiting from getApplePaySessionToken method *********");

		return response;
	}
	
	private KeyStore keyStore(String file, char[] password) throws Exception {
	    KeyStore keyStore = KeyStore.getInstance("PKCS12");
	    File key = ResourceUtils.getFile(file);
	    try (InputStream in = new FileInputStream(key)) {
	        keyStore.load(in, password);
	    }
	    return keyStore;
	}
	
	private void logResponse(final GPApplePaySessionResponseDTO response) {
	       LOG.info("============================ApplePaySessionToken response begin==========================================");
	       LOG.info("DisplayName : " + response.getDisplayName());
	       LOG.info("DomainName : " + response.getDomainName());
	       LOG.info("EpochTimestamp : " + response.getEpochTimestamp());
	       LOG.info("ExpiresAt : " + response.getExpiresAt());
	       LOG.info("MerchantIdentifie : " + response.getMerchantIdentifier());
	       LOG.info("MerchantSessionIdentifier : " + response.getMerchantSessionIdentifier());
	       LOG.info("Nonce : " + response.getNonce());
	       LOG.info("Signature : " + response.getSignature());
	       LOG.info("=======================ApplePaySessionToken response end=================================================");
	   }

}
