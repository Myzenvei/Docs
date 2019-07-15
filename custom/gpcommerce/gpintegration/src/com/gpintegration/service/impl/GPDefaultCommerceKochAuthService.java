/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
 
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.GPCommerceKochAuthService;
import com.gpintegration.exception.GPKochAuthException;
 
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.data.KochAuthData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

/**
 * The Class GPDefaultCommerceKochAuthService.
 *
 * @author spandiyan
 */
public class GPDefaultCommerceKochAuthService implements GPCommerceKochAuthService {
	
	private static final Logger LOG = Logger.getLogger(GPDefaultCommerceKochAuthService.class);
    
    @Resource
    private ConfigurationService configurationService;
    
    /**
     * Gets koch auth token
     * @param token
     * 			the token
     * @return register data
     */
    public RegisterData getKochAuthToken(RegisterData registerData) {
    	ResponseEntity<String> tokenResponse = getKochAuthAPIResponse(registerData.getToken());
    	ResponseEntity<String> userInfoResponse = null;
    	if(null != tokenResponse) {
			if(GpintegrationConstants.HTTP_STATUS_SUCCESS.equalsIgnoreCase(tokenResponse.getStatusCode().toString())) {
				mapKochAuthRegisterData(registerData, tokenResponse.getBody(), calculateAuthTS());
				LOG.debug("Koch Auth access token:"+registerData.getKochAuthAccessToken());
				userInfoResponse = getKochUserInfo(registerData.getKochAuthAccessToken());
				if(null != userInfoResponse) {
					if(GpintegrationConstants.HTTP_STATUS_SUCCESS.equalsIgnoreCase(userInfoResponse.getStatusCode().toString())) {
						LOG.debug("Koch auth user info response:"+userInfoResponse.getBody());
						mapKochEmailId(registerData, userInfoResponse.getBody());
					}
				}
			}
		}
    	return registerData;
    }
    
    private ResponseEntity<String> getKochUserInfo(String kochAuthAccessToken) {
    	ResponseEntity<String> userInfoResponse = null;
    	RestTemplate restTemplate = new RestTemplate();
    	MultiValueMap<String, String> kochAuthHeader = new LinkedMultiValueMap<>();
		URI kochAuthGetUserInfoURI;
		try {
			kochAuthGetUserInfoURI = new URI(Config.getParameter("gp.koch.auth.user.info.uri"));
			kochAuthHeader.add(GpintegrationConstants.HTTP_HEADER_AUTHORIZATION, GpintegrationConstants.HTTP_HEADER_AUTHORIZATION_BEARER+" "+kochAuthAccessToken);
	    	RequestEntity<String> kochUserInfoRequest = new RequestEntity<String>(null, kochAuthHeader, HttpMethod.GET, kochAuthGetUserInfoURI);
	    	userInfoResponse = restTemplate.exchange(kochUserInfoRequest, String.class);
		} catch (URISyntaxException invalidURIException) {
			LOG.error("Invalid URI exception", invalidURIException);
		} catch(HttpClientErrorException restTemplateException) {
    		processRestTemplateException(restTemplateException);
    	} catch (Exception e) {
    		LOG.error("Exception occured at Koch Auth User Info call", e);
    		throw new GPKochAuthException("Exception occured at Koch Auth User Info call");
    	}
    	return userInfoResponse;
    }

	/**
	 * Gets koch auth api response
	 * @param token
	 * @return
	 * @throws GPKochAuthException
	 */
	private ResponseEntity<String> getKochAuthAPIResponse(String token) throws GPKochAuthException {
	ResponseEntity<String> tokenResponse = null;
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders kochAuthHeaders = new HttpHeaders();
    	kochAuthHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	HttpEntity<MultiValueMap<String, String>> kochAuthRequest = setKochHeaders(token, kochAuthHeaders);
    	try {
    		tokenResponse = restTemplate.postForEntity(Config.getParameter("gp.koch.auth.token.api.uri"), kochAuthRequest, String.class);
    	} catch(HttpClientErrorException restTemplateException) {
    		processRestTemplateException(restTemplateException);
    	} catch(Exception e) {
    		LOG.error("Exeception occured at Koch Auth Token API", e);
    		throw new GPKochAuthException("Exception occured at Koch Auth Token API call.");
    	}
		return tokenResponse;
	}
    
    /**
     * Gets Koch auth data using token
     * @param token
     * @return koch auth data
     */
    public KochAuthData getKochAuthData(String token) {
    KochAuthData kochAuthData = new KochAuthData();
    	ResponseEntity<String> tokenResponse = getKochAuthAPIResponse(token);
    	if(null != tokenResponse) {
			if(GpintegrationConstants.HTTP_STATUS_SUCCESS.equalsIgnoreCase(tokenResponse.getStatusCode().toString())) {
				mapKochAuthData(kochAuthData, tokenResponse.getBody(), calculateAuthTS());
			}
		}
    	return kochAuthData;
    }

	private HttpEntity<MultiValueMap<String, String>> setKochHeaders(String token, HttpHeaders kochAuthHeaders) {
		MultiValueMap<String, String> kochAuthRequestMap = new LinkedMultiValueMap<>();
    	kochAuthRequestMap.add(GpintegrationConstants.KOCH_AUTH_ACCESS_CODE, token);
    	kochAuthRequestMap.add(GpintegrationConstants.KOCH_AUTH_GRANT_TYPE, GpintegrationConstants.KOCH_AUTH_GRANT_TYPE_VALUE);
    	kochAuthRequestMap.add(GpintegrationConstants.KOCH_AUTH_CLIENT_ID, Config.getParameter("gp.koch.auth.client.id"));
    	kochAuthRequestMap.add(GpintegrationConstants.KOCH_AUTH_CLIENT_SECRET, Config.getParameter("gp.koch.auth.client.secret"));
    	kochAuthRequestMap.add(GpintegrationConstants.KOCH_AUTH_REDIRECT_URI, Config.getParameter("gp.koch.auth.redirect.uri"));
    	HttpEntity<MultiValueMap<String, String>> kochAuthRequest = new HttpEntity<MultiValueMap<String, String>>(kochAuthRequestMap,kochAuthHeaders);
		return kochAuthRequest;
	}
    
    /**
     * Helper method to calculate the AUTH_TS.
     * AUTH_TS = CURRENT_DATE + 30 DAYS
     * 
     * @return String
     */
    private String calculateAuthTS() {
		final DateTimeFormatter dateFormat8 = DateTimeFormatter
				.ofPattern(GpintegrationConstants.KOCH_ATUH_TS_DATE_FORMAT);
		Date currentDate = new Date();
		int validityDays = configurationService.getConfiguration().getInt("gp.koch.auth.validity.date");
		String zoneId = configurationService.getConfiguration().getString("gp.koch.auth.validity.timezone");
		LocalDateTime loginValidityDateTime = currentDate.toInstant().atZone(ZoneId.of(zoneId)).toLocalDateTime()
				.plusDays(validityDays);
		return dateFormat8.format(loginValidityDateTime);
    }
    
    /**
     * Helper method to map the token API response to RegisterData.
     * 
     * @param registerData
     * @param kochAuthTokenResponse
     * @param authTS
     */
    private void mapKochAuthRegisterData(RegisterData registerData, String kochAuthTokenResponse, String authTS) {
    	try {
    		JSONObject kochAuthResponseBody = new JSONObject(kochAuthTokenResponse);
    		registerData.setKochAuthAccessToken(kochAuthResponseBody.optString(GpintegrationConstants.ACCESS_TOKEN));
    		registerData.setKochAuthIdToken(kochAuthResponseBody.optString(GpintegrationConstants.ID_TOKEN));
    		registerData.setKochAuthRefreshToken(kochAuthResponseBody.optString(GpintegrationConstants.REFRESH_TOKEN));
    		registerData.setKochAuthTS(authTS);
    	} catch(JSONException exception) {
    		LOG.error("Koch Auth token mapping failed", exception);
    		throw new GPKochAuthException("Koch Auth token API response mapping failed");
    	}
    }
    
    private void mapKochEmailId(RegisterData registerData, String kochUserInfoResponse) {
    	try {
    		JSONObject kochAuthUserInfo = new JSONObject(kochUserInfoResponse);
    		registerData.setKochEmailId(kochAuthUserInfo.optString(GpintegrationConstants.USER_PRINCIPAL_NAME));
    	} catch(JSONException jsonException) {
    		LOG.error("Koch Auth user info response mapping failed", jsonException);
    		throw new GPKochAuthException("Koch Auth user info response mapping failed");
    	}
    }
    
    /**
     * Helper method to map the token API response to RegisterData.
     * 
     * @param registerData
     * @param kochAuthTokenResponse
     * @param authTS
     */
    private void mapKochAuthData(KochAuthData kochAuthData, String kochAuthTokenResponse, String authTS) {
    	try {
    		JSONObject kochAuthResponseBody = new JSONObject(kochAuthTokenResponse);
    		kochAuthData.setAccessToken(kochAuthResponseBody.optString(GpintegrationConstants.ACCESS_TOKEN));
    		kochAuthData.setIdToken(kochAuthResponseBody.optString(GpintegrationConstants.ID_TOKEN));
    		kochAuthData.setRefreshToken(kochAuthResponseBody.optString(GpintegrationConstants.REFRESH_TOKEN));
    		kochAuthData.setModifiedTimeStamp(authTS);
    	} catch(JSONException exception) {
    		LOG.error("Koch Auth token mapping failed", exception);
    		throw new GPKochAuthException("Koch Auth token API response mapping failed");
    	}
    }
    
    private void processRestTemplateException(HttpClientErrorException restTemplateException) {
    	if(null != restTemplateException) {
    		String statusCode = restTemplateException.getStatusCode().toString();
    		if(GpintegrationConstants.HTTP_STATUS_BAD_REQUEST.equalsIgnoreCase(statusCode)) {
    			throw new GPKochAuthException(restTemplateException.getResponseBodyAsString());
    		}
    		
    		if(GpintegrationConstants.HTTP_STATUS_UNAUTHORIZED.equalsIgnoreCase(statusCode)) {
    			throw new GPKochAuthException(restTemplateException.getStatusCode()+restTemplateException.getStatusText());
    		}
    	}
    }
    	
}