/*
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gpintegration.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.gp.commerce.core.exceptions.GPCommerceBusinessException;
import com.gp.commerce.core.services.GPCMSSiteService;
import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.dto.ymarketing.ContactMarketingPermissionsDTO;
import com.gpintegration.dto.ymarketing.ContactsDTO;
import com.gpintegration.dto.ymarketing.CreateContactRequestDTO;
import com.gpintegration.service.GPYMarketingSyncService;

/**
 * Default implementation of {@link GPYMarketingSyncService}
 */
public class GPDefaultYMarketingSyncService implements GPYMarketingSyncService {

	private static final String FETCH = "fetch";

	private static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";

	private static final String COLON = ":";

	private static final String AUTHORIZATION = "Authorization";

	private static final String BASIC = "Basic ";

	private static final String X_CSRF_TOKEN = "x-csrf-token";

	private static final String COOKIE = "cookie";

	private static final String SET_COOKIE = "set-cookie";

	private static final String ACCEPT = "Accept";

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE = "Content-Type";

	private static final Logger LOG = Logger.getLogger(GPDefaultYMarketingSyncService.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;

	@Override
	public String createOrUpdateContact(final UpdatePreferenceData updatePreferences, final boolean create)
			throws GPCommerceBusinessException {
		final String endPointUrl = configurationService.getConfiguration()
				.getString(GpintegrationConstants.YMARKETING_CREATE_UPDATE_ENDPOINT);
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endPointUrl);
		String response = null;
		if (create) {
			createContact(updatePreferences, builder);
			response = updateContact(updatePreferences, builder);
		} else {
			try {
				response = updateContact(updatePreferences, builder);
			} catch (final GPCommerceBusinessException e) {
				LOG.debug(e.getMessage(),e);
				createContact(updatePreferences, builder);
				response = updateContact(updatePreferences, builder);
			}
		}

		return response;
	}

	private String createContact(final UpdatePreferenceData updatePreferences, final UriComponentsBuilder builder) throws GPCommerceBusinessException
	{
		final CreateContactRequestDTO request = createRequest(updatePreferences);
		final ResponseEntity<?> response = send(request, builder.toUriString());
		LOG.debug("Creation Response: " + response.toString());
		return response.getStatusCode().name();
	}

	private String updateContact(final UpdatePreferenceData updatePreferences, final UriComponentsBuilder builder) throws GPCommerceBusinessException
	{
		final CreateContactRequestDTO request = updateRequest(updatePreferences);
		final ResponseEntity<?> response = send(request, builder.toUriString());
		LOG.debug("Update Response: " + response.toString());
		return response.getStatusCode().name();
	}


	private ResponseEntity<?> send(final CreateContactRequestDTO data, final String url) throws GPCommerceBusinessException{
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		final HttpHeaders uncachedHeader = getNewHeader();
		addCookieAndXCsrfToken(uncachedHeader.entrySet(), headers);
		addBasicAuthorization(headers);
		headers.set(CONTENT_TYPE, APPLICATION_JSON);
		headers.set(ACCEPT, APPLICATION_JSON);
		final HttpEntity<CreateContactRequestDTO> newEntityWithUncachedHeaders = new HttpEntity<>(data, headers);
		LOG.debug("NEW ENTITY:" + newEntityWithUncachedHeaders.toString());
		try {
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			return restTemplate.exchange(url, HttpMethod.POST, newEntityWithUncachedHeaders, String.class);
		} catch (final HttpClientErrorException e) {
			LOG.error("Error received in updating: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
			throw new GPCommerceBusinessException("", "Error in yMarketing web service call.");
		}

	}

	private CreateContactRequestDTO createRequest(final UpdatePreferenceData userPreference) {
		final CreateContactRequestDTO contactRequest = new CreateContactRequestDTO();
		final List<ContactsDTO> contacts = new ArrayList<>();
		final ContactsDTO contact = new ContactsDTO();
		final List<ContactMarketingPermissionsDTO> marketingPermissions = new ArrayList<>();
		final DateTimeFormatter format =  DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS);
		final String timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(format);
		// Add specific communication categories for mardigras forms
		if(StringUtils.isNotEmpty(cmsSiteService.getSiteConfig(GpintegrationConstants.YMARKETING_ENABLE_COUPONS))) {
			marketingPermissions.addAll(getMarketingPermissions(userPreference));
		} else {
			final ContactMarketingPermissionsDTO marketingPermission = createMarketingPermission(userPreference,null);
			marketingPermissions.add(marketingPermission);
		}

		contact.setTimestamp(timeStamp);
		contact.setId(userPreference.getEmail());
		contact.setEmailAddress(userPreference.getEmail());
		contact.setFirstName(userPreference.getFirstName());
		contact.setIsConsumer(true);
		contact.setIsContact(true);
		contact.setLastName(userPreference.getLastName());
		contact.setMobilePhoneNumber(userPreference.getMobilePhoneNumber());
		contact.setFullName(userPreference.getFirstName() + " " + userPreference.getLastName());
		contact.setPostalCode(userPreference.getPostalCode());
		contact.setMarketingPermissions(marketingPermissions);

		contactRequest.setUsername(configurationService.getConfiguration()
				.getString(GpintegrationConstants.YMARKETING_REQUEST_USERNAME));
		contactRequest.setSourceSystemType(configurationService.getConfiguration()
				.getString(GpintegrationConstants.YMARKETING_SOURCE_SYSTEM_TYPE));
		contactRequest.setSourceSystemId(
				configurationService.getConfiguration().getString(GpintegrationConstants.YMARKETING_SOURCE_SYSTEM_ID));
		contacts.add(contact);

		contactRequest.setContacts(contacts);
		return contactRequest;
	}
	
	/**
	 * Method to get the list of marketing permission from the userPreference
	 * @param  UpdatePreferenceData userPreference
	 * @return  List<ContactMarketingPermissionsDTO>  , containing the list of marketing permissions for user
	 */
	private List<ContactMarketingPermissionsDTO>  getMarketingPermissions(final UpdatePreferenceData userPreference) {
		final List<ContactMarketingPermissionsDTO> marketingPermissions = new ArrayList<>();
		final String siteId  = cmsSiteService.getCurrentSite().getUid();
		final String welcomeSeriesCategoryId = configurationService.getConfiguration()
				.getString(siteId + GpintegrationConstants.SITE_WELCOME_SERIES_CATEGORY);
		//Add communication category if present 
		final ContactMarketingPermissionsDTO mardiGrasMarketingPermission = createMarketingPermission(userPreference,configurationService.getConfiguration()
				.getString(siteId + GpintegrationConstants.SITE_COMMUNICATION_CATEGORY));
		marketingPermissions.add(mardiGrasMarketingPermission);
		//Add welcome series id
		if(StringUtils.isNotBlank(welcomeSeriesCategoryId)) {
			final ContactMarketingPermissionsDTO welcomeSericeMarketingPermission = createMarketingPermission(userPreference,configurationService.getConfiguration()
					.getString(siteId + GpintegrationConstants.SITE_WELCOME_SERIES_CATEGORY));
			marketingPermissions.add(welcomeSericeMarketingPermission);
		}
		//Add page specific communication categories
		if(StringUtils.isNotEmpty(userPreference.getFromPage())){
			final ContactMarketingPermissionsDTO pageCouponPermission = createMarketingPermission(userPreference,configurationService.getConfiguration()
					.getString(siteId + "."+ userPreference.getFromPage()+ GpintegrationConstants.SITE_PAGE_COUPON_CATEGORY));
			marketingPermissions.add(pageCouponPermission);
			LOG.debug(" Coupon marketing permission " + pageCouponPermission );

		}
		return marketingPermissions;
	}
	
	/**
	 * Method to create the marketing permission data for contact based on the communication preferences
	 * @param  UpdatePreferenceData userPreference , user data for which marketing permissions are updated
	 * @param String communicationCategoryId , communication category id
	 * @return ContactMarketingPermissionsDTO  
	 */
	private ContactMarketingPermissionsDTO createMarketingPermission(final UpdatePreferenceData userPreference,String communicationCategoryId) {
		final List<ContactMarketingPermissionsDTO> marketingPermissions = new ArrayList<>();
		final ContactMarketingPermissionsDTO marketingPermission = new ContactMarketingPermissionsDTO();
		final DateTimeFormatter format =  DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS);
		final String timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(format);
		marketingPermission.setTimestamp(timeStamp);
		marketingPermission.setId(userPreference.getEmail());
		marketingPermission
				.setIdOrigin(cmsSiteService.getSiteConfig(GpintegrationConstants.MARKETING_PERMISSIONS_ID_ORIGIN_CREATE));
		marketingPermission.setOptIn(userPreference.getOpt());
		if(StringUtils.isNotEmpty(communicationCategoryId)) {
			marketingPermission.setCommunicationCategoryId(communicationCategoryId);
		}
		marketingPermission.setOutboundCommunicationMedium(configurationService.getConfiguration().getString(GpintegrationConstants.OUTBOUND_COMMUNICATION_MEDIUM));
		marketingPermission.setCommunicationDirection(configurationService.getConfiguration().getString(GpintegrationConstants.COMMUNICATION_DIRECTION));
		marketingPermission.setMarketingAreaId(cmsSiteService.getSiteConfig(GpintegrationConstants.MARKETING_AREA_ID));
		marketingPermissions.add(marketingPermission);
		return marketingPermission;
	}

	private CreateContactRequestDTO updateRequest(final UpdatePreferenceData userPreference)
	{
		final CreateContactRequestDTO contactRequest = new CreateContactRequestDTO();
		final List<ContactMarketingPermissionsDTO> marketingPermissions = new ArrayList<>();
		final ContactMarketingPermissionsDTO marketingPermission = new ContactMarketingPermissionsDTO();

		final DateTimeFormatter format = DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS);
		final String timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(format);
		marketingPermission.setTimestamp(timeStamp);
		marketingPermission.setId(userPreference.getEmail());
		marketingPermission
				.setIdOrigin(cmsSiteService.getSiteConfig(GpintegrationConstants.MARKETING_PERMISSIONS_ID_ORIGIN_UPDATE));
		marketingPermission.setOptIn(userPreference.getOpt());
		marketingPermission.setOutboundCommunicationMedium(
				configurationService.getConfiguration().getString(GpintegrationConstants.OUTBOUND_COMMUNICATION_MEDIUM));
		marketingPermission.setCommunicationDirection(
				configurationService.getConfiguration().getString(GpintegrationConstants.COMMUNICATION_DIRECTION));
		marketingPermission.setCommunicationMedium(
				configurationService.getConfiguration().getString(GpintegrationConstants.COMMUNICATION_MEDIUM));
		marketingPermission
				.setMarketingAreaId(cmsSiteService.getSiteConfig(GpintegrationConstants.MARKETING_AREA_ID));
		marketingPermissions.add(marketingPermission);

		contactRequest.setId("");
		contactRequest.setTimeStamp(timeStamp);
		contactRequest
				.setUsername(configurationService.getConfiguration().getString(GpintegrationConstants.YMARKETING_REQUEST_USERNAME));
		contactRequest.setSourceSystemType(
				configurationService.getConfiguration().getString(GpintegrationConstants.YMARKETING_SOURCE_SYSTEM_TYPE));
		contactRequest.setSourceSystemId(cmsSiteService.getSiteConfig(GpintegrationConstants.YMARKETING_SOURCE_SYSTEM_ID));

		contactRequest.setContactMarketingPermissions(marketingPermissions);
		return contactRequest;
	}

	private void addCookieAndXCsrfToken(final Set<Map.Entry<String, List<String>>> entries, final HttpHeaders headers) {
		entries.forEach(value -> {
			final String key = value.getKey();
			final List<String> values = value.getValue();
			if (key.equalsIgnoreCase(SET_COOKIE)) {
				headers.put(COOKIE, values);
			}
			if (key.equalsIgnoreCase(X_CSRF_TOKEN)) {
				values.forEach(val -> headers.add(X_CSRF_TOKEN, val));
			}
		});
	}

	private HttpHeaders addBasicAuthorization(final HttpHeaders headers) {
		final String username = configurationService.getConfiguration()
				.getString(GpintegrationConstants.YMARKETING_AUTH_USERNAME);
		final String password = configurationService.getConfiguration()
				.getString(GpintegrationConstants.YMARKETING_AUTH_PASSWORD);

		final String base64EncodedAuthHeaderValue = BASIC
				+ Base64.getEncoder().encodeToString((username + COLON + password).getBytes());
		headers.add(AUTHORIZATION, base64EncodedAuthHeaderValue);
		return headers;
	}

	private HttpHeaders getNewHeader() {
		final String urlToFetchCxrfToken = configurationService.getConfiguration()
				.getString(GpintegrationConstants.YMARKETING_AUTH_ENDPOINT);
		final RestTemplate template = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		addBasicAuthorization(headers);
		headers.set(X_CSRF_TOKEN, FETCH);
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<String> exchange = template.exchange(urlToFetchCxrfToken, HttpMethod.GET, entity, String.class);
		final HttpHeaders receivedHeaders = exchange.getHeaders();
		LOG.debug("UNCACHED HEADER: " + receivedHeaders);
		return receivedHeaders;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
