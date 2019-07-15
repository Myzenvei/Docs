/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.paypal.PayPalAuthorizationRequest;
import com.gpintegration.dto.paypal.PayPalAuthorizationResponse;
import com.gpintegration.dto.paypal.PayPalCaptureRequest;
import com.gpintegration.dto.paypal.PayPalCaptureResponse;
import com.gpintegration.dto.paypal.PayPalExecuteRequest;
import com.gpintegration.dto.paypal.PayPalExecuteResponse;
import com.gpintegration.dto.paypal.PayPalRefundRequest;
import com.gpintegration.dto.paypal.PayPalRefundResponse;
import com.gpintegration.service.GPPayPalIntegrationService;
import org.apache.commons.codec.binary.Base64;

import de.hybris.platform.servicelayer.config.ConfigurationService;

public class GPPayPalIntegrationServiceImpl implements GPPayPalIntegrationService{

	private static final String APPLICATION_JSON = "application/json";

	private static final String AUTHORIZATION = "Authorization";

	private static final String BASIC = "Basic ";
	
	private static final String BEARER = "Bearer ";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final Logger LOG = LoggerFactory.getLogger(GPPayPalIntegrationServiceImpl.class);
	

	@Override
	public String getAccessToken() {
		String response = null;
		String accessToken = null;
		try {
	        URL url = new URL(configurationService.getConfiguration().getString("gp.paypal.oauth.url")+"?grant_type=client_credentials");
	        String authStr = configurationService.getConfiguration().getString("gp.paypal.merchant.username") + ":" +
	        configurationService.getConfiguration().getString("gp.paypal.merchant.password");
	        String authEncoded = new String(new Base64().encode(authStr.getBytes()));
	        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", APPLICATION_JSON);
	        connection.setRequestProperty("Accept-Language", "en_US");
	        connection.setRequestProperty(AUTHORIZATION, BASIC + authEncoded);
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
	        InputStream content = (InputStream)connection.getInputStream();
	        BufferedReader in = new BufferedReader (new InputStreamReader (content));
	        String line;
	        if ((line = in.readLine()) != null) {
	            response = line;
	            JSONObject json = new JSONObject(response);
	            accessToken = json.optString(GpintegrationConstants.ACCESS_TOKEN);
	        }
	        return accessToken;
	    }
	    catch (Exception e) {
	    	LOG.error("Error in get Access Token : "+e.getMessage(),e);
	        return null;
	    }
		
	}

	@Override
	public PayPalAuthorizationResponse authorize(PayPalAuthorizationRequest payPalPaymentRequest) {
		try {
			final ObjectMapper requestMapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();
			final HttpHeaders headers = new HttpHeaders();
			String authHeader = BEARER + getAccessToken();
			headers.set(AUTHORIZATION, authHeader);
			headers.setContentType(MediaType.APPLICATION_JSON);
			LOG.info("PayPal Auth request :"+ new HttpEntity<>(requestMapper.writeValueAsString(payPalPaymentRequest), headers));
			final HttpEntity<byte []> entity = new HttpEntity<>(
					requestMapper.writeValueAsBytes(payPalPaymentRequest), headers);
			final ResponseEntity<PayPalAuthorizationResponse> payPalAuthResponse = restTemplate.exchange(
					configurationService.getConfiguration().getString("gp.paypal.payment.url")+"/payment", HttpMethod.POST, entity, 
					PayPalAuthorizationResponse.class);
			return payPalAuthResponse.getBody();
		}catch (HttpClientErrorException exception) {
	            LOG.error( "callToRestService Error :" + exception.getResponseBodyAsString(),exception);
	            
	     }catch (HttpStatusCodeException exception) {
	    	 		LOG.error( "callToRestService Error :" + exception.getResponseBodyAsString(),exception);
	     }
		catch(Exception e) {
			LOG.error( "PayPalAuthorizationResponse exception : "+e.getMessage(),e);
		}
		return null;
	}

	@Override
	public PayPalExecuteResponse execute(PayPalExecuteRequest payPalExecuteRequest, String token) {
		try {
			final ObjectMapper requestMapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();
			final HttpHeaders headers = new HttpHeaders();
			String authHeader = BEARER + getAccessToken();
			headers.set(AUTHORIZATION, authHeader);
			headers.setContentType(MediaType.APPLICATION_JSON);
			final HttpEntity<String> entity = new HttpEntity<>(requestMapper.writeValueAsString(payPalExecuteRequest),
					headers);
			final ResponseEntity<PayPalExecuteResponse> payPalExecuteResponse = restTemplate.exchange(
					configurationService.getConfiguration().getString("gp.paypal.payment.url")+"/payment/" + token + "/execute", HttpMethod.POST, entity,
					PayPalExecuteResponse.class);
			return payPalExecuteResponse.getBody();
		} catch (HttpClientErrorException exception) {
			LOG.error("callToRestService Error :" + exception.getResponseBodyAsString(),exception);

		} catch (HttpStatusCodeException exception) {
			LOG.error("callToRestService Error :" + exception.getResponseBodyAsString(),exception);
			
		} catch (Exception e) {
			LOG.error("PayPalAuthorizationResponse exception : " + e.getMessage(),e);
			
		}
		return null;
	}

	@Override
	public PayPalCaptureResponse capture(PayPalCaptureRequest payPalCaptureRequest, String token) {

		try {
			final ObjectMapper requestMapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();
			final HttpHeaders headers = new HttpHeaders();
			String authHeader = BEARER + getAccessToken();
			headers.set(AUTHORIZATION, authHeader);
			headers.setContentType(MediaType.APPLICATION_JSON);
			final HttpEntity<String> entity = new HttpEntity<>(requestMapper.writeValueAsString(payPalCaptureRequest),
					headers);
			final ResponseEntity<PayPalCaptureResponse> payPalCaptureResponse = restTemplate.exchange(
					configurationService.getConfiguration().getString("gp.paypal.payment.url")+"/authorization/" + token + "/capture", HttpMethod.POST, entity,
					PayPalCaptureResponse.class);
			return payPalCaptureResponse.getBody();
		} catch (HttpClientErrorException exception) {
			LOG.error("callToRestService Error :" + exception.getResponseBodyAsString(),exception);

		} catch (HttpStatusCodeException exception) {
			LOG.error("callToRestService Error :" + exception.getResponseBodyAsString(),exception);
			
		} catch (Exception e) {
			LOG.error("PayPalAuthorizationResponse exception : " + e.getMessage(),e);
			
		}
		return null;
	
	}
	
	@Override
	public PayPalRefundResponse refund(PayPalRefundRequest payPalCaptureRequest, String token) {

		try {
			final ObjectMapper requestMapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();
			final HttpHeaders headers = new HttpHeaders();
			String authHeader = BEARER + getAccessToken();
			headers.set(AUTHORIZATION, authHeader);
			headers.setContentType(MediaType.APPLICATION_JSON);
			final HttpEntity<String> entity = new HttpEntity<>(requestMapper.writeValueAsString(payPalCaptureRequest),
					headers);
			final ResponseEntity<PayPalRefundResponse> payPalRefundResponse = restTemplate.exchange(
					configurationService.getConfiguration().getString("gp.paypal.payment.url")+"/capture/" + token + "/refund", HttpMethod.POST, entity,
					PayPalRefundResponse.class);
			return payPalRefundResponse.getBody();
		} catch (HttpClientErrorException exception) {
			LOG.error("callToRestService Error :" + exception.getResponseBodyAsString(),exception);

		} catch (HttpStatusCodeException exception) {
			LOG.error("callToRestService Error :" + exception.getResponseBodyAsString(),exception);
			
		} catch (Exception e) {
			LOG.error("PayPalAuthorizationResponse exception : " + e.getMessage(),e);
			
		}
		return null;
	
	}
	
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
