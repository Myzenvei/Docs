/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */


package com.gpintegration.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import com.gp.commerce.core.model.GPEndUserLegalTermsModel;
import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.leaseagreement.dto.GPLegalTermResponseDTO;
import com.gpintegration.leaseagreement.dto.GPLegalTermResponseWrapperDTO;
import com.gpintegration.leaseagreement.dto.GPLegalTermsDTO;
import com.gpintegration.leaseagreement.dto.GPLegalTermsRequestDTO;
import com.gpintegration.leaseagreement.dto.GPLegalTermsRequestWrapperDTO;
import com.gpintegration.service.GPCRMService;
import com.gpintegration.service.GPCommerceLegalTermsService;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * Default implementation of {@link GPCommerceLegalTermsService}
 */
public class GPDefaultCommerceLegalTermsService implements GPCommerceLegalTermsService {

	private ConfigurationService configurationService;

	private ModelService modelService;

	private static final Logger LOG = LoggerFactory.getLogger(GPDefaultCommerceLegalTermsService.class);

	public  static final String CUSTOMER_LEASE_AGREEMENT_URI = "gp.scpi.legalterms.end.point.url";

	public static final String LEASE_AGREEMENT_COUNTRIES="gp.legalagreement.countries";

	public static final String DEFAULT_COUNTRY="United States";

	public static final String DEFAULT_LANGUAGE="gp.legalagreement.default.language";

	public static final String END_USER_LEASE="gp.legalagreement.legaltermname";

	public static final String GP_END_LEGAL_TERMS_COUNTRY="gp.legalagreement.country.";

	private FlexibleSearchService flexibleSearchService;
	
	@Resource
	private  GPCRMService gpCRMService;

	/**
	 * Method to fetch legal terms for each country
	 * @throws GPIntegrationException
	 */
	public  void fetchLegalTerms() throws GPIntegrationException  {
		try {
			final String countries= configurationService.getConfiguration().getString(LEASE_AGREEMENT_COUNTRIES,DEFAULT_COUNTRY);
			List<String> countryList =null;
			if(StringUtils.isNotBlank(countries))
			{
				countryList = Arrays.asList(StringUtils.split(countries, GpintegrationConstants.SEMI_COLON));
			} 
			if(CollectionUtils.isNotEmpty(countryList))
			{
				for(String country:countryList) {
					GPLegalTermResponseWrapperDTO response = getLegalTermsResponse(country);
					if(response != null && response.getLegalTermResponses() != null){
						Map<String,GPLegalTermsDTO> legalTermsMap = mapLegalTerms(response.getLegalTermResponses());
						persistLegalTerms(legalTermsMap);
					} else {
						String message = response != null && StringUtils.isNotEmpty(response.getError()) ? response.getError() :"Empty/Null response from legal terms";
						LOG.error("Error in legal terms response: {} ", message);
						throw new GPIntegrationException(message);
					}
				}
			}
		}catch( Exception e ) {
			LOG.error(" Error in getting legal terms {}" , e);
			throw new GPIntegrationException(e.getMessage());
		}
	}

	/**
	 * Method to retrieve request
	 * @return LegalTermsRequest
	 */
	private  GPLegalTermsRequestWrapperDTO getRequest(String country) {
		GPLegalTermsRequestWrapperDTO request = new GPLegalTermsRequestWrapperDTO();
		GPLegalTermsRequestDTO requestDTO = new GPLegalTermsRequestDTO();
		requestDTO.setCountry(country);
		request.setLegalTermsRequest(requestDTO);
		return request;
	}



	/**
	 * Method to map legal terms response
	 * @param legalTermsResponse
	 * @return
	 */
	private Map<String,GPLegalTermsDTO >  mapLegalTerms(GPLegalTermResponseDTO legalTermsResponse) {
		final Map<String,GPLegalTermsDTO > legalTermsMap = new HashMap<>();
		for(GPLegalTermsDTO legalTerm: legalTermsResponse.getLegalTerms()) {
			//Add only end user lease properties
			if(configurationService.getConfiguration().getString(END_USER_LEASE).equalsIgnoreCase(legalTerm.getLegalTermName())) {
				String key = legalTerm.getLegalTermName() +  legalTerm.getLegalLanguage() + legalTerm.getVersion();
				legalTermsMap.put(key, legalTerm);
			}
		}
		return legalTermsMap;
	}

	/**
	 * Method to get legal terms response from SCPI
	 * @param country , country for which legal terms will be fetched
	 * @return GPLegalTermResponseWrapperDTO
	 * @throws GPIntegrationException
	 */
	private GPLegalTermResponseWrapperDTO getLegalTermsResponse(String country) throws GPIntegrationException {
		GPLegalTermResponseWrapperDTO response = null;
		final Map<String, String> pathVariables =  new HashMap<>();
		try {
			final GPLegalTermsRequestWrapperDTO request = getRequest(country);
			final HttpHeaders headers = gpCRMService.getSCPIHeaders();
			final HttpEntity<GPLegalTermsRequestWrapperDTO> requestEntity = new HttpEntity<>(request, headers);
			final ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
			final RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
			final ResponseEntity<GPLegalTermResponseWrapperDTO> responseEntity =restTemplate.exchange(configurationService.getConfiguration().getString(CUSTOMER_LEASE_AGREEMENT_URI),
					HttpMethod.POST, requestEntity, GPLegalTermResponseWrapperDTO.class, pathVariables);
			response = responseEntity.getBody();
		} catch(HttpClientErrorException e ) {
			throw new GPIntegrationException(e.getMessage(),e);
		}catch(Exception e) {
			LOG.error("Exception in getting legal terms", e);
			throw new GPIntegrationException(e.getMessage(),e);
		}
		return response;
	}

	/**
	 * Method to save the legal terms
	 * @param legalTermsMap<GPLegalTermsDTO> legalTermsList , List of Legal terms that needs to be saved
	 */
	private void persistLegalTerms(final Map<String,GPLegalTermsDTO> legalTermsMap) {
		legalTermsMap.entrySet().forEach(entry->{
			final GPLegalTermsDTO legalTerm =entry.getValue();
			deleteExistingRecord(legalTerm);
			final GPEndUserLegalTermsModel obj = modelService.create(GPEndUserLegalTermsModel.class);
			obj.setLegalTermName(legalTerm.getLegalTermName());
			obj.setId(legalTerm.getId());
			obj.setLegalTermsText(legalTerm.getLegalTermsText());
			String countryKey = GP_END_LEGAL_TERMS_COUNTRY +
					(legalTerm.getCountry().contains(" ")?legalTerm.getCountry().replace(" ",GpintegrationConstants.HYPHEN):legalTerm.getCountry());
			obj.setCountry(getConfigurationService().getConfiguration().getString(countryKey,legalTerm.getCountry()));
			String defaultLanguage = legalTerm.getCountry() + GpintegrationConstants.HYPHEN + getConfigurationService().getConfiguration().getString(DEFAULT_LANGUAGE);
			//set default language in case it is empty
			obj.setLegalLanguage(StringUtils.isNotEmpty(legalTerm.getLegalLanguage())? legalTerm.getLegalLanguage() : defaultLanguage);
			obj.setVersion(Integer.parseInt(legalTerm.getVersion()));
			obj.setTimestamp(new Date());
			modelService.save(obj);
		});
	}

	/**
	 * Method to delete existing record
	 * @param legalTerm
	 */
	private void deleteExistingRecord(final GPLegalTermsDTO legalTerm) {
		final GPEndUserLegalTermsModel searchModel = new GPEndUserLegalTermsModel();
		searchModel.setLegalTermName(legalTerm.getLegalTermName());
		String defaultLanguage = legalTerm.getCountry() + GpintegrationConstants.HYPHEN + getConfigurationService().getConfiguration().getString(DEFAULT_LANGUAGE);
		searchModel.setLegalLanguage(StringUtils.isNotEmpty(legalTerm.getLegalLanguage())? legalTerm.getLegalLanguage(): defaultLanguage);
		String countryKey = GP_END_LEGAL_TERMS_COUNTRY +
				(legalTerm.getCountry().contains(" ")?legalTerm.getCountry().replace(" ",GpintegrationConstants.HYPHEN):legalTerm.getCountry());
		searchModel.setCountry(getConfigurationService().getConfiguration().getString(countryKey,legalTerm.getCountry()));
		if(StringUtils.isNotEmpty(legalTerm.getVersion())) {
			searchModel.setVersion(Integer.parseInt(legalTerm.getVersion()));
		}
		List<GPEndUserLegalTermsModel> legalTermsList  = flexibleSearchService.getModelsByExample(searchModel);
		if(CollectionUtils.isNotEmpty(legalTermsList)) {
			modelService.removeAll(legalTermsList);
		}
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
	 * @return the modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService the modelService to set
	 */
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
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
