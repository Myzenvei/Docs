/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gpintegration.service.impl;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPDataException;
import com.gpintegration.service.GPCommerceSocialAccountService;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commercefacades.user.data.RegisterData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.gp.commerce.core.enums.GPLoginType;
import com.gp.commerce.core.user.dao.impl.GPDefaultUserDao;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.storelocator.exception.GoogleMapException;

import org.apache.commons.io.IOUtils;


/**
 * The Class GPDefaultCommerceSocialAccountService.
 *
 * @author uchandrasekaran
 */
public class GPDefaultCommerceSocialAccountService implements GPCommerceSocialAccountService {
	
	private static final String MOZILLA_5_0 = "Mozilla/5.0";

	private static final String USER_AGENT = "User-Agent";

	private static final String GET = "GET";

	private static final Logger LOG = Logger.getLogger(GPCommerceSocialAccountService.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	
	@Resource(name = "gpUserDao")
	private GPDefaultUserDao gpUserDao;
	
	/**
	 * @param registerData
	 *  @return userDataFromFacebook      
	 */
	@Override
	public RegisterData getUserDataFromFacebook(RegisterData userDataFromFacebook){
		String userData = null;
		URLConnection fbGraphConnection= null;
		BufferedReader fbInputStream= null;
		String accessToken=userDataFromFacebook.getToken();

		try{
		final StringBuilder graphData = new StringBuilder();
		final URL fbGraphUrl = new URL(configurationService.getConfiguration().getString(GpintegrationConstants.FACEBOOK_ENDPOINT) +accessToken);
		fbGraphConnection = fbGraphUrl.openConnection();
		fbInputStream = new BufferedReader(new InputStreamReader(fbGraphConnection.getInputStream()));
		String inputLine;
		while ((inputLine = fbInputStream.readLine()) != null)
		{
			graphData.append(inputLine + "\n");
		}
		userData = graphData.toString();
		Map<String,String>socialUserProfile=getSocialProfileDetails(userData,userDataFromFacebook.getLoginType());
		userDataFromFacebook.setFirstName(socialUserProfile.get(GpintegrationConstants.FIRST_NAME));
		userDataFromFacebook.setLastName(socialUserProfile.get(GpintegrationConstants.LAST_NAME));
		userDataFromFacebook.setLogin(socialUserProfile.get(GpintegrationConstants.EMAIL_ID));
		userDataFromFacebook.setFbUniqueId(socialUserProfile.get(GpintegrationConstants.ID));
		userDataFromFacebook.setPassword(configurationService.getConfiguration().getString(GpintegrationConstants.SOCIAL_LOGIN_PASSWORD));     
	}
	catch (final Exception e)
	{   
		LOG.error(GpintegrationConstants.EXCEPTION,e);
		throw new GPDataException(GPDataException.FACEBOOK_DATA_ERROR);
	}
	finally
	{
		IOUtils.closeQuietly(fbInputStream);
	}	
		return userDataFromFacebook;
		
	}
	
	/**
	 * @param registerData
	 *  @return userDataFromGoogle      
	 */
	@Override
	public RegisterData getUserDataFromGoogle(RegisterData registerData){

		
		final String acessToken = registerData.getToken();
		BufferedReader inputStream=null;
		try
		{

			final URL url = new URL(configurationService.getConfiguration().getString(GpintegrationConstants.GOOGLE_ENDPOINT)+ acessToken) ;
			final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(GET);
			con.setRequestProperty(USER_AGENT, MOZILLA_5_0);
			inputStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			final StringBuilder userInfoResponse = new StringBuilder();
			while ((inputLine = inputStream.readLine()) != null)
			{
				userInfoResponse.append(inputLine);
			}
			inputStream.close();
			Map<String,String> socialUserProfile=getSocialProfileDetails(userInfoResponse.toString(),registerData.getLoginType());
			
			registerData.setFirstName(socialUserProfile.get(GpintegrationConstants.FIRST_NAME));
			registerData.setLastName(socialUserProfile.get(GpintegrationConstants.LAST_NAME));
			registerData.setLogin(socialUserProfile.get(GpintegrationConstants.EMAIL_ID));
			registerData.setPassword(configurationService.getConfiguration().getString(GpintegrationConstants.SOCIAL_LOGIN_PASSWORD));
		}
		catch(final GoogleMapException e) {
			LOG.error(GpintegrationConstants.EXCEPTION,e);
			throw new GPDataException(GPDataException.GOOGLE_DATA_ERROR);
		}
		catch (final IOException e)
		{
			LOG.error(GpintegrationConstants.EXCEPTION,e);
			throw new GPDataException(GPDataException.GOOGLE_DATA_ERROR);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}
		return registerData;
		
	}
	
	
	/**
	 * @param userData
	 *  @return socialUserProfile      
	 */
	@Override
	public Map<String,String> getSocialProfileDetails(final String userData,String socialLoginFlag){
		final Map<String,String>socialUserProfile=new HashMap<>();
		try{
			final JSONObject json = new JSONObject(userData);
			if(socialLoginFlag.equalsIgnoreCase(GpintegrationConstants.GOOGLE)) {
				socialUserProfile.put(GpintegrationConstants.FIRST_NAME, json.optString(GpintegrationConstants.GIVEN_NAME));
				socialUserProfile.put(GpintegrationConstants.LAST_NAME, json.optString(GpintegrationConstants.FAMILY_NAME));	
			}else if(socialLoginFlag.equalsIgnoreCase(GpintegrationConstants.FACEBOOK)) {
				socialUserProfile.put(GpintegrationConstants.FIRST_NAME, json.optString(GpintegrationConstants.FIRST_NAME));
				socialUserProfile.put(GpintegrationConstants.LAST_NAME, json.optString(GpintegrationConstants.LAST_NAME));
			}
			socialUserProfile.put(GpintegrationConstants.ID, json.getString(GpintegrationConstants.ID));
			socialUserProfile.put(GpintegrationConstants.EMAIL_ID,json.optString(GpintegrationConstants.EMAIL_ID,null));
			socialUserProfile.put(GpintegrationConstants.GENDER, json.optString(GpintegrationConstants.GENDER));
		}catch (final JSONException e)
		{
			LOG.error(GpintegrationConstants.EXCEPTION,e);
			throw new GPDataException(GPDataException.ERROR_SOCIAL_LOGIN_DATA_PARSING);
		}
		return socialUserProfile;
		
	}

	@Override
	public RegisterData getRegisterData(RegisterData registerData) {

		if(StringUtils.equalsIgnoreCase(GPLoginType.GOOGLE.getCode(), registerData.getLoginType())) {
			registerData=getUserDataFromGoogle(registerData);
		} else if(StringUtils.equalsIgnoreCase(GPLoginType.FACEBOOK.getCode(), registerData.getLoginType())) {
			registerData=getUserDataFromFacebook(registerData);
			if(null==registerData.getLogin() && null!=registerData.getFbUniqueId())
			{
				registerData.setLogin(gpUserDao.getUserForFBUniqueId(registerData.getFbUniqueId()));
			}
			if(registerData.getLogin()==null) {
				registerData.setErrorCode("1000");
			}
		}
		return registerData;
	}

}
