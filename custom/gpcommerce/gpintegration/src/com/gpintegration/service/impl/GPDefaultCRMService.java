/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPCRMService;

/**
 * The Class GPDefaultCRMService.
 */
public class GPDefaultCRMService implements GPCRMService {

	private String user;
	private String password;
	private String oAuthUrl;
	
	private static final String BASIC = "Basic ";

	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultCRMService.class);


	public String getCRMAccessToken(){
		String accessToken = null;
		String response = null;
			try {
			URL url = new URL(oAuthUrl);

			final byte[] userAndPassword = (this.user + ':' + this.password).getBytes(StandardCharsets.UTF_8);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(GpintegrationConstants.HTTP_METHOD_POST);
			connection.setDoOutput(true);
	        connection.setRequestProperty(GpintegrationConstants.AUTHORIZATION, BASIC + Base64.getEncoder().encodeToString(userAndPassword));

			InputStream content = connection.getInputStream();
			BufferedReader in = new BufferedReader (new InputStreamReader (content));
			String line;
			if ((line = in.readLine()) != null) {
				response = line;
				JSONObject json = new JSONObject(response);
				accessToken = json.optString(GpintegrationConstants.ACCESS_TOKEN);
			}
		}
		catch (Exception e) {
			LOG.error(" Error in getting CRM Access token {} " , e);
		}
		return accessToken;
	}


	public  HttpHeaders getSCPIHeaders () throws GPIntegrationException {
		String accessToken = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		try {
			accessToken = getCRMAccessToken();
			//Retry in case of any issues
			if(StringUtils.isEmpty(accessToken)) {
				accessToken = getCRMAccessToken();
			}
		}catch( Exception e) {
			LOG.error( " Error in getting SCPI Access Token {} " , e);
		}
		LOG.debug(" Successful Oauth Token from SCPI {} ", accessToken);
		if(StringUtils.isEmpty(accessToken)) {
			LOG.error( "Error in getting SCPI Access Token :Empty token ");
			throw new GPIntegrationException(" Error getting SCPI access token");
		}
		headers.add(GpintegrationConstants.AUTHORIZATION,
				GpintegrationConstants.BEARER + accessToken);
		return headers;
	}


	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @return the oAuthUrl
	 */
	public String getoAuthUrl() {
		return oAuthUrl;
	}


	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}


	/**
	 * Sets the password.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Sets the o auth url.
	 *
	 * @param oAuthUrl the oAuthUrl to set
	 */
	public void setoAuthUrl(String oAuthUrl) {
		this.oAuthUrl = oAuthUrl;
	}


}
